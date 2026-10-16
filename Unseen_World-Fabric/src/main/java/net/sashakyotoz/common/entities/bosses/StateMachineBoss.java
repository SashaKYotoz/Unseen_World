package net.sashakyotoz.common.entities.bosses;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public abstract class StateMachineBoss<S extends Enum<S> & BossState> extends BossLikePathfinderMob {
    public static final EntityDataAccessor<Vector3f> ABILITY_POS = SynchedEntityData.defineId(StateMachineBoss.class, EntityDataSerializers.VECTOR3);
    public static final EntityDataAccessor<Integer> BOSS_PHASE = SynchedEntityData.defineId(StateMachineBoss.class, EntityDataSerializers.INT);
    private static final float[] NO_PHASES = new float[0];
    public static final int DEATH_ANIMATION_TICKS = 80;
    public static final byte IMPACT_EVENT = 90;
    private static final int SLEEP_AFTER_NO_TARGET_TICKS = 200;

    private final Map<S, AnimationState> stateAnimations;
    private final List<AnimationState> boundAnimations = new ArrayList<>();
    protected final Queue<S> tactics = new ArrayDeque<>();
    protected final Deque<S> combo = new ArrayDeque<>();
    private boolean phaseShiftPending = false;
    private boolean phaseLockBypass = false;
    public int clientStateTicks = 0;
    protected int noTargetTicks = 0;
    protected int deathTicks = 0;
    protected float lockedYaw = 0.0F;
    private boolean internalStateChange = false;
    private int impactTick = Integer.MIN_VALUE;
    private Vec3 impactPos = Vec3.ZERO;
    @Nullable
    private LivingEntity cachedTarget;
    private int cachedTargetTick = Integer.MIN_VALUE;

    protected StateMachineBoss(EntityType<? extends BossLikePathfinderMob> entityType, Level level, Class<S> stateClass) {
        super(entityType, level);
        this.stateAnimations = new EnumMap<>(stateClass);
        this.xpReward = XP_REWARD_BOSS;
    }

    protected abstract EntityDataAccessor<S> stateAccessor();

    protected abstract S initialState();

    protected abstract S standingState();

    protected abstract S idlingState();

    protected abstract S sleepTransitionState();

    protected abstract S wakeTransitionState();

    protected abstract S dyingState();

    protected abstract void tickAbility(S state, int ticks, @Nullable LivingEntity target);

    protected abstract void tickAbilityClient(S state, int ticks);

    protected abstract void planTactics(LivingEntity target, Queue<S> plan);

    public abstract float alphaIntensity();

    protected int transitionTicks() {
        return 15;
    }

    protected int decisionDelay() {
        return 40;
    }

    protected double chaseSpeed() {
        return 1.0D;
    }

    protected double engageDistance() {
        return 2.0D;
    }

    protected double acquireRange() {
        return 16.0D;
    }

    protected boolean isImmuneTo(S state, DamageSource source) {
        return false;
    }

    protected void onDamageBlocked(DamageSource source) {
    }

    protected boolean ignoresFallDamage(S state) {
        return false;
    }

    @Nullable
    protected LivingEntity rememberedTarget() {
        return null;
    }

    protected void onTargetAcquired(LivingEntity target) {
    }

    protected void onStateChanged(S previous, S next) {
    }

    protected float[] phaseThresholds() {
        return NO_PHASES;
    }

    @Nullable
    protected S phaseTransitionState(int phase) {
        return null;
    }

    protected void onPhaseChanged(int phase) {
    }

    protected BossEvent.BossBarColor phaseBarColor(int phase) {
        return this.bossInfo().getColor();
    }

    public int getBossPhase() {
        return this.entityData.get(BOSS_PHASE);
    }

    public boolean isFinalPhase() {
        return this.getBossPhase() >= this.phaseThresholds().length;
    }

    private float nextPhaseFloor() {
        float[] thresholds = this.phaseThresholds();
        int phase = this.getBossPhase();
        return phase < thresholds.length ? thresholds[phase] * this.getMaxHealth() : 0.0F;
    }

    @Override
    public void setHealth(float health) {
        if (!this.level().isClientSide() && !this.phaseLockBypass && this.tickCount > 0 && this.isAlive() && !this.phaseShiftPending) {
            float floor = this.nextPhaseFloor();
            if (floor > 0.0F && health < floor) {
                health = floor;
                this.phaseShiftPending = true;
            }
        } else if (this.phaseShiftPending && !this.phaseLockBypass && health < this.nextPhaseFloor()) {
            health = this.nextPhaseFloor();
        }
        super.setHealth(health);
    }

    private void advancePhase() {
        int next = this.getBossPhase() + 1;
        this.entityData.set(BOSS_PHASE, next);
        this.tactics.clear();
        this.combo.clear();
        this.applyPhaseVisuals();
        this.onPhaseChanged(next);
        S transition = this.phaseTransitionState(next);
        if (transition != null) {
            this.getNavigation().stop();
            this.setBossState(transition);
        }
    }

    protected void applyPhaseVisuals() {
        this.bossInfo().setColor(this.phaseBarColor(this.getBossPhase()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("BossPhase", this.getBossPhase());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        int phase;
        if (compound.contains("BossPhase")) {
            phase = compound.getInt("BossPhase");
        } else {
            phase = 0;
            for (float threshold : this.phaseThresholds()) {
                if (this.getHealth() < threshold * this.getMaxHealth()) {
                    phase++;
                }
            }
        }
        this.entityData.set(BOSS_PHASE, Mth.clamp(phase, 0, this.phaseThresholds().length));
        this.applyPhaseVisuals();
    }

    @SafeVarargs
    protected final void bindAnimation(AnimationState animation, S... states) {
        if (!this.boundAnimations.contains(animation)) {
            this.boundAnimations.add(animation);
        }
        for (S state : states) {
            this.stateAnimations.put(state, animation);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(this.stateAccessor(), this.initialState());
        this.entityData.define(ABILITY_POS, new Vector3f());
        this.entityData.define(BOSS_PHASE, 0);
    }

    public S getBossState() {
        return this.entityData.get(this.stateAccessor());
    }

    public void setBossState(S state) {
        S previous = this.getBossState();
        if (previous != state) {
            this.entityData.set(this.stateAccessor(), state);
            this.stateTicks = 0;
            if (!this.internalStateChange && state.isAttack()) {
                this.tactics.clear();
            }
            this.onStateChanged(previous, state);
        }
    }

    protected void chainState(S state) {
        this.internalStateChange = true;
        this.setBossState(state);
        this.internalStateChange = false;
    }

    protected void returnToStanding() {
        this.combo.clear();
        this.setBossState(this.standingState());
    }

    public boolean isAwake() {
        S state = this.getBossState();
        return state != this.idlingState() && state != this.sleepTransitionState() && state != this.dyingState();
    }

    public float getIdleTransitionSpeed() {
        return this.getBossState() == this.wakeTransitionState() ? -1.0F : 1.0F;
    }

    public Vec3 getAbilityPos() {
        Vector3f pos = this.entityData.get(ABILITY_POS);
        return new Vec3(pos.x(), pos.y(), pos.z());
    }

    protected void setAbilityPos(Vec3 pos) {
        this.entityData.set(ABILITY_POS, new Vector3f((float) pos.x, (float) pos.y, (float) pos.z));
    }

    protected void broadcastImpact() {
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, IMPACT_EVENT);
        }
    }

    protected void onImpactClient(S state) {
    }

    public float ticksSinceImpact(float partialTick) {
        return this.impactTick == Integer.MIN_VALUE ? Float.MAX_VALUE : this.tickCount - this.impactTick + partialTick;
    }

    public Vec3 getImpactPos() {
        return this.impactPos;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == IMPACT_EVENT) {
            this.impactTick = this.tickCount;
            this.impactPos = this.position();
            this.onImpactClient(this.getBossState());
        } else {
            super.handleEntityEvent(id);
        }
    }

    public float getDeathProgress(float partialTick) {
        return Mth.clamp((this.deathTicks + partialTick) / DEATH_ANIMATION_TICKS, 0.0F, 1.0F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (this.stateAccessor().equals(accessor)) {
            this.clientStateTicks = 0;
            if (this.level().isClientSide() && this.stateAnimations != null) {
                this.refreshAnimations();
            }
        }
    }

    private void refreshAnimations() {
        for (AnimationState animation : this.boundAnimations) {
            animation.stop();
        }
        AnimationState animation = this.stateAnimations.get(this.getBossState());
        if (animation != null) {
            animation.start(this.tickCount);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide()) {
            this.clientStateTicks++;
            this.tickAbilityClient(this.getBossState(), this.clientStateTicks);
        } else if (this.isAlive()) {
            this.tickServerState();
        }
    }

    private void tickServerState() {
        LivingEntity target = this.getPrimaryTarget();
        this.noTargetTicks = target == null ? this.noTargetTicks + 1 : 0;
        this.stateTicks++;
        S state = this.getBossState();
        if (this.phaseShiftPending && state != this.dyingState()) {
            this.phaseShiftPending = false;
            this.advancePhase();
            return;
        }
        if (state == this.standingState()) {
            this.tickStanding(target);
        } else if (state == this.sleepTransitionState()) {
            this.tactics.clear();
            if (target != null) {
                this.setBossState(this.wakeTransitionState());
            } else if (this.stateTicks >= this.transitionTicks()) {
                this.setBossState(this.idlingState());
            }
        } else if (state == this.idlingState()) {
            this.getNavigation().stop();
            if (target != null) {
                this.setBossState(this.wakeTransitionState());
            }
        } else if (state == this.wakeTransitionState()) {
            this.getNavigation().stop();
            if (target != null) {
                this.faceTowards(target, 12.0F);
            }
            if (this.stateTicks >= this.transitionTicks()) {
                this.returnToStanding();
            }
        } else if (state != this.dyingState()) {
            this.tickAbility(state, this.stateTicks, target);
            if (this.getBossState() == state && state.duration() > 0 && this.stateTicks >= state.duration()) {
                S next = this.combo.poll();
                if (next != null) {
                    this.chainState(next);
                } else {
                    this.returnToStanding();
                }
            }
        }
    }

    protected void tickStanding(@Nullable LivingEntity target) {
        if (target == null) {
            this.tickStandingWithoutTarget();
            return;
        }
        this.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double engage = this.engageDistance() + this.halfWidth() + target.getBbWidth() * 0.5D;
        if (this.distanceToSqr(target) > engage * engage) {
            this.chase(target);
        } else {
            this.getNavigation().stop();
        }
        if (this.stateTicks >= this.decisionDelay()) {
            if (this.tactics.isEmpty()) {
                this.planTactics(target, this.tactics);
            }
            S next = this.tactics.poll();
            if (next != null) {
                this.chainState(next);
            }
        }
    }

    protected void chase(LivingEntity target) {
        if (this.tickCount % 5 == 0 || this.getNavigation().isDone()) {
            this.getNavigation().moveTo(target, this.chaseSpeed());
        }
    }

    protected void tickStandingWithoutTarget() {
        if (this.onGround() && this.noTargetTicks > SLEEP_AFTER_NO_TARGET_TICKS) {
            this.getNavigation().stop();
            this.setBossState(this.sleepTransitionState());
        }
    }

    @Nullable
    public LivingEntity getPrimaryTarget() {
        if (this.cachedTargetTick != this.tickCount) {
            this.cachedTargetTick = this.tickCount;
            this.cachedTarget = this.resolveTarget();
        }
        return this.isValidTarget(this.cachedTarget) ? this.cachedTarget : null;
    }

    protected boolean isValidTarget(@Nullable LivingEntity target) {
        if (target == null || !target.isAlive() || target.isRemoved() || target.level() != this.level()) {
            return false;
        }
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
        }
        double range = this.getAttributeValue(Attributes.FOLLOW_RANGE) * 1.5D;
        return this.distanceToSqr(target) <= range * range;
    }

    @Nullable
    private LivingEntity resolveTarget() {
        LivingEntity target = this.getTarget();
        if (this.isValidTarget(target)) {
            return target;
        }
        target = this.rememberedTarget();
        if (this.isValidTarget(target)) {
            this.setTarget(target);
            return target;
        }
        if (this.getTarget() != null) {
            this.setTarget(null);
        }
        Player nearest = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), this.acquireRange(), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        if (this.isValidTarget(nearest)) {
            this.setTarget(nearest);
            this.onTargetAcquired(nearest);
            return nearest;
        }
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() == this || source.getDirectEntity() == this) {
            return false;
        }
        boolean bypass = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        S current = this.getBossState();
        if (!this.level().isClientSide() && !bypass && (current.isPhaseTransition() || this.isImmuneTo(current, source))) {
            this.onDamageBlocked(source);
            return false;
        }
        this.phaseLockBypass = bypass;
        boolean result;
        try {
            result = super.hurt(source, amount);
        } finally {
            this.phaseLockBypass = false;
        }
        if (result && !this.level().isClientSide() && source.getEntity() instanceof LivingEntity attacker
                && !attacker.isAlliedTo(this) && this.isValidTarget(attacker)) {
            this.setTarget(attacker);
            this.cachedTargetTick = Integer.MIN_VALUE;
            this.onTargetAcquired(attacker);
            S state = this.getBossState();
            if (state == this.idlingState() || state == this.sleepTransitionState()) {
                this.setBossState(this.wakeTransitionState());
            }
        }
        return result;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return !this.ignoresFallDamage(this.getBossState()) && super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide()) {
            this.tactics.clear();
            this.combo.clear();
            this.phaseShiftPending = false;
            this.getNavigation().stop();
            this.setBossState(this.dyingState());
        }
        super.die(source);
    }

    @Override
    protected void tickDeath() {
        this.deathTicks++;
        if (this.deathTicks >= DEATH_ANIMATION_TICKS && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, EntityEvent.POOF);
            this.remove(RemovalReason.KILLED);
        }
    }

    protected void trackThenLock(@Nullable LivingEntity target, int ticks, int lockTick, float step) {
        if (ticks < lockTick && target != null) {
            this.faceTowards(target, step);
            this.lockedYaw = this.getYRot();
        } else {
            this.setFacing(this.lockedYaw);
        }
    }

    protected boolean canReach(LivingEntity target) {
        Path path = this.getNavigation().createPath(target, 0);
        return path != null && path.canReach();
    }

    protected double gapTo(LivingEntity target) {
        return Math.sqrt(horizontalDistanceSqr(target.position(), this.position())) - this.halfWidth() - target.getBbWidth() * 0.5D;
    }

    protected void playClientSound(SoundEvent sound, float volume, float pitch) {
        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), sound, this.getSoundSource(), volume, pitch, false);
    }

    protected Vec3 localPoint(double forward, double up, double left) {
        Vec3 f = this.horizontalForward();
        Vec3 l = this.horizontalLeft();
        return this.position().add(f.scale(forward)).add(l.scale(left)).add(0.0D, up, 0.0D);
    }
}