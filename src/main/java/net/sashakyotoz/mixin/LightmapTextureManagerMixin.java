package net.sashakyotoz.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import net.sashakyotoz.utils.JsonWorldController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Inject(method = "getBrightness",at = @At("HEAD"), cancellable = true)
    private static void getBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir){
        if (JsonWorldController.data.get(0) != null && JsonWorldController.data.get(0).galacticUnlock())
            cir.setReturnValue(1.0F);
    }
}