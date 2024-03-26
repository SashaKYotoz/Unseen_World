
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.PlayMessages;
import net.sashakyotoz.unseenworld.util.*;

public class MoonFishEntity extends WaterAnimal implements Bucketable {
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(MoonFishEntity.class, EntityDataSerializers.BOOLEAN);
	public int texture;

	public MoonFishEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(UnseenWorldModEntities.MOONFISH.get(), world);
	}

	public void travel(Vec3 vec3) {
		if (this.isEffectiveAi() && this.isInWater()) {
			this.moveRelative(0.015F, vec3);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(vec3);
		}
	}

	public MoonFishEntity(EntityType<MoonFishEntity> type, Level world) {
		super(type, world);
		xpReward = 2;
		setNoAi(false);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0);
		this.moveControl = new MoveControl(this) {
			@Override
			public void tick() {
				if (MoonFishEntity.this.isInWater())
					MoonFishEntity.this.setDeltaMovement(MoonFishEntity.this.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
				float fw = (float) (this.speedModifier * MoonFishEntity.this.getAttributeValue(Attributes.MOVEMENT_SPEED));
				if (this.operation == MoveControl.Operation.MOVE_TO && !MoonFishEntity.this.getNavigation().isDone()) {
					double dx = this.wantedX - MoonFishEntity.this.getX();
					double dy = this.wantedY - MoonFishEntity.this.getY();
					double dz = this.wantedZ - MoonFishEntity.this.getZ();
					if (dy != 0.0D) {
						double dw = Math.sqrt(dx * dx + dy * dy + dz * dz);
						MoonFishEntity.this.setDeltaMovement(MoonFishEntity.this.getDeltaMovement().add(0.0D, (double) MoonFishEntity.this.getSpeed() * (dy / dw) * 0.25D, 0.0D));
					}
					MoonFishEntity.this.setSpeed(Mth.lerp(0.125F, MoonFishEntity.this.getSpeed(), fw));
					float f = (float) (Mth.atan2(dz, dx) * (180 / Math.PI)) - 90;
					float f1 = (float) (this.speedModifier * MoonFishEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
					MoonFishEntity.this.setYRot(this.rotlerp(MoonFishEntity.this.getYRot(), f, 10));
					MoonFishEntity.this.yBodyRot = MoonFishEntity.this.getYRot();
					MoonFishEntity.this.yHeadRot = MoonFishEntity.this.getYRot();
					if (MoonFishEntity.this.isInWater()) {
						MoonFishEntity.this.setSpeed((float) MoonFishEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
						float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
						MoonFishEntity.this.setXRot(this.rotlerp(MoonFishEntity.this.getXRot(), f2, 5));
						float f3 = Mth.cos(MoonFishEntity.this.getXRot() * (float) (Math.PI / 180.0));
						MoonFishEntity.this.setZza(f3 * f1);
						MoonFishEntity.this.setYya((float) (f1 * dy));
					} else {
						MoonFishEntity.this.setSpeed(f1 * 0.1F);
					}
				} else {
					MoonFishEntity.this.setSpeed(0);
					MoonFishEntity.this.setYya(0);
					MoonFishEntity.this.setZza(0);
				}
			}
		};
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new WaterBoundPathNavigation(this, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1, 40));
		this.goalSelector.addGoal(2, new PanicGoal(this, 2));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, (float) 8, 1.5, 1));
	}

	@Override
	public MobType getMobType() {
		return MobType.WATER;
	}

	public void aiStep() {
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
			this.setOnGround(false);
			this.hasImpulse = true;
		}
		super.aiStep();
	}

	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
		super.dropCustomDeathLoot(source, looting, recentlyHitIn);
		this.spawnAtLocation(new ItemStack(UnseenWorldModItems.MOON_FISH_FOOD.get()));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.TROPICAL_FISH_AMBIENT;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.FISH_SWIM, 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.TROPICAL_FISH_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.TROPICAL_FISH_DEATH;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.DROWN))
			return false;
		if (source.is(DamageTypes.WITHER))
			return false;
		if (source.getMsgId().equals("witherSkull"))
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader world) {
		return world.isUnobstructed(this);
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	public static void init() {
		SpawnPlacements.register(UnseenWorldModEntities.MOONFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MoonFishEntity::checkSurfaceWaterAnimalSpawnRules);
	}
	public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> p_218283_, LevelAccessor accessor, MobSpawnType p_218285_, BlockPos pos, RandomSource p_218287_) {
		int i = accessor.getSeaLevel();
		int j = i - 8;
		return pos.getY() >= j && pos.getY() <= i && accessor.getFluidState(pos.below()).is(FluidTags.WATER) || accessor.getFluidState(pos.below()).is(UnseenWorldModFluids.DARK_WATER.get()) && accessor.getBlockState(pos.above()).is(Blocks.WATER) || accessor.getBlockState(pos.above()).is(UnseenWorldModBlocks.DARK_WATER.get());
	}
	@Override
	protected void handleAirSupply(int i) {
		if (this.isAlive() && !this.isInWaterOrBubble() && !this.level().getFluidState(new BlockPos(new Vec3i((int) this.getX(),(int) this.getY(),(int) this.getZ())).below()).is(UnseenWorldModFluids.DARK_WATER.get())) {
			this.setAirSupply(i - 1);
			if (this.getAirSupply() == -20) {
				this.setAirSupply(0);
				this.hurt(this.damageSources().drown(), 2.0F);
			}
		} else {
			this.setAirSupply(300);
		}

	}

	public void baseTick() {
		int i = this.getAirSupply();
		super.baseTick();
		this.handleAirSupply(i);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 5);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 0.3);
		return builder;
	}

	@Override
	public boolean canSwimInFluidType(FluidType type) {
		return super.canSwimInFluidType(UnseenWorldModFluidTypes.DARK_WATER_TYPE.get()) || super.canSwimInFluidType(Fluids.WATER.getFluidType());
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FROM_BUCKET, false);
	}

	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	public void setFromBucket(boolean set) {
		this.entityData.set(FROM_BUCKET, set);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("FromBucket", this.fromBucket());
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setFromBucket(tag.getBoolean("FromBucket"));
	}

	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
	}

	public void saveToBucketTag(ItemStack stack) {
		Bucketable.saveDefaultDataToBucketTag(this, stack);
	}

	public void loadFromBucketTag(CompoundTag tag) {
		Bucketable.loadDefaultDataFromBucketTag(this, tag);
	}

	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL_FISH;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(UnseenWorldModItems.MOON_FISHIN_BUCKET.get());
	}

}
