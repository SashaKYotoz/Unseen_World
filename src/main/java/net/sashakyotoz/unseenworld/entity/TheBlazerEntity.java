
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.sashakyotoz.unseenworld.UnseenWorldModConfigs;
import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.procedures.TheBlazerEntityDiesProcedure;
import net.sashakyotoz.unseenworld.procedures.TheBlazerOnEntityTickUpdateProcedure;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import java.util.EnumSet;

public class TheBlazerEntity extends Blaze implements RangedAttackMob {
	private final TheBlazerEntity blazer = this;
	public static boolean blocked;
	private static final EntityDataAccessor<Boolean> DATA_IS_BLOCKED = SynchedEntityData.defineId(TheBlazerEntity.class, EntityDataSerializers.BOOLEAN);
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.NOTCHED_6);

	public TheBlazerEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(UnseenWorldModEntities.THE_BLAZER.get(), world);
	}

	public TheBlazerEntity(EntityType<TheBlazerEntity> type, Level world) {
		super(type, world);
		xpReward = 30;
		setNoAi(false);
		setCustomName(Component.literal("§4The Blazer"));
		setCustomNameVisible(true);
		setPersistenceRequired();
		this.moveControl = new FlyingMoveControl(this, 10, true);
	}

	public boolean isBlocked() {
		return this.entityData.get(DATA_IS_BLOCKED);
	}

	public void setBlocking(boolean p_32759_) {
		this.entityData.set(DATA_IS_BLOCKED, p_32759_);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_IS_BLOCKED, false);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new FlyingPathNavigation(this, world);
	}

	static class TheBlazerisBlockingGoal extends Goal {
		private final TheBlazerEntity ghast;
		public int chargeTime;

		public TheBlazerisBlockingGoal(TheBlazerEntity p_32776_) {
			this.ghast = p_32776_;
		}

		public boolean canUse() {
			return this.ghast.getTarget() != null;
		}

		public void start() {
			this.chargeTime = 0;
		}

		public void stop() {
			this.ghast.setBlocking(false);
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity livingentity = this.ghast.getTarget();
			if (livingentity != null) {
				double d0 = 64.0D;
				if (livingentity.distanceToSqr(this.ghast) < 2048.0D && this.ghast.hasLineOfSight(livingentity)) {
					Level level = this.ghast.level();
					++this.chargeTime;
					if (this.chargeTime == 80 && !this.ghast.isSilent()) {
						level.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
						blocked = true;
					}
					if (this.chargeTime == 160) {
						blocked = false;
						this.chargeTime = -160;
					}
				} else if (this.chargeTime > 0) {
					--this.chargeTime;
				}
				this.ghast.setBlocking(this.chargeTime > 80);
			}
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false, false));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(3, new Goal() {
			{
				this.setFlags(EnumSet.of(Goal.Flag.MOVE));
			}

			public boolean canUse() {
				return TheBlazerEntity.this.getTarget() != null && !TheBlazerEntity.this.getMoveControl().hasWanted();
			}

			@Override
			public boolean canContinueToUse() {
				return TheBlazerEntity.this.getMoveControl().hasWanted() && TheBlazerEntity.this.getTarget() != null && TheBlazerEntity.this.getTarget().isAlive();
			}

			@Override
			public void start() {
				LivingEntity livingentity = TheBlazerEntity.this.getTarget();
				Vec3 vec3d = livingentity.getEyePosition(1);
				TheBlazerEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.5);
			}

			@Override
			public void tick() {
				LivingEntity livingentity = TheBlazerEntity.this.getTarget();
				if (TheBlazerEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
					TheBlazerEntity.this.doHurtTarget(livingentity);
				} else {
					double d0 = TheBlazerEntity.this.distanceToSqr(livingentity);
					if (d0 < 32) {
						Vec3 vec3d = livingentity.getEyePosition(1);
						TheBlazerEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.5);
					}
				}
			}
		});
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.25, 20) {
			@Override
			protected Vec3 getPosition() {
				RandomSource random = TheBlazerEntity.this.getRandom();
				double dir_x = TheBlazerEntity.this.getX() + ((random.nextFloat() * 2 - 1) * 16);
				double dir_y = TheBlazerEntity.this.getY() + ((random.nextFloat() * 2 - 1) * 16);
				double dir_z = TheBlazerEntity.this.getZ() + ((random.nextFloat() * 2 - 1) * 16);
				return new Vec3(dir_x, dir_y, dir_z);
			}
		});
		this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, (float) -0.5));
		this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 20, 10f) {
			@Override
			public boolean canContinueToUse() {
				return this.canUse();
			}
		});
		this.goalSelector.addGoal(6, new TheBlazerEntity.TheBlazerisBlockingGoal(this));
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.BLAZE_AMBIENT;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.NETHERITE_BLOCK_STEP, 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.BLAZE_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.BLAZE_DEATH;
	}

	@Override
	public boolean causeFallDamage(float l, float d, DamageSource source) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (blocked) {
			if (source.getDirectEntity() instanceof Player)
				return false;
		}
		if (source.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (source.is(DamageTypes.EXPLOSION))
			return false;
		if (source.is(DamageTypes.WITHER))
			return false;
		if (source.getMsgId().equals("witherSkull"))
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		TheBlazerEntityDiesProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this, source.getEntity());
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if(!(blazer.getTarget() == null))
			TheBlazerOnEntityTickUpdateProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float flval) {
		NetheriumStaffEntity.shoot(this, target);
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.MAX_HEALTH, 450);
		builder = builder.add(Attributes.ARMOR, 6);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 4);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 0.5);
		builder = builder.add(Attributes.FLYING_SPEED, 0.25);
		return builder;
	}
}
