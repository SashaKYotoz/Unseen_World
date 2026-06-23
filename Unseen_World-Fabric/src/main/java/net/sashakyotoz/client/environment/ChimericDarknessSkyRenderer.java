package net.sashakyotoz.client.environment;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.config.ChimericDarknessData;
import net.sashakyotoz.common.config.WorldConfigController;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.utils.Oscillator;
import org.joml.Matrix4f;

public class ChimericDarknessSkyRenderer implements DimensionRenderingRegistry.SkyRenderer {
    public static final Identifier STAR = UnseenWorld.makeID("textures/environment/remains_of_star.png");
    public static final Identifier GALAXY = UnseenWorld.makeID("textures/environment/dark_galactic_animated.png");

    @Override
    public void render(WorldRenderContext context) {
        if (context.world().getRegistryKey().equals(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY)) {
            ChimericDarknessData data = WorldConfigController.data.get(0);
            if (data != null) {
                context.matrixStack().push();
                context.matrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees((ClientTicks.getHalfTicks() / 32) % 36000));
                renderStars(context);
                if (data.sunUnlock())
                    renderSkyObject(data, context);
                context.matrixStack().pop();
            }
        }
    }

    private void renderSkyObject(ChimericDarknessData data, WorldRenderContext context) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        float k = 10.0F;
        Matrix4f matrix4f2 = context.matrixStack().peek().getPositionMatrix();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        if (!data.galacticUnlock()) {
            RenderSystem.setShaderTexture(0, STAR);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, -k).texture(0.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, -k).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, k).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, k).texture(0.0F, 1.0F).next();
        } else {
            RenderSystem.setShaderTexture(0, GALAXY);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            int currentFrame = ClientTicks.getTicks() % 32;
            float frameHeight = 1.0f / 16;
            float minV = currentFrame * frameHeight;
            float maxV = minV + frameHeight;
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, -k).texture(0.0F, minV).next();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, -k).texture(1.0F, minV).next();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, k).texture(1.0F, maxV).next();
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, k).texture(0.0F, maxV).next();
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }


    private void renderStars(WorldRenderContext context) {
        RenderSystem.setShaderColor(1f, 0.25f, Oscillator.getOscillatingValue(), 1f);
        BackgroundRenderer.clearFog();
        VertexBuffer starsBuffer = context.worldRenderer().starsBuffer;
        if (starsBuffer != null) {
            starsBuffer.bind();
            starsBuffer.draw(context.matrixStack().peek().getPositionMatrix(), context.projectionMatrix(), GameRenderer.getPositionProgram());
        }
        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }
}