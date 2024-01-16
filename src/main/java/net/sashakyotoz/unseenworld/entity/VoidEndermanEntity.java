
package net.sashakyotoz.unseenworld.entity;

import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;

public class VoidEndermanEntity extends EnderMan {
	private int attackAnimationRemainingTicks;

	public VoidEndermanEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(UnseenWorldModEntities.VOID_ENDERMEN.get(), world);
	}

	public VoidEndermanEntity(EntityType<VoidEndermanEntity> type, Level world) {
		super(type, world);
		xpReward = 6;
		setNoAi(false);
	}

	static boolean hurtAndThrowTarget(LivingEntity living, LivingEntity p_34644_) {
		float f1 = (float) living.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float f;
		if (!living.isBaby() && (int) f1 > 0) {
			f = f1 / 2.0F + (float) living.level().random.nextInt((int) f1);
		} else {
			f = f1;
		}
		boolean flag = p_34644_.hurt(living.damageSources().mobAttack(living), f);
		if (flag) {
			living.doEnchantDamageEffects(living, p_34644_);
		}
		return flag;
	}

	public boolean doHurtTarget(Entity entity) {
		if (!(entity instanceof LivingEntity)) {
			return false;
		} else {
			this.attackAnimationRemainingTicks = 100;
			this.level().broadcastEntityEvent(this, (byte) 4);
			this.playSound(SoundEvents.ENDERMAN_HURT, 1.0F, this.getVoicePitch());
			return VoidEndermanEntity.hurtAndThrowTarget(this, (LivingEntity) entity);
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
	}

	public int getAttackAnimationRemainingTicks() {
		return this.attackAnimationRemainingTicks;
	}

	public void aiStep() {
		if (this.attackAnimationRemainingTicks > 0) {
			--this.attackAnimationRemainingTicks;
		}
		super.aiStep();
	}

	public void handleEntityEvent(byte p_34496_) {
		if (p_34496_ == 4) {
			this.attackAnimationRemainingTicks = 100;
			this.playSound(SoundEvents.ENDERMAN_HURT, 1.0F, this.getVoicePitch());
		} else {
			super.handleEntityEvent(p_34496_);
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.VEX_AMBIENT;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.ENDERMAN_STARE, 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.ENDERMITE_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.ENDERMAN_DEATH;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.CACTUS))
			return false;
		if (source.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (source.is(DamageTypes.EXPLOSION))
			return false;
		return super.hurt(source, amount);
	}

	public static void init() {
		SpawnPlacements.register(UnseenWorldModEntities.VOID_ENDERMEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 40);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 8);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		return builder;
	}
}
