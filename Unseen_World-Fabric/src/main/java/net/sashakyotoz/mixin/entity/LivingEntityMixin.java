package net.sashakyotoz.mixin.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.client.environment.weather.ChimericWeatherState;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.config.ModMainConfig;
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
    private static final EntityDataAccessor<Integer> GRIPPING = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> DARKENING = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);

    @Inject(
            method = "defineSynchedData",
            at = @At("TAIL")
    )
    private void onInitDataTracker(CallbackInfo ci) {
        ((LivingEntity) ((Object) this)).getEntityData().define(GRIPPING, 0);
        ((LivingEntity) ((Object) this)).getEntityData().define(DARKENING, 0);
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void onWriteAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("gripping_value", ((LivingEntity) ((Object) this)).getEntityData().get(GRIPPING));
        nbt.putInt("darkening_value", ((LivingEntity) ((Object) this)).getEntityData().get(DARKENING));
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void onReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("gripping_value")) {
            int val = nbt.getInt("gripping_value");
            ((LivingEntity) ((Object) this)).getEntityData().set(GRIPPING, val);
        }
        if (nbt.contains("darkening_value")) {
            int val = nbt.getInt("darkening_value");
            ((LivingEntity) ((Object) this)).getEntityData().set(DARKENING, val);
        }
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) ((Object) this);
        if (livingEntity.tickCount % 20 == 0 && livingEntity instanceof IGrippingEntity entity) {
            Level world = livingEntity.level();

            if (entity.getGrippingData() > 0) {
                if (world.isClientSide() && ModMainConfig.spawnParticlesOfGripping) {
                    float angle = livingEntity.tickCount % 360;
                    world.addParticle(ParticleTypes.DRIPPING_WATER,
                            livingEntity.getX() + (float) Math.sin(angle),
                            livingEntity.getY() + (float) Math.tan(angle),
                            livingEntity.getZ() + (float) Math.cos(angle),
                            0, 0, 0);
                }

                if (livingEntity.isAlive() && !((IEntityDataSaver) livingEntity).getPersistentData().contains("Ordeal"))
                    livingEntity.hurt(livingEntity.damageSources().starve(), 1);

                if (world instanceof ServerLevel serverWorld) {
                    if (ActionsUtils.isEntityInCover(livingEntity, serverWorld) && !((IEntityDataSaver) livingEntity).getPersistentData().contains("Ordeal"))
                        GrippingData.removeGrippingPerTick(entity);
                    else {
                        if (livingEntity instanceof Ocelot ocelot)
                            ocelot.convertTo(ModEntities.SABERPARD, false);
                        else if (livingEntity instanceof Silverfish silverfish)
                            silverfish.convertTo(ModEntities.GLEAMCARVER, false);
                    }
                }
            } else if (world instanceof ServerLevel serverWorld
                    && ChimericWeatherState.get(serverWorld).isGrippfallActive()
                    && !ActionsUtils.isEntityInCover(livingEntity, serverWorld))
                GrippingData.addGrippingSeconds(entity, 2);

            if (entity.getDarkeningData() > 0 && !world.getFluidState(livingEntity.blockPosition()).is(ModFluids.DARK_WATER)) {
                entity.setDarkeningData(entity.getDarkeningData() - 1);
            }
        }
    }

    @Override
    public void setGrippingData(int value) {
        ((LivingEntity) ((Object) this)).getEntityData().set(GRIPPING, value);
    }

    @Override
    public int getGrippingData() {
        return ((LivingEntity) ((Object) this)).getEntityData().get(GRIPPING);
    }

    @Override
    public int getDarkeningData() {
        return ((LivingEntity) ((Object) this)).getEntityData().get(DARKENING);
    }

    @Override
    public void setDarkeningData(int value) {
        ((LivingEntity) ((Object) this)).getEntityData().set(DARKENING, value);
    }

    @Inject(method = "recreateFromPacket", at = @At("TAIL"))
    private void setPartId(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        if (this instanceof MultipartEntity multipartEntity) {
            for (int i = 0; i < multipartEntity.getParts().length; i++) {
                EntityPart part = multipartEntity.getParts()[i];
                part.setId(part.owner.getId() + i);
            }
        }
    }
}