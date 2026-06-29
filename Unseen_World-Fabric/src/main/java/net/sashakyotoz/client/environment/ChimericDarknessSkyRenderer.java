package net.sashakyotoz.client.environment;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.config.ChimericDarknessData;
import net.sashakyotoz.common.config.WorldConfigController;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.utils.Oscillator;
import org.joml.Matrix4f;

public class ChimericDarknessSkyRenderer implements DimensionRenderingRegistry.SkyRenderer {
    public static final ResourceLocation STAR = UnseenWorld.makeID("textures/environment/remains_of_star.png");
    public static final ResourceLocation GALAXY = UnseenWorld.makeID("textures/environment/dark_galactic_animated.png");

    @Override
    public void render(WorldRenderContext context) {
        if (context.world().dimension().equals(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY)) {
            ChimericDarknessData data = WorldConfigController.data.get(0);
            if (data != null) {
                context.matrixStack().pushPose();
                context.matrixStack().mulPose(Axis.ZP.rotationDegrees((ClientTicks.getHalfTicks() / 32) % 36000));
                renderStars(context);
                if (data.sunUnlock())
                    renderSkyObject(data, context);
                context.matrixStack().popPose();
            }
        }
    }

    private void renderSkyObject(ChimericDarknessData data, WorldRenderContext context) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        float k = 10.0F;
        Matrix4f matrix4f2 = context.matrixStack().last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        if (!data.galacticUnlock()) {
            RenderSystem.setShaderTexture(0, STAR);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, -k).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, -k).uv(1.0F, 0.0F).endVertex();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, k).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, k).uv(0.0F, 1.0F).endVertex();
        } else {
            RenderSystem.setShaderTexture(0, GALAXY);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            int currentFrame = ClientTicks.getTicks() % 32;
            float frameHeight = 1.0f / 16;
            float minV = currentFrame * frameHeight;
            float maxV = minV + frameHeight;
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, -k).uv(0.0F, minV).endVertex();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, -k).uv(1.0F, minV).endVertex();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, k).uv(1.0F, maxV).endVertex();
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, k).uv(0.0F, maxV).endVertex();
        }
        BufferUploader.drawWithShader(bufferBuilder.end());
    }


    private void renderStars(WorldRenderContext context) {
        RenderSystem.setShaderColor(1f, 0.25f, Oscillator.getOscillatingValue(), 1f);
        FogRenderer.setupNoFog();
        VertexBuffer starsBuffer = context.worldRenderer().starBuffer;
        if (starsBuffer != null) {
            starsBuffer.bind();
            starsBuffer.drawWithShader(context.matrixStack().last().pose(), context.projectionMatrix(), GameRenderer.getPositionShader());
        }
        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }
}