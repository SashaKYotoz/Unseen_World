package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.entities.util.BossParticles;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class EclipsedAcolyteEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DORMANT = SynchedEntityData.defineId(EclipsedAcolyteEntity.class, EntityDataSerializers.BOOLEAN);

    public static final byte SLAM_EVENT = 93;
    public static final byte SWEEP_EVENT = 94;
    public static final byte AWAKEN_EVENT = 95;
    private static final DustParticleOptions HORN_DUST = BossParticles.dust(0x9B6BD8, 0.7F);

    private static final int AWAKEN_TICKS = 30;
    private static final int SLAM_TICKS = 20;
    private static final int SLAM_HIT_TICK = 10;
    private static final int SWEEP_TICKS = 15;
    private static final int SWEEP_HIT_TICK = 8;
    private static final int ATTACK_COOLDOWN = 25;
    private static final double WAKE_RANGE = 8.0D;
    private static final double ALERT_RANGE = 10.0D;
    private static final int POST_RADIUS = 20;
    private static final double GIVE_UP_DISTANCE_SQR = 28.0D * 28.0D;
    private static final double SLAM_FORWARD = 1.8D;
    private static final double SLAM_RADIUS = 1.6D;
    private static final double SWEEP_REACH = 3.0D;
    private static final float SWEEP_DAMAGE_FACTOR = 0.7F;
    private static final int CALM_TICKS_TO_DORMANT = 60;

    public final AnimationState idle = new AnimationState();
    public final AnimationState dormant = new AnimationState();
    public final AnimationState awaken = new AnimationState();
    public final AnimationState slam = new AnimationState();
    public final AnimationState sweep = new AnimationState();

    private int awakenTicks;
    private int attackTicks;
    private byte attackKind;
    private int attackCooldown;
    @Nullable
    private BlockPos home;
    private float homeYaw;

    public EclipsedAcolyteEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.setMaxUpStep(1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DORMANT, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AcolyteAttackGoal(this));
        this.goalSelector.addGoal(2, new ReturnToPostGoal(this));
        this.goalSelector.addGoal(6, new AwakeGoal(this, new LookAtPlayerGoal(this, Player.class, 8.0F)));
        this.goalSelector.addGoal(7, new AwakeGoal(this, new RandomLookAroundGoal(this)));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EclipsedAcolyteEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                player -> !this.isDormant() || this.distanceToSqr(player) <= WAKE_RANGE * WAKE_RANGE));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
        this.setHome(this.blockPosition(), this.getYRot());
        if (spawnType == MobSpawnType.STRUCTURE) {
            this.setPersistenceRequired();
        }
        if (spawnType != MobSpawnType.SPAWNER && spawnType != MobSpawnType.REINFORCEMENT) {
            this.setDormant(true);
        }
        return data;
    }

    public boolean isDormant() {
        return this.entityData.get(DORMANT);
    }

    private void setDormant(boolean dormant) {
        this.entityData.set(DORMANT, dormant);
    }

    public boolean isAwakening() {
        return this.awakenTicks > 0;
    }

    public boolean isAttacking() {
        return this.attackTicks > 0;
    }

    private void setHome(BlockPos pos, float yaw) {
        this.home = pos.immutable();
        this.homeYaw = yaw;
        this.restrictTo(this.home, POST_RADIUS);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null && !this.level().isClientSide() && this.isDormant()) {
            this.wake(target);
        }
    }

    private void wake(LivingEntity target) {
        this.setDormant(false);
        this.awakenTicks = AWAKEN_TICKS;
        this.attackCooldown = Math.max(this.attackCooldown, 10);
        this.getNavigation().stop();
        this.level().broadcastEntityEvent(this, AWAKEN_EVENT);
        this.playSound(SoundEvents.DEEPSLATE_BREAK, 1.2F, 0.6F);
        this.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.0F, 0.7F);
        List<EclipsedAcolyteEntity> hall = this.level().getEntitiesOfClass(EclipsedAcolyteEntity.class, this.getBoundingBox().inflate(ALERT_RANGE),
                other -> other != this && other.isAlive() && other.isDormant() && other.getTarget() == null);
        for (EclipsedAcolyteEntity other : hall) {
            other.setTarget(target);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && this.isDormant() && source.getEntity() instanceof LivingEntity attacker && this.isFoe(attacker)) {
            this.setTarget(attacker);
        }
        return result;
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (!this.isDormant() && !this.isAwakening()) {
            super.knockback(strength, x, z);
        }
    }

    @Override
    public boolean isPushable() {
        return !this.isDormant() && super.isPushable();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            this.tickClientParticles();
            return;
        }
        if (this.home == null) {
            this.setHome(this.blockPosition(), this.getYRot());
        }
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }
        if (this.isDormant()) {
            this.getNavigation().stop();
            this.setYRot(this.homeYaw);
            this.yBodyRot = this.homeYaw;
            this.yHeadRot = this.homeYaw;
            this.setXRot(0.0F);
            return;
        }
        LivingEntity target = this.getTarget();
        if (this.awakenTicks > 0) {
            this.awakenTicks--;
            this.getNavigation().stop();
            if (target != null) {
                this.getLookControl().setLookAt(target, 20.0F, 20.0F);
            }
        }
        if (this.attackTicks > 0) {
            this.tickAttack(target);
        }
        if (target != null && this.home != null && this.home.distSqr(this.blockPosition()) > GIVE_UP_DISTANCE_SQR) {
            this.setTarget(null);
        }
    }

    private boolean canStartAttack() {
        return this.attackTicks == 0 && this.attackCooldown == 0 && this.awakenTicks == 0 && !this.isDormant();
    }

    private void startAttack(LivingEntity target) {
        Vec3 facing = Vec3.directionFromRotation(0.0F, this.yBodyRot);
        Vec3 toTarget = target.position().subtract(this.position()).multiply(1.0D, 0.0D, 1.0D);
        boolean flanked = toTarget.lengthSqr() > 1.0E-4D && facing.dot(toTarget.normalize()) < 0.64D;
        this.attackKind = flanked || this.random.nextFloat() < 0.3F ? SWEEP_EVENT : SLAM_EVENT;
        this.attackTicks = 1;
        this.getNavigation().stop();
        this.level().broadcastEntityEvent(this, this.attackKind);
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, this.attackKind == SLAM_EVENT ? 0.55F : 0.75F);
    }

    private void tickAttack(@Nullable LivingEntity target) {
        this.getNavigation().stop();
        boolean slamming = this.attackKind == SLAM_EVENT;
        int hitTick = slamming ? SLAM_HIT_TICK : SWEEP_HIT_TICK;
        int duration = slamming ? SLAM_TICKS : SWEEP_TICKS;
        if (target != null && this.attackTicks < hitTick) {
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.yBodyRot = Mth.approachDegrees(this.yBodyRot, this.yHeadRot, 15.0F);
            this.setYRot(this.yBodyRot);
        }
        if (this.attackTicks == hitTick) {
            if (slamming) {
                this.performSlam();
            } else {
                this.performSweep();
            }
        }
        if (++this.attackTicks > duration) {
            this.attackTicks = 0;
            this.attackKind = 0;
            this.attackCooldown = ATTACK_COOLDOWN;
        }
    }

    private float attackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    private boolean isFoe(LivingEntity entity) {
        if (entity == this || !entity.isAlive() || entity instanceof EclipsedAcolyteEntity || entity.isAlliedTo(this)) {
            return false;
        }
        return !(entity instanceof Player player) || (!player.isCreative() && !player.isSpectator());
    }

    private void performSlam() {
        Vec3 facing = Vec3.directionFromRotation(0.0F, this.yBodyRot);
        Vec3 impact = this.position().add(facing.scale(SLAM_FORWARD));
        List<LivingEntity> hit = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(SLAM_FORWARD + SLAM_RADIUS, 1.0D, SLAM_FORWARD + SLAM_RADIUS),
                entity -> this.isFoe(entity) && horizontalDistanceSqr(entity.position(), impact) <= SLAM_RADIUS * SLAM_RADIUS && Math.abs(entity.getY() - this.getY()) < 2.5D);
        for (LivingEntity entity : hit) {
            boolean wasBlocking = entity instanceof Player player && player.isBlocking();
            if (entity.hurt(this.damageSources().mobAttack(this), this.attackDamage())) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, 0.3D, 0.0D));
                if (entity instanceof IGrippingEntity gripping) {
                    GrippingData.addGrippingSeconds(gripping, 2);
                }
                this.doEnchantDamageEffects(this, entity);
            }
            if (wasBlocking) {
                ((Player) entity).disableShield(true);
            }
        }
        this.playSound(SoundEvents.DEEPSLATE_BREAK, 1.4F, 0.5F);
        this.playSound(SoundEvents.ANVIL_LAND, 0.4F, 0.6F);
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockPos groundPos = BlockPos.containing(impact.x, this.getY() - 0.2D, impact.z);
            BlockState ground = serverLevel.getBlockState(groundPos);
            if (!ground.isAir()) {
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ground), impact.x, this.getY() + 0.1D, impact.z, 30, 0.6D, 0.05D, 0.6D, 0.15D);
            }
            serverLevel.sendParticles(HORN_DUST, impact.x, this.getY() + 0.2D, impact.z, 16, 0.8D, 0.1D, 0.8D, 0.0D);
        }
    }

    private void performSweep() {
        Vec3 facing = Vec3.directionFromRotation(0.0F, this.yBodyRot);
        List<LivingEntity> hit = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(SWEEP_REACH, 0.5D, SWEEP_REACH),
                entity -> {
                    if (!this.isFoe(entity)) {
                        return false;
                    }
                    Vec3 offset = entity.position().subtract(this.position()).multiply(1.0D, 0.0D, 1.0D);
                    double reach = SWEEP_REACH + entity.getBbWidth() * 0.5D;
                    return offset.lengthSqr() <= reach * reach && (offset.lengthSqr() < 0.25D || facing.dot(offset.normalize()) >= 0.0D);
                });
        float yawRad = this.yBodyRot * Mth.DEG_TO_RAD;
        for (LivingEntity entity : hit) {
            if (entity.hurt(this.damageSources().mobAttack(this), this.attackDamage() * SWEEP_DAMAGE_FACTOR)) {
                entity.knockback(0.8D, Mth.sin(yawRad), -Mth.cos(yawRad));
                this.doEnchantDamageEffects(this, entity);
            }
        }
        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.6F);
        if (this.level() instanceof ServerLevel serverLevel) {
            Vec3 front = this.position().add(facing.scale(1.5D)).add(0.0D, 1.2D, 0.0D);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, front.x, front.y, front.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private static double horizontalDistanceSqr(Vec3 a, Vec3 b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return dx * dx + dz * dz;
    }

    @Override
    public void handleEntityEvent(byte id) {
        switch (id) {
            case SLAM_EVENT -> {
                this.sweep.stop();
                this.slam.start(this.tickCount);
            }
            case SWEEP_EVENT -> {
                this.slam.stop();
                this.sweep.start(this.tickCount);
            }
            case AWAKEN_EVENT -> {
                this.awaken.start(this.tickCount);
                this.awakenTicks = AWAKEN_TICKS;
                BossParticles.burst(this.level(), this.random, HORN_DUST, this.position().add(0.0D, this.getBbHeight() * 0.9D, 0.0D), 20, 0.15D);
            }
            default -> super.handleEntityEvent(id);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.awakenTicks > 0) {
                this.awakenTicks--;
            }
            boolean busy = this.slam.isStarted() || this.sweep.isStarted() || this.awakenTicks > 0;
            this.dormant.animateWhen(this.isDormant(), this.tickCount);
            this.idle.animateWhen(!this.isDormant() && !busy && !this.walkAnimation.isMoving(), this.tickCount);
        }
    }

    private void tickClientParticles() {
        if (this.isDormant()) {
            if (this.random.nextInt(12) == 0) {
                BossParticles.motes(this.level(), this.random, HORN_DUST, this.position().add(0.0D, this.getBbHeight() * 0.85D, 0.0D), 0.6D, 0.3D, 1, 0.01D);
            }
        } else if (this.random.nextInt(6) == 0) {
            BossParticles.motes(this.level(), this.random, HORN_DUST, this.position().add(0.0D, this.getBbHeight() * 0.85D, 0.0D), 0.7D, 0.4D, 1, 0.02D);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Dormant", this.isDormant());
        if (this.home != null) {
            compound.put("HomePos", NbtUtils.writeBlockPos(this.home));
            compound.putFloat("HomeYaw", this.homeYaw);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setDormant(compound.getBoolean("Dormant"));
        if (compound.contains("HomePos")) {
            this.setHome(NbtUtils.readBlockPos(compound.getCompound("HomePos")), compound.getFloat("HomeYaw"));
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isDormant() ? null : SoundEvents.DEEPSLATE_TILES_STEP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DEEPSLATE_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DEEPSLATE_BREAK;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.DEEPSLATE_STEP, 0.6F, 0.7F);
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.7F;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    static class AwakeGoal extends Goal {
        private final EclipsedAcolyteEntity acolyte;
        private final Goal wrapped;

        AwakeGoal(EclipsedAcolyteEntity acolyte, Goal wrapped) {
            this.acolyte = acolyte;
            this.wrapped = wrapped;
            this.setFlags(wrapped.getFlags());
        }

        private boolean ready() {
            return !this.acolyte.isDormant() && !this.acolyte.isAwakening() && !this.acolyte.isAttacking();
        }

        @Override
        public boolean canUse() {
            return this.ready() && this.wrapped.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.ready() && this.wrapped.canContinueToUse();
        }

        @Override
        public void start() {
            this.wrapped.start();
        }

        @Override
        public void stop() {
            this.wrapped.stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return this.wrapped.requiresUpdateEveryTick();
        }

        @Override
        public void tick() {
            this.wrapped.tick();
        }
    }

    static class AcolyteAttackGoal extends Goal {
        private final EclipsedAcolyteEntity acolyte;
        private int repathTicks;

        AcolyteAttackGoal(EclipsedAcolyteEntity acolyte) {
            this.acolyte = acolyte;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.acolyte.getTarget();
            return target != null && target.isAlive() && !this.acolyte.isDormant() && !this.acolyte.isAwakening();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.repathTicks = 0;
            this.acolyte.setAggressive(true);
        }

        @Override
        public void stop() {
            this.acolyte.setAggressive(false);
            this.acolyte.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.acolyte.getTarget();
            if (target == null || this.acolyte.isAttacking()) {
                return;
            }
            this.acolyte.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (--this.repathTicks <= 0) {
                this.repathTicks = 4 + this.acolyte.getRandom().nextInt(7);
                this.acolyte.getNavigation().moveTo(target, 1.0D);
            }
            double reach = SWEEP_REACH - 0.4D + target.getBbWidth() * 0.5D;
            if (this.acolyte.canStartAttack() && this.acolyte.distanceToSqr(target) <= reach * reach && this.acolyte.getSensing().hasLineOfSight(target)) {
                this.acolyte.startAttack(target);
            }
        }
    }

    static class ReturnToPostGoal extends Goal {
        private static final int GIVE_UP_TICKS = 400;
        private final EclipsedAcolyteEntity acolyte;
        private int calmTicks;
        private int travelTicks;

        ReturnToPostGoal(EclipsedAcolyteEntity acolyte) {
            this.acolyte = acolyte;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.acolyte.home != null && this.acolyte.getTarget() == null && !this.acolyte.isDormant()
                    && !this.acolyte.isAwakening() && !this.acolyte.isAttacking();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.calmTicks = 0;
            this.travelTicks = 0;
        }

        @Override
        public void stop() {
            this.acolyte.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            BlockPos home = this.acolyte.home;
            if (home == null) {
                return;
            }
            double distSqr = horizontalDistanceSqr(this.acolyte.position(), Vec3.atBottomCenterOf(home));
            if (distSqr > 2.25D) {
                this.calmTicks = 0;
                if (++this.travelTicks > GIVE_UP_TICKS) {
                    this.acolyte.setHome(this.acolyte.blockPosition(), this.acolyte.getYRot());
                    return;
                }
                if (this.travelTicks % 20 == 1 || this.acolyte.getNavigation().isDone()) {
                    this.acolyte.getNavigation().moveTo(home.getX() + 0.5D, home.getY(), home.getZ() + 0.5D, 0.8D);
                }
                return;
            }
            this.acolyte.getNavigation().stop();
            float yaw = Mth.approachDegrees(this.acolyte.getYRot(), this.acolyte.homeYaw, 10.0F);
            this.acolyte.setYRot(yaw);
            this.acolyte.yBodyRot = yaw;
            this.acolyte.yHeadRot = yaw;
            if (++this.calmTicks >= CALM_TICKS_TO_DORMANT) {
                this.acolyte.setDormant(true);
            }
        }
    }
}
