package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.SaberpardModel;
import net.sashakyotoz.common.entities.custom.SaberpardEntity;

public class SaberpardRenderer extends MobEntityRenderer<SaberpardEntity, SaberpardModel> {
    public SaberpardRenderer(EntityRendererFactory.Context context) {
        super(context, new SaberpardModel(context.getPart(SaberpardModel.SABERPARD)), 0.5f);
    }

    @Override
    protected void scale(SaberpardEntity entity, MatrixStack matrices, float amount) {
        float modifier = entity.isBaby() ? 0.5f : 1f;
        matrices.scale(modifier, modifier, modifier);
    }

    @Override
    protected boolean isShaking(SaberpardEntity entity) {
        return entity.isConverting();
    }

    @Override
    public void setupTransforms(SaberpardEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        double velocity = entity.getVelocity().y;
        if (!entity.isOnGround()
                && (velocity < -0.1 || velocity > 0.1)
                && !entity.getWorld().getBlockState(entity.getBlockPos().down()).isOpaque()
                && !entity.getWorld().getBlockState(entity.getBlockPos().down(2)).isOpaque()) {
            velocity = Math.max(velocity, 2);
            velocity = Math.min(velocity, -2);
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(velocity > 0 ? (float) Math.sin(-Math.PI / 12 * velocity) : (float) Math.sin(Math.PI / 12 * velocity)));
        }
    }

    @Override
    public Identifier getTexture(SaberpardEntity entity) {
        return entity.getVariant() == SaberpardEntity.Type.JUNGLE ?
                UnseenWorld.makeID("textures/entity/saberpard/saberpard_jungle.png") :
                UnseenWorld.makeID("textures/entity/saberpard/saberpard_steppe.png");
    }
}