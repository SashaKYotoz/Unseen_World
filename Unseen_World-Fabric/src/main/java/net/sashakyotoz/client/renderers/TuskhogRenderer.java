package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.TuskhogModel;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;

public class TuskhogRenderer extends MobRenderer<TuskhogEntity, TuskhogModel<TuskhogEntity>> {

    public TuskhogRenderer(EntityRendererProvider.Context context) {
        super(context, new TuskhogModel<>(context.bakeLayer(TuskhogModel.TUSKHOG)), 0.5F);
    }

    @Override
    protected void scale(TuskhogEntity entity, PoseStack matrices, float amount) {
        float modifier = entity.isBaby() ? 0.5f : 1f;
        matrices.scale(modifier, modifier, modifier);
    }

    @Override
    protected boolean isShaking(TuskhogEntity entity) {
        return super.isShaking(entity) || entity.isConverting();
    }

    public ResourceLocation getTextureLocation(TuskhogEntity tuskhog) {
        return tuskhog.getVariant() == TuskhogEntity.Type.WARM ?
                UnseenWorld.makeID("textures/entity/tuskhog/tuskhog_warm.png") :
                UnseenWorld.makeID("textures/entity/tuskhog/tuskhog_temperate.png");
    }
}