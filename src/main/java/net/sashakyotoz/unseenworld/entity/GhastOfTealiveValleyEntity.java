
package net.sashakyotoz.unseenworld.entity;

import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;

import java.util.EnumSet;

public class GhastOfTealiveValleyEntity extends FlyingMob implements Enemy {
	private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(GhastOfTealiveValleyEntity.class,
			EntityDataSerializers.BOOLEAN);
	private int explosionPower = 1;

	public GhastOfTealiveValleyEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(UnseenWorldEntities.GHAST_OF_TEALIVE_VALLEY.get(), world);
	}

	public GhastOfTealiveValleyEntity(EntityType<GhastOfTealiveValleyEntity> type, Level world) {
		super(type, world);
		this.xpReward = 5;
		this.moveControl = new GhastOfTealiveValleyEntity.GhastMoveControl(this);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(5, new GhastOfTealiveValleyEntity.RandomFloatAroundGoal(this));
		this.goalSelector.addGoal(7, new GhastOfTealiveValleyEntity.GhastLookGoal(this));
		this.goalSelector.addGoal(7, new GhastOfTealiveValleyEntity.GhastShootFireballGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (y) -> Math.abs(y.getY() - this.getY()) <= 4.0D));
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_IS_CHARGING);
	}

	public void setCharging(boolean p_32759_) {
		this.entityData.set(DATA_IS_CHARGING, p_32759_);
	}

	public int getExplosionPower() {
		return this.explosionPower;
	}

	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	private static boolean isReflectedFireball(DamageSource damageSource) {
		return damageSource.getDirectEntity() instanceof LargeFireball && damageSource.getEntity() instanceof Player;
	}

	public boolean isInvulnerableTo(DamageSource damageSource) {
		return !isReflectedFireball(damageSource) && super.isInvulnerableTo(damageSource);
	}

	public boolean hurt(DamageSource damageSource, float amount) {
		if (isReflectedFireball(damageSource)) {
			super.hurt(damageSource, 1000.0F);
			return true;
		} else {
			return !this.isInvulnerableTo(damageSource) && super.hurt(damageSource, amount);
		}
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_IS_CHARGING, false);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.FOLLOW_RANGE, 100.0D);
	}

	public SoundSource getSoundSource() {
		return SoundSource.HOSTILE;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.GHAST_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_32750_) {
		return SoundEvents.GHAST_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.GHAST_DEATH;
	}

	protected float getSoundVolume() {
		return 5.0F;
	}

	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putByte("ExplosionPower", (byte) this.explosionPower);
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("ExplosionPower", 99)) {
			this.explosionPower = tag.getByte("ExplosionPower");
		}
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		return 2.6F;
	}

	static class GhastLookGoal extends Goal {
		private final GhastOfTealiveValleyEntity ghast;

		public GhastLookGoal(GhastOfTealiveValleyEntity ghast) {
			this.ghast = ghast;
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return true;
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			if (this.ghast.getTarget() == null) {
				Vec3 vec3 = this.ghast.getDeltaMovement();
				this.ghast.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI));
				this.ghast.yBodyRot = this.ghast.getYRot();
			} else {
				LivingEntity livingentity = this.ghast.getTarget();
				if (livingentity.distanceToSqr(this.ghast) < 4096.0D) {
					double d1 = livingentity.getX() - this.ghast.getX();
					double d2 = livingentity.getZ() - this.ghast.getZ();
					this.ghast.setYRot(-((float) Mth.atan2(d1, d2)) * (180F / (float) Math.PI));
					this.ghast.yBodyRot = this.ghast.getYRot();
				}
			}
		}
	}

	static class GhastMoveControl extends MoveControl {
		private final GhastOfTealiveValleyEntity ghast;
		private int floatDuration;

		public GhastMoveControl(GhastOfTealiveValleyEntity ghastOfTealiveValleyEntity) {
			super(ghastOfTealiveValleyEntity);
			this.ghast = ghastOfTealiveValleyEntity;
		}

		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				if (this.floatDuration-- <= 0) {
					this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
					Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
					double d0 = vec3.length();
					vec3 = vec3.normalize();
					if (this.canReach(vec3, Mth.ceil(d0))) {
						this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.scale(0.1D)));
					} else {
						this.operation = MoveControl.Operation.WAIT;
					}
				}
			}
		}

		private boolean canReach(Vec3 vec3, int p_32772_) {
			AABB aabb = this.ghast.getBoundingBox();
			for (int i = 1; i < p_32772_; ++i) {
				aabb = aabb.move(vec3);
				if (!this.ghast.level().noCollision(this.ghast, aabb)) {
					return false;
				}
			}
			return true;
		}
	}

	static class GhastShootFireballGoal extends Goal {
		private final GhastOfTealiveValleyEntity ghast;
		public int chargeTime;

		public GhastShootFireballGoal(GhastOfTealiveValleyEntity ghast) {
			this.ghast = ghast;
		}

		public boolean canUse() {
			return this.ghast.getTarget() != null;
		}

		public void start() {
			this.chargeTime = 0;
		}

		public void stop() {
			this.ghast.setCharging(false);
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity livingentity = this.ghast.getTarget();
			if (livingentity != null) {
				if (livingentity.distanceToSqr(this.ghast) < 4096.0D && this.ghast.hasLineOfSight(livingentity)) {
					Level level = this.ghast.level();
					++this.chargeTime;
					if (this.chargeTime == 10 && !this.ghast.isSilent()) {
						level.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
					}
					if (this.chargeTime == 20) {
						Vec3 vec3 = this.ghast.getViewVector(1.0F);
						double d2 = livingentity.getX() - (this.ghast.getX() + vec3.x * 4.0D);
						double d3 = livingentity.getY(0.5D) - (0.5D + this.ghast.getY(0.5D));
						double d4 = livingentity.getZ() - (this.ghast.getZ() + vec3.z * 4.0D);
						if (!this.ghast.isSilent()) {
							level.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
						}
						LargeFireball largefireball = new LargeFireball(level, this.ghast, d2, d3, d4, this.ghast.getExplosionPower());
						largefireball.setPos(this.ghast.getX() + vec3.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, largefireball.getZ() + vec3.z * 4.0D);
						level.addFreshEntity(largefireball);
						this.chargeTime = -40;
					}
				} else if (this.chargeTime > 0) {
					--this.chargeTime;
				}
				this.ghast.setCharging(this.chargeTime > 10);
			}
		}
	}

	static class RandomFloatAroundGoal extends Goal {
		private final GhastOfTealiveValleyEntity ghast;

		public RandomFloatAroundGoal(GhastOfTealiveValleyEntity ghast) {
			this.ghast = ghast;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			MoveControl movecontrol = this.ghast.getMoveControl();
			if (!movecontrol.hasWanted()) {
				return true;
			} else {
				double d0 = movecontrol.getWantedX() - this.ghast.getX();
				double d1 = movecontrol.getWantedY() - this.ghast.getY();
				double d2 = movecontrol.getWantedZ() - this.ghast.getZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void start() {
			RandomSource randomsource = this.ghast.getRandom();
			double d0 = this.ghast.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d1 = this.ghast.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d2 = this.ghast.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
		}
	}
}
