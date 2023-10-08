
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.procedures.VoidStaffProjectileHitsBlockProcedure;
import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.util.UnseenWorldModSounds;
import org.jetbrains.annotations.NotNull;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class VoidStaffEntity extends AbstractArrow implements ItemSupplier {
	public VoidStaffEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(UnseenWorldModEntities.VOID_STAFF.get(), world);
	}

	public VoidStaffEntity(EntityType<? extends VoidStaffEntity> type, Level world) {
		super(type, world);
	}

	public VoidStaffEntity(EntityType<? extends VoidStaffEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return new ItemStack(UnseenWorldModItems.DARKPEARL.get());
	}

	@Override
	protected ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void onHitBlock(@NotNull BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		VoidStaffProjectileHitsBlockProcedure.execute(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ());
	}

	@Override
	public void tick() {
		super.tick();
		execute(this.getX(), this.getY(), this.getZ(), this.getOwner());
		if (this.inGround)
			this.discard();
	}
	public static void execute(double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		UnseenWorldMod.queueServerWork(20, () -> {
			{
				entity.teleportTo(x, y, z);
				if (entity instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(x, y, z, entity.getYRot(), entity.getXRot());
			}
		});
	}

	public static VoidStaffEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		VoidStaffEntity entityarrow = new VoidStaffEntity(UnseenWorldModEntities.VOID_STAFF.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldModSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static VoidStaffEntity shoot(LivingEntity entity, LivingEntity target) {
		VoidStaffEntity entityarrow = new VoidStaffEntity(UnseenWorldModEntities.VOID_STAFF.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1.5f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(4);
		entityarrow.setKnockback(2);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldModSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1,
				1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
