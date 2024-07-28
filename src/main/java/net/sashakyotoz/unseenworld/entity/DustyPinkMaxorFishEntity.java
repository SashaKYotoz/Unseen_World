
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
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
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
import net.sashakyotoz.unseenworld.registries.*;
import org.jetbrains.annotations.NotNull;

public class DustyPinkMaxorFishEntity extends WaterAnimal implements Bucketable {
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(DustyPinkMaxorFishEntity.class, EntityDataSerializers.BOOLEAN);
	public DustyPinkMaxorFishEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(UnseenWorldEntities.DUSTY_PINK_MAXOR_FISH.get(), world);
	}

	public void travel(@NotNull Vec3 vec3) {
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
	@Override
	public boolean canSwimInFluidType(FluidType type) {
		return super.canSwimInFluidType(UnseenWorldFluids.DARK_WATER_TYPE.get()) || super.canSwimInFluidType(Fluids.WATER.getFluidType());
	}

	public DustyPinkMaxorFishEntity(EntityType<DustyPinkMaxorFishEntity> type, Level world) {
		super(type, world);
		xpReward = 2;
		setNoAi(false);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0);
		this.moveControl = new MoveControl(this) {
			@Override
			public void tick() {
				if (DustyPinkMaxorFishEntity.this.isInWater())
					DustyPinkMaxorFishEntity.this.setDeltaMovement(DustyPinkMaxorFishEntity.this.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
				float fw = (float) (this.speedModifier * DustyPinkMaxorFishEntity.this.getAttributeValue(Attributes.MOVEMENT_SPEED));
				if (this.operation == MoveControl.Operation.MOVE_TO && !DustyPinkMaxorFishEntity.this.getNavigation().isDone()) {
					double dx = this.wantedX - DustyPinkMaxorFishEntity.this.getX();
					double dy = this.wantedY - DustyPinkMaxorFishEntity.this.getY();
					double dz = this.wantedZ - DustyPinkMaxorFishEntity.this.getZ();
					if (dy != 0.0D) {
						double dw = Math.sqrt(dx * dx + dy * dy + dz * dz);
						DustyPinkMaxorFishEntity.this.setDeltaMovement(DustyPinkMaxorFishEntity.this.getDeltaMovement().add(0.0D, (double) DustyPinkMaxorFishEntity.this.getSpeed() * (dy / dw) * 0.25D, 0.0D));
					}
					DustyPinkMaxorFishEntity.this.setSpeed(Mth.lerp(0.125F, DustyPinkMaxorFishEntity.this.getSpeed(), fw));
					float f = (float) (Mth.atan2(dz, dx) * (180 / Math.PI)) - 90;
					float f1 = (float) (this.speedModifier * DustyPinkMaxorFishEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
					DustyPinkMaxorFishEntity.this.setYRot(this.rotlerp(DustyPinkMaxorFishEntity.this.getYRot(), f, 10));
					DustyPinkMaxorFishEntity.this.yBodyRot = DustyPinkMaxorFishEntity.this.getYRot();
					DustyPinkMaxorFishEntity.this.yHeadRot = DustyPinkMaxorFishEntity.this.getYRot();
					if (DustyPinkMaxorFishEntity.this.isInWater()) {
						DustyPinkMaxorFishEntity.this.setSpeed((float) DustyPinkMaxorFishEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
						float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
						DustyPinkMaxorFishEntity.this.setXRot(this.rotlerp(DustyPinkMaxorFishEntity.this.getXRot(), f2, 5));
						float f3 = Mth.cos(DustyPinkMaxorFishEntity.this.getXRot() * (float) (Math.PI / 180.0));
						DustyPinkMaxorFishEntity.this.setZza(f3 * f1);
						DustyPinkMaxorFishEntity.this.setYya((float) (f1 * dy));
					} else {
						DustyPinkMaxorFishEntity.this.setSpeed(f1 * 0.1F);
					}
				} else {
					DustyPinkMaxorFishEntity.this.setSpeed(0);
					DustyPinkMaxorFishEntity.this.setYya(0);
					DustyPinkMaxorFishEntity.this.setZza(0);
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
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
		this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1, 40));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, (float) 8, 1, 1.5));
	}


	@Override
	public MobType getMobType() {
		return MobType.WATER;
	}

	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
		super.dropCustomDeathLoot(source, looting, recentlyHitIn);
		this.spawnAtLocation(new ItemStack(UnseenWorldItems.DUSTY_PINK_MAXOR_FISH_FOOD.get()));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.COD_AMBIENT;
	}

	public void aiStep() {
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
			this.hasImpulse = true;
			this.setOnGround(false);
		}
		super.aiStep();
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.COD_FLOP, 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.SALMON_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.SALMON_DEATH;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
			return false;
		if (source.is(DamageTypes.DROWN))
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
	public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> entityType, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource source) {
		int i = accessor.getSeaLevel();
		int j = i - 8;
		return pos.getY() >= j && pos.getY() <= i && accessor.getFluidState(pos.below()).is(FluidTags.WATER) || accessor.getFluidState(pos.below()).is(UnseenWorldFluids.DARK_WATER.get()) && accessor.getBlockState(pos.above()).is(Blocks.WATER) || accessor.getBlockState(pos.above()).is(UnseenWorldBlocks.DARK_WATER.get()) && accessor.getDifficulty() == Difficulty.EASY;
	}
	@Override
	protected void handleAirSupply(int i) {
		if (this.isAlive() && !this.isInWaterOrBubble() && !this.level().getFluidState(new BlockPos(new Vec3i((int) this.getX(),(int) this.getY(),(int) this.getZ())).below()).is(UnseenWorldFluids.DARK_WATER.get())) {
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
		builder = builder.add(Attributes.MAX_HEALTH, 6);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 0.3);
		return builder;
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

	public void addAdditionalSaveData(CompoundTag p_27485_) {
		super.addAdditionalSaveData(p_27485_);
		p_27485_.putBoolean("FromBucket", this.fromBucket());
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
		return new ItemStack(UnseenWorldItems.DUSTY_PINK_MAXOR_FISH_BUCKET.get());
	}
}
