package net.sashakyotoz.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.sashakyotoz.client.renderers.types.OwnedOutlineRenderType;

public class OwnedOutlineLayer<T extends Entity & TraceableEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final float OUTLINE_THICKNESS = 0.045F;
    private static final double MODEL_ORIGIN_HEIGHT = 1.501D;

    private final int red;
    private final int green;
    private final int blue;

    public OwnedOutlineLayer(RenderLayerParent<T, M> renderer, int color) {
        super(renderer);
        this.red = (color >> 16) & 0xFF;
        this.green = (color >> 8) & 0xFF;
        this.blue = color & 0xFF;
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       T entity,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTick,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {
        if (entity.isInvisible() || entity.getOwner() == null) {
            return;
        }
        VertexConsumer consumer = new FlatColorConsumer(buffer.getBuffer(OwnedOutlineRenderType.ownedOutline(this.getTextureLocation(entity))), this.red, this.green, this.blue);
        double pivot = MODEL_ORIGIN_HEIGHT - entity.getBbHeight() * 0.5D;
        float scale = 1.0F + OUTLINE_THICKNESS / Math.max(0.5F, Math.min(entity.getBbWidth(), entity.getBbHeight()) * 0.5F);

        poseStack.pushPose();
        poseStack.translate(0.0D, pivot, 0.0D);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0D, -pivot, 0.0D);
        this.getParentModel().renderToBuffer(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private record FlatColorConsumer(VertexConsumer delegate, int red, int green, int blue) implements VertexConsumer {
        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            this.delegate.vertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer color(int r, int g, int b, int a) {
            this.delegate.color(this.red, this.green, this.blue, 255);
            return this;
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            this.delegate.uv(u, v);
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        @Override
        public void endVertex() {
            this.delegate.endVertex();
        }

        @Override
        public void defaultColor(int r, int g, int b, int a) {
        }

        @Override
        public void unsetDefaultColor() {
        }
    }
}