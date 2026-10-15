package net.sashakyotoz.common.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.entities.util.BossParticles;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public class WarriorOfChimericDarkness extends MultipartBoss<WarriorOfChimericDarkness.WarriorPose> {
    public static final EntityDataSerializer<WarriorPose> WARRIOR_STATE = EntityDataSerializer.simpleEnum(WarriorPose.class);
    protected static final EntityDataAccessor<WarriorPose> PHASE = SynchedEntityData.defineId(WarriorOfChimericDarkness.class, WARRIOR_STATE);

    public static final int ABYSS_VIOLET = 0x5B1E8C;
    public static final int GRIP_GLOW = 0xB86BFF;
    public static final int VOID = 0x0D0414;
    private static final DustParticleOptions VIOLET_DUST = BossParticles.dust(ABYSS_VIOLET, 1.6F);
    private static final DustParticleOptions GLOW_DUST = BossParticles.dust(GRIP_GLOW, 1.0F);
    private static final DustColorTransitionOptions SHADOW_DUST = BossParticles.fade(GRIP_GLOW, VOID, 1.8F);

    public static final int SHIELDED_KNOCKBACK_HIT_TICK = 20;
    public static final int SMASH_HIT_TICK = 20;
    public static final int CHARGE_START_TICK = 10;
    public static final int CHARGE_END_TICK = 30;
    public static final int CHARGED_STRIKE_HIT_TICK = 40;
    public static final int FISSURE_SLAM_TICK = 25;
    public static final int FISSURE_LENGTH = 16;
    public static final double CHARGED_STRIKE_RADIUS = 7.0D;
    private static final int SHIELD_COOLDOWN = 120;
    private static final float STAGGER_THRESHOLD = 30.0F;

    public final AnimationState idleTransition = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState shielded_knockback = new AnimationState();
    public final AnimationState smash = new AnimationState();
    public final AnimationState shield = new AnimationState();
    public final AnimationState charged_strike = new AnimationState();
    public final AnimationState fissure = new AnimationState();
    public final AnimationState death = new AnimationState();

    private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("entity.unseen_world.warrior_of_chimeric_darkness"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_6);
    private final Set<UUID> fissureHits = new HashSet<>();

    private Vec3 fissureOrigin = Vec3.ZERO;
    private Vec3 fissureDirection = Vec3.ZERO;
    private boolean fissureBlocked;
    private int lastShieldTick = -1000;
    private int lastFissureTick = -1000;
    private float chargeDamageTaken;

    public WarriorOfChimericDarkness(EntityType<? extends BossLikePathfinderMob> entityType, Level level) {
        super(entityType, level, WarriorPose.class,
                new PartLayout("body", 0.6F, 0.8F, 1.25D, 1.75D, false),
                new PartLayout("backCrack", 0.6F, 0.8F, -1.1D, 1.75D, true));
        this.setMaxUpStep(1.5F);
        if (this.getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanFloat(true);
            navigation.setCanWalkOverFences(true);
        }
        this.bindAnimation(this.idleTransition, WarriorPose.AWAKE_TO_IDLE, WarriorPose.IDLE_TO_AWAKE);
        this.bindAnimation(this.idle, WarriorPose.IDLING);
        this.bindAnimation(this.shielded_knockback, WarriorPose.SHIELDED_KNOCKBACK);
        this.bindAnimation(this.smash, WarriorPose.SMASH);
        this.bindAnimation(this.shield, WarriorPose.SHIELD);
        this.bindAnimation(this.charged_strike, WarriorPose.CHARGED_STRIKE);
        this.bindAnimation(this.fissure, WarriorPose.FISSURE);
        this.bindAnimation(this.death, WarriorPose.DYING);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 350.0D)
                .add(Attributes.ARMOR, 14.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.ATTACK_DAMAGE, 18.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected EntityDataAccessor<WarriorPose> stateAccessor() {
        return PHASE;
    }

    @Override
    protected WarriorPose initialState() {
        return WarriorPose.IDLING;
    }

    @Override
    protected WarriorPose standingState() {
        return WarriorPose.STANDING;
    }

    @Override
    protected WarriorPose idlingState() {
        return WarriorPose.IDLING;
    }

    @Override
    protected WarriorPose sleepTransitionState() {
        return WarriorPose.AWAKE_TO_IDLE;
    }

    @Override
    protected WarriorPose wakeTransitionState() {
        return WarriorPose.IDLE_TO_AWAKE;
    }

    @Override
    protected WarriorPose dyingState() {
        return WarriorPose.DYING;
    }

    @Override
    protected int decisionDelay() {
        return this.isEnraged() ? 18 : 30;
    }

    @Override
    protected double chaseSpeed() {
        return 0.9D;
    }

    @Override
    protected double engageDistance() {
        return 2.5D;
    }

    public boolean isEnraged() {
        return this.getHealth() < this.getMaxHealth() * 0.4F;
    }

    public WarriorPose getWarriorPose() {
        return this.getBossState();
    }

    public boolean isShielding() {
        WarriorPose pose = this.getBossState();
        return pose == WarriorPose.SHIELDED_KNOCKBACK || pose == WarriorPose.SHIELD;
    }

    public float getChargeProgress(float partialTick) {
        if (this.getBossState() != WarriorPose.CHARGED_STRIKE) {
            return 0.0F;
        }
        return Mth.clamp((this.clientStateTicks + partialTick - CHARGE_START_TICK) / (CHARGED_STRIKE_HIT_TICK - CHARGE_START_TICK), 0.0F, 1.0F);
    }

    @Override
    protected void onStateChanged(WarriorPose previous, WarriorPose next) {
        if (next.isAttack()) {
            this.getNavigation().stop();
            this.lockedYaw = this.getYRot();
        }
        switch (next) {
            case CHARGED_STRIKE -> this.chargeDamageTaken = 0.0F;
            case FISSURE -> {
                this.lastFissureTick = this.tickCount;
                this.fissureHits.clear();
                this.fissureBlocked = false;
            }
            case SHIELD -> this.lastShieldTick = this.tickCount;
            default -> {
            }
        }
    }

    @Override
    protected boolean isImmuneTo(WarriorPose state, DamageSource source) {
        if (state == WarriorPose.SHIELDED_KNOCKBACK) {
            return true;
        }
        return state == WarriorPose.SHIELD && source.is(DamageTypeTags.IS_PROJECTILE);
    }

    @Override
    protected void onDamageBlocked(DamageSource source) {
        this.playSound(SoundEvents.SHIELD_BLOCK, 1.5F, 0.6F + this.random.nextFloat() * 0.2F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide() && this.isAlive() && source.is(DamageTypeTags.IS_PROJECTILE)
                && this.getBossState() == WarriorPose.STANDING && this.tickCount - this.lastShieldTick > SHIELD_COOLDOWN) {
            this.setBossState(WarriorPose.SHIELD);
            this.onDamageBlocked(source);
            return false;
        }
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && this.getBossState() == WarriorPose.CHARGED_STRIKE
                && this.stateTicks >= CHARGE_START_TICK && this.stateTicks < CHARGE_END_TICK) {
            this.chargeDamageTaken += amount;
            if (this.chargeDamageTaken >= STAGGER_THRESHOLD) {
                this.stagger();
            }
        }
        return result;
    }

    private void stagger() {
        this.playSound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 2.0F, 0.5F);
        this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, this.getX(), this.getY(0.6D), this.getZ(), 30, 0.8D, 0.8D, 0.8D, 0.2D);
        this.returnToStanding();
        this.stateTicks = -30;
    }

    @Override
    protected void tickAbility(WarriorPose state, int ticks, @Nullable LivingEntity target) {
        switch (state) {
            case SHIELDED_KNOCKBACK -> {
                this.trackThenLock(target, ticks, SHIELDED_KNOCKBACK_HIT_TICK - 6, 8.0F);
                if (ticks == SHIELDED_KNOCKBACK_HIT_TICK) {
                    this.performShieldedKnockback();
                }
            }
            case SMASH -> {
                this.trackThenLock(target, ticks, SMASH_HIT_TICK - 5, 8.0F);
                if (ticks == SMASH_HIT_TICK) {
                    this.performSmash();
                }
            }
            case CHARGED_STRIKE -> {
                this.trackThenLock(target, ticks, CHARGE_START_TICK, 8.0F);
                if (ticks == CHARGE_START_TICK) {
                    this.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 2.0F, 0.5F);
                    this.playSound(SoundEvents.WARDEN_HEARTBEAT, 2.0F, 0.8F);
                }
                if (ticks > CHARGE_START_TICK && ticks < CHARGE_END_TICK && ticks % 5 == 0) {
                    this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, this.getX(), this.getY(), this.getZ(), 6, CHARGED_STRIKE_RADIUS * 0.5D, 0.1D, CHARGED_STRIKE_RADIUS * 0.5D, 0.02D);
                }
                if (ticks == CHARGED_STRIKE_HIT_TICK) {
                    this.performChargedStrike();
                }
            }
            case FISSURE -> this.tickFissure(ticks, target);
            case SHIELD -> {
                if (target != null) {
                    this.faceTowards(target, 15.0F);
                }
            }
            default -> {
            }
        }
    }

    private void performShieldedKnockback() {
        Vec3 forward = this.horizontalForward();
        for (LivingEntity entity : this.enemiesInLine(0.0D, this.halfWidth() + 4.0D, 1.25D, 3.5D)) {
            this.strike(entity, 0.8F);
            this.impulse(entity, forward.scale(1.6D).add(0.0D, 0.4D, 0.0D));
            this.grip(entity, 2);
        }
        this.playSound(SoundEvents.SHIELD_BLOCK, 2.0F, 0.5F);
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 2.0F, 0.6F);
        Vec3 front = this.localPoint(this.halfWidth() + 1.0D, 1.5D, 0.0D);
        this.serverParticles(ParticleTypes.CLOUD, front.x, front.y, front.z, 16, 0.6D, 0.8D, 0.6D, 0.15D);
        this.broadcastImpact();
    }

    private void performSmash() {
        Vec3 forward = this.horizontalForward();
        Vec3 left = this.horizontalLeft();
        double front = this.halfWidth();
        for (LivingEntity entity : this.enemiesInLine(0.0D, front + 3.0D, 1.4D, 3.5D)) {
            this.strike(entity, 1.5F);
            this.impulse(entity, forward.scale(1.2D).add(0.0D, 0.35D, 0.0D));
            this.grip(entity, 3);
        }
        if (this.canGrief()) {
            int broken = 0;
            for (int depth = 1; depth <= 3; depth++) {
                for (int side = -1; side <= 1; side++) {
                    for (int up = 0; up < 4; up++) {
                        Vec3 point = this.position().add(forward.scale(front + depth - 0.5D)).add(left.scale(side)).add(0.0D, up + 0.5D, 0.0D);
                        if (this.breakBlock(BlockPos.containing(point), true)) {
                            broken++;
                        }
                    }
                }
            }
            if (broken > 0) {
                this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 2.0F, 0.6F);
            }
        }
        Vec3 impact = this.position().add(forward.scale(front + 1.5D));
        this.groundBurst(impact, 40, 0.9D);
        this.serverParticles(VIOLET_DUST, impact.x, impact.y + 0.4D, impact.z, 20, 1.0D, 0.4D, 1.0D, 0.0D);
        this.playSound(SoundEvents.ANVIL_LAND, 2.0F, 0.5F);
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 2.0F, 0.5F);
        this.broadcastImpact();
    }

    private void performChargedStrike() {
        Vec3 center = this.position();
        for (LivingEntity entity : this.enemiesInRadius(CHARGED_STRIKE_RADIUS, 4.0D)) {
            double distance = Math.sqrt(horizontalDistanceSqr(entity.position(), center));
            float falloff = (float) Mth.clamp(1.0D - distance / (CHARGED_STRIKE_RADIUS * 1.6D), 0.55D, 1.0D);
            this.strike(entity, 1.2F * falloff);
            this.impulse(entity, new Vec3(0.0D, 1.25D * falloff, 0.0D));
            this.grip(entity, 8);
        }
        this.level().playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 2.5F, 0.5F);
        this.playSound(SoundEvents.ANVIL_LAND, 2.5F, 0.4F);
        this.serverParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 0.2D, center.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        for (int i = 0; i < 20; i++) {
            double angle = i * Math.PI / 10.0D;
            double radius = 1.5D + (i % 4) * 1.6D;
            Vec3 point = center.add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius);
            this.groundBurst(point, 8, 0.4D);
            this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, point.x, point.y + 0.2D, point.z, 2, 0.2D, 0.1D, 0.2D, 0.3D);
        }
        this.broadcastImpact();
    }

    private void tickFissure(int ticks, @Nullable LivingEntity target) {
        if (ticks < FISSURE_SLAM_TICK - 5) {
            if (target != null) {
                this.faceTowards(target, 8.0F);
            }
            this.lockedYaw = this.getYRot();
            return;
        }
        this.setFacing(this.lockedYaw);
        if (ticks == FISSURE_SLAM_TICK) {
            this.fissureDirection = this.horizontalForward();
            this.fissureOrigin = this.position().add(this.fissureDirection.scale(this.halfWidth() + 0.5D));
            this.setAbilityPos(this.fissureOrigin.add(this.fissureDirection.scale(FISSURE_LENGTH)));
            this.playSound(SoundEvents.ANVIL_LAND, 2.0F, 0.4F);
            this.playSound(SoundEvents.WARDEN_SONIC_BOOM, 1.0F, 0.5F);
            this.broadcastImpact();
        }
        int step = ticks - FISSURE_SLAM_TICK;
        if (step >= 0 && step < FISSURE_LENGTH && !this.fissureBlocked) {
            Vec3 flat = this.fissureOrigin.add(this.fissureDirection.scale(step));
            Vec3 ground = this.snapToGround(new Vec3(flat.x, this.getY() + 3.0D, flat.z), 7);
            if (ground.y - this.getY() > 2.5D) {
                this.fissureBlocked = true;
                return;
            }
            AABB eruption = new AABB(ground.x - 1.0D, ground.y - 0.5D, ground.z - 1.0D, ground.x + 1.0D, ground.y + 4.5D, ground.z + 1.0D);
            for (LivingEntity entity : this.enemiesIn(eruption)) {
                if (this.fissureHits.add(entity.getUUID())) {
                    this.strike(entity, 1F);
                    this.impulse(entity, this.fissureDirection.scale(0.25D).add(0.0D, 0.75D, 0.0D));
                    this.grip(entity, 5);
                }
            }
            this.groundBurst(ground, 14, 0.45D);
            this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, ground.x, ground.y + 0.3D, ground.z, 3, 0.25D, 0.2D, 0.25D, 0.25D);
            if (step % 3 == 0) {
                this.level().playSound(null, ground.x, ground.y, ground.z, SoundEvents.DEEPSLATE_BREAK, SoundSource.HOSTILE, 1.5F, 0.6F);
            }
        }
    }

    @Override
    protected void planTactics(LivingEntity target, Queue<WarriorPose> plan) {
        double gap = this.gapTo(target);
        double dy = target.getY() - this.getY();
        boolean fissureReady = this.tickCount - this.lastFissureTick > (this.isEnraged() ? 100 : 180);
        int roll = this.random.nextInt(3);

        if ((dy > 2.0D || !this.canReach(target)) && gap <= FISSURE_LENGTH) {
            if (gap <= 3.0D) {
                plan.add(WarriorPose.SMASH);
            } else if (fissureReady) {
                plan.add(WarriorPose.FISSURE);
            } else {
                plan.add(WarriorPose.SMASH);
            }
            return;
        }
        if (gap <= 3.5D) {
            switch (roll) {
                case 0 -> {
                    plan.add(WarriorPose.SMASH);
                    plan.add(WarriorPose.SHIELDED_KNOCKBACK);
                }
                case 1 -> {
                    plan.add(WarriorPose.SHIELDED_KNOCKBACK);
                    plan.add(WarriorPose.CHARGED_STRIKE);
                }
                default -> {
                    plan.add(WarriorPose.SMASH);
                    plan.add(WarriorPose.CHARGED_STRIKE);
                }
            }
        } else if (gap <= CHARGED_STRIKE_RADIUS - 1.0D && roll == 0) {
            plan.add(WarriorPose.CHARGED_STRIKE);
        } else if (gap <= FISSURE_LENGTH && fissureReady && (gap > 6.0D || roll == 1)) {
            plan.add(WarriorPose.FISSURE);
        }
    }

    @Override
    protected void tickAbilityClient(WarriorPose state, int ticks) {
        Level level = this.level();
        Vec3 chest = this.position().add(0.0D, this.getBbHeight() * 0.6D, 0.0D);
        switch (state) {
            case IDLING -> {
                if (this.tickCount % 5 == 0) {
                    BossParticles.motes(level, this.random, SHADOW_DUST, this.position(), 1.6D, 0.4D, 1, 0.01D);
                }
            }
            case IDLE_TO_AWAKE -> {
                if (ticks == 1) {
                    this.playClientSound(SoundEvents.WARDEN_EMERGE, 1.0F, 1.4F);
                    BossParticles.ring(level, SHADOW_DUST, this.position().add(0.0D, 0.1D, 0.0D), 1.5D, 24, 0.15D, 0.05D);
                }
            }
            case SHIELDED_KNOCKBACK, SHIELD -> {
                if (ticks % 3 == 0) {
                    BossParticles.dome(level, this.random, GLOW_DUST, this.position(), this.halfWidth() + 1.0D, 6);
                }
            }
            case SMASH -> {
                if (ticks > 8 && ticks < SMASH_HIT_TICK) {
                    level.addParticle(GLOW_DUST, this.getRandomX(1.2D), this.getY() + this.getBbHeight() * (0.7D + this.random.nextDouble() * 0.3D), this.getRandomZ(1.2D), 0.0D, 0.05D, 0.0D);
                }
            }
            case CHARGED_STRIKE -> {
                if (ticks >= CHARGE_START_TICK && ticks < CHARGED_STRIKE_HIT_TICK) {
                    float progress = this.getChargeProgress(0.0F);
                    Vec3 hammer = this.position().add(0.0D, this.getBbHeight() + 0.8D, 0.0D);
                    BossParticles.converge(level, this.random, ModParticleTypes.GRIPPING_CRYSTAL, hammer, 3.0D, 1);
                    BossParticles.converge(level, this.random, ParticleTypes.PORTAL, hammer, 2.5D, 2 + (int) (progress * 4.0F));
                    if (ticks % 4 == 0) {
                        BossParticles.ring(level, VIOLET_DUST, this.position().add(0.0D, 0.15D, 0.0D), CHARGED_STRIKE_RADIUS, 40, 0.0D, 0.0D);
                    }
                }
            }
            case FISSURE -> {
                if (ticks > 5 && ticks < FISSURE_SLAM_TICK) {
                    Vec3 hammer = this.position().add(0.0D, this.getBbHeight() + 0.6D, 0.0D);
                    level.addParticle(GLOW_DUST, hammer.x + this.random.nextGaussian() * 0.3D, hammer.y, hammer.z + this.random.nextGaussian() * 0.3D, 0.0D, 0.0D, 0.0D);
                }
                int step = ticks - FISSURE_SLAM_TICK;
                if (step >= 0 && step < FISSURE_LENGTH) {
                    Vec3 end = this.getAbilityPos();
                    Vec3 origin = this.position().add(this.horizontalForward().scale(this.halfWidth() + 0.5D));
                    Vec3 direction = new Vec3(end.x - origin.x, 0.0D, end.z - origin.z);
                    if (direction.lengthSqr() > 1.0E-4D) {
                        Vec3 point = origin.add(direction.normalize().scale(step));
                        BossParticles.column(level, this.random, SHADOW_DUST, new Vec3(point.x, this.getY(), point.z), 0.6D, 6, 0.25D);
                        level.addParticle(ParticleTypes.SQUID_INK, point.x, this.getY() + 0.3D, point.z, 0.0D, 0.2D, 0.0D);
                    }
                }
            }
            case DYING -> {
                double progress = Math.min(1.0D, ticks / (double) DEATH_ANIMATION_TICKS);
                BossParticles.motes(level, this.random, SHADOW_DUST, this.position(), 1.4D, this.getBbHeight(), 2 + (int) (progress * 4.0D), 0.04D);
                if (ticks % 10 == 0) {
                    BossParticles.burst(level, this.random, ModParticleTypes.GRIPPING_CRYSTAL, chest, 6, 0.3D);
                }
                if (ticks == DEATH_ANIMATION_TICKS - 2) {
                    BossParticles.burst(level, this.random, SHADOW_DUST, chest, 60, 0.6D);
                    BossParticles.ring(level, ParticleTypes.LARGE_SMOKE, this.position().add(0.0D, 0.2D, 0.0D), 1.5D, 30, 0.3D, 0.02D);
                    this.playClientSound(SoundEvents.WARDEN_DEATH, 1.5F, 0.6F);
                }
            }
            default -> {
                if (this.isAwake() && this.tickCount % 3 == 0) {
                    BossParticles.motes(level, this.random, SHADOW_DUST, this.position(), this.halfWidth() + 0.2D, this.getBbHeight() * 0.5D, 1, 0.02D);
                }
            }
        }
    }

    @Override
    protected void onImpactClient(WarriorPose state) {
        Level level = this.level();
        Vec3 base = this.position().add(0.0D, 0.15D, 0.0D);
        switch (state) {
            case CHARGED_STRIKE -> {
                BossParticles.ring(level, ParticleTypes.CLOUD, base, 1.5D, 48, 1.0D, 0.08D);
                BossParticles.ring(level, SHADOW_DUST, base, CHARGED_STRIKE_RADIUS, 56, 0.0D, 0.1D);
                BossParticles.column(level, this.random, ModParticleTypes.GRIPPING_CRYSTAL, base, CHARGED_STRIKE_RADIUS, 30, 0.6D);
            }
            case SMASH -> {
                Vec3 impact = this.localPoint(this.halfWidth() + 1.5D, 0.15D, 0.0D);
                BossParticles.ring(level, ParticleTypes.CLOUD, impact, 0.6D, 20, 0.4D, 0.04D);
            }
            case SHIELDED_KNOCKBACK -> BossParticles.verticalRing(level, GLOW_DUST, this.localPoint(this.halfWidth() + 0.8D, 1.6D, 0.0D), this.horizontalForward(), 1.4D, 24, this.tickCount * 0.2D);
            case FISSURE -> BossParticles.ring(level, ParticleTypes.CLOUD, this.localPoint(this.halfWidth() + 0.6D, 0.15D, 0.0D), 0.8D, 18, 0.35D, 0.02D);
            default -> {
            }
        }
    }

    @Override
    public float alphaIntensity() {
        return switch (this.getBossState()) {
            case IDLING, AWAKE_TO_IDLE -> 0.1F;
            case CHARGED_STRIKE -> 0.4F + 0.6F * this.getChargeProgress(0.0F);
            case SHIELDED_KNOCKBACK, SHIELD -> 0.8F;
            case SMASH, FISSURE -> 0.6F;
            case DYING -> 0.0F;
            default -> 0.3F;
        };
    }

    @Override
    public ServerBossEvent bossInfo() {
        return this.bossBar;
    }

    @Override
    public boolean haveToDropLoot(DamageSource source) {
        return source.getEntity() instanceof Player;
    }

    public enum WarriorPose implements BossState {
        STANDING(0, 0, false),
        SHIELDED_KNOCKBACK(1, 40, true),
        SMASH(2, 40, true),
        FISSURE(3, 45, true),
        SHIELD(4, 20, false),
        CHARGED_STRIKE(5, 60, true),
        DYING(6, 0, false),
        AWAKE_TO_IDLE(7, 0, false),
        IDLING(8, 0, false),
        IDLE_TO_AWAKE(9, 0, false);

        private final int id;
        private final int duration;
        private final boolean attack;

        WarriorPose(int id, int duration, boolean attack) {
            this.id = id;
            this.duration = duration;
            this.attack = attack;
        }

        public int id() {
            return this.id;
        }

        @Override
        public int duration() {
            return this.duration;
        }

        @Override
        public boolean isAttack() {
            return this.attack;
        }
    }
}
