package net.sashakyotoz.mixin.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Silverfish;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.client.environment.weather.ChimericWeatherState;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.effects.ModMobEffects;
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
    @Unique
    private static final int MAX_DARKENING = 10;
    @Unique
    private static final int GRIPPFALL_SECONDS = 2;

    @Unique
    private LivingEntity unseen_world$self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void unseen_world$defineGrippingData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(GRIPPING, 0);
        builder.define(DARKENING, 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void unseen_world$writeGrippingData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("gripping_value", this.getGrippingData());
        nbt.putInt("darkening_value", this.getDarkeningData());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void unseen_world$readGrippingData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("gripping_value")) {
            this.setGrippingData(nbt.getInt("gripping_value"));
        }
        if (nbt.contains("darkening_value")) {
            this.setDarkeningData(nbt.getInt("darkening_value"));
        }
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void unseen_world$tickGripping(CallbackInfo ci) {
        LivingEntity living = this.unseen_world$self();
        if (living.tickCount % 20 != 0 || !(living.level() instanceof ServerLevel level)) {
            return;
        }
        boolean ordealMob = living.getTags().contains(OrdealSpawnerBlockEntity.ORDEAL_TAG);
        if (this.getGrippingData() > 0) {
            if (!ordealMob) {
                if (living.isAlive() && !living.hasEffect(ModMobEffects.GRIPPING_RESISTANCE.getAsHolder())) {
                    living.hurt(living.damageSources().starve(), 1.0F);
                }
                if (ActionsUtils.isEntityInCover(living, level)) {
                    GrippingData.removeGrippingPerTick(this);
                } else {
                    this.unseen_world$convertGripped(living);
                }
            }
        } else if (!ordealMob
                && GrippingData.canBeGripped(this)
                && ChimericWeatherState.get(level).isGrippfallActive()
                && !ActionsUtils.isEntityInCover(living, level)) {
            GrippingData.addGrippingSeconds(this, GRIPPFALL_SECONDS);
        }
        if (this.getDarkeningData() > 0 && !level.getFluidState(living.blockPosition()).is(ModFluids.DARK_WATER)) {
            this.setDarkeningData(this.getDarkeningData() - 1);
        }
    }

    @Unique
    private void unseen_world$convertGripped(LivingEntity living) {
        if (living instanceof Ocelot ocelot) {
            ocelot.convertTo(ModEntities.SABERPARD, false);
        } else if (living instanceof Silverfish silverfish) {
            silverfish.convertTo(ModEntities.GLEAMCARVER, false);
        }
    }

    @Override
    public void setGrippingData(int value) {
        this.unseen_world$self().getEntityData().set(GRIPPING, Mth.clamp(value, 0, GrippingData.MAX_GRIPPING));
    }

    @Override
    public int getGrippingData() {
        return this.unseen_world$self().getEntityData().get(GRIPPING);
    }

    @Override
    public int getDarkeningData() {
        return this.unseen_world$self().getEntityData().get(DARKENING);
    }

    @Override
    public void setDarkeningData(int value) {
        this.unseen_world$self().getEntityData().set(DARKENING, Mth.clamp(value, 0, MAX_DARKENING));
    }

    @Inject(method = "recreateFromPacket", at = @At("TAIL"))
    private void unseen_world$setPartIds(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        if (this instanceof MultipartEntity multipartEntity) {
            for (int i = 0; i < multipartEntity.getMultipartParts().length; i++) {
                EntityPart part = multipartEntity.getMultipartParts()[i];
                part.setId(part.owner.getId() + i);
            }
        }
    }
}