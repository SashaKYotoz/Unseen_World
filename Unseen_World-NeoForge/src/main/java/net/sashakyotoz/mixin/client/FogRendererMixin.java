package net.sashakyotoz.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.sashakyotoz.client.environment.ChimericFog;
import net.sashakyotoz.common.world.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FogRenderer.class, priority = 2000)
public abstract class FogRendererMixin {
    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void unseen_world$shapeChimericFog(Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance, boolean thickFog, float partialTick, CallbackInfo ci) {
        if (fogMode != FogRenderer.FogMode.FOG_TERRAIN || !ChimericFog.hasClearView(camera)) {
            return;
        }
        if (!(camera.getEntity().level() instanceof ClientLevel level) || !level.dimension().equals(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY)) {
            return;
        }
        ChimericFog.apply(camera, farPlaneDistance, level.getRainLevel(partialTick), partialTick);
    }
}