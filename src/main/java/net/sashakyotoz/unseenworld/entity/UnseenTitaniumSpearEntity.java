
package net.sashakyotoz.unseenworld.entity;

import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class UnseenTitaniumSpearEntity  extends AbstractArrow {
	private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(UnseenTitaniumSpearEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(UnseenTitaniumSpearEntity.class, EntityDataSerializers.BOOLEAN);
	private ItemStack spearItem = new ItemStack(UnseenWorldItems.UNSEEN_TITANIUM_SPEAR.get());
	private boolean dealtDamage;
	public int clientSideReturnTridentTickCount;

	public UnseenTitaniumSpearEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(UnseenWorldEntities.UNSEEN_TITANIUM_SPEAR.get(), world);
	}

	public UnseenTitaniumSpearEntity(EntityType<UnseenTitaniumSpearEntity> type, Level world) {
		super(type, world);
	}

	public void tick() {
		if (this.inGroundTime > 4) {
			this.dealtDamage = true;
		}
		Entity entity = this.getOwner();
		int i = this.entityData.get(ID_LOYALTY);
		if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
			if (!this.isAcceptibleReturnOwner()) {
				if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}
				this.discard();
			} else {
				this.setNoPhysics(true);
				Vec3 vec3 = entity.getEyePosition().subtract(this.position());
				this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
				if (this.level().isClientSide) {
					this.yOld = this.getY();
				}
				double d0 = 0.05D * (double) i;
				this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
				if (this.clientSideReturnTridentTickCount == 0) {
					this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
				}
				++this.clientSideReturnTridentTickCount;
			}
		}

		super.tick();
	}

	private boolean isAcceptibleReturnOwner() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayer) || !entity.isSpectator();
		} else {
			return false;
		}
	}

	@Nullable
	protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec31) {
		return this.dealtDamage ? null : super.findHitEntity(vec3, vec31);
	}

	protected void onHitEntity(EntityHitResult p_37573_) {
		Entity entity = p_37573_.getEntity();
		float f = 8.0F;
		if (entity instanceof LivingEntity livingentity) {
			f += EnchantmentHelper.getDamageBonus(this.spearItem, livingentity.getMobType());
		}

		Entity entity1 = this.getOwner();
		DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
		this.dealtDamage = true;
		SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
		if (entity.hurt(damagesource, f)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (entity instanceof LivingEntity livingentity1) {
				if (entity1 instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
					EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
				}

				this.doPostHurtEffects(livingentity1);
			}
		}

		this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
		float f1 = 1.0F;
		if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling()) {
			BlockPos blockpos = entity.blockPosition();
			if (this.level().canSeeSky(blockpos)) {
				LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
				if (lightningbolt != null) {
					lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
					lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
					this.level().addFreshEntity(lightningbolt);
					soundevent = SoundEvents.TRIDENT_THUNDER;
					f1 = 5.0F;
				}
			}
		}

		this.playSound(soundevent, f1, 1.0F);
	}

	public boolean isChanneling() {
		return EnchantmentHelper.hasChanneling(this.spearItem);
	}

	protected boolean tryPickup(Player player) {
		return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
	}

	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.TRIDENT_HIT_GROUND;
	}

	public void playerTouch(Player p_37580_) {
		if (this.ownedBy(p_37580_) || this.getOwner() == null) {
			super.playerTouch(p_37580_);
		}

	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Trident", 10)) {
			this.spearItem = ItemStack.of(tag.getCompound("Trident"));
		}

		this.dealtDamage = tag.getBoolean("DealtDamage");
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.spearItem));
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Trident", this.spearItem.save(new CompoundTag()));
		tag.putBoolean("DealtDamage", this.dealtDamage);
	}

	public void tickDespawn() {
		int i = this.entityData.get(ID_LOYALTY);
		if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
			super.tickDespawn();
		}

	}

	protected float getWaterInertia() {
		return 0.99F;
	}

	public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
		return true;
	}

	public UnseenTitaniumSpearEntity(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
		super(UnseenWorldEntities.UNSEEN_TITANIUM_SPEAR.get(), p_37570_, p_37569_);
		this.spearItem = p_37571_.copy();
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(p_37571_));
		this.entityData.set(ID_FOIL, p_37571_.hasFoil());
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_LOYALTY, (byte) 0);
		this.entityData.define(ID_FOIL, false);
	}

	public static void init() {
	}

	@Override
	protected ItemStack getPickupItem() {
		return this.spearItem.copy();
	}
}