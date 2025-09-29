
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.PlayMessages;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;

public class DarkHoglinEntity extends Hoglin implements Enemy, HoglinBase {
	private int attackAnimationRemainingTicks;

	public DarkHoglinEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(UnseenWorldEntities.DARK_HOGLIN.get(), world);
	}

	public DarkHoglinEntity(EntityType<DarkHoglinEntity> type, Level world) {
		super(type, world);
		xpReward = XP_REWARD_MEDIUM;
		setNoAi(false);
	}

	public boolean doHurtTarget(Entity entity) {
		if (!(entity instanceof LivingEntity)) {
			return false;
		} else {
			this.attackAnimationRemainingTicks = 15;
			this.level().broadcastEntityEvent(this, (byte) 4);
			this.playSound(SoundEvents.HOGLIN_ATTACK, 1.0F, this.getVoicePitch());
			return HoglinBase.hurtAndThrowTarget(this, (LivingEntity) entity);
		}
	}

	public void aiStep() {
		if (this.attackAnimationRemainingTicks > 0) {
			--this.attackAnimationRemainingTicks;
		}
		super.aiStep();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true, false));
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.HOGLIN_AMBIENT;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.HOGLIN_STEP, 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.HOGLIN_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.HOGLIN_DEATH;
	}

	public boolean isFood(ItemStack stack) {
		return stack.is(UnseenWorldBlocks.TANZASHROOM.get().asItem());
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.WITHER))
			return false;
		if (source.getMsgId().equals("witherSkull"))
			return false;
		return super.hurt(source, amount);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.MAX_HEALTH, 35);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 6);
		builder = builder.add(Attributes.FOLLOW_RANGE, 24);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2);
		return builder;
	}
}
