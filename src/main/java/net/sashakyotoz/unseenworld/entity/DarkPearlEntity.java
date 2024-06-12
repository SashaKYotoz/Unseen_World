
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

import net.sashakyotoz.unseenworld.managers.DarkPearlHitsLivingEntityProcedure;
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class DarkPearlEntity extends AbstractArrow implements ItemSupplier {

	public DarkPearlEntity(EntityType<? extends DarkPearlEntity> type, Level world) {
		super(type, world);
	}

	public DarkPearlEntity(EntityType<? extends DarkPearlEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(UnseenWorldModItems.DARK_PEARL.get());
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void playerTouch(Player entity) {
		super.playerTouch(entity);
		DarkPearlHitsLivingEntityProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), entity);
	}

	@Override
	public void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		DarkPearlHitsLivingEntityProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), entityHitResult.getEntity());
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		DarkPearlHitsLivingEntityProcedure.execute(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), this.getOwner());
	}

	@Override
	public void tick() {
		super.tick();
		if (this.inGround)
			this.discard();
	}

	public static DarkPearlEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		DarkPearlEntity pearl = new DarkPearlEntity(UnseenWorldModEntities.DARK_PEARL.get(), entity, world);
		pearl.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		pearl.setSilent(true);
		pearl.setCritArrow(false);
		pearl.setBaseDamage(damage);
		pearl.setKnockback(knockback);
		world.addFreshEntity(pearl);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return pearl;
	}

	public static DarkPearlEntity shoot(LivingEntity entity, LivingEntity target) {
		DarkPearlEntity pearl = new DarkPearlEntity(UnseenWorldModEntities.DARK_PEARL.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		pearl.shoot(dx, dy - pearl.getY() + Math.hypot(dx, dz) * 0.2F, dz, 2f * 2, 12.0F);
		pearl.setSilent(true);
		pearl.setCritArrow(false);
		entity.level().addFreshEntity(pearl);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return pearl;
	}
	@Override
	public ItemStack getItem() {
		return UnseenWorldModItems.DARK_PEARL.get().getDefaultInstance();
	}
}
