package net.sashakyotoz.client.renderers.layers.bosses;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;

public class EclipseSentinelGlowingLayer<T extends EclipseSentinelEntity, M extends SinglePartEntityModel<T>> extends FeatureRenderer<T, M> {

    public EclipseSentinelGlowingLayer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EclipseSentinelEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(
                entity.isExalted()
                        ? UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_glowing_parts_exalted.png")
                        : UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_glowing_parts.png")
        ));
        this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}