
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModMobEffects;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModParticleTypes;

public class VoidArrowEntity extends AbstractArrow {
	private final ItemStack projectile;

	public VoidArrowEntity(EntityType<? extends VoidArrowEntity> type, Level world) {
		super(type, world);
		projectile = Items.ARROW.getDefaultInstance();
	}

	public VoidArrowEntity(EntityType<? extends VoidArrowEntity> type, LivingEntity entity, Level world,ItemStack stack) {
		super(type, entity, world);
		projectile = stack;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected ItemStack getPickupItem() {
		return projectile;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		Entity entity = entityHitResult.getEntity();
		Entity sourceEntity = this.getOwner();
		if (entity != sourceEntity && entity instanceof LivingEntity livingEntity && !this.level().isClientSide()){
			livingEntity.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.DARK_VOID.get(), 40, 1));
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.level().addParticle(ParticleTypes.ENCHANT, this.getX(), this.getY(), this.getZ(), 0, 1, 0);
		if (this.level() instanceof ServerLevel level && this.tickCount % 5 == 0)
			level.sendParticles(UnseenWorldModParticleTypes.BLUE_VOID_PARTICLE.get(),this.getX(), this.getY(), this.getZ(), 9, 3, 3, 3, 1);
		if (this.inGround && projectile.isEmpty())
			this.discard();
	}

	public static VoidArrowEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback,ItemStack arrow) {
		VoidArrowEntity voidArrow = new VoidArrowEntity(UnseenWorldModEntities.VOID_BOW.get(), entity, world,arrow);
		voidArrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		voidArrow.setSilent(true);
		voidArrow.setCritArrow(false);
		voidArrow.setBaseDamage(damage);
		voidArrow.setKnockback(knockback);
		world.addFreshEntity(voidArrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return voidArrow;
	}

	public static VoidArrowEntity shoot(LivingEntity entity, LivingEntity target) {
		VoidArrowEntity voidArrow = new VoidArrowEntity(UnseenWorldModEntities.VOID_BOW.get(), entity, entity.level(),ItemStack.EMPTY);
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		voidArrow.shoot(dx, dy - voidArrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1.5f * 2, 12.0F);
		voidArrow.setSilent(true);
		voidArrow.setBaseDamage(4);
		voidArrow.setKnockback(2);
		voidArrow.setCritArrow(false);
		entity.level().addFreshEntity(voidArrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return voidArrow;
	}
}
