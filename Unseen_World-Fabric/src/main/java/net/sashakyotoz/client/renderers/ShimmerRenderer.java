package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ShimmerModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.ShimmerEntity;

public class ShimmerRenderer extends MobRenderer<ShimmerEntity, ShimmerModel> {

    public ShimmerRenderer(EntityRendererProvider.Context context) {
        super(context, new ShimmerModel(context.bakeLayer(ShimmerModel.SHIMMER)), 1);
        this.addLayer(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/shimmer/shimmer_glowing_parts.png")));
    }

    public ResourceLocation getTextureLocation(ShimmerEntity shimmerEntity) {
        return UnseenWorld.makeID("textures/entity/shimmer/shimmer.png");
    }

    protected void setupRotations(ShimmerEntity shimmerEntity, PoseStack matrixStack, float f, float g, float h) {
        float i = Mth.lerp(h, shimmerEntity.prevTiltAngle, shimmerEntity.tiltAngle);
        float j = Mth.lerp(h, shimmerEntity.prevRollAngle, shimmerEntity.rollAngle);
        matrixStack.translate(0.0F, 0.5F, 0.0F);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - g));
        matrixStack.mulPose(Axis.XP.rotationDegrees(i));
        matrixStack.mulPose(Axis.YP.rotationDegrees(j));
        matrixStack.translate(0.0F, -1.2F, 0.0F);
    }

    protected float getAnimationProgress(ShimmerEntity shimmerEntity, float f) {
        return Mth.lerp(f, shimmerEntity.prevTentacleAngle, shimmerEntity.tentacleAngle);
    }
}