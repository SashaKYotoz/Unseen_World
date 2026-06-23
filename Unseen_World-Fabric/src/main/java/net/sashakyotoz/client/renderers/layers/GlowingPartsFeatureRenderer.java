package net.sashakyotoz.client.renderers.layers;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class GlowingPartsFeatureRenderer<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final Identifier texture;

    public GlowingPartsFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, Identifier texture) {
        super(featureRendererContext);
        this.texture = texture;
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            T entity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEyes(texture));
        this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
