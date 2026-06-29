package net.sashakyotoz.client.renderers.layers.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;

public class EclipseSentinelGlowingLayer<T extends EclipseSentinel, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {

    public EclipseSentinelGlowingLayer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EclipseSentinel entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityTranslucent(
                entity.isExalted()
                        ? UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_glowing_parts_exalted.png")
                        : UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_glowing_parts.png")
        ));
        this.getParentModel().renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}