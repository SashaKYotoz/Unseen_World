package net.sashakyotoz.common.entities.bosses;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.ai.brains.AuricPatriarchBrain;
import net.sashakyotoz.common.entities.ai.listeners.PatriarchEventListener;
import net.sashakyotoz.common.entities.custom.AureateVexEntity;
import net.sashakyotoz.common.entities.custom.ReaverEntity;
import net.sashakyotoz.common.entities.custom.projectiles.ParticleProjectileEntity;
import net.sashakyotoz.common.entities.util.BossParticles;
import net.sashakyotoz.common.entities.util.EnumDataSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class AuricPatriarch extends StateMachineBoss<AuricPatriarch.PatriarchState> {
    public static final EntityDataSerializer<PatriarchState> PATRIARCH_STATE = EnumDataSerializer.of(PatriarchState.class);
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
    private static final int ASCENSION_PRESSURE_HITS = 5;
    private static final int ASCENSION_PRESSURE_WINDOW = 80;
    private static final double ASCENSION_MELEE_RANGE_SQR = 36.0D;
    private static final int AERIAL_PHASE_TICKS = 400;
    private static final int AERIAL_DECISION_DELAY = 40;
    private static final double HOVER_RADIUS = 6.0D;
    private static final double HOVER_HEIGHT = 7.0D;
    private static final float[] PHASES = {0.6F, 0.25F};
    private static final int RITE_TICKS = 40;
    private static final int REASCEND_DELAY = 260;
    private static final int FALLING_SUN_IMPACT_TICK = 30;
    private static final int FALLING_SUN_TICKS = 60;
    public static final int JUDGEMENT_FIRST_TICK = 15;
    private static final int JUDGEMENT_INTERVAL = 4;
    private static final int JUDGEMENT_LAST_TICK = 63;
    private static final int JUDGEMENT_TICKS = 80;
    private static final int JUDGEMENT_DELAY = 10;
    private static final double JUDGEMENT_STEP = 2.2D;
    private static final double JUDGEMENT_START_OFFSET = 8.0D;
    private static final double PILLAR_RADIUS = 1.4D;
    private static final double PILLAR_HEIGHT = 5.0D;
    private static final double CHAIN_MIN_RANGE = 20.0D;
    private static final double CHAIN_MAX_RANGE = 48.0D;
    private static final int CHAIN_COOLDOWN = 140;
    private static final int SACRIFICE_TICK = 25;
    private static final int SACRIFICE_TICKS = 50;
    private static final float SACRIFICE_HEAL_PER_MINION = 0.03F;
    private static final float SACRIFICE_MAX_HEAL = 0.15F;
    private static final int LANDING_SCAN_DEPTH = 48;
    private static final int DESCENT_MAX_TICKS = 140;
    private static final int DESCENT_EASE_TICKS = 10;
    private static final int DESCENT_NO_GROUND_TICKS = 20;
    private static final double DESCENT_MIN_SPEED = 0.1D;
    private static final double DESCENT_MAX_SPEED = 0.6D;
    private static final double DESCENT_SPEED_PER_BLOCK = 0.09D;
    private static final double TOUCHDOWN_HEIGHT = 0.3D;
    private static final int TOUCHDOWN_TICKS = 25;
    private static final int FALLING_SUN_WINDUP_TICKS = 10;
    private static final double FALLING_SUN_MAX_DIVE_SPEED = 2.5D;
    private static final double DYING_MAX_SINK_SPEED = 0.25D;
    private static final int EMPOWERED_ATTACKS = 2;
    private static final float EMPOWER_FACTOR = 1.5F;
    public static final int SUNBURST_CHANNEL_TICK = 10;
    public static final int SUNBURST_BLAST_TICK = 90;
    private static final int SUNBURST_TICKS = 110;
    public static final double SUNBURST_RADIUS = 16.0D;
    private static final double SANCTUARY_RADIUS = 2.5D;
    private static final int SANCTUARY_COUNT = 2;
    private static final float SUNBURST_STAGGER_DAMAGE = 45.0F;
    private static final int SUNBURST_COOLDOWN = 600;

    private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("entity.unseen_world.auric_patriarch"), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_10);
    public final DynamicGameEventListener<PatriarchEventListener> listener = new DynamicGameEventListener<>(
            new PatriarchEventListener(this, new EntityPositionSource(this, this.getEyeHeight())));
    private final List<LivingEntity> minions = new ArrayList<>();

    public final AnimationState idleTransition = new AnimationState();
    public final AnimationState idle = new AnimationState();
    public final AnimationState flyingTransition = new AnimationState();
    public final AnimationState hover = new AnimationState();
    public final AnimationState descent = new AnimationState();
    public final AnimationState landing = new AnimationState();
    public final AnimationState skyBurst = new AnimationState();
    public final AnimationState vexTime = new AnimationState();
    public final AnimationState divineTouch = new AnimationState();
    public final AnimationState auricBarrier = new AnimationState();
    public final AnimationState wingKnockback = new AnimationState();
    public final AnimationState grabAndStrike = new AnimationState();
    public final AnimationState suddenBeam = new AnimationState();
    public final AnimationState aureateUndead = new AnimationState();
    public final AnimationState fallingSun = new AnimationState();
    public final AnimationState finalSunburst = new AnimationState();
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
    private final TelegraphedStrikes judgement = new TelegraphedStrikes(new JudgementHandler());
    private final List<Vec3> sanctuaries = new ArrayList<>();
    private Vec3 judgementCursor = Vec3.ZERO;
    private int lastLandedTick = -1000;
    private double landingY = Double.NaN;
    private int lastChainTick = -1000;
    private int empoweredStrikes = 0;
    private boolean empoweredAttack = false;
    private float sunburstDamage = 0.0F;
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
        this.bindAnimation(this.flyingTransition, PatriarchState.TO_FLYING);
        this.bindAnimation(this.descent, PatriarchState.TO_GROUND);
        this.bindAnimation(this.landing, PatriarchState.TOUCHDOWN);
        this.bindAnimation(this.vexTime, PatriarchState.VEX_TIME, PatriarchState.ASCENSION_RITE);
        this.bindAnimation(this.skyBurst, PatriarchState.SKY_BURST);
        this.bindAnimation(this.divineTouch, PatriarchState.DIVINE_TOUCH);
        this.bindAnimation(this.auricBarrier, PatriarchState.AURIC_BARRIER);
        this.bindAnimation(this.wingKnockback, PatriarchState.WING_KNOCKBACK);
        this.bindAnimation(this.grabAndStrike, PatriarchState.GRAB_AND_STRIKE);
        this.bindAnimation(this.aureateUndead, PatriarchState.AUREATE_UNDEAD, PatriarchState.SACRIFICE);
        this.bindAnimation(this.suddenBeam, PatriarchState.SUDDEN_BEAM, PatriarchState.SOLAR_JUDGEMENT);
        this.bindAnimation(this.fallingSun, PatriarchState.FALLING_SUN);
        this.bindAnimation(this.finalSunburst, PatriarchState.FINAL_SUNBURST);
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
        if (this.getBossPhase() >= 2) {
            return 30;
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_FLYING, false);
        builder.define(ARE_MINIONS_OUT, false);
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

    @Override
    protected float[] phaseThresholds() {
        return PHASES;
    }

    @Override
    protected PatriarchState phaseTransitionState(int phase) {
        return phase >= 2 ? PatriarchState.FALLING_SUN : PatriarchState.ASCENSION_RITE;
    }

    @Override
    protected BossEvent.BossBarColor phaseBarColor(int phase) {
        return switch (phase) {
            case 0 -> BossEvent.BossBarColor.YELLOW;
            case 1 -> BossEvent.BossBarColor.WHITE;
            default -> BossEvent.BossBarColor.RED;
        };
    }

    @Override
    protected void onPhaseChanged(int phase) {
        this.judgement.clear();
        this.sanctuaries.clear();
        if (phase >= 2) {
            this.aerialPhaseTicks = 0;
            this.forcedTakeoff = false;
            this.hasUsedVexTimeInFlight = false;
            this.landingY = Double.NaN;
            this.lastUsed.put(PatriarchState.FINAL_SUNBURST, this.tickCount - SUNBURST_COOLDOWN + 100);
        }
    }

    private float power() {
        return this.empoweredAttack ? EMPOWER_FACTOR : 1.0F;
    }

    public boolean isInAerialPhase() {
        return this.isFlying() && this.aerialPhaseTicks > 0;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && this.isAlive() && this.isAwake()) {
            this.trackAscensionPressure(source);
            if (this.getPatriarchState() == PatriarchState.FINAL_SUNBURST && this.stateTicks >= SUNBURST_CHANNEL_TICK && this.stateTicks < SUNBURST_BLAST_TICK) {
                this.sunburstDamage += amount;
                if (this.sunburstDamage >= SUNBURST_STAGGER_DAMAGE) {
                    this.staggerSunburst();
                }
            }
        }
        return result;
    }

    private void trackAscensionPressure(DamageSource source) {
        if (this.ascensionUsed || this.forcedTakeoff || this.isFlying() || this.getBossPhase() > 0) {
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
        if (this.pressureHits >= ASCENSION_PRESSURE_HITS) {
            this.forcedTakeoff = true;
            this.pressureHits = 0;
        }
    }

    @Override
    protected void tickStanding(@Nullable LivingEntity target) {
        if (this.getBossPhase() == 1 && target != null && !this.isFlying() && !this.forcedTakeoff && this.tickCount - this.lastLandedTick > REASCEND_DELAY) {
            this.forcedTakeoff = true;
        }
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
    protected boolean ignoresFallDamage(PatriarchState state) {
        return this.isFlying() || state == PatriarchState.TO_FLYING || state == PatriarchState.TO_GROUND
                || state == PatriarchState.TOUCHDOWN || state == PatriarchState.FALLING_SUN;
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
            if (this.isFlying()) {
                this.resetFallDistance();
            }
            this.updateMinionSyncState();
            this.tickGildedChains();
        } else if (this.level().isClientSide()) {
            if (this.isFlying()) {
                this.hover.startIfStopped(this.tickCount);
            } else {
                this.hover.stop();
            }
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (!this.level().isClientSide() && !this.onGround()) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.y < -DYING_MAX_SINK_SPEED) {
                this.setDeltaMovement(motion.x * 0.8D, -DYING_MAX_SINK_SPEED, motion.z * 0.8D);
            }
            this.resetFallDistance();
        }
    }

    private void tickGildedChains() {
        PatriarchState state = this.getPatriarchState();
        if (this.getBossPhase() < 1 || state.isPhaseTransition() || !this.isAwake() || this.tickCount - this.lastChainTick < CHAIN_COOLDOWN) {
            return;
        }
        LivingEntity target = this.getPrimaryTarget();
        if (target == null) {
            return;
        }
        double distance = Math.sqrt(horizontalDistanceSqr(target.position(), this.position()));
        if (distance <= CHAIN_MIN_RANGE || distance >= CHAIN_MAX_RANGE || !this.hasLineOfSight(target)) {
            return;
        }
        this.lastChainTick = this.tickCount;
        Vec3 chest = this.position().add(0.0D, this.getBbHeight() * 0.6D, 0.0D);
        Vec3 pull = chest.subtract(target.position()).multiply(1.0D, 0.0D, 1.0D).normalize();
        this.impulse(target, pull.scale(1.8D).add(0.0D, 0.5D, 0.0D));
        this.serverLine(GOLD_DUST, chest, target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D), 0.8D);
        this.playSound(SoundEvents.CHAIN_BREAK, 2.0F, 0.6F);
        this.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.CHAIN_PLACE, SoundSource.HOSTILE, 2.0F, 0.5F);
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
                        if (this.getBossPhase() == 0) {
                            this.ascensionUsed = true;
                        }
                        this.aerialPhaseTicks = AERIAL_PHASE_TICKS;
                        this.lastTactic = null;
                    }
                    this.returnToStanding();
                }
            }
            case TO_GROUND -> this.tickDescent(ticks);
            case TOUCHDOWN -> {
                this.getNavigation().stop();
                if (ticks >= TOUCHDOWN_TICKS) {
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
            case ASCENSION_RITE -> {
                if (ticks == 1) {
                    this.playSound(SoundEvents.BEACON_POWER_SELECT, 2.5F, 0.8F);
                    this.playSound(SoundEvents.TOTEM_USE, 1.5F, 1.4F);
                }
                if (ticks % 5 == 0) {
                    this.serverRing(GOLD_DUST, this.position().add(0.0D, 0.15D, 0.0D), 1.0D + ticks * 0.1D, 20);
                }
                if (ticks >= RITE_TICKS) {
                    if (this.isFlying()) {
                        this.aerialPhaseTicks = AERIAL_PHASE_TICKS;
                        this.returnToStanding();
                    } else {
                        this.forcedTakeoff = true;
                        this.hasUsedVexTimeInFlight = false;
                        this.chainState(PatriarchState.WING_KNOCKBACK);
                    }
                }
            }
            case FALLING_SUN -> {
                if (ticks == 1) {
                    this.getNavigation().stop();
                    this.landingY = Double.NaN;
                    this.playSound(SoundEvents.PHANTOM_DEATH, 2.5F, 0.5F);
                }
                if (this.isFlying()) {
                    this.tickFallingSunDive(ticks);
                } else if (!this.onGround()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
                if (ticks == FALLING_SUN_IMPACT_TICK) {
                    if (this.isFlying()) {
                        this.finishLanding();
                    }
                    this.performFallingSunImpact();
                }
                if (ticks >= FALLING_SUN_TICKS) {
                    this.returnToStanding();
                }
            }
            case SOLAR_JUDGEMENT -> {
                this.tickSolarJudgement(ticks, target);
                if (ticks >= JUDGEMENT_TICKS) {
                    this.returnToStanding();
                }
            }
            case SACRIFICE -> {
                if (ticks == 5) {
                    this.getNavigation().stop();
                }
                if (ticks == SACRIFICE_TICK) {
                    this.performSacrifice();
                }
                if (ticks >= SACRIFICE_TICKS) {
                    this.returnToStanding();
                }
            }
            case FINAL_SUNBURST -> {
                this.tickFinalSunburst(ticks, target);
                if (ticks >= SUNBURST_TICKS) {
                    this.returnToStanding();
                }
            }
            case DIVINE_TOUCH, SKY_BURST -> {
                if (ticks == 20) {
                    double xOff = this.getXVector(7.0D, this.getYRot());
                    double zOff = this.getZVector(7.0D, this.getYRot());
                    this.damageInArea(new AABB(Vec3.atLowerCornerOf(this.getOnPos()), Vec3.atLowerCornerOf(BlockPos.containing(this.getX() + xOff, this.getY(), this.getZ() + zOff))).inflate(2.5D));
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

    private void tickDescent(int ticks) {
        this.getNavigation().stop();
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.resetFallDistance();
        if (!this.isFlying()) {
            this.chainState(PatriarchState.TOUCHDOWN);
            return;
        }
        if (ticks == 1 || ticks % 10 == 0 || Double.isNaN(this.landingY)) {
            this.landingY = this.findLandingY();
        }
        Vec3 motion = this.getDeltaMovement();
        if (Double.isNaN(this.landingY)) {
            this.setDeltaMovement(motion.x * 0.8D, motion.y * 0.5D, motion.z * 0.8D);
            if (ticks >= DESCENT_NO_GROUND_TICKS) {
                this.returnToStanding();
            }
            return;
        }
        double height = this.getY() - this.landingY;
        if (this.onGround() || height <= TOUCHDOWN_HEIGHT || ticks >= DESCENT_MAX_TICKS) {
            this.touchDown();
            return;
        }
        double speed = Mth.clamp(height * DESCENT_SPEED_PER_BLOCK, DESCENT_MIN_SPEED, DESCENT_MAX_SPEED);
        double ease = Math.min(1.0D, ticks / (double) DESCENT_EASE_TICKS);
        double vertical = Mth.lerp(0.35D, motion.y, -speed * ease);
        this.setDeltaMovement(motion.x * 0.8D, Math.max(vertical, -height), motion.z * 0.8D);
    }

    private void tickFallingSunDive(int ticks) {
        this.resetFallDistance();
        if (ticks == 1 || ticks % 5 == 0 || Double.isNaN(this.landingY)) {
            this.landingY = this.findLandingY();
        }
        Vec3 motion = this.getDeltaMovement();
        if (Double.isNaN(this.landingY)) {
            this.finishLanding();
            return;
        }
        double height = this.getY() - this.landingY;
        if (this.onGround() || height <= TOUCHDOWN_HEIGHT) {
            this.finishLanding();
            return;
        }
        if (ticks < FALLING_SUN_WINDUP_TICKS) {
            this.setDeltaMovement(motion.x * 0.5D, motion.y * 0.5D, motion.z * 0.5D);
            return;
        }
        int remaining = Math.max(1, FALLING_SUN_IMPACT_TICK - ticks);
        double dive = Math.min(height / remaining, FALLING_SUN_MAX_DIVE_SPEED);
        this.setDeltaMovement(motion.x * 0.6D, -Math.min(dive, height), motion.z * 0.6D);
    }

    private double findLandingY() {
        Level level = this.level();
        BlockPos.MutableBlockPos cursor = BlockPos.containing(this.getX(), this.getY(), this.getZ()).mutable();
        int bottom = Math.max(level.getMinBuildHeight(), cursor.getY() - LANDING_SCAN_DEPTH);
        for (int y = cursor.getY(); y >= bottom; y--) {
            cursor.setY(y);
            BlockState state = level.getBlockState(cursor);
            if (!state.getFluidState().isEmpty()) {
                return y + state.getFluidState().getHeight(level, cursor);
            }
            VoxelShape shape = state.getCollisionShape(level, cursor);
            if (!shape.isEmpty()) {
                return y + shape.max(Direction.Axis.Y);
            }
        }
        return Double.NaN;
    }

    private void finishLanding() {
        this.landingY = Double.NaN;
        this.setGroundMovement();
        this.setFlying(false);
        this.hasUsedVexTimeInFlight = false;
        this.aerialPhaseTicks = 0;
        this.lastLandedTick = this.tickCount;
        this.resetFallDistance();
        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * 0.3D, Math.min(0.0D, motion.y) * 0.3D, motion.z * 0.3D);
    }

    private void touchDown() {
        this.finishLanding();
        this.groundBurst(this.position(), 24, 0.8D);
        this.serverRing(ParticleTypes.CLOUD, this.position().add(0.0D, 0.1D, 0.0D), 1.6D, 20);
        this.playSound(SoundEvents.PHANTOM_FLAP, 1.5F, 0.6F);
        this.playSound(SoundEvents.RAVAGER_STEP, 1.2F, 0.7F);
        this.chainState(PatriarchState.TOUCHDOWN);
    }

    private void performFallingSunImpact() {
        Vec3 center = this.position();
        for (LivingEntity entity : this.enemiesInRadius(6.0D, 4.0D)) {
            Vec3 outward = new Vec3(entity.getX() - center.x, 0.0D, entity.getZ() - center.z);
            outward = outward.lengthSqr() < 1.0E-4D ? this.horizontalForward() : outward.normalize();
            this.impulse(entity, outward.scale(1.3D).add(0.0D, 0.45D, 0.0D));
        }
        this.groundBurst(center, 50, 1.2D);
        this.serverParticles(ParticleTypes.FLAME, center.x, center.y + 0.5D, center.z, 60, 1.5D, 0.6D, 1.5D, 0.08D);
        this.serverParticles(ParticleTypes.LARGE_SMOKE, center.x, center.y + 0.5D, center.z, 30, 1.5D, 0.6D, 1.5D, 0.04D);
        this.level().playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 2.5F, 0.6F);
        this.playSound(SoundEvents.ANVIL_LAND, 2.0F, 0.5F);
        this.broadcastImpact();
    }

    private void tickSolarJudgement(int ticks, @Nullable LivingEntity target) {
        if (ticks == 10) {
            this.getNavigation().stop();
        }
        if (target != null && ticks >= JUDGEMENT_FIRST_TICK && ticks <= JUDGEMENT_LAST_TICK && (ticks - JUDGEMENT_FIRST_TICK) % JUDGEMENT_INTERVAL == 0) {
            Vec3 goal = target.position();
            if (ticks == JUDGEMENT_FIRST_TICK) {
                Vec3 back = this.position().subtract(goal).multiply(1.0D, 0.0D, 1.0D);
                back = back.lengthSqr() < 1.0E-4D ? this.horizontalForward() : back.normalize();
                this.judgementCursor = goal.add(back.scale(JUDGEMENT_START_OFFSET));
                this.playSound(SoundEvents.BEACON_ACTIVATE, 2.0F, 1.4F);
            } else {
                Vec3 toGoal = new Vec3(goal.x - this.judgementCursor.x, 0.0D, goal.z - this.judgementCursor.z);
                double length = toGoal.length();
                this.judgementCursor = length <= JUDGEMENT_STEP ? goal : this.judgementCursor.add(toGoal.scale(JUDGEMENT_STEP / length));
            }
            Vec3 ground = this.findStandableGround(this.judgementCursor.x, this.judgementCursor.z, target.getY(), 12);
            if (ground != null) {
                this.judgement.schedule(ground, this.stateTicks, JUDGEMENT_DELAY);
            }
        }
        this.judgement.tick(this.stateTicks);
    }

    private void telegraphPillar(Vec3 pos, boolean urgent) {
        this.serverRing(urgent ? PALE_DUST : GOLD_DUST, pos.add(0.0D, 0.1D, 0.0D), PILLAR_RADIUS, 10);
        if (urgent) {
            this.serverParticles(ParticleTypes.END_ROD, pos.x, pos.y + 0.2D, pos.z, 6, 0.4D, 0.1D, 0.4D, 0.08D);
        } else {
            this.serverParticles(ModParticleTypes.EXPLOSION_CIRCLE, pos.x, pos.y + 0.05D, pos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private void strikePillar(Vec3 pos) {
        AABB column = new AABB(pos.x - PILLAR_RADIUS, pos.y - 0.5D, pos.z - PILLAR_RADIUS, pos.x + PILLAR_RADIUS, pos.y + PILLAR_HEIGHT, pos.z + PILLAR_RADIUS);
        for (LivingEntity entity : this.enemiesIn(column)) {
            if (!(entity instanceof AureateVexEntity) && this.judgement.firstHit(entity.getUUID(), this.stateTicks) && this.strikeMagic(entity, 0.7F * this.power())) {
                entity.setRemainingFireTicks(60);
            }
        }
        this.serverParticles(ParticleTypes.END_ROD, pos.x, pos.y + 2.5D, pos.z, 24, 0.2D, 2.0D, 0.2D, 0.02D);
        this.serverParticles(GOLD_DUST, pos.x, pos.y + 2.5D, pos.z, 20, 0.4D, 2.0D, 0.4D, 0.0D);
        this.serverParticles(ParticleTypes.FLASH, pos.x, pos.y + 1.0D, pos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        this.groundBurst(pos, 10, 0.4D);
        this.level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.HOSTILE, 0.8F, 1.8F);
    }

    private final class JudgementHandler implements TelegraphedStrikes.Handler {
        @Override
        public void telegraph(Vec3 pos, boolean urgent) {
            AuricPatriarch.this.telegraphPillar(pos, urgent);
        }

        @Override
        public void strike(Vec3 pos) {
            AuricPatriarch.this.strikePillar(pos);
        }
    }

    private void performSacrifice() {
        this.pruneMinions();
        Vec3 chest = this.position().add(0.0D, this.getBbHeight() * 0.6D, 0.0D);
        int consumed = 0;
        for (LivingEntity minion : new ArrayList<>(this.minions)) {
            if (minion.isAlive()) {
                this.serverLine(GOLD_DUST, minion.position().add(0.0D, minion.getBbHeight() * 0.5D, 0.0D), chest, 0.6D);
                this.serverParticles(ParticleTypes.TOTEM_OF_UNDYING, minion.getX(), minion.getY(0.5D), minion.getZ(), 20, 0.3D, 0.5D, 0.3D, 0.2D);
                minion.kill();
                consumed++;
            }
        }
        this.minions.clear();
        if (consumed > 0) {
            this.heal(Math.min(SACRIFICE_MAX_HEAL, consumed * SACRIFICE_HEAL_PER_MINION) * this.getMaxHealth());
            this.empoweredStrikes = EMPOWERED_ATTACKS;
            this.serverParticles(ParticleTypes.FLASH, chest.x, chest.y, chest.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        this.playSound(SoundEvents.EVOKER_CAST_SPELL, 2.0F, 0.6F);
        this.playSound(SoundEvents.TOTEM_USE, 1.5F, 0.7F);
    }

    private void tickFinalSunburst(int ticks, @Nullable LivingEntity target) {
        if (ticks == 1) {
            this.getNavigation().stop();
            this.playSound(SoundEvents.BEACON_ACTIVATE, 3.0F, 0.5F);
        }
        if (ticks == 5) {
            this.placeSanctuaries();
        }
        if (ticks >= 5 && ticks < SUNBURST_BLAST_TICK && ticks % 5 == 0) {
            for (Vec3 sanctuary : this.sanctuaries) {
                this.serverRing(PALE_DUST, sanctuary.add(0.0D, 0.15D, 0.0D), SANCTUARY_RADIUS, 16);
                this.serverParticles(ParticleTypes.END_ROD, sanctuary.x, sanctuary.y + 1.5D, sanctuary.z, 3, 0.3D, 1.2D, 0.3D, 0.01D);
            }
        }
        if (ticks > SUNBURST_CHANNEL_TICK && ticks < SUNBURST_BLAST_TICK && ticks % 20 == 0) {
            this.playSound(SoundEvents.BEACON_AMBIENT, 3.0F, 0.5F + ticks / (float) SUNBURST_BLAST_TICK);
        }
        if (target != null && ticks < SUNBURST_BLAST_TICK) {
            this.faceTowards(target, 4.0F);
        }
        if (ticks == SUNBURST_BLAST_TICK) {
            this.performSunburstBlast();
        }
    }

    private void placeSanctuaries() {
        this.sanctuaries.clear();
        double base = this.random.nextDouble() * Math.PI * 2.0D;
        for (int i = 0; i < SANCTUARY_COUNT; i++) {
            double angle = base + i * Math.PI + (this.random.nextDouble() - 0.5D) * 1.2D;
            double radius = 7.0D + this.random.nextDouble() * 3.0D;
            Vec3 ground = this.findStandableGround(this.getX() + Math.cos(angle) * radius, this.getZ() + Math.sin(angle) * radius, this.getY(), 12);
            if (ground != null) {
                this.sanctuaries.add(ground);
                this.serverParticles(ModParticleTypes.EXPLOSION_CIRCLE, ground.x, ground.y + 0.05D, ground.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        this.playSound(SoundEvents.EVOKER_PREPARE_WOLOLO, 2.0F, 1.2F);
    }

    private boolean isSheltered(LivingEntity entity) {
        for (Vec3 sanctuary : this.sanctuaries) {
            if (horizontalDistanceSqr(entity.position(), sanctuary) <= SANCTUARY_RADIUS * SANCTUARY_RADIUS && Math.abs(entity.getY() - sanctuary.y) < 3.0D) {
                return true;
            }
        }
        return false;
    }

    private void performSunburstBlast() {
        Vec3 center = this.position();
        for (LivingEntity entity : this.enemiesInRadius(SUNBURST_RADIUS, 8.0D)) {
            if (entity instanceof AureateVexEntity || this.isSheltered(entity)) {
                continue;
            }
            if (this.strikeMagic(entity, 2.2F * this.power())) {
                entity.setRemainingFireTicks(100);
            }
            Vec3 outward = new Vec3(entity.getX() - center.x, 0.0D, entity.getZ() - center.z);
            outward = outward.lengthSqr() < 1.0E-4D ? this.horizontalForward() : outward.normalize();
            this.impulse(entity, outward.scale(1.2D).add(0.0D, 0.6D, 0.0D));
        }
        this.serverParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 1.0D, center.z, 2, 1.0D, 0.5D, 1.0D, 0.0D);
        this.serverParticles(ParticleTypes.FLASH, center.x, center.y + 3.0D, center.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        this.level().playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 4.0F, 0.5F);
        this.playSound(SoundEvents.TOTEM_USE, 2.0F, 0.5F);
        this.sanctuaries.clear();
        this.broadcastImpact();
    }

    private void staggerSunburst() {
        this.sanctuaries.clear();
        this.playSound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 2.0F, 0.6F);
        this.playSound(SoundEvents.BEACON_DEACTIVATE, 2.5F, 0.6F);
        this.serverParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(0.7D), this.getZ(), 30, 0.8D, 0.8D, 0.8D, 0.05D);
        this.returnToStanding();
        this.stateTicks = -40;
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
                if (ticks % 4 == 0 && !this.level().getBlockState(this.blockPosition().below(3)).isAir()) {
                    BossParticles.ring(level, ParticleTypes.CLOUD, this.position().add(0.0D, 0.1D, 0.0D), 1.2D, 16, 0.25D, 0.0D);
                }
            }
            case TOUCHDOWN -> {
                if (ticks == 1) {
                    BossParticles.ring(level, ParticleTypes.CLOUD, this.position().add(0.0D, 0.1D, 0.0D), 1.0D, 28, 0.45D, 0.0D);
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
                    this.playClientSound(SoundEvents.TRIDENT_RIPTIDE_1.value(), 0.75F, 1.5F);
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
            case ASCENSION_RITE -> {
                BossParticles.converge(level, this.random, GOLD_DUST, chest, 3.5D, 3);
                if (ticks % 4 == 0) {
                    BossParticles.helix(level, ParticleTypes.END_ROD, this.position(), 2.0D, 4.0D, this.tickCount, 2, 20);
                }
            }
            case FALLING_SUN -> {
                if (ticks < FALLING_SUN_IMPACT_TICK) {
                    Vec3 left = this.localPoint(-0.4D, this.getBbHeight() * 0.7D, 1.6D);
                    Vec3 right = this.localPoint(-0.4D, this.getBbHeight() * 0.7D, -1.6D);
                    level.addParticle(ParticleTypes.FLAME, left.x, left.y, left.z, 0.0D, 0.05D, 0.0D);
                    level.addParticle(ParticleTypes.FLAME, right.x, right.y, right.z, 0.0D, 0.05D, 0.0D);
                    level.addParticle(ParticleTypes.LARGE_SMOKE, chest.x, chest.y, chest.z, 0.0D, 0.08D, 0.0D);
                } else if (ticks % 3 == 0) {
                    BossParticles.motes(level, this.random, ParticleTypes.SMOKE, this.position(), 1.0D, this.getBbHeight(), 2, 0.05D);
                }
            }
            case SOLAR_JUDGEMENT -> {
                Vec3 hand = this.localPoint(0.9D, this.getBbHeight() * 0.65D, -0.6D);
                if (ticks < JUDGEMENT_FIRST_TICK) {
                    BossParticles.converge(level, this.random, ParticleTypes.ENCHANT, hand, 2.5D, 3);
                } else if (ticks <= JUDGEMENT_LAST_TICK && ticks % 4 == 0) {
                    BossParticles.burst(level, this.random, ParticleTypes.END_ROD, hand, 4, 0.15D);
                }
            }
            case SACRIFICE -> {
                if (ticks < SACRIFICE_TICK) {
                    BossParticles.converge(level, this.random, GOLD_DUST, chest, 4.0D, 3);
                }
                if (ticks == SACRIFICE_TICK) {
                    BossParticles.burst(level, this.random, ParticleTypes.TOTEM_OF_UNDYING, chest, 50, 0.6D);
                }
            }
            case FINAL_SUNBURST -> {
                Vec3 sun = this.position().add(0.0D, this.getBbHeight() + 1.5D, 0.0D);
                if (ticks >= SUNBURST_CHANNEL_TICK && ticks < SUNBURST_BLAST_TICK) {
                    float progress = (ticks - SUNBURST_CHANNEL_TICK) / (float) (SUNBURST_BLAST_TICK - SUNBURST_CHANNEL_TICK);
                    BossParticles.converge(level, this.random, ParticleTypes.END_ROD, sun, 4.0D + progress * 4.0D, 2 + (int) (progress * 4.0F));
                    BossParticles.converge(level, this.random, GOLD_DUST, sun, 3.0D, 2);
                    if (ticks % 10 == 0) {
                        BossParticles.ring(level, GOLD_DUST, this.position().add(0.0D, 0.2D, 0.0D), SUNBURST_RADIUS, 72, 0.0D, 0.0D);
                    }
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
            if (next != PatriarchState.SACRIFICE) {
                this.empoweredAttack = this.empoweredStrikes > 0;
                if (this.empoweredAttack) {
                    this.empoweredStrikes--;
                }
            }
        }
        if (previous == PatriarchState.SOLAR_JUDGEMENT) {
            this.judgement.clear();
        }
        if (previous == PatriarchState.FINAL_SUNBURST) {
            this.sanctuaries.clear();
        }
        if (next == PatriarchState.FINAL_SUNBURST) {
            this.sunburstDamage = 0.0F;
        }
    }

    @Override
    protected void onImpactClient(PatriarchState state) {
        Level level = this.level();
        Vec3 base = this.position().add(0.0D, 0.15D, 0.0D);
        switch (state) {
            case FALLING_SUN -> {
                BossParticles.ring(level, ParticleTypes.CLOUD, base, 1.5D, 48, 1.0D, 0.06D);
                BossParticles.ring(level, ParticleTypes.FLAME, base, 2.0D, 40, 0.6D, 0.08D);
            }
            case FINAL_SUNBURST -> {
                BossParticles.ring(level, ParticleTypes.END_ROD, base.add(0.0D, 1.0D, 0.0D), 2.0D, 80, 1.6D, 0.1D);
                BossParticles.ring(level, GOLD_DUST, base, SUNBURST_RADIUS, 96, 0.0D, 0.2D);
                BossParticles.burst(level, this.random, ParticleTypes.TOTEM_OF_UNDYING, base.add(0.0D, this.getBbHeight(), 0.0D), 120, 1.2D);
            }
            default -> {
            }
        }
    }

    @Override
    protected void planTactics(LivingEntity target, Queue<PatriarchState> plan) {
        double distSqr = this.distanceToSqr(target);
        boolean targetOnGround = target.onGround();
        if (this.getBossPhase() >= 2) {
            this.planFinalTactics(distSqr, plan);
            return;
        }

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
        if (this.getBossPhase() >= 1) {
            this.offer(weights, PatriarchState.SOLAR_JUDGEMENT, 3, 160);
        }
        this.addWeightedPlan(weights, plan, PatriarchState.AUREATE_UNDEAD, PatriarchState.SOLAR_JUDGEMENT);
    }

    private void planFinalTactics(double distSqr, Queue<PatriarchState> plan) {
        this.pruneMinions();
        Map<PatriarchState, Integer> weights = new EnumMap<>(PatriarchState.class);
        if (distSqr <= 81.0D) {
            this.offer(weights, PatriarchState.DIVINE_TOUCH, 4, 60);
            this.offer(weights, PatriarchState.AURIC_BARRIER, 3, 160);
            if (distSqr <= 25.0D) {
                this.offer(weights, PatriarchState.WING_KNOCKBACK, 2, 240);
            }
        } else {
            this.offer(weights, PatriarchState.SUDDEN_BEAM, 3, 80);
        }
        this.offer(weights, PatriarchState.GRAB_AND_STRIKE, 2, 200);
        this.offer(weights, PatriarchState.SOLAR_JUDGEMENT, 4, 140);
        if (this.countMinions(ReaverEntity.class) <= REAVER_RESUMMON_THRESHOLD) {
            this.offer(weights, PatriarchState.AUREATE_UNDEAD, 3, 360);
        }
        if (this.minions.size() >= 2) {
            this.offer(weights, PatriarchState.SACRIFICE, 5, 300);
        }
        this.offer(weights, PatriarchState.FINAL_SUNBURST, 8, SUNBURST_COOLDOWN);
        this.addWeightedPlan(weights, plan, PatriarchState.AUREATE_UNDEAD, PatriarchState.SACRIFICE, PatriarchState.FINAL_SUNBURST, PatriarchState.SOLAR_JUDGEMENT);
    }

    private void addWeightedPlan(Map<PatriarchState, Integer> weights, Queue<PatriarchState> plan, PatriarchState... soloStates) {
        weights.remove(this.lastTactic);
        PatriarchState first = this.pickWeighted(weights);
        if (first == null) {
            return;
        }
        plan.add(first);
        weights.remove(first);
        for (PatriarchState solo : soloStates) {
            if (first == solo) {
                return;
            }
            weights.remove(solo);
        }
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
        if (this.getBossPhase() >= 1) {
            this.offer(weights, PatriarchState.SOLAR_JUDGEMENT, 4, 140);
        }
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
                area.setPotionContents(new PotionContents(Potions.WEAKNESS));
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
                    vex.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(this.getOnPos()), MobSpawnType.MOB_SUMMONED, null);
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
        float damageAmount = this.attackDamage(0.65F * this.power());
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
            case TO_FLYING, TO_GROUND, TOUCHDOWN -> 0.5F;
            case SUDDEN_BEAM, DIVINE_TOUCH, ASCENSION_RITE, FALLING_SUN, FINAL_SUNBURST -> 1.0F;
            case WING_KNOCKBACK, VEX_TIME, SOLAR_JUDGEMENT, SACRIFICE -> 0.8F;
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
        this.judgement.clear();
        this.sanctuaries.clear();
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
                .add(Attributes.MAX_HEALTH, 600.0D)
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
        IDLE_TO_AWAKE(14),
        ASCENSION_RITE(15),
        SOLAR_JUDGEMENT(16),
        FALLING_SUN(17),
        SACRIFICE(18),
        FINAL_SUNBURST(19),
        TOUCHDOWN(20);

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
                    || this == AUREATE_UNDEAD || this == SOLAR_JUDGEMENT || this == SACRIFICE || this == FINAL_SUNBURST;
        }

        @Override
        public boolean isPhaseTransition() {
            return this == ASCENSION_RITE || this == FALLING_SUN;
        }
    }
}