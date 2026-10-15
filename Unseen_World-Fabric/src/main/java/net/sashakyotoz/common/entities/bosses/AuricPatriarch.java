package net.sashakyotoz.common.entities.bosses;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.ai.brains.AuricPatriarchBrain;
import net.sashakyotoz.common.entities.ai.listeners.PatriarchEventListener;
import net.sashakyotoz.common.entities.custom.AureateVexEntity;
import net.sashakyotoz.common.entities.custom.ReaverEntity;
import net.sashakyotoz.common.entities.custom.projectiles.ParticleProjectileEntity;
import net.sashakyotoz.common.entities.util.BossParticles;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class AuricPatriarch extends StateMachineBoss<AuricPatriarch.PatriarchState> {
    public static final EntityDataSerializer<PatriarchState> PATRIARCH_STATE = EntityDataSerializer.simpleEnum(PatriarchState.class);
    public static final EntityDataAccessor<PatriarchState> PHASE = SynchedEntityData.defineId(AuricPatriarch.class, PATRIARCH_STATE);
    public static final EntityDataAccessor<Boolean> IS_FLYING = SynchedEntityData.defineId(AuricPatriarch.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ARE_MINIONS_OUT = SynchedEntityData.defineId(AuricPatriarch.class, EntityDataSerializers.BOOLEAN);

    public static final int GOLD = 0xFFC83D;
    public static final int PALE_GOLD = 0xFFEFB0;
    public static final int AMBER = 0xE0861E;
    private static final DustParticleOptions GOLD_DUST = BossParticles.dust(GOLD, 1.2F);
    private static final DustParticleOptions PALE_DUST = BossParticles.dust(PALE_GOLD, 0.8F);
    private static final double MINION_LEASH_DISTANCE_SQR = 64.0D * 64.0D;
    private static final int AUREATE_UNDEAD_TICKS = 80;
    private static final int MAX_REAVERS_PER_SUMMON = 4;
    private static final int MAX_REAVERS_ALIVE = 5;
    private static final int REAVER_RESUMMON_THRESHOLD = 1;
    private static final float ASCENSION_HEALTH_FRACTION = 0.6F;
    private static final int ASCENSION_PRESSURE_HITS = 5;
    private static final int ASCENSION_PRESSURE_WINDOW = 80;
    private static final double ASCENSION_MELEE_RANGE_SQR = 36.0D;
    private static final int AERIAL_PHASE_TICKS = 400;
    private static final int AERIAL_DECISION_DELAY = 40;
    private static final double HOVER_RADIUS = 6.0D;
    private static final double HOVER_HEIGHT = 7.0D;

    private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("entity.unseen_world.auric_patriarch"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
    public final DynamicGameEventListener<PatriarchEventListener> listener = new DynamicGameEventListener<>(
            new PatriarchEventListener(this, new EntityPositionSource(this, this.getEyeHeight())));
    private final List<LivingEntity> minions = new ArrayList<>();

    public final AnimationState idleTransition = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState flyingTransition = new AnimationState();
    public final AnimationState skyBurst = new AnimationState();
    public final AnimationState vexTime = new AnimationState();
    public final AnimationState divineTouch = new AnimationState();
    public final AnimationState auricBarrier = new AnimationState();
    public final AnimationState wingKnockback = new AnimationState();
    public final AnimationState grabAndStrike = new AnimationState();
    public final AnimationState suddenBeam = new AnimationState();
    public final AnimationState aureateUndead = new AnimationState();
    public final AnimationState death = new AnimationState();

    private final GroundPathNavigation groundNavigation;
    private final MoveControl groundMoveControl;
    private final FlyingPathNavigation flyingNavigation;
    private final FlyingMoveControl flyingMoveControl;

    private boolean hasUsedVexTimeInFlight = false;
    private boolean ascensionUsed = false;
    private boolean forcedTakeoff = false;
    private int aerialPhaseTicks = 0;
    private int pressureHits = 0;
    private int pressureWindowStart = Integer.MIN_VALUE;
    private final Map<PatriarchState, Integer> lastUsed = new EnumMap<>(PatriarchState.class);
    @Nullable
    private PatriarchState lastTactic;

    protected static final ImmutableList<SensorType<? extends Sensor<? super AuricPatriarch>>> SENSORS = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.NEAREST_PLAYERS
    );

    public AuricPatriarch(EntityType<? extends BossLikePathfinderMob> entityType, Level level) {
        super(entityType, level, PatriarchState.class);
        this.groundNavigation = new GroundPathNavigation(this, level);
        this.groundMoveControl = new MoveControl(this);
        this.flyingNavigation = new FlyingPathNavigation(this, level);
        this.flyingMoveControl = new FlyingMoveControl(this, 20, true);
        this.setGroundMovement();

        this.bindAnimation(this.death, PatriarchState.DYING);
        this.bindAnimation(this.idleTransition, PatriarchState.AWAKE_TO_IDLE, PatriarchState.IDLE_TO_AWAKE);
        this.bindAnimation(this.idle, PatriarchState.IDLING);
        this.bindAnimation(this.flyingTransition, PatriarchState.TO_FLYING, PatriarchState.TO_GROUND);
        this.bindAnimation(this.vexTime, PatriarchState.VEX_TIME);
        this.bindAnimation(this.skyBurst, PatriarchState.SKY_BURST);
        this.bindAnimation(this.divineTouch, PatriarchState.DIVINE_TOUCH);
        this.bindAnimation(this.auricBarrier, PatriarchState.AURIC_BARRIER);
        this.bindAnimation(this.wingKnockback, PatriarchState.WING_KNOCKBACK);
        this.bindAnimation(this.grabAndStrike, PatriarchState.GRAB_AND_STRIKE);
        this.bindAnimation(this.aureateUndead, PatriarchState.AUREATE_UNDEAD);
        this.bindAnimation(this.suddenBeam, PatriarchState.SUDDEN_BEAM);
    }

    @Override
    protected EntityDataAccessor<PatriarchState> stateAccessor() {
        return PHASE;
    }

    @Override
    protected PatriarchState initialState() {
        return PatriarchState.STANDING;
    }

    @Override
    protected PatriarchState standingState() {
        return PatriarchState.STANDING;
    }

    @Override
    protected PatriarchState idlingState() {
        return PatriarchState.IDLING;
    }

    @Override
    protected PatriarchState sleepTransitionState() {
        return PatriarchState.AWAKE_TO_IDLE;
    }

    @Override
    protected PatriarchState wakeTransitionState() {
        return PatriarchState.IDLE_TO_AWAKE;
    }

    @Override
    protected PatriarchState dyingState() {
        return PatriarchState.DYING;
    }

    @Override
    protected int transitionTicks() {
        return 40;
    }

    @Override
    protected int decisionDelay() {
        if (this.isInAerialPhase()) {
            return AERIAL_DECISION_DELAY;
        }
        return this.getHealth() < this.getMaxHealth() * 0.4F ? 35 : 50;
    }

    @Override
    protected double chaseSpeed() {
        return this.isFlying() ? 1.2D : 1.0D;
    }

    @Override
    protected double engageDistance() {
        return 1.0D;
    }

    @Override
    protected Brain.Provider<AuricPatriarch> brainProvider() {
        return Brain.provider(MEMORY_MODULES, SENSORS);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return AuricPatriarchBrain.create(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<AuricPatriarch> getBrain() {
        return (Brain<AuricPatriarch>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("patriarchBrain");
        if (this.level() instanceof ServerLevel world) {
            this.getBrain().tick(world, this);
        }
        this.level().getProfiler().pop();
        this.level().getProfiler().push("patriarchActivityUpdate");
        AuricPatriarchBrain.updateActivities(this);
        this.level().getProfiler().pop();
        super.customServerAiStep();

        LivingEntity target = this.getPrimaryTarget();
        if (target != null) {
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_FLYING, false);
        this.entityData.define(ARE_MINIONS_OUT, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsFlying", this.isFlying());
        compound.putBoolean("AscensionUsed", this.ascensionUsed);
        compound.putInt("AerialPhaseTicks", this.aerialPhaseTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("IsFlying")) {
            boolean flying = compound.getBoolean("IsFlying");
            this.setFlying(flying);
            if (flying) {
                this.setFlyingMovement();
            } else {
                this.setGroundMovement();
            }
        }
        this.ascensionUsed = compound.getBoolean("AscensionUsed");
        this.aerialPhaseTicks = this.isFlying() ? Math.max(0, compound.getInt("AerialPhaseTicks")) : 0;
    }

    public boolean isInAerialPhase() {
        return this.isFlying() && this.aerialPhaseTicks > 0;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && this.isAlive() && this.isAwake()) {
            this.trackAscensionPressure(source);
        }
        return result;
    }

    private void trackAscensionPressure(DamageSource source) {
        if (this.ascensionUsed || this.forcedTakeoff || this.isFlying()) {
            return;
        }
        Entity direct = source.getDirectEntity();
        if (direct instanceof LivingEntity && direct == source.getEntity() && this.distanceToSqr(direct) <= ASCENSION_MELEE_RANGE_SQR) {
            if (this.tickCount - this.pressureWindowStart > ASCENSION_PRESSURE_WINDOW) {
                this.pressureWindowStart = this.tickCount;
                this.pressureHits = 0;
            }
            this.pressureHits++;
        }
        if (this.pressureHits >= ASCENSION_PRESSURE_HITS || this.getHealth() <= this.getMaxHealth() * ASCENSION_HEALTH_FRACTION) {
            this.forcedTakeoff = true;
            this.pressureHits = 0;
        }
    }

    @Override
    protected void tickStanding(@Nullable LivingEntity target) {
        if (this.forcedTakeoff && target != null && !this.isFlying()) {
            this.tactics.clear();
            this.getNavigation().stop();
            this.chainState(PatriarchState.WING_KNOCKBACK);
            return;
        }
        super.tickStanding(target);
    }

    @Nullable
    @Override
    protected LivingEntity rememberedTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    protected void onTargetAcquired(LivingEntity target) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        this.getBrain().setMemory(MemoryModuleType.HURT_BY_ENTITY, target);
    }

    public void setPatriarchState(PatriarchState state) {
        this.setBossState(state);
    }

    public PatriarchState getPatriarchState() {
        return this.getBossState();
    }

    public boolean isFlying() {
        return this.entityData.get(IS_FLYING);
    }

    public void setFlying(boolean flying) {
        this.entityData.set(IS_FLYING, flying);
    }

    public boolean areMinionsOut() {
        return this.entityData.get(ARE_MINIONS_OUT);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        int damage = super.calculateFallDamage(fallDistance, damageMultiplier);
        return this.getPatriarchState() == PatriarchState.TO_GROUND ? Mth.floor(damage / 2.0F) : damage;
    }

    public float getFlyingTransitionSpeed() {
        return this.getPatriarchState() == PatriarchState.TO_GROUND ? -1.0F : 1.0F;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide() && this.isAlive()) {
            if (this.tickCount % 20 == 0) {
                this.pruneMinions();
            }
            if (this.aerialPhaseTicks > 0 && (!this.isFlying() || --this.aerialPhaseTicks <= 0)) {
                this.aerialPhaseTicks = 0;
            }
            this.updateMinionSyncState();
        }
    }

    @Override
    protected void chase(LivingEntity target) {
        float speed = (float) this.chaseSpeed();
        if (this.isInAerialPhase()) {
            Vec3 away = this.position().subtract(target.position()).multiply(1.0D, 0.0D, 1.0D);
            away = away.lengthSqr() < 1.0E-4D ? Vec3.directionFromRotation(0.0F, this.getYRot()) : away.normalize();
            Vec3 hover = target.position().add(away.scale(HOVER_RADIUS)).add(0.0D, HOVER_HEIGHT, 0.0D);
            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(BlockPos.containing(hover)), speed, 1));
            return;
        }
        this.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(target, false), speed, 2));
        super.chase(target);
    }

    @Override
    protected void tickStandingWithoutTarget() {
        if (this.isFlying()) {
            if (this.stateTicks >= 100) {
                this.setPatriarchState(PatriarchState.TO_GROUND);
            }
        } else {
            super.tickStandingWithoutTarget();
        }
    }

    @Override
    protected void tickAbility(PatriarchState state, int ticks, @Nullable LivingEntity target) {
        switch (state) {
            case TO_FLYING -> {
                if (ticks == 35) {
                    this.push(0.0D, 1.0D, 0.0D);
                }
                if (ticks >= 40) {
                    this.setFlyingMovement();
                    this.setFlying(true);
                    if (this.forcedTakeoff) {
                        this.forcedTakeoff = false;
                        this.ascensionUsed = true;
                        this.aerialPhaseTicks = AERIAL_PHASE_TICKS;
                        this.lastTactic = null;
                    }
                    this.returnToStanding();
                }
            }
            case TO_GROUND -> {
                if (ticks <= 40 && this.level().getBlockState(this.getOnPos().below()).isAir()) {
                    this.addDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.25D, 0.0D));
                }
                if (ticks >= 40) {
                    this.setGroundMovement();
                    this.setFlying(false);
                    this.hasUsedVexTimeInFlight = false;
                    this.aerialPhaseTicks = 0;
                    this.returnToStanding();
                }
            }
            case WING_KNOCKBACK -> {
                if (ticks == 20) {
                    this.getNavigation().stop();
                    this.executeWingKnockback();
                }
                if (ticks >= 30) {
                    if (this.forcedTakeoff && !this.isFlying()) {
                        this.hasUsedVexTimeInFlight = false;
                        this.chainState(PatriarchState.TO_FLYING);
                    } else {
                        this.returnToStanding();
                    }
                }
            }
            case VEX_TIME -> {
                if (ticks == 5) {
                    this.spawnVexMinions();
                }
                if (ticks > 20 && ticks % 20 == 0 && this.countMinions(AureateVexEntity.class) == 0) {
                    if (!this.isInAerialPhase() && target != null && (target.onGround() || this.distanceToSqr(target) <= 25.0D)) {
                        this.setPatriarchState(PatriarchState.TO_GROUND);
                    } else {
                        this.returnToStanding();
                    }
                }
            }
            case AUREATE_UNDEAD -> {
                if (ticks == 5) {
                    this.getNavigation().stop();
                }
                if (ticks == 25) {
                    this.spawnReaverMinions(target);
                }
                if (ticks >= AUREATE_UNDEAD_TICKS) {
                    this.returnToStanding();
                }
            }
            case SUDDEN_BEAM -> {
                if (ticks == 10) {
                    this.getNavigation().stop();
                }
                if (ticks == 50) {
                    this.executeBeamStrike(target);
                }
                if (ticks >= 80) {
                    this.returnToStanding();
                }
            }
            case AURIC_BARRIER -> {
                if (ticks == 10) {
                    this.getNavigation().stop();
                    this.executeAuricBarrier(target);
                }
                if (ticks >= 50) {
                    this.returnToStanding();
                }
            }
            case GRAB_AND_STRIKE -> {
                if (target != null) {
                    if (ticks == 1) {
                        double xOff = this.getXVector(1.0D, target.getYRot());
                        double zOff = this.getZVector(1.0D, target.getYRot());
                        this.serverParticles(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY() + 1.5D, this.getZ(), 40, 0.6D, 1.2D, 0.6D, 0.05D);
                        this.teleportTo(target.getX() + xOff, target.getY(), target.getZ() + zOff);
                        this.lookAt(target, 90.0F, 90.0F);
                    }
                    if (ticks == 10) {
                        this.impulse(target, new Vec3(0.0D, 1.5D, 0.0D));
                    }
                    if (ticks >= 50) {
                        this.returnToStanding();
                    }
                } else if (ticks >= 20) {
                    this.returnToStanding();
                }
            }
            case DIVINE_TOUCH, SKY_BURST -> {
                if (ticks == 20) {
                    double xOff = this.getXVector(7.0D, this.getYRot());
                    double zOff = this.getZVector(7.0D, this.getYRot());
                    this.damageInArea(new AABB(this.getOnPos(), BlockPos.containing(this.getX() + xOff, this.getY(), this.getZ() + zOff)).inflate(2.5D));
                    this.push(this.getXVector(1.0D, this.getYRot()), 0.125D, this.getZVector(1.0D, this.getYRot()));
                }
                if (ticks >= 40) {
                    this.returnToStanding();
                }
            }
            default -> {
            }
        }
    }

    @Override
    protected void tickAbilityClient(PatriarchState state, int ticks) {
        Level level = this.level();
        Vec3 chest = this.position().add(0.0D, this.getBbHeight() * 0.6D, 0.0D);
        switch (state) {
            case IDLING -> this.spawnSpiralWayParticles();
            case AWAKE_TO_IDLE, IDLE_TO_AWAKE -> {
                if (ticks % 2 == 0) {
                    BossParticles.converge(level, this.random, ParticleTypes.ENCHANT, chest, 3.0D, 4);
                }
            }
            case TO_FLYING -> {
                if (ticks == 30) {
                    this.playClientSound(SoundEvents.PHANTOM_FLAP, 0.75F, 1.5F);
                    this.spawnParticle(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY() + 1.0D, this.getZ(), 1.5F);
                    BossParticles.ring(level, ParticleTypes.CLOUD, this.position().add(0.0D, 0.1D, 0.0D), 0.8D, 24, 0.35D, 0.0D);
                }
                if (ticks > 30) {
                    BossParticles.motes(level, this.random, GOLD_DUST, this.position(), 1.5D, 0.5D, 2, -0.05D);
                }
            }
            case TO_GROUND -> {
                if (this.onGround() && ticks > 5 && ticks % 10 == 0) {
                    BossParticles.ring(level, ParticleTypes.CLOUD, this.position().add(0.0D, 0.1D, 0.0D), 1.0D, 20, 0.3D, 0.0D);
                }
            }
            case SUDDEN_BEAM -> {
                Vec3 hand = this.localPoint(0.9D, this.getBbHeight() * 0.65D, -0.6D);
                if (ticks == 10) {
                    this.playClientSound(ModSoundEvents.WIND_SLIDE, 1.0F, 1.0F);
                }
                if (ticks > 10 && ticks < 50) {
                    BossParticles.converge(level, this.random, ParticleTypes.ENCHANT, hand, 2.5D + (50 - ticks) * 0.05D, 3);
                    level.addParticle(PALE_DUST, hand.x, hand.y, hand.z, 0.0D, 0.0D, 0.0D);
                }
                if (ticks == 50) {
                    this.playClientSound(SoundEvents.BEACON_AMBIENT, 1.0F, 1.5F - this.getRandom().nextFloat() * 0.5F);
                    level.addParticle(ParticleTypes.FLASH, hand.x, hand.y, hand.z, 0.0D, 0.0D, 0.0D);
                    BossParticles.burst(level, this.random, ParticleTypes.END_ROD, hand, 24, 0.35D);
                }
            }
            case AUREATE_UNDEAD -> {
                if (ticks <= 30 && ticks % 5 == 0) {
                    for (int i = 0; i < 4; i++) {
                        Vec3 spot = this.position().add(this.getXVector(i + 1, this.getYRot()), 0.1D, this.getZVector(i + 1, this.getYRot()));
                        BossParticles.ring(level, GOLD_DUST, spot, 0.3D + ticks / 30.0D * 0.6D, 10, 0.0D, 0.0D);
                    }
                }
                if (ticks == 30) {
                    this.playClientSound(ModSoundEvents.WIND_SLIDE, 1.0F, 1.6F);
                    for (int i = 0; i < 4; i++) {
                        double offsetX = this.getXVector(i + 1, this.getYRot());
                        double offsetZ = this.getZVector(i + 1, this.getYRot());
                        Vec3 spot = new Vec3(this.getX() + offsetX, this.getY() + 0.125D, this.getZ() + offsetZ);
                        level.addParticle(ModParticleTypes.EXPLOSION_CIRCLE, spot.x, spot.y, spot.z, 0.0D, 0.0D, 0.0D);
                        BossParticles.column(level, this.random, ParticleTypes.END_ROD, spot, 0.4D, 10, 0.35D);
                    }
                }
                if (ticks >= 70) {
                    this.spawnSpiralWayParticles();
                }
            }
            case VEX_TIME -> {
                Vec3 crown = this.position().add(0.0D, this.getBbHeight() + 0.6D, 0.0D);
                if (ticks <= 5) {
                    BossParticles.ring(level, GOLD_DUST, crown, 0.4D + ticks * 0.15D, 16, 0.0D, 0.0D);
                }
                if (ticks == 5) {
                    level.addParticle(ParticleTypes.FLASH, crown.x, crown.y, crown.z, 0.0D, 0.0D, 0.0D);
                    BossParticles.burst(level, this.random, ParticleTypes.TOTEM_OF_UNDYING, crown, 40, 0.6D);
                }
                if (this.areMinionsOut() && ticks % 3 == 0) {
                    double angle = this.tickCount * 0.15D;
                    for (int i = 0; i < 3; i++) {
                        double a = angle + i * (Math.PI * 2.0D / 3.0D);
                        level.addParticle(GOLD_DUST, this.getX() + Math.cos(a) * 2.0D, this.getY() + this.getBbHeight() * 0.8D, this.getZ() + Math.sin(a) * 2.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            case GRAB_AND_STRIKE -> {
                if (ticks == 1) {
                    this.playClientSound(SoundEvents.ENDERMAN_TELEPORT, 0.75F, 1.5F);
                    this.spawnParticle(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY() + 1.0D, this.getZ(), 1.5F);
                    level.addParticle(ParticleTypes.FLASH, chest.x, chest.y, chest.z, 0.0D, 0.0D, 0.0D);
                }
                if (ticks == 10) {
                    this.playClientSound(SoundEvents.IRON_GOLEM_ATTACK, 0.75F, 1.5F);
                    this.spawnParticle(ParticleTypes.ASH, this.getX(), this.getY() + 2.0D, this.getZ(), 1.0F);
                    BossParticles.column(level, this.random, ParticleTypes.ELECTRIC_SPARK, this.localPoint(1.2D, 0.2D, 0.0D), 0.5D, 16, 0.6D);
                }
            }
            case WING_KNOCKBACK -> {
                if (ticks > 10 && ticks < 25) {
                    BossParticles.converge(level, this.random, PALE_DUST, chest, 2.5D, 2);
                }
                if (ticks == 25) {
                    this.playClientSound(SoundEvents.PHANTOM_FLAP, 1.5F, 1.8F);
                    this.spawnParticle(ParticleTypes.ASH, this.getX(), this.getY() + 1.0D, this.getZ(), 1.5F);
                    BossParticles.ring(level, ParticleTypes.CLOUD, this.position().add(0.0D, 0.2D, 0.0D), 1.0D, 36, 0.7D, 0.02D);
                    BossParticles.ring(level, ParticleTypes.END_ROD, this.position().add(0.0D, 1.2D, 0.0D), 1.0D, 24, 0.5D, 0.0D);
                }
            }
            case DIVINE_TOUCH, SKY_BURST -> {
                if (ticks == 5 && state == PatriarchState.DIVINE_TOUCH) {
                    this.playClientSound(SoundEvents.TRIDENT_RIPTIDE_1, 0.75F, 1.5F);
                }
                if (ticks == 20) {
                    this.playClientSound(ModSoundEvents.WIND_SLIDE, 1.0F, state == PatriarchState.SKY_BURST ? 1.2F : 1.0F);
                    Vec3 start = this.position().add(0.0D, 0.15D, 0.0D);
                    Vec3 end = start.add(this.horizontalForward().scale(7.0D));
                    BossParticles.line(level, ParticleTypes.END_ROD, start, end, 0.5D, 0.12D);
                    BossParticles.line(level, GOLD_DUST, start, end, 0.35D, 0.0D);
                    level.addParticle(ModParticleTypes.EXPLOSION_CIRCLE, end.x, end.y, end.z, 0.0D, 0.0D, 0.0D);
                }
            }
            case AURIC_BARRIER -> {
                if (ticks == 10) {
                    this.playClientSound(SoundEvents.BEACON_ACTIVATE, 1.0F, 1.6F);
                    BossParticles.dome(level, this.random, GOLD_DUST, this.position(), 3.0D, 60);
                    BossParticles.ring(level, ParticleTypes.END_ROD, this.position().add(0.0D, 0.1D, 0.0D), 3.0D, 32, 0.0D, 0.05D);
                }
            }
            case DYING -> {
                double progress = Math.min(1.0D, ticks / (double) DEATH_ANIMATION_TICKS);
                BossParticles.motes(level, this.random, GOLD_DUST, this.position(), 1.2D, this.getBbHeight(), 1 + (int) (progress * 4.0D), 0.08D);
                if (ticks % 12 == 0) {
                    BossParticles.burst(level, this.random, ParticleTypes.END_ROD, chest, 8 + (int) (progress * 16.0D), 0.25D);
                }
                if (ticks == DEATH_ANIMATION_TICKS - 2) {
                    level.addParticle(ParticleTypes.FLASH, chest.x, chest.y, chest.z, 0.0D, 0.0D, 0.0D);
                    BossParticles.burst(level, this.random, ParticleTypes.TOTEM_OF_UNDYING, chest, 80, 0.9D);
                    this.playClientSound(SoundEvents.TOTEM_USE, 1.2F, 0.6F);
                }
            }
            default -> {
                if (this.isAwake() && this.tickCount % 3 == 0) {
                    BossParticles.motes(level, this.random, PALE_DUST, this.position(), 0.9D, this.getBbHeight() * 0.8D, 1, 0.03D);
                }
            }
        }

        if (this.isFlying() && this.tickCount % 20 == 0) {
            this.playClientSound(SoundEvents.PHANTOM_FLAP, 0.75F, 1.75F);
        }
        if (this.isFlying() && this.tickCount % 2 == 0) {
            Vec3 left = this.localPoint(-0.4D, this.getBbHeight() * 0.7D, 1.6D);
            Vec3 right = this.localPoint(-0.4D, this.getBbHeight() * 0.7D, -1.6D);
            level.addParticle(GOLD_DUST, left.x, left.y, left.z, 0.0D, -0.02D, 0.0D);
            level.addParticle(GOLD_DUST, right.x, right.y, right.z, 0.0D, -0.02D, 0.0D);
        }
    }

    private void updateMinionSyncState() {
        boolean active = !this.minions.isEmpty();
        if (this.entityData.get(ARE_MINIONS_OUT) != active) {
            this.entityData.set(ARE_MINIONS_OUT, active);
        }
    }

    private void pruneMinions() {
        this.minions.removeIf(entity -> entity.isRemoved() || !entity.isAlive() || entity.distanceToSqr(this) > MINION_LEASH_DISTANCE_SQR);
    }

    private int countMinions(Class<? extends LivingEntity> type) {
        int count = 0;
        for (LivingEntity minion : this.minions) {
            if (type.isInstance(minion)) {
                count++;
            }
        }
        return count;
    }

    private boolean isReady(PatriarchState state, int cooldown) {
        Integer last = this.lastUsed.get(state);
        return last == null || this.tickCount - last >= cooldown;
    }

    @Override
    protected void onStateChanged(PatriarchState previous, PatriarchState next) {
        if (next.isAttack() || next == PatriarchState.SKY_BURST) {
            this.lastUsed.put(next, this.tickCount);
            this.lastTactic = next;
        }
    }

    @Override
    protected void planTactics(LivingEntity target, Queue<PatriarchState> plan) {
        double distSqr = this.distanceToSqr(target);
        boolean targetOnGround = target.onGround();

        if (this.isFlying()) {
            if (this.isInAerialPhase()) {
                this.planAerialTactics(plan);
                return;
            }
            if (distSqr <= 1024.0D) {
                if (!this.hasUsedVexTimeInFlight) {
                    this.hasUsedVexTimeInFlight = true;
                    plan.add(PatriarchState.VEX_TIME);
                } else if (targetOnGround || distSqr <= 36.0D) {
                    plan.add(PatriarchState.TO_GROUND);
                } else {
                    plan.add(PatriarchState.SKY_BURST);
                }
            } else {
                plan.add(PatriarchState.TO_GROUND);
            }
            return;
        }

        if (target.getY() > this.getY() + 2.5D && !targetOnGround) {
            this.hasUsedVexTimeInFlight = false;
            plan.add(PatriarchState.TO_FLYING);
            return;
        }

        this.pruneMinions();
        boolean enraged = this.getHealth() < this.getMaxHealth() * 0.5F;
        Map<PatriarchState, Integer> weights = new EnumMap<>(PatriarchState.class);
        if (distSqr <= 81.0D && targetOnGround) {
            this.offer(weights, PatriarchState.DIVINE_TOUCH, 4, 60);
            this.offer(weights, PatriarchState.AURIC_BARRIER, 3, 160);
            this.offer(weights, PatriarchState.GRAB_AND_STRIKE, 2, 200);
            if (distSqr <= 25.0D) {
                this.offer(weights, PatriarchState.WING_KNOCKBACK, 2, 240);
            }
        } else {
            this.offer(weights, PatriarchState.SUDDEN_BEAM, 4, 80);
            this.offer(weights, PatriarchState.GRAB_AND_STRIKE, 3, 200);
            if (distSqr <= 144.0D) {
                this.offer(weights, PatriarchState.DIVINE_TOUCH, 1, 60);
            }
        }
        if (this.countMinions(ReaverEntity.class) <= REAVER_RESUMMON_THRESHOLD) {
            this.offer(weights, PatriarchState.AUREATE_UNDEAD, enraged ? 3 : 2, enraged ? 360 : 500);
        }
        weights.remove(this.lastTactic);

        PatriarchState first = this.pickWeighted(weights);
        if (first == null) {
            return;
        }
        plan.add(first);
        weights.remove(first);
        weights.remove(PatriarchState.AUREATE_UNDEAD);
        PatriarchState second = this.pickWeighted(weights);
        if (second != null) {
            plan.add(second);
        }
    }

    private void planAerialTactics(Queue<PatriarchState> plan) {
        if (!this.hasUsedVexTimeInFlight) {
            this.hasUsedVexTimeInFlight = true;
            plan.add(PatriarchState.VEX_TIME);
            return;
        }
        Map<PatriarchState, Integer> weights = new EnumMap<>(PatriarchState.class);
        this.offer(weights, PatriarchState.SUDDEN_BEAM, 4, 60);
        this.offer(weights, PatriarchState.AURIC_BARRIER, 3, 140);
        weights.remove(this.lastTactic);
        PatriarchState pick = this.pickWeighted(weights);
        if (pick != null) {
            plan.add(pick);
        }
    }

    private void offer(Map<PatriarchState, Integer> weights, PatriarchState state, int weight, int cooldown) {
        if (this.isReady(state, cooldown)) {
            weights.put(state, weight);
        }
    }

    @Nullable
    private PatriarchState pickWeighted(Map<PatriarchState, Integer> weights) {
        int total = 0;
        for (int weight : weights.values()) {
            total += weight;
        }
        if (total <= 0) {
            return null;
        }
        int roll = this.random.nextInt(total);
        for (Map.Entry<PatriarchState, Integer> entry : weights.entrySet()) {
            roll -= entry.getValue();
            if (roll < 0) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void setFlyingMovement() {
        if (this.navigation != this.flyingNavigation) {
            this.navigation.stop();
            this.navigation = this.flyingNavigation;
            this.moveControl = this.flyingMoveControl;
            this.setNoGravity(true);
        }
    }

    private void setGroundMovement() {
        if (this.navigation != this.groundNavigation) {
            this.navigation.stop();
            this.navigation = this.groundNavigation;
            this.moveControl = this.groundMoveControl;
            this.setNoGravity(false);
        }
    }

    private void spawnSpiralWayParticles() {
        if (this.tickCount % 2 == 0) {
            BossParticles.helix(this.level(), ParticleTypes.REVERSE_PORTAL, this.position(), 2.5D, 3.0D, this.tickCount, 1, 40);
            BossParticles.helix(this.level(), GOLD_DUST, this.position(), 2.2D, 3.0D, this.tickCount + 20, 2, 40);
        }
    }

    private void executeBeamStrike(@Nullable LivingEntity target) {
        if (target == null) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            ParticleProjectileEntity projectile = ModEntities.PARTICLE_PROJECTILE.create(this.level());
            if (projectile != null) {
                projectile.moveTo(this.getX(), this.getEyeY() - 0.2D, this.getZ());
                projectile.setClusterExplosion(true);
                projectile.setOwner(this);
                float yawOffset = (i - 1) * 20.0F;
                projectile.shootFromRotation(this, this.getXRot() - 30.0F, this.getYRot() + yawOffset, 0.0F, 0.75F, 0.5F);
                this.level().addFreshEntity(projectile);
            }
        }
    }

    private void executeWingKnockback() {
        this.playSound(SoundEvents.PHANTOM_FLAP, 1.0F, 0.8F);
        this.hitNearbyMobs(10.0F, 6);
        for (Entity entity : this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(16.0D))) {
            if (entity != this && !(entity instanceof AureateVexEntity) && !entity.isAlliedTo(this) && !(entity instanceof Player player && (player.isCreative() || player.isSpectator()))) {
                Vec3 direction = entity.position().subtract(this.position()).normalize().scale(2.5D);
                this.impulse(entity, new Vec3(direction.x, 1.0D, direction.z));
            }
        }
    }

    private void executeAuricBarrier(@Nullable LivingEntity target) {
        this.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.7F);
        this.hitNearbyMobs(8.0F, 5);
        if (target != null) {
            AreaEffectCloud area = EntityType.AREA_EFFECT_CLOUD.create(this.level());
            if (area != null) {
                area.moveTo(target.getX(), target.getY() + 0.25D, target.getZ());
                area.setRadius(4.0F);
                area.setDuration(100);
                area.setWaitTime(10);
                area.setOwner(this);
                area.setParticle(ParticleTypes.REVERSE_PORTAL);
                area.setPotion(Potions.WEAKNESS);
                this.level().addFreshEntity(area);
            }
        }
    }

    private void summonFlourish(ServerLevel level, Vec3 pos) {
        level.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 1.0D, pos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(GOLD_DUST, pos.x, pos.y + 1.0D, pos.z, 24, 0.25D, 1.0D, 0.25D, 0.0D);
        level.sendParticles(ParticleTypes.END_ROD, pos.x, pos.y + 0.2D, pos.z, 12, 0.2D, 0.1D, 0.2D, 0.12D);
    }

    private void spawnVexMinions() {
        if (this.level() instanceof ServerLevel serverLevel) {
            this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 1.4F);
            for (int i = 0; i < 4; i++) {
                AureateVexEntity vex = ModEntities.AUREATE_VEX.create(serverLevel);
                if (vex != null) {
                    double offsetX = (this.random.nextDouble() - 0.5D) * 6.0D;
                    double offsetZ = (this.random.nextDouble() - 0.5D) * 6.0D;
                    vex.moveTo(this.getX() + offsetX, this.getY() + 1.0D, this.getZ() + offsetZ, this.random.nextFloat() * 360.0F, 0.0F);
                    vex.setOwner(this);
                    vex.setBoundOrigin(this.getOnPos().below());
                    vex.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(this.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
                    serverLevel.addFreshEntity(vex);
                    this.minions.add(vex);
                    this.summonFlourish(serverLevel, vex.position());
                }
            }
        }
    }

    private void spawnReaverMinions(@Nullable LivingEntity target) {
        if (this.level() instanceof ServerLevel serverLevel) {
            this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 0.4F);
            this.pruneMinions();
            int count = Math.min(MAX_REAVERS_PER_SUMMON, MAX_REAVERS_ALIVE - this.countMinions(ReaverEntity.class));
            for (int i = 0; i < count; i++) {
                ReaverEntity reaver = ModEntities.REAVER.create(serverLevel);
                if (reaver != null) {
                    double offsetX = this.getXVector(i + 1, this.getYRot());
                    double offsetZ = this.getZVector(i + 1, this.getYRot());
                    reaver.moveTo(this.getX() + offsetX, this.getY() + 1.0D, this.getZ() + offsetZ, this.random.nextFloat() * 360.0F, 0.0F);
                    reaver.setOwner(this);
                    if (target != null) {
                        reaver.setTarget(target);
                        reaver.setPersistentAngerTarget(target.getUUID());
                    }
                    serverLevel.addFreshEntity(reaver);
                    this.minions.add(reaver);
                    this.summonFlourish(serverLevel, reaver.position());
                }
            }
        }
    }

    private void damageInArea(AABB damageBox) {
        float damageAmount = this.attackDamage(0.65F);
        DamageSource source = this.damageSources().indirectMagic(this, this);
        for (LivingEntity entity : this.enemiesIn(damageBox)) {
            if (!(entity instanceof AureateVexEntity)) {
                entity.hurt(source, damageAmount);
            }
        }
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        if (this.level() instanceof ServerLevel serverWorld) {
            callback.accept(this.listener, serverWorld);
        }
    }

    @Override
    public boolean haveToDropLoot(DamageSource source) {
        return source.getEntity() instanceof Player;
    }

    @Override
    protected boolean isImmuneTo(PatriarchState state, DamageSource source) {
        return state == PatriarchState.WING_KNOCKBACK || (state == PatriarchState.TO_FLYING && this.forcedTakeoff) || (state == PatriarchState.VEX_TIME && this.countMinions(AureateVexEntity.class) > 0);
    }

    @Override
    protected void onDamageBlocked(DamageSource source) {
        this.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 0.6F);
    }

    @Override
    public float alphaIntensity() {
        return switch (this.getPatriarchState()) {
            case IDLING -> 0.75F;
            case TO_FLYING, TO_GROUND -> 0.5F;
            case SUDDEN_BEAM, DIVINE_TOUCH -> 1.0F;
            case WING_KNOCKBACK, VEX_TIME -> 0.8F;
            case AURIC_BARRIER, GRAB_AND_STRIKE -> 0.6F;
            default -> 0.0F;
        };
    }

    @Override
    public ServerBossEvent bossInfo() {
        return this.bossBar;
    }

    @Override
    public void die(DamageSource source) {
        this.setGroundMovement();
        this.aerialPhaseTicks = 0;
        this.forcedTakeoff = false;
        if (!this.level().isClientSide()) {
            this.setFlying(false);
            for (LivingEntity minion : this.minions) {
                if (minion.isAlive()) {
                    minion.kill();
                }
            }
            this.minions.clear();
        }
        super.die(source);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FLYING_SPEED, 0.3D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    public enum PatriarchState implements BossState {
        STANDING(0),
        TO_FLYING(1),
        TO_GROUND(2),
        VEX_TIME(3),
        SKY_BURST(4),
        DIVINE_TOUCH(5),
        AURIC_BARRIER(6),
        WING_KNOCKBACK(7),
        GRAB_AND_STRIKE(8),
        SUDDEN_BEAM(9),
        AUREATE_UNDEAD(10),
        DYING(11),
        AWAKE_TO_IDLE(12),
        IDLING(13),
        IDLE_TO_AWAKE(14);

        private final int id;

        PatriarchState(int id) {
            this.id = id;
        }

        public int id() {
            return this.id;
        }

        @Override
        public boolean isAttack() {
            return this == VEX_TIME || this == DIVINE_TOUCH || this == AURIC_BARRIER
                    || this == WING_KNOCKBACK || this == GRAB_AND_STRIKE || this == SUDDEN_BEAM
                    || this == AUREATE_UNDEAD;
        }
    }
}