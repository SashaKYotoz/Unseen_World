
package net.sashakyotoz.unseenworld.entity;

import com.google.common.collect.Sets;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModFluids;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.Set;

public class StrederEntity extends Animal implements ItemSteerable, Saddleable {
    private static final EntityDataAccessor<Boolean> DATA_IS_SADDLED = SynchedEntityData.defineId(StrederEntity.class, EntityDataSerializers.BOOLEAN);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(UnseenWorldModBlocks.DEEP_WATER_ANFELTSIA.get());
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(StrederEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(StrederEntity.class, EntityDataSerializers.BOOLEAN);
    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_IS_SADDLED);
    @Nullable
    private TemptGoal temptGoal;

    public StrederEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldModEntities.STREDER.get(), world);
    }
    public StrederEntity(EntityType<StrederEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setMaxUpStep(1f);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (DATA_BOOST_TIME.equals(dataAccessor) && this.level().isClientSide)
            this.steering.onSynced();
        super.onSyncedDataUpdated(dataAccessor);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BOOST_TIME, 0);
        this.entityData.define(DATA_SUFFOCATING, false);
        this.entityData.define(DATA_IS_SADDLED, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.steering.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.steering.readAdditionalSaveData(tag);
    }

    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    public void equipSaddle(@Nullable SoundSource source) {
        this.steering.setSaddle(true);
        if (source != null) {
            this.level().playSound(null, this, SoundEvents.STRIDER_SADDLE, source, 0.5F, 1.0F);
        }
    }
    protected void tickRidden(Player player, Vec3 p_278234_) {
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.steering.tickBoost();
        super.tickRidden(player, p_278234_);
    }

    protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
        return super.getRiddenInput(player,vec3);
    }

    protected float getRiddenSpeed(Player player) {
        return (float)(this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double)(this.isSuffocating() ? 0.35F : 0.55F) * (double)this.steering.boostFactor());
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.temptGoal = new TemptGoal(this, 1.4D, Ingredient.of(Items.WARPED_FUNGUS_ON_A_STICK), false);
        this.goalSelector.addGoal(3, this.temptGoal);
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 60));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, StrederEntity.class, 8.0F));
    }

    public void setSuffocating(boolean b) {
        this.entityData.set(DATA_SUFFOCATING, b);
        AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
    }

    public boolean isSuffocating() {
        return this.entityData.get(DATA_SUFFOCATING);
    }

    @Override
    public boolean canStandOnFluid(FluidState state) {
        return state.is(FluidTags.LAVA) || state.is(UnseenWorldModFluids.DARK_WATER.get());
    }
    @Override
    public float nextStep() {
        return this.moveDist + 0.5F;
    }

    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.walkAnimation.speed());
        float f1 = this.walkAnimation.position();
        return (double) this.getBbHeight() - 0.34D + (double) (0.12F * Mth.cos(f1 * 1.5F) * 2.0F * f);
    }

    public boolean checkSpawnObstruction(LevelReader reader) {
        return reader.isUnobstructed(this);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player player) {
            if ((player.getMainHandItem().is(Items.WARPED_FUNGUS_ON_A_STICK) && (player.getMainHandItem().getOrCreateTag().getDouble("CustomModelData") == 1)) || (player.getOffhandItem().is(Items.WARPED_FUNGUS_ON_A_STICK)&& (player.getOffhandItem().getOrCreateTag().getDouble("CustomModelData") == 1))) {
                return player;
            }
        }
        return null;
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
        Vec3[] avec3 = new Vec3[]{getCollisionHorizontalEscapeVector(this.getBbWidth(), entity.getBbWidth(), entity.getYRot()),
                getCollisionHorizontalEscapeVector(this.getBbWidth(), entity.getBbWidth(), entity.getYRot() - 22.5F),
                getCollisionHorizontalEscapeVector(this.getBbWidth(), entity.getBbWidth(), entity.getYRot() + 22.5F),
                getCollisionHorizontalEscapeVector(this.getBbWidth(), entity.getBbWidth(), entity.getYRot() - 45.0F),
                getCollisionHorizontalEscapeVector(this.getBbWidth(), entity.getBbWidth(), entity.getYRot() + 45.0F)};
        Set<BlockPos> set = Sets.newLinkedHashSet();
        double d0 = this.getBoundingBox().maxY;
        double d1 = this.getBoundingBox().minY - 0.5D;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (Vec3 vec3 : avec3) {
            blockpos$mutableblockpos.set(this.getX() + vec3.x, d0, this.getZ() + vec3.z);
            for (double d2 = d0; d2 > d1; --d2) {
                set.add(blockpos$mutableblockpos.immutable());
                blockpos$mutableblockpos.move(Direction.DOWN);
            }
        }
        for (BlockPos blockpos : set) {
            if (!this.level().getFluidState(blockpos).is(FluidTags.LAVA)) {
                double d3 = this.level().getBlockFloorHeight(blockpos);
                if (DismountHelper.isBlockFloorValid(d3)) {
                    Vec3 vec31 = Vec3.upFromBottomCenterOf(blockpos, d3);
                    for (Pose pose : entity.getDismountPoses()) {
                        AABB aabb = entity.getLocalBoundsForPose(pose);
                        if (DismountHelper.canDismountTo(this.level(), entity, aabb.move(vec31))) {
                            entity.setPose(pose);
                            return vec31;
                        }
                    }
                }
            }
        }
        return new Vec3(this.getX(), this.getBoundingBox().maxY, this.getZ());
    }

    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(this.isInLava() ? SoundEvents.STRIDER_STEP_LAVA : SoundEvents.STRIDER_STEP, 1.0F, 1.0F);
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    protected void checkFallDamage(double p_33870_, boolean p_33871_, BlockState state, BlockPos pos) {
        this.checkInsideBlocks();
        if (this.isInLava()) {
            this.resetFallDistance();
        } else {
            super.checkFallDamage(p_33870_, p_33871_, state, pos);
        }
    }

    public void tick() {
        if (this.isBeingTempted() && this.random.nextInt(140) == 0) {
            this.playSound(SoundEvents.STRIDER_HAPPY, 1.0F, this.getVoicePitch());
        }
        if (!this.isNoAi()) {
            boolean flag;
            boolean flag2;
            label36:
            {
                BlockState blockstate = this.level().getBlockState(this.blockPosition());
                BlockState stateOnLegacy = this.getBlockStateOnLegacy();
                flag = blockstate.is(BlockTags.STRIDER_WARM_BLOCKS) || stateOnLegacy.is(UnseenWorldModBlocks.DARK_WATER.get()) || stateOnLegacy.is(BlockTags.STRIDER_WARM_BLOCKS) || this.getFluidHeight(FluidTags.LAVA) > 0.0D;
                Entity entity = this.getVehicle();
                if (entity instanceof StrederEntity strider) {
                    if (strider.isSuffocating()) {
                        flag2 = true;
                        break label36;
                    }
                }
                flag2 = false;
            }
            boolean flag1 = flag2;
            this.setSuffocating(!flag || flag1);
        }
        super.tick();
        this.floatStrider();
        this.checkInsideBlocks();
    }

    private boolean isBeingTempted() {
        return this.temptGoal != null && this.temptGoal.isRunning();
    }

    protected boolean shouldPassengersInheritMalus() {
        return true;
    }

    private void floatStrider() {
        if (this.isInLava()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true)
                    && (!this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA) || !this.level().getFluidState(this.blockPosition().above()).is(UnseenWorldModFluids.DARK_WATER.get()))) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
            }
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.STRIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STRIDER_DEATH;
    }

    protected boolean canAddPassenger(Entity entity) {
        return !this.isVehicle() && !this.isEyeInFluid(FluidTags.LAVA);
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    public boolean isOnFire() {
        return false;
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader reader) {
        if (reader.getBlockState(pos).getFluidState().is(FluidTags.LAVA) || reader.getBlockState(pos).getFluidState().is(UnseenWorldModFluids.DARK_WATER.get())) {
            return 10.0F;
        } else {
            return this.isInLava() ? Float.NEGATIVE_INFINITY : 0.0F;
        }
    }

    @Nullable
    public StrederEntity getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return UnseenWorldModEntities.STREDER.get().create(level);
    }

    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean flag = this.isFood(player.getItemInHand(hand));
        if (!flag && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            InteractionResult interactionresult = super.mobInteract(player, hand);
            if (!interactionresult.consumesAction()) {
                ItemStack itemstack = player.getItemInHand(hand);
                return itemstack.is(Items.SADDLE) ? itemstack.interactLivingEntity(player, this, hand) : InteractionResult.PASS;
            } else {
                if (flag && !this.isSilent()) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.STRIDER_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                }
                return interactionresult;
            }
        }
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag p_33891_) {
        if (!this.isBaby()) {
            RandomSource randomsource = accessor.getRandom();
            if (randomsource.nextInt(30) == 0) {
                Mob mob = EntityType.ZOMBIFIED_PIGLIN.create(accessor.getLevel());
                if (mob != null) {
                    data = this.spawnJockey(accessor, instance, mob, new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(randomsource), false));
                    mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK));
                    this.equipSaddle(null);
                }
            } else if (randomsource.nextInt(10) == 0) {
                AgeableMob ageablemob = UnseenWorldModEntities.STREDER.get().create(accessor.getLevel());
                if (ageablemob != null) {
                    ageablemob.setAge(-24000);
                    data = this.spawnJockey(accessor, instance, ageablemob, null);
                }
            } else {
                data = new AgeableMobGroupData(0.5F);
            }
        }
        return super.finalizeSpawn(accessor, instance, spawnType, data, p_33891_);
    }

    private SpawnGroupData spawnJockey(ServerLevelAccessor accessor, DifficultyInstance instance, Mob mob, @Nullable SpawnGroupData p_33885_) {
        mob.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        mob.finalizeSpawn(accessor, instance, MobSpawnType.JOCKEY, p_33885_, null);
        mob.startRiding(this, true);
        return new AgeableMob.AgeableMobGroupData(0.0F);
    }
    protected PathNavigation createNavigation(Level level) {
        return new StrederEntity.StrederPathNavigation(this, level);
    }

    static class StrederPathNavigation extends GroundPathNavigation {
        StrederPathNavigation(StrederEntity entity, Level level) {
            super(entity, level);
        }

        protected PathFinder createPathFinder(int i) {
            this.nodeEvaluator = new WalkNodeEvaluator();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, i);
        }

        protected boolean hasValidPathType(BlockPathTypes pathTypes) {
            return pathTypes == BlockPathTypes.LAVA || pathTypes == BlockPathTypes.DAMAGE_FIRE || pathTypes == BlockPathTypes.DANGER_FIRE || super.hasValidPathType(pathTypes);
        }

        public boolean isStableDestination(BlockPos pos) {
            return this.level.getBlockState(pos).is(Blocks.LAVA) || super.isStableDestination(pos) || this.level.getBlockState(pos).is(UnseenWorldModBlocks.DARK_WATER.get());
        }
    }

    public static void init() {
        SpawnPlacements.register(UnseenWorldModEntities.STREDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 15);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }
}
