package net.sashakyotoz.common.entities.custom.projectiles;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.List;

public class GrippingCrystalProjectileEntity extends AbstractArrow {

    public GrippingCrystalProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround && this.tickCount % 5 == 0 && this.level() instanceof ServerLevel world)
            world.sendParticles(ModParticleTypes.GRIPPING_CRYSTAL,
                    this.getX(), this.getY(), this.getZ(), 5,
                    ActionsUtils.getXVector(1, this.getYRot()),
                    ActionsUtils.getYVector(-1, this.getXRot()),
                    ActionsUtils.getZVector(1, this.getYRot()), 1);
        if (this.inGround && this.tickCount % 30 == 0) {
            this.playSound(SoundEvents.ALLAY_DEATH, 1.5f, 2);
            this.discard();
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        if (target instanceof IGrippingEntity entity)
            GrippingData.addGrippingSeconds(entity, 8);
        else
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL,
                this.level(), this.getX(), this.getY() + 1.5, this.getZ(), 2);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5), LivingEntity::isPickable);
        for (LivingEntity entity : entities) {
            if (this.getOwner() != null && entity != this.getOwner())
                entity.hurt(this.getOwner().damageSources().indirectMagic(this, this.getOwner()), 5);
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_BLOCK_CHIME;
    }
}