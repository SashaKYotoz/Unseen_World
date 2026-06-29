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
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorOfDarknessEroflameLayer<T extends WarriorOfChimericDarkness, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {
    public WarriorOfDarknessEroflameLayer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, WarriorOfChimericDarkness entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.EROFLAMING)
                || entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.SHIELDED_WALK)
                || entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.BLASTING)){
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.eyes(UnseenWorld.makeID("textures/entity/warrior_of_darkness/warrior_of_chimeric_darkness_eroflame.png")));
            this.getParentModel().renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.75F);
        }
    }
}
