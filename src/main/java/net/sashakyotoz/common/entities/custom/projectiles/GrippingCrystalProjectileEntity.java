package net.sashakyotoz.common.entities.custom.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.ActionsManager;
import net.sashakyotoz.utils.IEntityDataSaver;

import java.util.List;

public class GrippingCrystalProjectileEntity extends PersistentProjectileEntity {

    public GrippingCrystalProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        UnseenWorld.log("Gripping crystal created");
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround && this.age % 5 == 0 && this.getWorld() instanceof ServerWorld world)
            world.spawnParticles(ModParticleTypes.GRIPPING_CRYSTAL,
                    this.getX(), this.getY(), this.getZ(), 5,
                    ActionsManager.getXVector(1, this.getYaw()),
                    ActionsManager.getYVector(-1, this.getPitch()),
                    ActionsManager.getZVector(1, this.getYaw()), 1);
        if (this.inGround && this.age % 30 == 0) {
            this.playSound(SoundEvents.ENTITY_ALLAY_DEATH, 1.5f, 2);
            this.discard();
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        if (target instanceof ServerPlayerEntity player)
            GrippingData.addGrippingSeconds((IEntityDataSaver) player, 8);
        else
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (this.getWorld() instanceof ServerWorld world)
            ActionsManager.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL,
                    world, this.getX(), this.getY() + 1.5, this.getZ(), 2);
        List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(5), LivingEntity::canHit);
        for (LivingEntity entity : entities) {
            if (this.getOwner() != null && entity != this.getOwner())
                entity.damage(this.getOwner().getDamageSources().indirectMagic(this, this.getOwner()), 5);
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME;
    }
}