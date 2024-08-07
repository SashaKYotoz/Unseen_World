
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.sashakyotoz.unseenworld.managers.FireLikeStaffProjectileFlyingTick;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;
import net.sashakyotoz.unseenworld.registries.UnseenWorldSounds;
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class NetheriumStaffEntity extends AbstractArrow implements ItemSupplier {

	public NetheriumStaffEntity(EntityType<? extends NetheriumStaffEntity> type, Level world) {
		super(UnseenWorldEntities.NETHERIUM_STAFF.get(), world);
	}

	public NetheriumStaffEntity(EntityType<? extends NetheriumStaffEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}


	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(UnseenWorldItems.FIRE_PEARL.get());
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		if (!this.level().isClientSide())
			this.level().explode(null, blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), 1, Level.ExplosionInteraction.BLOCK);
		this.level().addParticle(UnseenWorldParticleTypes.REDNESS.get(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), 0, 1, 0);
	}

	@Override
	public void tick() {
		super.tick();
		FireLikeStaffProjectileFlyingTick.onTickParticles(ParticleTypes.FALLING_LAVA,this.level(), this.getX(), this.getY(), this.getZ(),1,1,1);
		if (this.inGround)
			this.discard();
	}

	public static NetheriumStaffEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		NetheriumStaffEntity entityarrow = new NetheriumStaffEntity(UnseenWorldEntities.NETHERIUM_STAFF.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		entityarrow.setSecondsOnFire(100);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static NetheriumStaffEntity shoot(LivingEntity entity, LivingEntity target) {
		NetheriumStaffEntity entityarrow = new NetheriumStaffEntity(UnseenWorldEntities.NETHERIUM_STAFF.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 3f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(2);
		entityarrow.setKnockback(2);
		entityarrow.setCritArrow(false);
		entityarrow.setSecondsOnFire(100);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), UnseenWorldSounds.ITEM_STAFF_SHOT, SoundSource.PLAYERS, 1,
				1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}

	@Override
	public ItemStack getItem() {
		return UnseenWorldItems.FIRE_PEARL.get().getDefaultInstance();
	}
}
