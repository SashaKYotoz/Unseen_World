package net.sashakyotoz.mixin.client;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.config.WorldConfigController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void getBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
        if (type.effects().equals(UnseenWorld.makeID("the_chimeric_darkness")) && WorldConfigController.data.get(0) != null && WorldConfigController.data.get(0).galacticUnlock())
            cir.setReturnValue(1.0F);
    }
}