package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.bosses.BossLikePathfinderMob;
import net.sashakyotoz.common.entities.util.MinionOwnership;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class AureateVexEntity extends Monster implements TraceableEntity {
    public static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AureateVexEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(AureateVexEntity.class, EntityDataSerializers.INT);
    private static final int FLAG_IS_CHARGING = 1;
    private static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);

    private final MinionOwnership ownership = new MinionOwnership(this, OWNER_ID);
    @Nullable
    private BlockPos boundOrigin;
    private boolean hasLimitedLife;
    private int limitedLifeTicks;

    public AureateVexEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new AureateVexEntity.VexMoveControl(this);
        this.xpReward = 3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height - 0.28125F;
    }

    public boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        super.move(type, pos);
        this.checkInsideBlocks();
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(this.damageSources().starve(), 1.0F);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new AureateVexEntity.VexChargeAttackGoal());
        this.goalSelector.addGoal(8, new AureateVexEntity.VexRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(2, new AureateVexEntity.VexCopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        MinionOwnership.define(this.entityData, OWNER_ID);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }
        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }
        this.ownership.load(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }
        if (this.hasLimitedLife) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }
        this.ownership.save(compound);
    }

    @Nullable
    @Override
    public Mob getOwner() {
        return this.ownership.get() instanceof Mob mob ? mob : null;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return entity instanceof BossLikePathfinderMob;
    }

    public void setOwner(@Nullable Mob owner) {
        this.ownership.set(owner);
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOrigin) {
        this.boundOrigin = boundOrigin;
    }

    private boolean getVexFlag(int mask) {
        return (this.entityData.get(DATA_FLAGS_ID) & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (value) {
            i |= mask;
        } else {
            i &= ~mask;
        }
        this.entityData.set(DATA_FLAGS_ID, (byte) (i & 255));
    }

    public boolean isCharging() {
        return this.getVexFlag(FLAG_IS_CHARGING);
    }

    public void setIsCharging(boolean charging) {
        this.setVexFlag(FLAG_IS_CHARGING, charging);
    }

    public void setLimitedLife(int limitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = limitedLifeTicks;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        RandomSource randomSource = level.getRandom();
        this.populateDefaultEquipmentSlots(randomSource, difficulty);
        this.populateDefaultEquipmentEnchantments(randomSource, difficulty);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.UNSEENIUM_SWORD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    @Override
    public double getMyRidingOffset() {
        return 0.4D;
    }

    private class VexMoveControl extends MoveControl {
        public VexMoveControl(AureateVexEntity vex) {
            super(vex);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                double dx = this.wantedX - AureateVexEntity.this.getX();
                double dy = this.wantedY - AureateVexEntity.this.getY();
                double dz = this.wantedZ - AureateVexEntity.this.getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (distance < AureateVexEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    AureateVexEntity.this.setDeltaMovement(AureateVexEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    AureateVexEntity.this.setDeltaMovement(AureateVexEntity.this.getDeltaMovement().add(dx / distance * this.speedModifier * 0.05D, dy / distance * this.speedModifier * 0.05D, dz / distance * this.speedModifier * 0.05D));
                    if (AureateVexEntity.this.getTarget() == null) {
                        Vec3 movement = AureateVexEntity.this.getDeltaMovement();
                        AureateVexEntity.this.setYRot(-((float) Mth.atan2(movement.x, movement.z)) * (180.0F / (float) Math.PI));
                    } else {
                        double tx = AureateVexEntity.this.getTarget().getX() - AureateVexEntity.this.getX();
                        double tz = AureateVexEntity.this.getTarget().getZ() - AureateVexEntity.this.getZ();
                        AureateVexEntity.this.setYRot(-((float) Mth.atan2(tx, tz)) * (180.0F / (float) Math.PI));
                    }
                    AureateVexEntity.this.yBodyRot = AureateVexEntity.this.getYRot();
                }
            }
        }
    }

    private class VexChargeAttackGoal extends Goal {
        public VexChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = AureateVexEntity.this.getTarget();
            if (target != null && target.isAlive() && !AureateVexEntity.this.getMoveControl().hasWanted() && AureateVexEntity.this.getRandom().nextInt(adjustedTickDelay(7)) == 0) {
                return AureateVexEntity.this.distanceToSqr(target) > 4.0D;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return AureateVexEntity.this.getMoveControl().hasWanted() && AureateVexEntity.this.isCharging() && AureateVexEntity.this.getTarget() != null && AureateVexEntity.this.getTarget().isAlive();
        }

        @Override
        public void start() {
            LivingEntity target = AureateVexEntity.this.getTarget();
            if (target != null) {
                Vec3 eyePos = target.getEyePosition();
                AureateVexEntity.this.moveControl.setWantedPosition(eyePos.x, eyePos.y, eyePos.z, 1.0D);
            }
            AureateVexEntity.this.setIsCharging(true);
            AureateVexEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            AureateVexEntity.this.setIsCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = AureateVexEntity.this.getTarget();
            if (target != null) {
                if (AureateVexEntity.this.getBoundingBox().intersects(target.getBoundingBox())) {
                    AureateVexEntity.this.doHurtTarget(target);
                    AureateVexEntity.this.setIsCharging(false);
                } else {
                    double distSqr = AureateVexEntity.this.distanceToSqr(target);
                    if (distSqr < 9.0D) {
                        Vec3 eyePos = target.getEyePosition();
                        AureateVexEntity.this.moveControl.setWantedPosition(eyePos.x, eyePos.y, eyePos.z, 1.0D);
                    }
                }
            }
        }
    }

    private class VexRandomMoveGoal extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !AureateVexEntity.this.getMoveControl().hasWanted() && AureateVexEntity.this.getRandom().nextInt(adjustedTickDelay(7)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos origin = AureateVexEntity.this.getBoundOrigin();
            if (origin == null) {
                origin = AureateVexEntity.this.blockPosition();
            }

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            for (int i = 0; i < 3; ++i) {
                mutablePos.setWithOffset(origin, AureateVexEntity.this.getRandom().nextInt(17) - 8, AureateVexEntity.this.getRandom().nextInt(15) - 7, AureateVexEntity.this.getRandom().nextInt(17) - 8);
                if (AureateVexEntity.this.level().isEmptyBlock(mutablePos)) {
                    AureateVexEntity.this.moveControl.setWantedPosition( mutablePos.getX() + 0.5D,  mutablePos.getY() + 0.5D,  mutablePos.getZ() + 0.5D, 0.25D);
                    if (AureateVexEntity.this.getTarget() == null) {
                        AureateVexEntity.this.getLookControl().setLookAt( mutablePos.getX() + 0.5D,  mutablePos.getY() + 0.5D,  mutablePos.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    private class VexCopyOwnerTargetGoal extends TargetGoal {
        private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

        public VexCopyOwnerTargetGoal(PathfinderMob mob) {
            super(mob, false);
        }

        @Override
        public boolean canUse() {
            Mob owner = AureateVexEntity.this.getOwner();
            return owner != null && owner.getTarget() != null && this.canAttack(owner.getTarget(), this.copyOwnerTargeting);
        }

        @Override
        public void start() {
            Mob owner = AureateVexEntity.this.getOwner();
            if (owner != null) {
                AureateVexEntity.this.setTarget(owner.getTarget());
            }
            super.start();
        }
    }
}