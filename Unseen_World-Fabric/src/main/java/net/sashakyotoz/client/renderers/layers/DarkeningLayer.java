package net.sashakyotoz.client.renderers.layers;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.config.ConfigEntries;
import org.apache.commons.lang3.tuple.Triple;

public class DarkeningLayer<T extends LivingEntity, M extends EntityModel<T>> extends StuckObjectsRenderer<T, M> {
    private final Identifier TEXTURE = UnseenWorld.makeID("textures/entity/layers/darkening_layer.png");

    public DarkeningLayer(LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
    }

    @Override
    protected int getObjectCount(T entity) {
        if (!ConfigEntries.renderDarkeningOnMobs)
            return 0;
        if (entity instanceof IGrippingEntity entity1 && entity1.getDarkeningData() > 0)
            return Math.round(entity1.getDarkeningData() / 1.5f);
        return 0;
    }

    @Override
    protected void renderModelPartOverlay(ModelPart part, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity) {
        if (entity instanceof IGrippingEntity iGrippingEntity)
            part.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, iGrippingEntity.getDarkeningData() / 10f);
    }

    @Override
    protected Triple<Float, Float, Float> getSizedPart() {
        return Triple.of(8.0f, 8.0f, 8.0f);
    }

    @Override
    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity) {
    }
}