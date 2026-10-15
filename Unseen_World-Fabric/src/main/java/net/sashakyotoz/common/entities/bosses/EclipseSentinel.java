package net.sashakyotoz.common.entities.bosses;

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
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.util.BossParticles;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;

public class EclipseSentinel extends MultipartBoss<EclipseSentinel.SentinelPose> {
    public static final EntityDataSerializer<SentinelPose> SENTINEL_STATE = EntityDataSerializer.simpleEnum(SentinelPose.class);
    public static final EntityDataAccessor<SentinelPose> PHASE = SynchedEntityData.defineId(EclipseSentinel.class, SENTINEL_STATE);

    public static final int ECLIPSE_PURPLE = 0x9B4DFF;
    public static final int CORONA = 0xD7B8FF;
    public static final int UMBRA = 0x12061F;
    private static final DustParticleOptions PURPLE_DUST = BossParticles.dust(ECLIPSE_PURPLE, 1.3F);
    private static final DustParticleOptions CORONA_DUST = BossParticles.dust(CORONA, 0.9F);
    private static final DustColorTransitionOptions FADING_DUST = BossParticles.fade(ECLIPSE_PURPLE, UMBRA, 1.5F);

    public static final int CLEAVE_HIT_TICK = 15;
    public static final int SLASH_HIT_TICK = 20;
    public static final int DASH_LOCK_TICK = 20;
    public static final int DASH_HIT_TICK = 30;
    public static final int RISE_HIT_TICK = 20;
    public static final int HYPER_TARGET_TICK = 10;
    public static final int HYPER_LAUNCH_TICK = 15;
    public static final int HYPER_LAND_TICK = 44;
    public static final int LASER_CHARGE_TICKS = 15;
    public static final int LASER_FIRE_TICKS = 70;
    public static final double LASER_FORWARD = 1.25D;
    public static final double LASER_HEIGHT = 1.78D;
    public static final int PULSE_HIT_TICK = 20;
    public static final double PULSE_RADIUS = 5.0D;
    private static final double PULSE_PUSH = 2.2D;
    private static final int PULSE_COOLDOWN = 140;
    private static final int MELEE_WINDOW = 60;
    private static final int MELEE_HITS_TO_PULSE = 3;
    private static final int HUG_TICKS_TO_PULSE = 50;
    private static final double HUG_GAP = 2.0D;
    private static final int ECLIPSE_FADE_TICKS = 6;
    private static final double LASER_RANGE = 32.0D;
    private static final double HYPER_APEX = 10.0D;
    private static final double HYPER_MAX_DISTANCE = 18.0D;

    public final AnimationState idleTransition = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState laserTransition = new AnimationState();
    public final AnimationState laser = new AnimationState();
    public final AnimationState cleave = new AnimationState();
    public final AnimationState slash = new AnimationState();
    public final AnimationState rise = new AnimationState();
    public final AnimationState dash = new AnimationState();
    public final AnimationState hyper_burst = new AnimationState();
    public final AnimationState death = new AnimationState();

    private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("entity.unseen_world.eclipse_sentinel"), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.NOTCHED_6);

    private float laserPitch;
    private double dashDistance;
    private Vec3 jumpStart = Vec3.ZERO;
    private Vec3 landing = Vec3.ZERO;
    private boolean landed;
    private int lastHyperBurstTick = -1000;
    private int lastLaserTick = -1000;
    private int lastPulseTick = -1000;
    private int meleeHits;
    private int meleeWindowStart;
    private int hugTicks;
    private boolean pulsePending;

    public EclipseSentinel(EntityType<? extends BossLikePathfinderMob> entityType, Level level) {
        super(entityType, level, SentinelPose.class,
                new PartLayout("body", 0.45F, 0.35F, 0.75D, 1.9D, false),
                new PartLayout("crack", 0.45F, 0.35F, -0.75D, 1.75D, true));
        this.setMaxUpStep(1.5F);
        if (this.getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanFloat(true);
            navigation.setCanWalkOverFences(true);
        }
        this.bindAnimation(this.idleTransition, SentinelPose.AWAKE_TO_IDLE, SentinelPose.IDLE_TO_AWAKE);
        this.bindAnimation(this.idle, SentinelPose.IDLING);
        this.bindAnimation(this.laserTransition, SentinelPose.TO_LASER, SentinelPose.FROM_LASER);
        this.bindAnimation(this.laser, SentinelPose.LASER);
        this.bindAnimation(this.cleave, SentinelPose.CLEAVE);
        this.bindAnimation(this.slash, SentinelPose.SLASH);
        this.bindAnimation(this.rise, SentinelPose.RISE, SentinelPose.ECLIPSE_PULSE);
        this.bindAnimation(this.dash, SentinelPose.DASH);
        this.bindAnimation(this.hyper_burst, SentinelPose.HYPER_BURST);
        this.bindAnimation(this.death, SentinelPose.DYING);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 400.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected EntityDataAccessor<SentinelPose> stateAccessor() {
        return PHASE;
    }

    @Override
    protected SentinelPose initialState() {
        return SentinelPose.IDLING;
    }

    @Override
    protected SentinelPose standingState() {
        return SentinelPose.STANDING;
    }

    @Override
    protected SentinelPose idlingState() {
        return SentinelPose.IDLING;
    }

    @Override
    protected SentinelPose sleepTransitionState() {
        return SentinelPose.AWAKE_TO_IDLE;
    }

    @Override
    protected SentinelPose wakeTransitionState() {
        return SentinelPose.IDLE_TO_AWAKE;
    }

    @Override
    protected SentinelPose dyingState() {
        return SentinelPose.DYING;
    }

    @Override
    protected int decisionDelay() {
        return this.isEnraged() ? 18 : 30;
    }

    @Override
    protected double chaseSpeed() {
        return this.isEnraged() ? 1.2D : 1.0D;
    }

    @Override
    protected double engageDistance() {
        return 2.5D;
    }

    @Override
    protected boolean ignoresFallDamage(SentinelPose state) {
        return state == SentinelPose.HYPER_BURST;
    }

    public boolean isEnraged() {
        return this.getHealth() < this.getMaxHealth() * 0.5F;
    }

    public SentinelPose getSentinelPose() {
        return this.getBossState();
    }

    public float getLaserTransitionSpeed() {
        return this.getBossState() == SentinelPose.FROM_LASER ? -1.0F : 1.0F;
    }

    public Vec3 laserOrigin(float partialTick) {
        float yaw = Mth.rotLerp(partialTick, this.yBodyRotO, this.yBodyRot) * Mth.DEG_TO_RAD;
        double x = Mth.lerp(partialTick, this.xo, this.getX()) - Mth.sin(yaw) * LASER_FORWARD;
        double y = Mth.lerp(partialTick, this.yo, this.getY()) + LASER_HEIGHT;
        double z = Mth.lerp(partialTick, this.zo, this.getZ()) + Mth.cos(yaw) * LASER_FORWARD;
        return new Vec3(x, y, z);
    }

    @Override
    protected void onStateChanged(SentinelPose previous, SentinelPose next) {
        if (next.isAttack()) {
            this.getNavigation().stop();
            this.lockedYaw = this.getYRot();
        }
        switch (next) {
            case ECLIPSE_PULSE -> {
                this.lastPulseTick = this.tickCount;
                this.pulsePending = false;
            }
            case HYPER_BURST -> {
                this.landed = false;
                this.lastHyperBurstTick = this.tickCount;
            }
            case TO_LASER -> {
                this.lastLaserTick = this.tickCount;
                this.laserPitch = 0.0F;
                this.setAbilityPos(this.laserOrigin(1.0F));
            }
            default -> {
            }
        }
    }

    @Override
    protected void tickAbility(SentinelPose state, int ticks, @Nullable LivingEntity target) {
        switch (state) {
            case CLEAVE -> {
                this.trackThenLock(target, ticks, CLEAVE_HIT_TICK - 3, 10.0F);
                if (ticks == CLEAVE_HIT_TICK) {
                    this.performCleave();
                }
            }
            case SLASH -> {
                this.trackThenLock(target, ticks, SLASH_HIT_TICK - 4, 10.0F);
                if (ticks == SLASH_HIT_TICK) {
                    this.performSlash();
                }
            }
            case DASH -> {
                this.trackThenLock(target, ticks, DASH_LOCK_TICK, 8.0F);
                if (ticks == DASH_LOCK_TICK) {
                    this.dashDistance = target == null ? 4.0D : Mth.clamp(this.gapTo(target) + 0.5D, 0.0D, 9.0D);
                    this.playSound(SoundEvents.TRIDENT_RIPTIDE_2, 1.5F, 0.7F);
                }
                if (ticks > DASH_LOCK_TICK + 2 && ticks <= DASH_HIT_TICK) {
                    Vec3 forward = this.horizontalForward();
                    double speed = this.dashDistance / (DASH_HIT_TICK - DASH_LOCK_TICK - 2);
                    this.setDeltaMovement(forward.x * speed, this.getDeltaMovement().y, forward.z * speed);
                }
                if (ticks == DASH_HIT_TICK) {
                    this.performDash();
                }
            }
            case RISE -> {
                if (target != null && ticks < RISE_HIT_TICK - 5) {
                    this.faceTowards(target, 8.0F);
                }
                if (ticks == RISE_HIT_TICK) {
                    this.performRise();
                }
            }
            case ECLIPSE_PULSE -> {
                if (target != null && ticks < PULSE_HIT_TICK - 5) {
                    this.faceTowards(target, 12.0F);
                }
                if (ticks == PULSE_HIT_TICK) {
                    this.performEclipsePulse(target);
                }
            }
            case HYPER_BURST -> this.tickHyperBurst(ticks, target);
            case TO_LASER -> {
                if (target != null) {
                    this.aimAt(target, 6.0F);
                }
                this.setAbilityPos(this.laserOrigin(1.0F));
                if (ticks == 1) {
                    this.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 2.0F, 1.3F);
                }
                if (ticks >= LASER_CHARGE_TICKS) {
                    this.chainState(SentinelPose.LASER);
                }
            }
            case LASER -> {
                this.tickLaser(ticks, target);
                if (ticks >= LASER_FIRE_TICKS) {
                    this.chainState(SentinelPose.FROM_LASER);
                }
            }
            default -> {
            }
        }
    }

    private void aimAt(LivingEntity target, float step) {
        this.faceTowards(target, step);
        Vec3 origin = this.laserOrigin(1.0F);
        double dx = target.getX() - origin.x;
        double dz = target.getZ() - origin.z;
        double dy = target.getY(0.5D) - origin.y;
        float desiredPitch = (float) (-Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * Mth.RAD_TO_DEG);
        this.laserPitch = Mth.clamp(Mth.approachDegrees(this.laserPitch, desiredPitch, step), -60.0F, 60.0F);
    }

    private void performCleave() {
        Vec3 forward = this.horizontalForward();
        double front = this.halfWidth();
        for (LivingEntity entity : this.enemiesInLine(0.0D, front + 3.0D, 1.0D, 3.5D)) {
            this.strike(entity, 1.2F);
            this.impulse(entity, forward.scale(0.4D).add(0.0D, 0.2D, 0.0D));
        }
        for (LivingEntity entity : this.enemiesInLine(front + 4.0D, front + 8.0D, 1.25D, 4.0D)) {
            this.strike(entity, 0.4F);
            this.impulse(entity, forward.scale(0.15D).add(0.0D, 1.15D, 0.0D));
        }
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 2.0F, 0.6F);
        this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 1.0F, 0.5F);
        for (double d = 0.5D; d <= front + 8.0D; d += 1.0D) {
            Vec3 point = this.position().add(forward.scale(d));
            this.groundBurst(point, d > front + 4.0D ? 10 : 5, 0.3D);
            if (d > front + 4.0D) {
                this.serverParticles(PURPLE_DUST, point.x, point.y + 0.6D, point.z, 6, 0.3D, 0.6D, 0.3D, 0.0D);
            }
        }
        this.broadcastImpact();
    }

    private void performSlash() {
        Vec3 left = this.horizontalLeft();
        for (LivingEntity entity : this.enemiesInArc(this.halfWidth() + 3.0D, 75.0D, 3.5D)) {
            this.strike(entity, 1.0F);
            this.impulse(entity, left.scale(1.3D).add(0.0D, 0.35D, 0.0D));
        }
        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2.0F, 0.6F);
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.5F, 0.8F);
    }

    private void performDash() {
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
        for (LivingEntity entity : this.enemiesInLine(0.0D, this.halfWidth() + 2.5D, 1.2D, 3.5D)) {
            if (this.strike(entity, 1.4F)) {
                this.serverParticles(ParticleTypes.CRIT, entity.getX(), entity.getY(0.5D), entity.getZ(), 20, 0.3D, 0.4D, 0.3D, 0.4D);
            }
            this.impulse(entity, new Vec3(0.0D, 1.2D, 0.0D));
            this.grip(entity, 6);
        }
        this.playSound(SoundEvents.TRIDENT_HIT, 2.0F, 0.6F);
        this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1.5F, 0.7F);
    }

    private void performRise() {
        Vec3 center = this.position();
        DamageSource source = this.damageSources().explosion(this, this);
        for (LivingEntity entity : this.enemiesInRadius(6.0D, 4.0D)) {
            double distance = Math.sqrt(horizontalDistanceSqr(entity.position(), center));
            float falloff = (float) Mth.clamp(1.0D - distance / 7.0D, 0.35D, 1.0D);
            entity.hurt(source, this.attackDamage(1.0F) * falloff);
            Vec3 outward = new Vec3(entity.getX() - center.x, 0.0D, entity.getZ() - center.z);
            outward = outward.lengthSqr() < 1.0E-4D ? this.horizontalForward() : outward.normalize();
            this.impulse(entity, outward.scale(0.9D * falloff).add(0.0D, 0.6D, 0.0D));
            entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 140, 0), this);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 2.0F, 0.7F);
        this.playSound(SoundEvents.WARDEN_SONIC_BOOM, 1.0F, 1.4F);
        this.serverParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 0.2D, center.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        for (int ring = 1; ring <= 3; ring++) {
            double radius = ring * 1.75D;
            for (int i = 0; i < 8; i++) {
                double angle = i * Math.PI / 4.0D + ring * 0.3D;
                this.groundBurst(center.add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius), 6, 0.35D);
            }
        }
        this.broadcastImpact();
    }

    private void performEclipsePulse(@Nullable LivingEntity target) {
        Vec3 center = this.position();
        for (LivingEntity entity : this.enemiesInRadius(PULSE_RADIUS, 4.0D)) {
            Vec3 outward = new Vec3(entity.getX() - center.x, 0.0D, entity.getZ() - center.z);
            outward = outward.lengthSqr() < 1.0E-4D ? this.horizontalForward() : outward.normalize();
            this.strikeMagic(entity, 0.3F);
            this.impulse(entity, outward.scale(PULSE_PUSH).add(0.0D, 0.55D, 0.0D));
        }
        this.playSound(SoundEvents.WARDEN_SONIC_BOOM, 2.0F, 0.8F);
        this.playSound(SoundEvents.BEACON_DEACTIVATE, 2.0F, 0.6F);
        this.serverParticles(ParticleTypes.SONIC_BOOM, center.x, center.y + LASER_HEIGHT, center.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        this.broadcastImpact();
        this.meleeHits = 0;
        this.hugTicks = 0;
        this.tactics.clear();
        boolean enraged = this.isEnraged();
        if (target != null && this.tickCount - this.lastLaserTick > (enraged ? 60 : 100)) {
            this.tactics.add(SentinelPose.TO_LASER);
        } else if (target != null && this.tickCount - this.lastHyperBurstTick > (enraged ? 80 : 140)) {
            this.tactics.add(SentinelPose.HYPER_BURST);
        } else {
            this.tactics.add(this.random.nextBoolean() ? SentinelPose.CLEAVE : SentinelPose.DASH);
        }
    }

    @Override
    protected void tickStanding(@Nullable LivingEntity target) {
        if (target != null && this.gapTo(target) <= HUG_GAP) {
            this.hugTicks++;
        } else {
            this.hugTicks = Math.max(0, this.hugTicks - 2);
        }
        if (this.pulsePending && this.tickCount - this.meleeWindowStart > MELEE_WINDOW * 2) {
            this.pulsePending = false;
        }
        if (target != null && (this.pulsePending || this.hugTicks >= HUG_TICKS_TO_PULSE) && this.isPulseReady()) {
            this.pulsePending = false;
            this.setBossState(SentinelPose.ECLIPSE_PULSE);
            return;
        }
        super.tickStanding(target);
    }

    private boolean isPulseReady() {
        return this.tickCount - this.lastPulseTick > PULSE_COOLDOWN;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && this.isAlive() && source.getEntity() instanceof LivingEntity attacker
                && source.getDirectEntity() == attacker && this.gapTo(attacker) <= 3.5D) {
            if (this.tickCount - this.meleeWindowStart > MELEE_WINDOW) {
                this.meleeWindowStart = this.tickCount;
                this.meleeHits = 0;
            }
            this.meleeHits++;
            if (this.meleeHits >= (this.isEnraged() ? MELEE_HITS_TO_PULSE - 1 : MELEE_HITS_TO_PULSE) && this.isPulseReady()) {
                this.pulsePending = true;
            }
        }
        return result;
    }

    public float getEclipseCircleAlpha(float ageInTicks) {
        SentinelPose pose = this.getBossState();
        if (pose == SentinelPose.TO_LASER || pose == SentinelPose.LASER || pose == SentinelPose.FROM_LASER) {
            return 1.0F;
        }
        if (pose != SentinelPose.ECLIPSE_PULSE && pose != SentinelPose.RISE && pose != SentinelPose.HYPER_BURST) {
            return 0.0F;
        }
        float elapsed = this.clientStateTicks + (ageInTicks - (int) ageInTicks);
        float fadeIn = Mth.clamp(elapsed / ECLIPSE_FADE_TICKS, 0.0F, 1.0F);
        float fadeOut = Mth.clamp((pose.duration() - elapsed) / ECLIPSE_FADE_TICKS, 0.0F, 1.0F);
        return fadeIn * fadeOut;
    }

    private void tickHyperBurst(int ticks, @Nullable LivingEntity target) {
        if (ticks < HYPER_LAUNCH_TICK && target != null) {
            this.faceTowards(target, 10.0F);
        }
        if (ticks == HYPER_TARGET_TICK) {
            Vec3 aim = target != null ? target.position() : this.position().add(this.horizontalForward().scale(6.0D));
            Vec3 offset = aim.subtract(this.position());
            double horizontal = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
            if (horizontal > HYPER_MAX_DISTANCE) {
                aim = this.position().add(offset.x / horizontal * HYPER_MAX_DISTANCE, offset.y, offset.z / horizontal * HYPER_MAX_DISTANCE);
            }
            this.landing = this.snapToGround(aim, 12);
            this.setAbilityPos(this.landing);
            this.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 1.5F, 1.6F);
        }
        if (ticks == HYPER_LAUNCH_TICK) {
            this.jumpStart = this.position();
            this.groundBurst(this.position(), 30, 0.8D);
            this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 2.0F, 0.6F);
        }
        if (this.landed) {
            return;
        }
        if (ticks >= HYPER_LAUNCH_TICK && ticks <= HYPER_LAND_TICK) {
            double t = Mth.clamp((ticks - HYPER_LAUNCH_TICK + 1) / (double) (HYPER_LAND_TICK - HYPER_LAUNCH_TICK), 0.0D, 1.0D);
            Vec3 desired = this.jumpStart.lerp(this.landing, t).add(0.0D, HYPER_APEX * 4.0D * t * (1.0D - t), 0.0D);
            this.setDeltaMovement(desired.subtract(this.position()));
            this.faceTowardsPoint(this.landing);
        } else if (ticks > HYPER_LAND_TICK) {
            this.setDeltaMovement(0.0D, -1.2D, 0.0D);
        }
        if (ticks > HYPER_LAUNCH_TICK + 15 && (this.onGround() || ticks >= HYPER_LAND_TICK + 6)) {
            this.performHyperLanding();
        }
    }

    private void faceTowardsPoint(Vec3 point) {
        double dx = point.x - this.getX();
        double dz = point.z - this.getZ();
        if (dx * dx + dz * dz > 0.25D) {
            this.setFacing((float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F);
        }
    }

    private void performHyperLanding() {
        this.landed = true;
        this.setDeltaMovement(Vec3.ZERO);
        Vec3 center = this.position();
        for (LivingEntity entity : this.enemiesInRadius(7.0D, 5.0D)) {
            double distance = Math.sqrt(horizontalDistanceSqr(entity.position(), center));
            float falloff = distance <= 3.0D ? 1.0F : (float) Mth.lerp((distance - 3.0D) / 4.0D, 1.0D, 0.6D);
            this.strike(entity, 1.3F * falloff);
            this.grip(entity, 6);
            Vec3 outward = new Vec3(entity.getX() - center.x, 0.0D, entity.getZ() - center.z);
            outward = outward.lengthSqr() < 1.0E-4D ? Vec3.ZERO : outward.normalize();
            this.impulse(entity, outward.scale(0.8D).add(0.0D, 0.7D, 0.0D));
        }
        this.setAbilityPos(center);
        this.level().playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 2.5F, 0.6F);
        this.playSound(SoundEvents.ANVIL_LAND, 2.0F, 0.5F);
        this.serverParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 0.3D, center.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        for (int i = 0; i < 16; i++) {
            double angle = i * Math.PI / 8.0D;
            double radius = 2.0D + (i % 3) * 2.0D;
            this.groundBurst(center.add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius), 8, 0.4D);
        }
        this.broadcastImpact();
    }

    private void tickLaser(int ticks, @Nullable LivingEntity target) {
        if (target != null) {
            this.aimAt(target, this.isEnraged() ? 2.0F : 1.5F);
        } else {
            this.setFacing(this.getYRot());
        }
        Vec3 start = this.laserOrigin(1.0F);
        Vec3 direction = Vec3.directionFromRotation(this.laserPitch, this.getYRot());
        Vec3 end = start.add(direction.scale(LASER_RANGE));
        BlockHitResult hit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        Vec3 hitPos = hit.getType() == HitResult.Type.MISS ? end : hit.getLocation();
        this.setAbilityPos(hitPos);

        if (ticks % 5 == 0) {
            AABB sweep = new AABB(start, hitPos).inflate(1.0D);
            for (LivingEntity entity : this.enemiesIn(sweep)) {
                if (entity.getBoundingBox().inflate(0.35D).clip(start, hitPos).isPresent()) {
                    this.strikeMagic(entity, 0.3F);
                }
            }
        }
        if (ticks % 8 == 0 && hit.getType() == HitResult.Type.BLOCK && this.canGrief()) {
            this.breakBlock(hit.getBlockPos(), true);
        }
        if (ticks % 20 == 1) {
            this.playSound(SoundEvents.BEACON_AMBIENT, 2.5F, 1.8F);
        }
    }

    @Override
    protected void planTactics(LivingEntity target, Queue<SentinelPose> plan) {
        double gap = this.gapTo(target);
        double dy = target.getY() - this.getY();
        boolean enraged = this.isEnraged();
        boolean hyperReady = this.tickCount - this.lastHyperBurstTick > (enraged ? 160 : 260);
        boolean laserReady = this.tickCount - this.lastLaserTick > (enraged ? 140 : 220);
        int roll = this.random.nextInt(3);

        if (dy > 3.0D || (gap > 3.5D && !this.canReach(target))) {
            if (hyperReady && gap < HYPER_MAX_DISTANCE) {
                plan.add(SentinelPose.HYPER_BURST);
            } else if (laserReady) {
                plan.add(SentinelPose.TO_LASER);
            } else {
                plan.add(SentinelPose.CLEAVE);
            }
            return;
        }
        if (gap <= 3.5D) {
            switch (roll) {
                case 0 -> {
                    plan.add(SentinelPose.SLASH);
                    plan.add(SentinelPose.CLEAVE);
                }
                case 1 -> {
                    plan.add(SentinelPose.CLEAVE);
                    plan.add(SentinelPose.RISE);
                }
                default -> {
                    plan.add(SentinelPose.SLASH);
                    plan.add(SentinelPose.RISE);
                }
            }
            if (enraged && this.random.nextBoolean()) {
                plan.add(SentinelPose.DASH);
            }
        } else if (gap <= 9.0D) {
            if (hyperReady && roll == 0) {
                plan.add(SentinelPose.HYPER_BURST);
            } else if (roll == 1 && gap >= 3.5D) {
                plan.add(SentinelPose.CLEAVE);
            } else {
                plan.add(SentinelPose.DASH);
                plan.add(SentinelPose.SLASH);
            }
        } else if (gap <= 18.0D) {
            if (laserReady) {
                plan.add(SentinelPose.TO_LASER);
            } else if (hyperReady) {
                plan.add(SentinelPose.HYPER_BURST);
            }
        }
    }

    @Override
    protected void tickAbilityClient(SentinelPose state, int ticks) {
        Level level = this.level();
        Vec3 chest = this.position().add(0.0D, LASER_HEIGHT, 0.0D);
        switch (state) {
            case IDLING -> {
                if (this.tickCount % 4 == 0) {
                    BossParticles.converge(level, this.random, ParticleTypes.PORTAL, chest, 2.5D, 2);
                }
            }
            case IDLE_TO_AWAKE -> {
                if (ticks == 1) {
                    this.playClientSound(SoundEvents.BEACON_POWER_SELECT, 1.5F, 0.6F);
                    BossParticles.burst(level, this.random, ParticleTypes.REVERSE_PORTAL, chest, 40, 0.3D);
                }
            }
            case TO_LASER -> {
                Vec3 origin = this.laserOrigin(1.0F);
                BossParticles.converge(level, this.random, ParticleTypes.PORTAL, origin, 3.0D, 6);
                level.addParticle(CORONA_DUST, origin.x, origin.y, origin.z, 0.0D, 0.0D, 0.0D);
            }
            case LASER -> {
                Vec3 end = this.getAbilityPos();
                BossParticles.burst(level, this.random, PURPLE_DUST, end, 3, 0.2D);
                if (ticks % 2 == 0) {
                    level.addParticle(ParticleTypes.DRAGON_BREATH, end.x, end.y, end.z, 0.0D, 0.05D, 0.0D);
                    level.addParticle(ParticleTypes.LARGE_SMOKE, end.x, end.y, end.z, 0.0D, 0.03D, 0.0D);
                }
                if (ticks % 4 == 0) {
                    Vec3 origin = this.laserOrigin(1.0F);
                    BossParticles.burst(level, this.random, ParticleTypes.END_ROD, origin, 2, 0.08D);
                }
            }
            case FROM_LASER -> {
                if (ticks == 1) {
                    Vec3 origin = this.laserOrigin(1.0F);
                    BossParticles.burst(level, this.random, ParticleTypes.LARGE_SMOKE, origin, 8, 0.05D);
                }
            }
            case CLEAVE -> {
                if (ticks > 5 && ticks < CLEAVE_HIT_TICK) {
                    level.addParticle(CORONA_DUST, this.getRandomX(1.0D), this.getY() + this.getBbHeight() * (0.6D + this.random.nextDouble() * 0.4D), this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
                }
                if (ticks == CLEAVE_HIT_TICK) {
                    Vec3 forward = this.horizontalForward();
                    double front = this.halfWidth();
                    BossParticles.line(level, ParticleTypes.SQUID_INK, this.position().add(0.0D, 0.1D, 0.0D), this.position().add(forward.scale(front + 3.0D)).add(0.0D, 0.1D, 0.0D), 0.4D, 0.02D);
                    for (double d = front + 4.0D; d <= front + 8.0D; d += 0.5D) {
                        Vec3 point = this.position().add(forward.scale(d));
                        level.addParticle(ParticleTypes.CLOUD, point.x, point.y + 0.2D, point.z, 0.0D, 0.35D, 0.0D);
                        level.addParticle(ParticleTypes.REVERSE_PORTAL, point.x, point.y + 0.5D, point.z, 0.0D, 0.25D, 0.0D);
                    }
                }
            }
            case SLASH -> {
                if (ticks >= SLASH_HIT_TICK - 2 && ticks <= SLASH_HIT_TICK + 1) {
                    double progress = (ticks - (SLASH_HIT_TICK - 2)) / 3.0D;
                    double angle = Math.toRadians(70.0D - progress * 140.0D);
                    Vec3 forward = this.horizontalForward();
                    Vec3 left = this.horizontalLeft();
                    double radius = this.halfWidth() + 1.8D;
                    for (int i = -1; i <= 1; i++) {
                        double a = angle + i * 0.15D;
                        Vec3 point = this.position().add(forward.scale(Math.cos(a) * radius)).add(left.scale(-Math.sin(a) * radius)).add(0.0D, 1.6D, 0.0D);
                        level.addParticle(ParticleTypes.SWEEP_ATTACK, point.x, point.y, point.z, 0.0D, 0.0D, 0.0D);
                        level.addParticle(PURPLE_DUST, point.x, point.y + 0.3D, point.z, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            case DASH -> {
                if (ticks > DASH_LOCK_TICK && ticks <= DASH_HIT_TICK) {
                    BossParticles.motes(level, this.random, FADING_DUST, this.position(), 0.6D, this.getBbHeight(), 6, 0.0D);
                    level.addParticle(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY(0.5D), this.getZ(), 0.0D, 0.0D, 0.0D);
                }
                if (ticks == DASH_HIT_TICK) {
                    Vec3 tip = this.localPoint(this.halfWidth() + 1.5D, 1.6D, 0.0D);
                    BossParticles.burst(level, this.random, ParticleTypes.CRIT, tip, 20, 0.5D);
                    BossParticles.burst(level, this.random, PURPLE_DUST, tip, 12, 0.3D);
                }
            }
            case RISE -> {
                if (ticks > 5 && ticks < RISE_HIT_TICK) {
                    double radius = 6.0D * (1.0D - (ticks - 5) / (double) (RISE_HIT_TICK - 5));
                    BossParticles.ring(level, PURPLE_DUST, this.position().add(0.0D, 0.15D, 0.0D), Math.max(0.5D, radius), 20, 0.0D, 0.0D);
                }
            }
            case ECLIPSE_PULSE -> {
                if (ticks == 1) {
                    this.playClientSound(SoundEvents.WARDEN_SONIC_CHARGE, 1.5F, 1.4F);
                }
                if (ticks > 3 && ticks < PULSE_HIT_TICK) {
                    Vec3 circle = this.localPoint(-1.0D, LASER_HEIGHT, 0.0D);
                    BossParticles.converge(level, this.random, ParticleTypes.PORTAL, circle, 2.5D, 4);
                }
            }
            case HYPER_BURST -> {
                if (ticks >= HYPER_TARGET_TICK && ticks <= HYPER_LAND_TICK && ticks % 3 == 0) {
                    BossParticles.ring(level, PURPLE_DUST, this.getAbilityPos().add(0.0D, 0.15D, 0.0D), 7.0D, 36, 0.0D, 0.0D);
                }
                if (ticks > HYPER_LAUNCH_TICK && !this.onGround()) {
                    level.addParticle(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 0.0D, -0.05D, 0.0D);
                    level.addParticle(FADING_DUST, this.getRandomX(0.8D), this.getY() + 0.5D, this.getRandomZ(0.8D), 0.0D, 0.0D, 0.0D);
                }
            }
            case DYING -> {
                double progress = Math.min(1.0D, ticks / (double) DEATH_ANIMATION_TICKS);
                BossParticles.converge(level, this.random, ParticleTypes.PORTAL, chest, 3.5D - progress * 2.0D, 3 + (int) (progress * 6.0D));
                BossParticles.motes(level, this.random, FADING_DUST, this.position(), 1.0D, this.getBbHeight(), 2, 0.02D);
                if (ticks == DEATH_ANIMATION_TICKS - 2) {
                    level.addParticle(ParticleTypes.SONIC_BOOM, chest.x, chest.y, chest.z, 0.0D, 0.0D, 0.0D);
                    BossParticles.burst(level, this.random, ParticleTypes.REVERSE_PORTAL, chest, 80, 0.8D);
                    this.playClientSound(SoundEvents.WARDEN_SONIC_BOOM, 1.5F, 0.6F);
                }
            }
            default -> {
                if (this.isAwake() && this.tickCount % 4 == 0) {
                    BossParticles.motes(level, this.random, CORONA_DUST, this.position(), 0.7D, this.getBbHeight(), 1, 0.02D);
                }
            }
        }
    }

    @Override
    protected void onImpactClient(SentinelPose state) {
        Level level = this.level();
        Vec3 base = this.position().add(0.0D, 0.15D, 0.0D);
        switch (state) {
            case ECLIPSE_PULSE -> {
                BossParticles.ring(level, ParticleTypes.CLOUD, base, 1.0D, 36, 1.1D, 0.05D);
                BossParticles.ring(level, PURPLE_DUST, base.add(0.0D, 1.0D, 0.0D), PULSE_RADIUS, 40, 0.0D, 0.0D);
                BossParticles.ring(level, ParticleTypes.REVERSE_PORTAL, base.add(0.0D, LASER_HEIGHT, 0.0D), 0.8D, 24, 0.6D, 0.0D);
            }
            case HYPER_BURST -> {
                level.addParticle(ParticleTypes.SONIC_BOOM, base.x, base.y + 1.0D, base.z, 0.0D, 0.0D, 0.0D);
                BossParticles.ring(level, ParticleTypes.CLOUD, base, 1.5D, 40, 0.9D, 0.05D);
                BossParticles.ring(level, PURPLE_DUST, base, 7.0D, 48, 0.0D, 0.0D);
            }
            case RISE -> {
                BossParticles.ring(level, ParticleTypes.SQUID_INK, base, 1.0D, 36, 0.7D, 0.05D);
                BossParticles.column(level, this.random, ParticleTypes.SCULK_SOUL, base, 4.0D, 24, 0.15D);
            }
            case CLEAVE -> BossParticles.ring(level, ParticleTypes.CLOUD, this.localPoint(this.halfWidth() + 1.0D, 0.15D, 0.0D), 0.5D, 12, 0.3D, 0.0D);
            default -> {
            }
        }
    }

    @Override
    public float alphaIntensity() {
        return switch (this.getBossState()) {
            case IDLING, AWAKE_TO_IDLE -> 0.15F;
            case TO_LASER, FROM_LASER -> 0.7F;
            case LASER, HYPER_BURST -> 1.0F;
            case CLEAVE, SLASH, DASH, RISE -> 0.6F;
            case ECLIPSE_PULSE -> 0.9F;
            case DYING -> 0.0F;
            default -> 0.35F;
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

    public enum SentinelPose implements BossState {
        STANDING(0, 0, false),
        TO_LASER(1, 0, true),
        LASER(2, 0, true),
        FROM_LASER(3, LASER_CHARGE_TICKS, false),
        CLEAVE(4, 35, true),
        SLASH(5, 35, true),
        RISE(6, 40, true),
        DASH(7, 50, true),
        HYPER_BURST(8, 60, true),
        ECLIPSE_PULSE(13, 40, true),
        DYING(9, 0, false),
        AWAKE_TO_IDLE(10, 0, false),
        IDLING(11, 0, false),
        IDLE_TO_AWAKE(12, 0, false);

        private final int id;
        private final int duration;
        private final boolean attack;

        SentinelPose(int id, int duration, boolean attack) {
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