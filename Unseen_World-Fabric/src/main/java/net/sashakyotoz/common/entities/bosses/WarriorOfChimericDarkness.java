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
import net.minecraft.world.entity.*;
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

import java.util.*;

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
    public static final double THORNFIELD_RADIUS = 11.0D;
    public static final int THORNFIELD_PLANT_TICK = 20;
    public static final int THORNFIELD_FIRST_WAVE_TICK = 26;
    public static final int THORNFIELD_WAVE_INTERVAL = 10;
    public static final int THORNFIELD_WAVES = 6;
    public static final int THORNFIELD_TELEGRAPH_TICKS = 12;
    private static final int THORNFIELD_COOLDOWN = 260;
    private static final int THORNFIELD_ENRAGED_COOLDOWN = 180;
    private static final int THORNFIELD_FILLER_MARKS = 5;
    private static final int THORNFIELD_ENRAGED_EXTRA_MARKS = 3;
    private static final int THORNFIELD_INNER_RING_MARKS = 7;
    private static final int THORNFIELD_MAX_MARKS_PER_WAVE = 18;
    private static final double THORNFIELD_MARK_SPACING_SQR = 1.6D * 1.6D;
    private static final double THORNFIELD_HIT_RADIUS = 1.1D;
    private static final double THORNFIELD_HIT_HEIGHT = 2.8D;
    private static final double THORNFIELD_BRACKET_OFFSET = 2.4D;
    private static final double THORNFIELD_MAX_LEAD = 4.0D;
    private static final float THORNFIELD_DAMAGE = 0.45F;
    private static final int PRESSURE_WINDOW_TICKS = 80;
    private static final int PRESSURE_HITS = 4;
    private static final double PRESSURE_RANGE_SQR = 25.0D;
    private static final int SHIELD_COOLDOWN = 120;
    private static final float STAGGER_THRESHOLD = 30.0F;
    private static final float[] PHASES = {0.5F};
    public static final int SHATTER_TICK = 30;
    public static final int HAMMER_RELEASE_TICK = 15;
    public static final int HAMMER_LAND_TICK = 25;
    public static final int HAMMER_RECALL_TICK = 45;
    public static final int HAMMER_CATCH_TICK = 55;
    private static final double HAMMER_MAX_RANGE = 14.0D;
    private static final double HAMMER_HIT_RADIUS = 1.3D;
    private static final double HAMMER_IMPACT_RADIUS = 2.5D;
    private static final float HAMMER_EXPOSED_DAMAGE_FACTOR = 1.3F;
    private static final int HAMMER_THROW_COOLDOWN = 200;
    private static final int THORNFIELD_ECHO_DELAY = 10;

    public final AnimationState idleTransition = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState shielded_knockback = new AnimationState();
    public final AnimationState smash = new AnimationState();
    public final AnimationState shield = new AnimationState();
    public final AnimationState charged_strike = new AnimationState();
    public final AnimationState fissure = new AnimationState();
    public final AnimationState thornfield = new AnimationState();
    public final AnimationState shatter = new AnimationState();
    public final AnimationState hammerThrow = new AnimationState();
    public final AnimationState death = new AnimationState();

    private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("entity.unseen_world.warrior_of_chimeric_darkness"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_6);
    private final Set<UUID> fissureHits = new HashSet<>();
    private final TelegraphedStrikes thorns = new TelegraphedStrikes(new ThornHandler());
    private final List<Vec3> firstWaveSpots = new ArrayList<>();
    private final Set<UUID> hammerHits = new HashSet<>();
    private final Map<UUID, Vec3> trackedPositions = new HashMap<>();
    private final Map<UUID, Vec3> trackedVelocities = new HashMap<>();
    private final Deque<Integer> meleeHitTicks = new ArrayDeque<>();
    private int lastThornfieldTick = -1000;

    private Vec3 fissureOrigin = Vec3.ZERO;
    private Vec3 fissureDirection = Vec3.ZERO;
    private boolean fissureBlocked;
    private int lastShieldTick = -1000;
    private int lastFissureTick = -1000;
    private float chargeDamageTaken;
    private int lastHammerThrowTick = -1000;
    private Vec3 hammerStart = Vec3.ZERO;
    private Vec3 hammerLanding = Vec3.ZERO;

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
        this.bindAnimation(this.thornfield, WarriorPose.THORNFIELD);
        this.bindAnimation(this.shatter, WarriorPose.SHATTER);
        this.bindAnimation(this.hammerThrow, WarriorPose.HAMMER_THROW);
        this.bindAnimation(this.death, WarriorPose.DYING);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 450.0D)
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
        return this.getBossPhase() >= 1;
    }

    @Override
    protected float[] phaseThresholds() {
        return PHASES;
    }

    @Override
    protected WarriorPose phaseTransitionState(int phase) {
        return WarriorPose.SHATTER;
    }

    @Override
    protected BossEvent.BossBarColor phaseBarColor(int phase) {
        return phase >= 1 ? BossEvent.BossBarColor.RED : BossEvent.BossBarColor.PURPLE;
    }

    public boolean hasShield() {
        return this.getBossPhase() < 1 || (this.getBossState() == WarriorPose.SHATTER && this.clientStateTicks < SHATTER_TICK);
    }

    public boolean isHammerThrown() {
        return this.getBossState() == WarriorPose.HAMMER_THROW && this.clientStateTicks >= HAMMER_RELEASE_TICK && this.clientStateTicks < HAMMER_CATCH_TICK;
    }

    public WarriorPose getWarriorPose() {
        return this.getBossState();
    }

    public boolean isShielding() {
        WarriorPose pose = this.getBossState();
        return this.getBossPhase() < 1 && (pose == WarriorPose.SHIELDED_KNOCKBACK || pose == WarriorPose.SHIELD);
    }

    public float getChargeProgress(float partialTick) {
        if (this.getBossState() != WarriorPose.CHARGED_STRIKE) {
            return 0.0F;
        }
        return Mth.clamp((this.clientStateTicks + partialTick - CHARGE_START_TICK) / (CHARGED_STRIKE_HIT_TICK - CHARGE_START_TICK), 0.0F, 1.0F);
    }

    @Override
    protected void onStateChanged(WarriorPose previous, WarriorPose next) {
        if (previous == WarriorPose.THORNFIELD) {
            this.clearThornfield();
        }
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
            case THORNFIELD -> {
                this.lastThornfieldTick = this.tickCount;
                this.meleeHitTicks.clear();
                this.clearThornfield();
            }
            case HAMMER_THROW -> {
                this.lastHammerThrowTick = this.tickCount;
                this.hammerHits.clear();
            }
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
        if (!this.level().isClientSide() && this.getBossState() == WarriorPose.HAMMER_THROW
                && this.stateTicks >= HAMMER_LAND_TICK && this.stateTicks < HAMMER_RECALL_TICK) {
            amount *= HAMMER_EXPOSED_DAMAGE_FACTOR;
        }
        if (!this.level().isClientSide() && this.isAlive() && source.is(DamageTypeTags.IS_PROJECTILE) && this.getBossPhase() < 1
                && this.getBossState() == WarriorPose.STANDING && this.tickCount - this.lastShieldTick > SHIELD_COOLDOWN) {
            this.setBossState(WarriorPose.SHIELD);
            this.onDamageBlocked(source);
            return false;
        }
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide()) {
            this.trackMeleePressure(source);
        }
        if (result && !this.level().isClientSide() && this.getBossState() == WarriorPose.CHARGED_STRIKE
                && this.stateTicks >= CHARGE_START_TICK && this.stateTicks < CHARGE_END_TICK) {
            this.chargeDamageTaken += amount;
            if (this.chargeDamageTaken >= STAGGER_THRESHOLD) {
                this.stagger();
            }
        }
        return result;
    }

    private void trackMeleePressure(DamageSource source) {
        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity) || source.getDirectEntity() != attacker || this.distanceToSqr(attacker) > PRESSURE_RANGE_SQR + this.halfWidth() * this.halfWidth()) {
            return;
        }
        this.meleeHitTicks.addLast(this.tickCount);
        while (!this.meleeHitTicks.isEmpty() && this.tickCount - this.meleeHitTicks.peekFirst() > PRESSURE_WINDOW_TICKS) {
            this.meleeHitTicks.pollFirst();
        }
        if (this.meleeHitTicks.size() >= PRESSURE_HITS && this.isThornfieldReady() && this.getBossState() == WarriorPose.STANDING) {
            this.tactics.clear();
            this.tactics.add(WarriorPose.THORNFIELD);
        }
    }

    private boolean isThornfieldReady() {
        return this.tickCount - this.lastThornfieldTick > (this.isEnraged() ? THORNFIELD_ENRAGED_COOLDOWN : THORNFIELD_COOLDOWN);
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
            case THORNFIELD -> this.tickThornfield(ticks, target);
            case SHATTER -> {
                if (target != null) {
                    this.faceTowards(target, 6.0F);
                }
                if (ticks == 1) {
                    this.playSound(SoundEvents.WARDEN_HEARTBEAT, 2.5F, 0.6F);
                }
                if (ticks == SHATTER_TICK) {
                    this.performShatter();
                }
            }
            case HAMMER_THROW -> this.tickHammerThrow(ticks, target);
            case SHIELD -> {
                if (target != null) {
                    this.faceTowards(target, 15.0F);
                }
            }
            default -> {
            }
        }
    }

    private void performShatter() {
        Vec3 center = this.position();
        for (LivingEntity entity : this.enemiesInRadius(6.0D, 4.0D)) {
            Vec3 outward = new Vec3(entity.getX() - center.x, 0.0D, entity.getZ() - center.z);
            outward = outward.lengthSqr() < 1.0E-4D ? this.horizontalForward() : outward.normalize();
            this.impulse(entity, outward.scale(1.4D).add(0.0D, 0.45D, 0.0D));
        }
        Vec3 shieldPos = this.localPoint(0.6D, 1.8D, -1.4D);
        this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, shieldPos.x, shieldPos.y, shieldPos.z, 40, 0.6D, 0.8D, 0.6D, 0.35D);
        this.serverParticles(VIOLET_DUST, shieldPos.x, shieldPos.y, shieldPos.z, 30, 0.8D, 0.8D, 0.8D, 0.0D);
        this.serverParticles(ParticleTypes.LARGE_SMOKE, center.x, center.y + 0.3D, center.z, 20, 1.5D, 0.2D, 1.5D, 0.05D);
        this.playSound(SoundEvents.SHIELD_BREAK, 3.0F, 0.5F);
        this.playSound(SoundEvents.WARDEN_ROAR, 2.0F, 0.8F);
        this.playSound(SoundEvents.ANVIL_DESTROY, 1.5F, 0.6F);
        this.broadcastImpact();
    }

    private Vec3 hammerHand() {
        return this.localPoint(this.halfWidth() + 0.4D, 2.2D, 1.0D);
    }

    private void tickHammerThrow(int ticks, @Nullable LivingEntity target) {
        if (ticks < HAMMER_RELEASE_TICK) {
            this.trackThenLock(target, ticks, HAMMER_RELEASE_TICK - 3, 10.0F);
            if (ticks == HAMMER_RELEASE_TICK - 3) {
                Vec3 aim = target != null ? target.position() : this.position().add(this.horizontalForward().scale(8.0D));
                Vec3 offset = aim.subtract(this.position()).multiply(1.0D, 0.0D, 1.0D);
                if (offset.lengthSqr() > HAMMER_MAX_RANGE * HAMMER_MAX_RANGE) {
                    aim = this.position().add(offset.normalize().scale(HAMMER_MAX_RANGE));
                }
                Vec3 ground = this.findStandableGround(aim.x, aim.z, target != null ? target.getY() : this.getY(), 12);
                this.hammerLanding = ground != null ? ground : this.snapToGround(aim, 12);
            }
            return;
        }
        if (ticks == HAMMER_RELEASE_TICK) {
            this.hammerStart = this.hammerHand();
            this.hammerHits.clear();
            this.playSound(SoundEvents.TRIDENT_THROW, 2.0F, 0.4F);
            this.playSound(SoundEvents.WARDEN_ATTACK_IMPACT, 1.5F, 0.6F);
        }
        if (ticks <= HAMMER_LAND_TICK) {
            this.setFacing(this.lockedYaw);
            double t = (ticks - HAMMER_RELEASE_TICK) / (double) (HAMMER_LAND_TICK - HAMMER_RELEASE_TICK);
            Vec3 pos = this.hammerStart.lerp(this.hammerLanding.add(0.0D, 0.5D, 0.0D), t).add(0.0D, 3.0D * 4.0D * t * (1.0D - t), 0.0D);
            this.setAbilityPos(pos);
            this.hammerSweep(pos, this.hammerLanding.subtract(this.hammerStart).multiply(1.0D, 0.0D, 1.0D), 1.0F, 0.6D, 3);
            if (ticks == HAMMER_LAND_TICK) {
                this.performHammerImpact();
            }
            return;
        }
        if (ticks < HAMMER_RECALL_TICK) {
            this.faceTowardsPoint(this.hammerLanding);
            if (ticks % 4 == 0) {
                this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, this.hammerLanding.x, this.hammerLanding.y + 0.4D, this.hammerLanding.z, 3, 0.3D, 0.3D, 0.3D, 0.05D);
            }
            if (ticks == HAMMER_RECALL_TICK - 1) {
                this.hammerHits.clear();
                this.playSound(SoundEvents.TRIDENT_RIPTIDE_3, 2.0F, 0.5F);
                this.level().playSound(null, this.hammerLanding.x, this.hammerLanding.y, this.hammerLanding.z, SoundEvents.DEEPSLATE_BREAK, SoundSource.HOSTILE, 2.0F, 0.5F);
            }
            return;
        }
        if (ticks <= HAMMER_CATCH_TICK) {
            this.faceTowardsPoint(this.hammerLanding);
            double t = (ticks - HAMMER_RECALL_TICK) / (double) (HAMMER_CATCH_TICK - HAMMER_RECALL_TICK);
            Vec3 hand = this.hammerHand();
            Vec3 pos = this.hammerLanding.add(0.0D, 0.8D, 0.0D).lerp(hand, t);
            this.setAbilityPos(pos);
            this.hammerSweep(pos, hand.subtract(this.hammerLanding).multiply(1.0D, 0.0D, 1.0D), 0.8F, 0.7D, 2);
            if (ticks == HAMMER_CATCH_TICK) {
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 2.0F, 0.5F);
            }
        }
    }

    private void hammerSweep(Vec3 pos, Vec3 travel, float damage, double push, int gripSeconds) {
        Vec3 direction = travel.lengthSqr() < 1.0E-4D ? this.horizontalForward() : travel.normalize();
        AABB box = new AABB(pos, pos).inflate(HAMMER_HIT_RADIUS);
        for (LivingEntity entity : this.enemiesIn(box)) {
            if (this.hammerHits.add(entity.getUUID()) && this.strike(entity, damage)) {
                this.impulse(entity, direction.scale(push).add(0.0D, 0.35D, 0.0D));
                this.grip(entity, gripSeconds);
            }
        }
    }

    private void performHammerImpact() {
        Vec3 landing = this.hammerLanding;
        this.setAbilityPos(landing);
        AABB area = new AABB(landing, landing).inflate(HAMMER_IMPACT_RADIUS, 1.5D, HAMMER_IMPACT_RADIUS);
        for (LivingEntity entity : this.enemiesIn(area)) {
            if (this.hammerHits.add(entity.getUUID()) && this.strike(entity, 0.6F)) {
                this.impulse(entity, new Vec3(0.0D, 0.5D, 0.0D));
            }
        }
        this.groundBurst(landing, 40, 0.9D);
        this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, landing.x, landing.y + 0.3D, landing.z, 16, 0.5D, 0.3D, 0.5D, 0.3D);
        this.level().playSound(null, landing.x, landing.y, landing.z, SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 2.5F, 0.4F);
        this.level().playSound(null, landing.x, landing.y, landing.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.2F, 0.8F);
    }

    private void faceTowardsPoint(Vec3 point) {
        double dx = point.x - this.getX();
        double dz = point.z - this.getZ();
        if (dx * dx + dz * dz > 0.25D) {
            float yaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            this.setFacing(Mth.approachDegrees(this.getYRot(), yaw, 12.0F));
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

    private void tickThornfield(int ticks, @Nullable LivingEntity target) {
        if (ticks < THORNFIELD_PLANT_TICK - 4) {
            if (target != null) {
                this.faceTowards(target, 10.0F);
            }
            this.lockedYaw = this.getYRot();
        } else {
            this.setFacing(this.lockedYaw);
        }
        if (ticks == THORNFIELD_PLANT_TICK) {
            Vec3 front = this.localPoint(this.halfWidth() + 1.0D, 0.1D, 0.0D);
            this.groundBurst(front, 40, 0.8D);
            this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, front.x, front.y + 0.2D, front.z, 20, 0.6D, 0.2D, 0.6D, 0.3D);
            this.playSound(SoundEvents.ANVIL_LAND, 2.5F, 0.4F);
            this.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 2.0F, 0.6F);
            this.broadcastImpact();
        }
        if (ticks >= THORNFIELD_PLANT_TICK) {
            this.sampleTargetMotion();
        }
        int sinceFirst = ticks - THORNFIELD_FIRST_WAVE_TICK;
        if (sinceFirst >= 0 && sinceFirst % THORNFIELD_WAVE_INTERVAL == 0) {
            int wave = sinceFirst / THORNFIELD_WAVE_INTERVAL;
            int waves = this.isEnraged() ? THORNFIELD_WAVES + 1 : THORNFIELD_WAVES;
            if (wave < waves) {
                this.castThornWave(wave);
            }
        }
        this.thorns.tick(this.stateTicks);
    }

    private void sampleTargetMotion() {
        for (LivingEntity entity : this.enemiesInRadius(THORNFIELD_RADIUS + 4.0D, 8.0D)) {
            Vec3 now = entity.position();
            Vec3 previous = this.trackedPositions.put(entity.getUUID(), now);
            if (previous != null) {
                Vec3 step = new Vec3(now.x - previous.x, 0.0D, now.z - previous.z);
                Vec3 smoothed = this.trackedVelocities.getOrDefault(entity.getUUID(), Vec3.ZERO).scale(0.5D).add(step.scale(0.5D));
                this.trackedVelocities.put(entity.getUUID(), smoothed);
            }
        }
    }

    private void castThornWave(int wave) {
        List<Vec3> spots = new ArrayList<>();
        Vec3 center = this.position();
        for (LivingEntity entity : this.enemiesInRadius(THORNFIELD_RADIUS, 8.0D)) {
            Vec3 now = entity.position();
            Vec3 velocity = this.trackedVelocities.getOrDefault(entity.getUUID(), Vec3.ZERO);
            double leadScale = wave % 2 == 0 ? 1.0D : 1.5D;
            Vec3 lead = velocity.scale(THORNFIELD_TELEGRAPH_TICKS * leadScale);
            if (lead.lengthSqr() > THORNFIELD_MAX_LEAD * THORNFIELD_MAX_LEAD) {
                lead = lead.normalize().scale(THORNFIELD_MAX_LEAD);
            }
            Vec3 predicted = now.add(lead);
            this.addThornSpot(spots, predicted, now.y);
            if (wave % 2 == 0) {
                Vec3 heading = lead.lengthSqr() > 1.0E-3D ? lead.normalize() : now.subtract(center).multiply(1.0D, 0.0D, 1.0D).normalize();
                Vec3 side = new Vec3(-heading.z, 0.0D, heading.x).scale(THORNFIELD_BRACKET_OFFSET);
                this.addThornSpot(spots, predicted.add(side), now.y);
                this.addThornSpot(spots, predicted.subtract(side), now.y);
            } else {
                this.addThornSpot(spots, now, now.y);
            }
        }
        if (wave == 2 || wave == 5) {
            double ring = this.halfWidth() + 1.3D;
            double offset = this.random.nextDouble() * Math.PI;
            for (int i = 0; i < THORNFIELD_INNER_RING_MARKS; i++) {
                double angle = offset + i * Math.PI * 2.0D / THORNFIELD_INNER_RING_MARKS;
                this.addThornSpot(spots, center.add(Math.cos(angle) * ring, 0.0D, Math.sin(angle) * ring), this.getY());
            }
        }
        int fillers = THORNFIELD_FILLER_MARKS + (this.isEnraged() ? THORNFIELD_ENRAGED_EXTRA_MARKS : 0);
        double bandInner = 2.5D + (wave % 3) * 2.5D;
        int placed = 0;
        for (int attempt = 0; attempt < fillers * 3 && placed < fillers && spots.size() < THORNFIELD_MAX_MARKS_PER_WAVE; attempt++) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            double radius = Mth.clamp(bandInner + this.random.nextDouble() * 4.0D, 2.5D, THORNFIELD_RADIUS);
            int before = spots.size();
            this.addThornSpot(spots, center.add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius), this.getY());
            if (spots.size() > before) {
                placed++;
            }
        }
        for (Vec3 spot : spots) {
            this.thorns.schedule(spot, this.stateTicks, THORNFIELD_TELEGRAPH_TICKS);
        }
        if (wave == 0) {
            this.firstWaveSpots.clear();
            this.firstWaveSpots.addAll(spots);
        }
        if (this.isEnraged() && wave == THORNFIELD_WAVES) {
            for (Vec3 spot : this.firstWaveSpots) {
                this.thorns.schedule(spot, this.stateTicks, THORNFIELD_TELEGRAPH_TICKS + THORNFIELD_ECHO_DELAY);
            }
            this.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 1.5F, 1.2F);
        }
        this.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 2.0F, 0.5F + wave * 0.08F);
    }

    private void addThornSpot(List<Vec3> spots, Vec3 wanted, double referenceY) {
        if (spots.size() >= THORNFIELD_MAX_MARKS_PER_WAVE) {
            return;
        }
        Vec3 flat = new Vec3(wanted.x, 0.0D, wanted.z);
        Vec3 offset = flat.subtract(this.getX(), 0.0D, this.getZ());
        if (offset.lengthSqr() > THORNFIELD_RADIUS * THORNFIELD_RADIUS) {
            flat = new Vec3(this.getX(), 0.0D, this.getZ()).add(offset.normalize().scale(THORNFIELD_RADIUS));
        }
        Vec3 ground = this.findStandableGround(flat.x, flat.z, referenceY, 10);
        if (ground == null) {
            return;
        }
        for (Vec3 existing : spots) {
            if (horizontalDistanceSqr(existing, ground) < THORNFIELD_MARK_SPACING_SQR) {
                return;
            }
        }
        spots.add(ground);
    }

    private void telegraphThorn(Vec3 spot, boolean urgent) {
        DustParticleOptions dust = urgent ? VIOLET_DUST : GLOW_DUST;
        this.serverRing(dust, spot.add(0.0D, 0.1D, 0.0D), THORNFIELD_HIT_RADIUS, 10);
        if (urgent) {
            this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, spot.x, spot.y + 0.1D, spot.z, 4, 0.4D, 0.05D, 0.4D, 0.05D);
        } else {
            this.serverParticles(ModParticleTypes.EXPLOSION_CIRCLE, spot.x, spot.y + 0.05D, spot.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private void eruptThorn(Vec3 spot) {
        AABB box = new AABB(spot.x - THORNFIELD_HIT_RADIUS, spot.y - 0.5D, spot.z - THORNFIELD_HIT_RADIUS,
                spot.x + THORNFIELD_HIT_RADIUS, spot.y + THORNFIELD_HIT_HEIGHT, spot.z + THORNFIELD_HIT_RADIUS);
        for (LivingEntity entity : this.enemiesIn(box)) {
            if (this.thorns.firstHit(entity.getUUID(), this.stateTicks) && this.strike(entity, THORNFIELD_DAMAGE)) {
                this.impulse(entity, new Vec3(0.0D, 0.55D, 0.0D));
                this.grip(entity, 2);
            }
        }
        this.groundBurst(spot, 14, 0.4D);
        this.serverParticles(ModParticleTypes.GRIPPING_CRYSTAL, spot.x, spot.y + 0.4D, spot.z, 8, 0.25D, 0.6D, 0.25D, 0.25D);
        this.serverParticles(SHADOW_DUST, spot.x, spot.y + 1.0D, spot.z, 10, 0.3D, 0.9D, 0.3D, 0.0D);
        this.level().playSound(null, spot.x, spot.y, spot.z, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.HOSTILE, 1.4F, 0.5F + this.random.nextFloat() * 0.3F);
    }

    private void clearThornfield() {
        this.thorns.clear();
        this.firstWaveSpots.clear();
        this.trackedPositions.clear();
        this.trackedVelocities.clear();
    }

    private final class ThornHandler implements TelegraphedStrikes.Handler {
        @Override
        public void telegraph(Vec3 pos, boolean urgent) {
            WarriorOfChimericDarkness.this.telegraphThorn(pos, urgent);
        }

        @Override
        public void strike(Vec3 pos) {
            WarriorOfChimericDarkness.this.eruptThorn(pos);
        }
    }

    @Override
    public void die(DamageSource source) {
        this.clearThornfield();
        super.die(source);
    }

    @Override
    protected void planTactics(LivingEntity target, Queue<WarriorPose> plan) {
        double gap = this.gapTo(target);
        double dy = target.getY() - this.getY();
        boolean fissureReady = this.tickCount - this.lastFissureTick > (this.isEnraged() ? 100 : 180);
        int roll = this.random.nextInt(3);
        if (this.isThornfieldReady() && gap <= THORNFIELD_RADIUS - 1.0D && (this.random.nextInt(4) == 0 || (dy > 2.0D && !fissureReady))) {
            plan.add(WarriorPose.THORNFIELD);
            return;
        }

        if (this.isEnraged()) {
            this.planBerserkTactics(target, gap, dy, fissureReady, roll, plan);
            return;
        }
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

    private void planBerserkTactics(LivingEntity target, double gap, double dy, boolean fissureReady, int roll, Queue<WarriorPose> plan) {
        boolean throwReady = this.tickCount - this.lastHammerThrowTick > HAMMER_THROW_COOLDOWN;
        if (throwReady && gap > 4.0D && gap <= HAMMER_MAX_RANGE && (dy > 2.0D || !this.canReach(target) || roll != 2)) {
            plan.add(WarriorPose.HAMMER_THROW);
            return;
        }
        if (gap <= 3.5D) {
            if (fissureReady && roll == 0) {
                plan.add(WarriorPose.SMASH);
                this.combo.add(WarriorPose.FISSURE);
                this.combo.add(WarriorPose.SMASH);
            } else if (roll == 1) {
                plan.add(WarriorPose.SMASH);
                this.combo.add(WarriorPose.CHARGED_STRIKE);
            } else {
                plan.add(WarriorPose.CHARGED_STRIKE);
            }
        } else if (gap <= FISSURE_LENGTH && fissureReady) {
            plan.add(WarriorPose.FISSURE);
            this.combo.add(WarriorPose.SMASH);
        } else if (throwReady) {
            plan.add(WarriorPose.HAMMER_THROW);
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
            case THORNFIELD -> {
                Vec3 hammer = this.position().add(0.0D, this.getBbHeight() + 0.6D, 0.0D);
                if (ticks < THORNFIELD_PLANT_TICK) {
                    BossParticles.converge(level, this.random, ModParticleTypes.GRIPPING_CRYSTAL, hammer, 2.5D, 1);
                    level.addParticle(GLOW_DUST, hammer.x + this.random.nextGaussian() * 0.3D, hammer.y, hammer.z + this.random.nextGaussian() * 0.3D, 0.0D, 0.0D, 0.0D);
                } else if (ticks < THORNFIELD_FIRST_WAVE_TICK + THORNFIELD_WAVES * THORNFIELD_WAVE_INTERVAL + THORNFIELD_TELEGRAPH_TICKS) {
                    Vec3 front = this.localPoint(this.halfWidth() + 1.0D, 0.15D, 0.0D);
                    if (ticks % 2 == 0) {
                        level.addParticle(ModParticleTypes.GRIPPING_CRYSTAL, front.x + this.random.nextGaussian() * 0.4D, front.y, front.z + this.random.nextGaussian() * 0.4D, 0.0D, 0.08D, 0.0D);
                    }
                    if ((ticks - THORNFIELD_FIRST_WAVE_TICK) % THORNFIELD_WAVE_INTERVAL == 0) {
                        BossParticles.ring(level, SHADOW_DUST, front, 0.5D, 16, 0.6D, 0.0D);
                        BossParticles.ring(level, VIOLET_DUST, this.position().add(0.0D, 0.15D, 0.0D), THORNFIELD_RADIUS, 48, 0.0D, 0.0D);
                    }
                    if (this.tickCount % 3 == 0) {
                        BossParticles.motes(level, this.random, SHADOW_DUST, this.position(), this.halfWidth() + 0.6D, this.getBbHeight() * 0.6D, 1, 0.03D);
                    }
                }
            }
            case SHATTER -> {
                if (ticks < SHATTER_TICK) {
                    BossParticles.converge(level, this.random, SHADOW_DUST, chest, 3.0D, 2);
                    if (ticks % 6 == 0) {
                        BossParticles.ring(level, VIOLET_DUST, this.position().add(0.0D, 0.15D, 0.0D), 2.0D + ticks * 0.1D, 24, 0.0D, 0.0D);
                    }
                } else if (ticks % 2 == 0) {
                    BossParticles.motes(level, this.random, GLOW_DUST, this.position(), this.halfWidth() + 0.5D, this.getBbHeight(), 2, 0.05D);
                }
            }
            case HAMMER_THROW -> {
                if (ticks < HAMMER_RELEASE_TICK) {
                    level.addParticle(GLOW_DUST, this.getRandomX(1.2D), this.getY() + this.getBbHeight() * 0.9D, this.getRandomZ(1.2D), 0.0D, 0.05D, 0.0D);
                } else if (this.isHammerThrown()) {
                    Vec3 hammer = this.getAbilityPos();
                    level.addParticle(ModParticleTypes.GRIPPING_CRYSTAL, hammer.x, hammer.y, hammer.z, 0.0D, 0.02D, 0.0D);
                    level.addParticle(SHADOW_DUST, hammer.x + this.random.nextGaussian() * 0.2D, hammer.y + this.random.nextGaussian() * 0.2D, hammer.z + this.random.nextGaussian() * 0.2D, 0.0D, 0.0D, 0.0D);
                    if (ticks >= HAMMER_RECALL_TICK) {
                        BossParticles.line(level, GLOW_DUST, hammer, this.position().add(0.0D, this.getBbHeight() * 0.6D, 0.0D), 0.8D, 0.0D);
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
            case SHATTER -> {
                BossParticles.ring(level, ParticleTypes.CLOUD, base, 1.5D, 48, 1.1D, 0.06D);
                BossParticles.ring(level, SHADOW_DUST, base, 6.0D, 56, 0.0D, 0.08D);
                BossParticles.burst(level, this.random, ModParticleTypes.GRIPPING_CRYSTAL, chestPoint(), 40, 0.6D);
            }
            case THORNFIELD -> {
                Vec3 front = this.localPoint(this.halfWidth() + 1.0D, 0.15D, 0.0D);
                BossParticles.ring(level, ParticleTypes.CLOUD, front, 0.8D, 24, 0.45D, 0.03D);
                BossParticles.ring(level, SHADOW_DUST, base, 2.0D, 32, 0.8D, 0.02D);
                BossParticles.burst(level, this.random, ModParticleTypes.GRIPPING_CRYSTAL, front, 16, 0.35D);
            }
            default -> {
            }
        }
    }

    private Vec3 chestPoint() {
        return this.position().add(0.0D, this.getBbHeight() * 0.6D, 0.0D);
    }

    @Override
    public float alphaIntensity() {
        return switch (this.getBossState()) {
            case IDLING, AWAKE_TO_IDLE -> 0.1F;
            case CHARGED_STRIKE -> 0.4F + 0.6F * this.getChargeProgress(0.0F);
            case SHIELDED_KNOCKBACK, SHIELD -> 0.8F;
            case SMASH, FISSURE -> 0.6F;
            case THORNFIELD -> 0.7F;
            case SHATTER -> 1.0F;
            case HAMMER_THROW -> 0.6F;
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
        IDLE_TO_AWAKE(9, 0, false),
        THORNFIELD(10, 110, true),
        SHATTER(11, 60, false),
        HAMMER_THROW(12, 70, true);

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

        @Override
        public boolean isPhaseTransition() {
            return this == SHATTER;
        }
    }
}