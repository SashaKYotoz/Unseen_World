
package net.sashakyotoz.unseenworld.entity;

import com.google.common.collect.Sets;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
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
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.Set;

public class ChimericPurplemarerEntity extends TamableAnimal implements ItemSteerable, Saddleable {
    private static final EntityDataAccessor<Boolean> DATA_IS_SADDLED = SynchedEntityData.defineId(ChimericPurplemarerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(UnseenWorldModItems.LUMINOUS_PORKCHOP.get(), UnseenWorldModItems.LUMINOUS_COOKED_PORKCHOP.get());
    private static final Ingredient TEMPT_ITEMS = Ingredient.of(UnseenWorldModItems.LUMINOUS_PORKCHOP.get(), Items.WARPED_FUNGUS_ON_A_STICK);
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(ChimericPurplemarerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(ChimericPurplemarerEntity.class, EntityDataSerializers.BOOLEAN);
    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_IS_SADDLED);

    public ChimericPurplemarerEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldModEntities.CHIMERIC_PURPLEMARER.get(), world);
    }

    public ChimericPurplemarerEntity(EntityType<ChimericPurplemarerEntity> type, Level world) {
        super(type, world);
        setMaxUpStep(1.25f);
        xpReward = 2;
        setNoAi(false);
    }
    protected void tickRidden(Player player, Vec3 vec3) {
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.steering.tickBoost();
        super.tickRidden(player, vec3);
    }

    protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
        return new Vec3(0.0D, 0.0D, 1.0D);
    }

    protected float getRiddenSpeed(Player player) {
        return (float)(this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double)(this.isSuffocating() ? 0.35F : 0.55F) * (double)this.steering.boostFactor());
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_BOOST_TIME.equals(accessor) && this.level().isClientSide) {
            this.steering.onSynced();
        }
        super.onSyncedDataUpdated(accessor);
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

    public void equipSaddle(@javax.annotation.Nullable SoundSource source) {
        this.steering.setSaddle(true);
        if (source != null) {
            this.level().playSound(null, this, SoundEvents.STRIDER_SADDLE, source, 0.5F, 1.0F);
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        TemptGoal temptGoal = new TemptGoal(this, 1.4D, Ingredient.of(Items.WARPED_FUNGUS_ON_A_STICK), false);
        this.goalSelector.addGoal(3, temptGoal);
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D, 60));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, ChimericPurplemarerEntity.class, 8.0F));
        this.goalSelector.addGoal(9, new OwnerHurtByTargetGoal(this));
        this.goalSelector.addGoal(10, new FloatGoal(this));
        this.goalSelector.addGoal(11, new FollowOwnerGoal(this, 1.25, (float) 9, (float) 3, false));
    }

    public void setSuffocating(boolean set) {
        this.entityData.set(DATA_SUFFOCATING, set);
        AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
    }

    public boolean isSuffocating() {
        return this.entityData.get(DATA_SUFFOCATING);
    }


    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.walkAnimation.speed());
        float f1 = this.walkAnimation.position();
        return (double) this.getBbHeight() - 0.42D + (double) (0.12F * Mth.cos(f1 * 1.5F) * 2.0F * f);
    }

    public boolean checkSpawnObstruction(LevelReader p_33880_) {
        return p_33880_.isUnobstructed(this);
    }

    @javax.annotation.Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player player) {
            if ((player.getMainHandItem().is(Items.WARPED_FUNGUS_ON_A_STICK) && (player.getMainHandItem().getOrCreateTag().getDouble("CustomModelData") == 1)) || (player.getOffhandItem().is(Items.WARPED_FUNGUS_ON_A_STICK) && (player.getOffhandItem().getOrCreateTag().getDouble("CustomModelData") == 1))) {
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
        return new Vec3(this.getX(), this.getBoundingBox().maxY, this.getZ());
    }

    protected void tickRidden(LivingEntity entity, Vec3 vec3) {
        this.setRot(entity.getYRot(), entity.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.steering.tickBoost();
        super.tickRidden((Player) entity, vec3);
    }

    protected float nextStep() {
        return this.moveDist + 0.5F;
    }

    protected void playStepSound(BlockPos p_33915_, BlockState p_33916_) {
        this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    protected boolean shouldPassengersInheritMalus() {
        return true;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.FOX_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33934_) {
        return SoundEvents.FOX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    protected boolean canAddPassenger(Entity p_33950_) {
        return !this.isVehicle();
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    public boolean isOnFire() {
        return false;
    }


    @javax.annotation.Nullable
    public ChimericPurplemarerEntity getBreedOffspring(ServerLevel level, AgeableMob p_149862_) {
        return UnseenWorldModEntities.CHIMERIC_PURPLEMARER.get().create(level);
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
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FOX_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                }
                return interactionresult;
            }
        }
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType p_33889_, @javax.annotation.Nullable SpawnGroupData p_33890_, @javax.annotation.Nullable CompoundTag p_33891_) {
        if (!this.isBaby()) {
            RandomSource randomsource = accessor.getRandom();
            if (randomsource.nextInt(30) == 0) {
                Mob mob = EntityType.ZOMBIFIED_PIGLIN.create(accessor.getLevel());
                if (mob != null) {
                    p_33890_ = this.spawnJockey(accessor, instance, mob, new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(randomsource), false));
                    this.equipSaddle(null);
                }
            } else if (randomsource.nextInt(10) == 0) {
                AgeableMob ageablemob = UnseenWorldModEntities.CHIMERIC_PURPLEMARER.get().create(accessor.getLevel());
                if (ageablemob != null) {
                    ageablemob.setAge(-24000);
                    p_33890_ = this.spawnJockey(accessor, instance, ageablemob, null);
                }
            } else {
                p_33890_ = new AgeableMobGroupData(0.5F);
            }
        }
        return super.finalizeSpawn(accessor, instance, p_33889_, p_33890_, p_33891_);
    }

    private SpawnGroupData spawnJockey(ServerLevelAccessor accessor, DifficultyInstance p_33883_, Mob mob, @Nullable SpawnGroupData p_33885_) {
        mob.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        mob.finalizeSpawn(accessor, p_33883_, MobSpawnType.JOCKEY, p_33885_, null);
        mob.startRiding(this, true);
        return new AgeableMob.AgeableMobGroupData(0.0F);
    }

    public static void init() {
        SpawnPlacements.register(UnseenWorldModEntities.CHIMERIC_PURPLEMARER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 15);
        builder = builder.add(Attributes.ARMOR, 1.5);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 5);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }
}
