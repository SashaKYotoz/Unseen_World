package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class DreadflapEntity extends AmbientCreature {
    public static final int field_28637 = Mth.ceil(2.4166098F);
    private static final EntityDataAccessor<Byte> BAT_FLAGS = SynchedEntityData.defineId(DreadflapEntity.class, EntityDataSerializers.BYTE);
    private static final TargetingConditions CLOSE_PLAYER_PREDICATE = TargetingConditions.forNonCombat().range(4.0);
    public final AnimationState flying = new AnimationState();
    public final AnimationState roosting = new AnimationState();
    @Nullable
    private BlockPos hangingPosition;

    public DreadflapEntity(EntityType<? extends DreadflapEntity> entityType, Level world) {
        super(entityType, world);
        if (!world.isClientSide)
            this.setRoosting(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10);
    }

    @Override
    public boolean isFlapping() {
        return !this.isRoosting() && this.tickCount % field_28637 == 0;
    }

    @Override
    public void onFlap() {
        float f = Mth.cos((this.getId() * 2 + this.tickCount) * 7.448451F * (float) (Math.PI / 180.0) + (float) Math.PI);
        float h = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0)) * (1.3F + 0.21F * 2);
        float j = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)) * (1.3F + 0.21F * 2);
        float k = (0.3F + f * 0.45F) * ((float) 2 * 0.2F + 1.0F);
        this.level().addParticle(ParticleTypes.SQUID_INK, this.getX() + h, this.getY() + k, this.getZ() + j, 0.0, 0.0, 0.0);
        this.level().addParticle(ParticleTypes.SQUID_INK, this.getX() - h, this.getY() + k, this.getZ() - j, 0.0, 0.0, 0.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BAT_FLAGS, (byte) 0);
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.95F;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return this.isRoosting() && this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    public boolean isRoosting() {
        return (this.entityData.get(BAT_FLAGS) & 1) != 0;
    }

    public void setRoosting(boolean roosting) {
        byte b = this.entityData.get(BAT_FLAGS);
        if (roosting) {
            this.entityData.set(BAT_FLAGS, (byte) (b | 1));
        } else {
            this.entityData.set(BAT_FLAGS, (byte) (b & -2));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isRoosting()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPosRaw(this.getX(), (double) Mth.floor(this.getY()) + 1.0 - (double) this.getBbHeight(), this.getZ());
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        }
        this.updateAnimations();
    }

    private void updateAnimations() {
        this.flying.animateWhen(!this.isRoosting(), this.tickCount);
        this.roosting.animateWhen(this.isRoosting(), this.tickCount);
    }

    @Override
    public void customServerAiStep() {
        Level world = this.level();
        BlockPos blockPos = this.blockPosition();
        BlockPos blockPosAbove = blockPos.above();

        if (this.isRoosting()) {
            boolean isCeilingSolid = world.getBlockState(blockPosAbove).isRedstoneConductor(world, blockPosAbove);
            boolean shouldSpook = false;

            if (isCeilingSolid) {
                if (this.random.nextInt(200) == 0) {
                    this.yHeadRot = (float) this.random.nextInt(360);
                }
                if (world.getNearestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                    shouldSpook = true;
                }
            }
            if (!isCeilingSolid || shouldSpook) {
                this.setRoosting(false);
                if (!this.isSilent()) {
                    world.levelEvent(null, LevelEvent.SOUND_BAT_LIFTOFF, blockPos, 0);
                }
            }
        } else {
            if (this.hangingPosition != null && (!world.isEmptyBlock(this.hangingPosition) || this.hangingPosition.getY() <= world.getMinBuildHeight())) {
                this.hangingPosition = null;
            }
            if (this.hangingPosition == null || this.random.nextInt(31) == 10 || this.hangingPosition.closerToCenterThan(this.position(), 2.0)) {
                this.hangingPosition = BlockPos.containing(this.getX() + this.random.nextInt(7) - this.random.nextInt(7), this.getY() + this.random.nextInt(6) - 2.0, this.getZ() + this.random.nextInt(7) - this.random.nextInt(7));
            }
            Vec3 newVelocity = getVec3d();
            this.setDeltaMovement(newVelocity);
            float targetYaw = (float) (Mth.atan2(newVelocity.z, newVelocity.x) * 180.0 / Math.PI) - 90.0F;
            float yawOffset = Mth.wrapDegrees(targetYaw - this.getYRot());

            this.zza = 0.35F;
            this.setYRot(this.getYRot() + yawOffset);
            if (this.random.nextInt(100) == 0 && world.getBlockState(blockPosAbove).isRedstoneConductor(world, blockPosAbove)) {
                this.setRoosting(true);
            }
        }
    }

    private Vec3 getVec3d() {
        double deltaX = this.hangingPosition.getX() + 0.5 - this.getX();
        double deltaY = this.hangingPosition.getY() + 0.1 - this.getY();
        double deltaZ = this.hangingPosition.getZ() + 0.5 - this.getZ();

        Vec3 currentVelocity = this.getDeltaMovement();
        return currentVelocity.add((Math.signum(deltaX) * 0.5 - currentVelocity.x) * 0.1, (Math.signum(deltaY) * 0.7 - currentVelocity.y) * 0.1, (Math.signum(deltaZ) * 0.5 - currentVelocity.z) * 0.1);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.level().isClientSide && this.isRoosting()) this.setRoosting(false);
            return super.hurt(source, amount);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(BAT_FLAGS, nbt.getByte("BatFlags"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putByte("BatFlags", this.entityData.get(BAT_FLAGS));
    }

    public static boolean canSpawn(EntityType<Bat> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        if (pos.getY() >= world.getSeaLevel()) {
            return false;
        } else {
            int i = world.getMaxLocalRawBrightness(pos);
            int j = 3;
            if (isTodayAroundHalloween()) {
                j = 6;
            } else if (random.nextBoolean()) return false;
            return i <= random.nextInt(j) && checkMobSpawnRules(type, world, spawnReason, pos, random);
        }
    }

    private static boolean isTodayAroundHalloween() {
        LocalDate localDate = LocalDate.now();
        int i = localDate.getDayOfMonth();
        int j = localDate.get(ChronoField.MONTH_OF_YEAR);
        return j == 10 && i >= 20 || j == 11 && i <= 3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.8f;
    }
}