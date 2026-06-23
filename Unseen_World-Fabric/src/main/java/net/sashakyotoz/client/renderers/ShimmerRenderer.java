package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ShimmerModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.ShimmerEntity;

public class ShimmerRenderer extends MobEntityRenderer<ShimmerEntity, ShimmerModel> {

    public ShimmerRenderer(EntityRendererFactory.Context context) {
        super(context, new ShimmerModel(context.getPart(ShimmerModel.SHIMMER)), 1);
        this.addFeature(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/shimmer/shimmer_glowing_parts.png")));
    }

    public Identifier getTexture(ShimmerEntity shimmerEntity) {
        return UnseenWorld.makeID("textures/entity/shimmer/shimmer.png");
    }

    protected void setupTransforms(ShimmerEntity shimmerEntity, MatrixStack matrixStack, float f, float g, float h) {
        float i = MathHelper.lerp(h, shimmerEntity.prevTiltAngle, shimmerEntity.tiltAngle);
        float j = MathHelper.lerp(h, shimmerEntity.prevRollAngle, shimmerEntity.rollAngle);
        matrixStack.translate(0.0F, 0.5F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - g));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(i));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        matrixStack.translate(0.0F, -1.2F, 0.0F);
    }

    protected float getAnimationProgress(ShimmerEntity shimmerEntity, float f) {
        return MathHelper.lerp(f, shimmerEntity.prevTentacleAngle, shimmerEntity.tentacleAngle);
    }
}