package net.sashakyotoz.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.entities.ModEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements IGrippingEntity {

    @Unique
    private static final TrackedData<Integer> GRIPPING = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(
            method = "initDataTracker",
            at = @At("TAIL")
    )
    private void onInitDataTracker(CallbackInfo ci) {
        ((LivingEntity) ((Object) this)).getDataTracker().startTracking(GRIPPING, 0);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    private void onWriteAdditionalSaveData(NbtCompound nbt, CallbackInfo ci) {
        int val = ((LivingEntity) ((Object) this)).getDataTracker().get(GRIPPING);
        nbt.putInt("gripping_value", val);
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    private void onReadAdditionalSaveData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("gripping_value")) {
            int val = nbt.getInt("gripping_value");
            ((LivingEntity) ((Object) this)).getDataTracker().set(GRIPPING, val);
        }
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) ((Object) this);
        if (!(livingEntity instanceof PlayerEntity) && livingEntity instanceof IGrippingEntity entity && entity.getGrippingData() > 0 && livingEntity.age % 5 == 0) {
            GrippingData.removeGrippingPerTick(entity);
            if (livingEntity instanceof OcelotEntity entity1)
                entity1.convertTo(ModEntities.SABERPARD, false);
            if (livingEntity instanceof SilverfishEntity entity1)
                entity1.convertTo(ModEntities.GLEAMCARVER, false);
            if (livingEntity instanceof IllagerEntity entity1)
                entity1.convertTo(ModEntities.VIOLEGER, false);
            if (livingEntity.getWorld().isClient() && ConfigEntries.spawnParticlesOfGripping) {
                float sin = (float) Math.sin(livingEntity.age % 360);
                float cos = (float) Math.cos(livingEntity.age % 360);
                float tan = (float) Math.tan(livingEntity.age % 360);
                livingEntity.getWorld().addParticle(ParticleTypes.DRIPPING_WATER, livingEntity.getX() + sin, livingEntity.getY() + tan, livingEntity.getZ() + cos, 0, 0, 0);
            }
        }
    }

    @Override
    public void setGrippingData(int value) {
        ((LivingEntity) ((Object) this)).getDataTracker().set(GRIPPING, value);
    }

    @Override
    public int getGrippingData() {
        return ((LivingEntity) ((Object) this)).getDataTracker().get(GRIPPING);
    }

    @Inject(method = "onSpawnPacket", at = @At("TAIL"))
    private void setPartId(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        if (this instanceof MultipartEntity multipartEntity) {
            for (int i = 0; i < multipartEntity.getParts().length; i++) {
                EntityPart part = multipartEntity.getParts()[i];
                part.setId(part.owner.getId() + i);
            }
        }
    }
}