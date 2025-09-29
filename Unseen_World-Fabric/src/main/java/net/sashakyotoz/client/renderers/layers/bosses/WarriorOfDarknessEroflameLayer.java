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
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorOfDarknessEroflameLayer<T extends WarriorOfChimericDarkness, M extends SinglePartEntityModel<T>> extends FeatureRenderer<T, M> {
    public WarriorOfDarknessEroflameLayer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, WarriorOfChimericDarkness entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.EROFLAMING)
                || entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.SHIELDED_WALK)
                || entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.BLASTING)){
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEyes(UnseenWorld.makeID("textures/entity/warrior_of_darkness/warrior_of_chimeric_darkness_eroflame.png")));
            this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.75F);
        }
    }
}
