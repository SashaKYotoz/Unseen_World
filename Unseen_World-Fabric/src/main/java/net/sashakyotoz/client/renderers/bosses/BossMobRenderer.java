package net.sashakyotoz.client.renderers.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.sashakyotoz.common.entities.bosses.BossLikePathfinderMob;

public class BossMobRenderer<E extends BossLikePathfinderMob, M extends HierarchicalModel<E>> extends MobRenderer<E, M> {

    public BossMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected boolean isShaking(E entity) {
        return entity.isDeadOrDying();
    }

    @Override
    protected void setupRotations(E entityLiving, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        if (this.isShaking(entityLiving)) {
            rotationYaw += (float) (Math.cos(entityLiving.tickCount * 3.25F) * Math.PI * 0.4F);
        }

        if (!entityLiving.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        }

        if (isEntityUpsideDown(entityLiving)) {
            poseStack.translate(0.0F, entityLiving.getBbHeight() + 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(E entity) {
        return null;
    }
}
