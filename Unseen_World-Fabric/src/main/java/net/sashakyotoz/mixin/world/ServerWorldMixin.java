package net.sashakyotoz.mixin.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.api.multipart_entity.WorldMultipartHelper;
import net.sashakyotoz.client.environment.weather.ChimericWeatherState;
import net.sashakyotoz.common.world.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin implements WorldMultipartHelper {
    @Inject(method = "getEntityOrPart", at = @At("RETURN"), cancellable = true)
    public void getEntityParts(int id, CallbackInfoReturnable<Entity> cir) {
        Entity entity = cir.getReturnValue();
        if (entity == null) cir.setReturnValue(getPMEPartMap().get(id));
    }

    @Shadow
    public abstract ServerLevel getLevel();

    //weather
    @Inject(method = "advanceWeatherCycle", at = @At(value = "TAIL"))
    public void tickWeather(CallbackInfo ci) {
        if (getLevel().dimensionTypeId().equals(ModDimensions.CHIMERIC_DARKNESS_TYPE))
            ChimericWeatherState.get((ServerLevel) (Object) this).tick();
    }

    @Inject(method = "addNewPlayer", at = @At("TAIL"))
    private void onPlayerJoinDimension(ServerPlayer player, CallbackInfo ci) {
        if (this.getLevel().dimension().equals(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY))
            ChimericWeatherState.get((ServerLevel) (Object) this).syncToPlayer(player);
        GripcrystalManaData.addMana((IEntityDataSaver) player, 0);
    }
}