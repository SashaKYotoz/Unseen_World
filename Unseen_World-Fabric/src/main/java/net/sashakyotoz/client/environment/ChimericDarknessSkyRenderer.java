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
                renderStars(context);
                if (data.sunUnlock())
                    renderSkyObject(data, context);
                context.matrixStack().pop();
            }
        }
    }

    private void renderSkyObject(ChimericDarknessData data, WorldRenderContext context) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        float k = 30.0F;
        context.matrixStack().scale(0.5f, 0.5f, 0.5f);
        if (!data.galacticUnlock())
            context.matrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees((ClientTicks.getHalfTicks() / 4) % 36000));
        else
            context.matrixStack().scale(0.25f, 0.25f, 0.25f);
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
            int currentFrame = ClientTicks.getTicks() % 16;
            float frameHeight = 1.0f / 16;
            float minV = currentFrame * frameHeight;
            float maxV = minV + frameHeight;
            float alpha = Math.max(0.25f, Oscillator.getOscillatingValue());
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, -k).texture(0.0F, minV).color(1, 1, 1, alpha).next();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, -k).texture(1.0F, minV).color(1, 1, 1, alpha).next();
            bufferBuilder.vertex(matrix4f2, k, 100.0F, k).texture(1.0F, maxV).color(1, 1, 1, alpha).next();
            bufferBuilder.vertex(matrix4f2, -k, 100.0F, k).texture(0.0F, maxV).color(1, 1, 1, alpha).next();
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        context.matrixStack().scale(1, 1, 1);
    }

    private void renderStars(WorldRenderContext context) {
        RenderSystem.setShaderColor(1f, 0.25f, Oscillator.getOscillatingValue(), 1f);
        context.matrixStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees((ClientTicks.getHalfTicks() / 16) % 360));
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