package net.sashakyotoz.client.environment;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.utils.ChimericDarknessData;
import net.sashakyotoz.utils.JsonWorldController;
import org.joml.Matrix4f;

public class WorldRendererEventsHandler implements WorldRenderEvents.AfterTranslucent {
    private int macroObjectsRotation;

    @Override
    public void afterTranslucent(WorldRenderContext context) {
        if (context.world().getRegistryKey().equals(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY)) {
            float i = 1.0F - context.world().getRainGradient(context.tickDelta());
            float k = 30.0F;
            ChimericDarknessData data = JsonWorldController.data.get(0);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, i);
            context.matrixStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
            context.matrixStack().push();
            macroObjectsRotation = macroObjectsRotation >= 3600 ? 0 : macroObjectsRotation + 1;
            for (AbstractClientPlayerEntity player : context.world().getPlayers()) {
                context.matrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees((player.age % 36000) * 0.02f));
            }
            Matrix4f matrix4f2 = context.matrixStack().peek().getPositionMatrix();
            if (data != null && data.sunUnlock()) {
                context.matrixStack().scale(0.5f, 0.5f, 0.5f);
                BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderTexture(0, UnseenWorld.makeID("textures/environment/remains_of_star.png"));
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                bufferBuilder.vertex(matrix4f2, -k, 100.0F, -k).texture(0.0F, 0.0F).next();
                bufferBuilder.vertex(matrix4f2, k, 100.0F, -k).texture(1.0F, 0.0F).next();
                bufferBuilder.vertex(matrix4f2, k, 100.0F, k).texture(1.0F, 1.0F).next();
                bufferBuilder.vertex(matrix4f2, -k, 100.0F, k).texture(0.0F, 1.0F).next();
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                context.matrixStack().scale(1, 1, 1);
            }
            if (data != null && data.starsUnlock()) {
                RenderSystem.setShaderColor(1f, 0.25f, macroObjectsRotation * 0.01f, 0.75f);
                BackgroundRenderer.clearFog();
                if (context.worldRenderer().starsBuffer != null) {
                    context.worldRenderer().starsBuffer.bind();
                    context.worldRenderer().starsBuffer.draw(context.matrixStack().peek().getPositionMatrix(), context.projectionMatrix(), GameRenderer.getPositionProgram());
                }
                VertexBuffer.unbind();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            }
            context.matrixStack().pop();
//            context.matrixStack().push();
//            context.matrixStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
//            context.matrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(45));
//            context.matrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
//            Matrix4f matrix4f3 = context.matrixStack().peek().getPositionMatrix();
//            if (data != null && data.galacticUnlock()) {
//                BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
//                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//                RenderSystem.setShaderColor(1, 1, 1, 0.25f);
//                context.matrixStack().scale(0.0225f, 0.0225f, 0.0225f);
//                RenderSystem.setShaderTexture(0, UnseenWorld.makeID("textures/environment/dark_galactic_animated.png"));
//                int totalFrames = 8;
//                int currentFrame = 0;
//                for (AbstractClientPlayerEntity player : context.world().getPlayers()) {
//                    currentFrame = (player.age / 2) % totalFrames;
//                }
//                float frameHeight = 1.0f / totalFrames;
//                float minV = currentFrame * frameHeight;
//                float maxV = minV + frameHeight;
//
//                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//                bufferBuilder.vertex(matrix4f3, -k, 100.0F, -k).texture(0.0F, minV).next();
//                bufferBuilder.vertex(matrix4f3, k, 100.0F, -k).texture(1.0F, minV).next();
//                bufferBuilder.vertex(matrix4f3, k, 100.0F, k).texture(1.0F, maxV).next();
//                bufferBuilder.vertex(matrix4f3, -k, 100.0F, k).texture(0.0F, maxV).next();
//                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
//            }
//            context.matrixStack().pop();
        }
    }
}