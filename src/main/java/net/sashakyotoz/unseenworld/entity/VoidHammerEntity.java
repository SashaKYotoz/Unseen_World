package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class VoidHammerEntity extends AbstractArrow {
    private int timer;
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(VoidHammerEntity.class, EntityDataSerializers.BYTE);
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public VoidHammerEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(UnseenWorldModEntities.VOID_HAMMER.get(), world);
        timer = 150;
    }

    public VoidHammerEntity(EntityType<VoidHammerEntity> type, Level world) {
        super(type, world);
        timer = 150;
    }

    public void tick() {
        if (timer > 0)
            timer--;
        else
            this.discard();
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
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
                    this.playSound(SoundEvents.ANVIL_HIT, 10.0F, 1.0F);
                }
                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        final Vec3 center = new Vec3(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ());
        List<Entity> entities = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(entcnd -> entcnd.distanceToSqr(center))).toList();
        for (Entity entityiterator : entities) {
            if (entityiterator != null && !(entityiterator instanceof DarkGolemEntity)) {
                entityiterator.hurt(entityiterator.damageSources().lightningBolt(), 10);
            }
        }
        if(this.level() instanceof ServerLevel level)
            level.sendParticles(ParticleTypes.ASH,result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), 32, 2, 3, 2, 1);
        super.onHitBlock(result);
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return entity instanceof DarkGolemEntity || entity instanceof Player;
        } else {
            return false;
        }
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec31) {
        return this.dealtDamage ? null : super.findHitEntity(vec3, vec31);
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float f = 15f;
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN)
                return;
            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }
                this.doPostHurtEffects(livingentity1);
            }
        }
        if(entity instanceof DarkGolemEntity)
            this.discard();
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ANVIL_HIT;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }
    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    protected float getWaterInertia() {
        return 0.95F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }

    public VoidHammerEntity(Level level, LivingEntity livingEntity) {
        super(UnseenWorldModEntities.VOID_HAMMER.get(), livingEntity, level);
        this.entityData.set(ID_LOYALTY, (byte) 2);
        timer = 160;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
    }
}
