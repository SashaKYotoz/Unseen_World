package net.sashakyotoz.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.config.ModMainConfig;
import org.apache.commons.lang3.tuple.Triple;

public class DarkeningLayer<T extends LivingEntity, M extends EntityModel<T>> extends StuckObjectsRenderer<T, M> {
    private final ResourceLocation TEXTURE = UnseenWorld.makeID("textures/entity/layers/darkening_layer.png");

    public DarkeningLayer(LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
    }

    @Override
    protected int getObjectCount(T entity) {
        if (!ModMainConfig.renderDarkeningOnMobs.get())
            return 0;
        if (entity instanceof IGrippingEntity entity1 && entity1.getDarkeningData() > 0)
            return Math.round(entity1.getDarkeningData() / 1.5f);
        return 0;
    }

    @Override
    protected void renderModelPartOverlay(ModelPart part, PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity) {
        if (entity instanceof IGrippingEntity iGrippingEntity)
            part.render(matrices, vertexConsumers.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, iGrippingEntity.getDarkeningData() / 10f);
    }

    @Override
    protected Triple<Float, Float, Float> getSizedPart() {
        return Triple.of(8.0f, 8.0f, 8.0f);
    }

    @Override
    protected void renderObject(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity) {
    }
}