package net.sashakyotoz.common.entities.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class DreadflapEntity extends AmbientEntity {
    public static final int field_28637 = MathHelper.ceil(2.4166098F);
    private static final TrackedData<Byte> BAT_FLAGS = DataTracker.registerData(DreadflapEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(4.0);
    public final AnimationState flying = new AnimationState();
    public final AnimationState roosting = new AnimationState();
    @Nullable
    private BlockPos hangingPosition;

    public DreadflapEntity(EntityType<? extends DreadflapEntity> entityType, World world) {
        super(entityType, world);
        if (!world.isClient)
            this.setRoosting(true);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
    }

    @Override
    public boolean isFlappingWings() {
        return !this.isRoosting() && this.age % field_28637 == 0;
    }

    @Override
    public void addFlapEffects() {
        float f = MathHelper.cos((this.getId() * 2 + this.age) * 7.448451F * (float) (Math.PI / 180.0) + (float) Math.PI);
        float h = MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0)) * (1.3F + 0.21F * 2);
        float j = MathHelper.sin(this.getYaw() * (float) (Math.PI / 180.0)) * (1.3F + 0.21F * 2);
        float k = (0.3F + f * 0.45F) * ((float) 2 * 0.2F + 1.0F);
        this.getWorld().addParticle(ParticleTypes.SQUID_INK, this.getX() + h, this.getY() + k, this.getZ() + j, 0.0, 0.0, 0.0);
        this.getWorld().addParticle(ParticleTypes.SQUID_INK, this.getX() - h, this.getY() + k, this.getZ() - j, 0.0, 0.0, 0.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BAT_FLAGS, (byte) 0);
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() * 0.95F;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return this.isRoosting() && this.random.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    protected void tickCramming() {
    }

    public boolean isRoosting() {
        return (this.dataTracker.get(BAT_FLAGS) & 1) != 0;
    }

    public void setRoosting(boolean roosting) {
        byte b = this.dataTracker.get(BAT_FLAGS);
        if (roosting) {
            this.dataTracker.set(BAT_FLAGS, (byte) (b | 1));
        } else {
            this.dataTracker.set(BAT_FLAGS, (byte) (b & -2));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isRoosting()) {
            this.setVelocity(Vec3d.ZERO);
            this.setPos(this.getX(), (double) MathHelper.floor(this.getY()) + 1.0 - (double) this.getHeight(), this.getZ());
        } else {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
        }
        this.updateAnimations();
    }

    private void updateAnimations() {
        this.flying.setRunning(!this.isRoosting(), this.age);
        this.roosting.setRunning(this.isRoosting(), this.age);
    }

    @Override
    public void mobTick() {
        World world = this.getWorld();
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPosAbove = blockPos.up();

        if (this.isRoosting()) {
            boolean isCeilingSolid = world.getBlockState(blockPosAbove).isSolidBlock(world, blockPosAbove);
            boolean shouldSpook = false;

            if (isCeilingSolid) {
                if (this.random.nextInt(200) == 0) {
                    this.headYaw = (float) this.random.nextInt(360);
                }
                if (world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                    shouldSpook = true;
                }
            }
            if (!isCeilingSolid || shouldSpook) {
                this.setRoosting(false);
                if (!this.isSilent()) {
                    world.syncWorldEvent(null, WorldEvents.BAT_TAKES_OFF, blockPos, 0);
                }
            }
        } else {
            if (this.hangingPosition != null && (!world.isAir(this.hangingPosition) || this.hangingPosition.getY() <= world.getBottomY())) {
                this.hangingPosition = null;
            }
            if (this.hangingPosition == null || this.random.nextInt(31) == 10 || this.hangingPosition.isWithinDistance(this.getPos(), 2.0)) {
                this.hangingPosition = BlockPos.ofFloored(this.getX() + this.random.nextInt(7) - this.random.nextInt(7), this.getY() + this.random.nextInt(6) - 2.0, this.getZ() + this.random.nextInt(7) - this.random.nextInt(7));
            }
            Vec3d newVelocity = getVec3d();
            this.setVelocity(newVelocity);
            float targetYaw = (float) (MathHelper.atan2(newVelocity.z, newVelocity.x) * 180.0 / Math.PI) - 90.0F;
            float yawOffset = MathHelper.wrapDegrees(targetYaw - this.getYaw());

            this.forwardSpeed = 0.35F;
            this.setYaw(this.getYaw() + yawOffset);
            if (this.random.nextInt(100) == 0 && world.getBlockState(blockPosAbove).isSolidBlock(world, blockPosAbove)) {
                this.setRoosting(true);
            }
        }
    }

    private Vec3d getVec3d() {
        double deltaX = this.hangingPosition.getX() + 0.5 - this.getX();
        double deltaY = this.hangingPosition.getY() + 0.1 - this.getY();
        double deltaZ = this.hangingPosition.getZ() + 0.5 - this.getZ();

        Vec3d currentVelocity = this.getVelocity();
        return currentVelocity.add((Math.signum(deltaX) * 0.5 - currentVelocity.x) * 0.1, (Math.signum(deltaY) * 0.7 - currentVelocity.y) * 0.1, (Math.signum(deltaZ) * 0.5 - currentVelocity.z) * 0.1);
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.EVENTS;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public boolean canAvoidTraps() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.getWorld().isClient && this.isRoosting()) this.setRoosting(false);
            return super.damage(source, amount);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(BAT_FLAGS, nbt.getByte("BatFlags"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("BatFlags", this.dataTracker.get(BAT_FLAGS));
    }

    public static boolean canSpawn(EntityType<BatEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= world.getSeaLevel()) {
            return false;
        } else {
            int i = world.getLightLevel(pos);
            int j = 3;
            if (isTodayAroundHalloween()) {
                j = 6;
            } else if (random.nextBoolean()) return false;
            return i <= random.nextInt(j) && canMobSpawn(type, world, spawnReason, pos, random);
        }
    }

    private static boolean isTodayAroundHalloween() {
        LocalDate localDate = LocalDate.now();
        int i = localDate.getDayOfMonth();
        int j = localDate.get(ChronoField.MONTH_OF_YEAR);
        return j == 10 && i >= 20 || j == 11 && i <= 3;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.8f;
    }
}