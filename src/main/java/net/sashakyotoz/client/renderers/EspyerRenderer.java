package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.EspyerModel;
import net.sashakyotoz.client.renderers.layers.EspyerChargeFeatureRenderer;
import net.sashakyotoz.common.entities.custom.EspyerEntity;

public class EspyerRenderer extends MobEntityRenderer<EspyerEntity, EspyerModel> {

    public EspyerRenderer(EntityRendererFactory.Context context) {
        super(context, new EspyerModel(context.getPart(EspyerModel.ESPYER)), 0.5F);
        this.addFeature(new EspyerChargeFeatureRenderer(this, context.getModelLoader()));
        this.addFeature(new EyesFeatureRenderer<>(this) {
            @Override
            public RenderLayer getEyesTexture() {
                return RenderLayer.getEyes(UnseenWorld.makeID("textures/entity/espyer/espyer_glowing_parts.png"));
            }
        });
    }

    protected void scale(EspyerEntity espyer, MatrixStack matrixStack, float f) {
        float g = espyer.getClientFuseTime(f);
        float h = 1.0F + MathHelper.sin(g * 100.0F) * g * 0.01F;
        g = MathHelper.clamp(g, 0.0F, 1.0F);
        g *= g;
        g *= g;
        float i = (1.0F + g * 0.4F) * h;
        float j = (1.0F + g * 0.1F) / h;
        if (espyer.getWorld().getClosestPlayer(espyer, 32) != null
                && espyer.isEntityLookingAtMe(espyer.getWorld().getClosestPlayer(espyer, 32), 0.5, false, true, LivingEntity::canHit, espyer::getEyeY, espyer::getY, () -> (espyer.getEyeY() + espyer.getY()) / 2.0))
            matrixStack.translate(0, 0.75f, 0);
        matrixStack.scale(i, j, i);
    }

    protected float getAnimationCounter(EspyerEntity espyer, float f) {
        float g = espyer.getClientFuseTime(f);
        return (int) (g * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(g, 0.5F, 1.0F);
    }

    public Identifier getTexture(EspyerEntity espyer) {
        return UnseenWorld.makeID("textures/entity/espyer/espyer.png");
    }
}