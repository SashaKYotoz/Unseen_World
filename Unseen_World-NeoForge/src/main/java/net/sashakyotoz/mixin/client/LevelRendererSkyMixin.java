package net.sashakyotoz.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.ChimericDarknessEffects;
import net.sashakyotoz.client.environment.ChimericDarknessSkyRenderer;
import net.sashakyotoz.client.environment.ChimericFog;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.compat.iris.IrisCompat;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererSkyMixin {
    @Unique
    private static boolean unseen_world$effectsChecked;

    @Shadow
    @Nullable
    private ClientLevel level;

    @WrapWithCondition(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V"))
    private boolean unseen_world$replaceSky(LevelRenderer renderer, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean thickFog, Runnable skyFogSetup) {
        ClientLevel clientLevel = this.level;
        if (clientLevel == null || !clientLevel.dimension().equals(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY)) {
            return true;
        }
        if (!unseen_world$effectsChecked) {
            unseen_world$effectsChecked = true;
            if (!(clientLevel.effects() instanceof ChimericDarknessEffects)) {
                UnseenWorld.log().warn("Dimension {} uses effects {} instead of ChimericDarknessEffects; check the effects id in its dimension_type", clientLevel.dimension().location(), clientLevel.dimensionType().effectsLocation());
            }
        }
        if (!ChimericFog.isBossFog() && ChimericFog.hasClearView(camera)) {
            boolean shaderPack = IrisCompat.isShaderPipelineActive();
            if (shaderPack) {
                IrisCompat.beginCustomSky();
            }
            try {
                PoseStack poseStack = new PoseStack();
                poseStack.mulPose(frustumMatrix);
                ChimericDarknessSkyRenderer.INSTANCE.render(clientLevel, poseStack, projectionMatrix, partialTick);
            } finally {
                if (shaderPack) {
                    IrisCompat.endCustomSky();
                }
            }
        }
        return false;
    }
}