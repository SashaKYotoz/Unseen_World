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
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.utils.Oscillator;

public class EclipseSentinelEroflameLayer<T extends EclipseSentinel, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {
    public EclipseSentinelEroflameLayer(RenderLayerParent<T, M> context) {
        super(context);
    }
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EclipseSentinel entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityTranslucent(UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_eroflame.png")));
        if (entity.getHealth() < entity.getMaxHealth() / 2f)
            this.getParentModel().renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, (float) Oscillator.getOscillatingValue(ClientTicks.getTicks()));
        if (entity.isInSentinelPose(EclipseSentinel.SentinelPose.BLASTING_EROFLAME))
            this.getParentModel().renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 1.0F, 1.0F);
    }
}