package net.sashakyotoz.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.client.environment.weather.ChimericWeatherState;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.utils.ActionsUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements IGrippingEntity {

    @Unique
    private static final TrackedData<Integer> GRIPPING = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> DARKENING = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(
            method = "initDataTracker",
            at = @At("TAIL")
    )
    private void onInitDataTracker(CallbackInfo ci) {
        ((LivingEntity) ((Object) this)).getDataTracker().startTracking(GRIPPING, 0);
        ((LivingEntity) ((Object) this)).getDataTracker().startTracking(DARKENING, 0);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    private void onWriteAdditionalSaveData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("gripping_value", ((LivingEntity) ((Object) this)).getDataTracker().get(GRIPPING));
        nbt.putInt("darkening_value", ((LivingEntity) ((Object) this)).getDataTracker().get(DARKENING));
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
        if (nbt.contains("darkening_value")) {
            int val = nbt.getInt("darkening_value");
            ((LivingEntity) ((Object) this)).getDataTracker().set(DARKENING, val);
        }
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) ((Object) this);
        if (livingEntity.age % 20 == 0 && livingEntity instanceof IGrippingEntity entity) {
            World world = livingEntity.getWorld();

            if (entity.getGrippingData() > 0) {
                if (world.isClient() && ConfigEntries.spawnParticlesOfGripping) {
                    float angle = livingEntity.age % 360;
                    world.addParticle(ParticleTypes.DRIPPING_WATER,
                            livingEntity.getX() + (float) Math.sin(angle),
                            livingEntity.getY() + (float) Math.tan(angle),
                            livingEntity.getZ() + (float) Math.cos(angle),
                            0, 0, 0);
                }

                if (livingEntity.isAlive() && !((IEntityDataSaver) livingEntity).getPersistentData().contains("Ordeal"))
                    livingEntity.damage(livingEntity.getDamageSources().starve(), 1);

                if (world instanceof ServerWorld serverWorld) {
                    if (ActionsUtils.isEntityInCover(livingEntity, serverWorld) && !((IEntityDataSaver) livingEntity).getPersistentData().contains("Ordeal"))
                        GrippingData.removeGrippingPerTick(entity);
                    else {
                        if (livingEntity instanceof OcelotEntity ocelot)
                            ocelot.convertTo(ModEntities.SABERPARD, false);
                        else if (livingEntity instanceof SilverfishEntity silverfish)
                            silverfish.convertTo(ModEntities.GLEAMCARVER, false);
                    }
                }
            } else if (world instanceof ServerWorld serverWorld
                    && ChimericWeatherState.get(serverWorld).isGrippfallActive()
                    && !ActionsUtils.isEntityInCover(livingEntity, serverWorld))
                GrippingData.addGrippingSeconds(entity, 2);

            if (entity.getDarkeningData() > 0 && !world.getFluidState(livingEntity.getBlockPos()).isOf(ModFluids.DARK_WATER)) {
                entity.setDarkeningData(entity.getDarkeningData() - 1);
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

    @Override
    public int getDarkeningData() {
        return ((LivingEntity) ((Object) this)).getDataTracker().get(DARKENING);
    }

    @Override
    public void setDarkeningData(int value) {
        ((LivingEntity) ((Object) this)).getDataTracker().set(DARKENING, value);
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