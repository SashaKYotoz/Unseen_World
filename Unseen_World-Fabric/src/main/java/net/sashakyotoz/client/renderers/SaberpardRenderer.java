package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.SaberpardModel;
import net.sashakyotoz.common.entities.custom.SaberpardEntity;

public class SaberpardRenderer extends MobRenderer<SaberpardEntity, SaberpardModel> {
    public SaberpardRenderer(EntityRendererProvider.Context context) {
        super(context, new SaberpardModel(context.bakeLayer(SaberpardModel.SABERPARD)), 0.5f);
    }

    @Override
    protected void scale(SaberpardEntity entity, PoseStack matrices, float amount) {
        float modifier = entity.isBaby() ? 0.5f : 1f;
        matrices.scale(modifier, modifier, modifier);
    }

    @Override
    protected boolean isShaking(SaberpardEntity entity) {
        return entity.isConverting();
    }

    @Override
    public void setupRotations(SaberpardEntity entity, PoseStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupRotations(entity, matrices, animationProgress, bodyYaw, tickDelta);
        double velocity = entity.getDeltaMovement().y;
        if (!entity.onGround()
                && (velocity < -0.1 || velocity > 0.1)
                && !entity.level().getBlockState(entity.blockPosition().below()).canOcclude()
                && !entity.level().getBlockState(entity.blockPosition().below(2)).canOcclude()) {
            velocity = Math.max(velocity, 2);
            velocity = Math.min(velocity, -2);
            matrices.mulPose(Axis.XP.rotation(velocity > 0 ? (float) Math.sin(-Math.PI / 12 * velocity) : (float) Math.sin(Math.PI / 12 * velocity)));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SaberpardEntity entity) {
        return entity.getVariant() == SaberpardEntity.Type.JUNGLE ?
                UnseenWorld.makeID("textures/entity/saberpard/saberpard_jungle.png") :
                UnseenWorld.makeID("textures/entity/saberpard/saberpard_steppe.png");
    }
}