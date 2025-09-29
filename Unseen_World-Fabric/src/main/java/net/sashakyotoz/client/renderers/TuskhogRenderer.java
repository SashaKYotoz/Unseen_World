package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.TuskhogModel;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;

public class TuskhogRenderer extends MobEntityRenderer<TuskhogEntity, TuskhogModel<TuskhogEntity>> {

    public TuskhogRenderer(EntityRendererFactory.Context context) {
        super(context, new TuskhogModel<>(context.getPart(TuskhogModel.TUSKHOG)), 0.5F);
    }

    @Override
    protected void scale(TuskhogEntity entity, MatrixStack matrices, float amount) {
        float modifier = entity.isBaby() ? 0.5f : 1f;
        matrices.scale(modifier, modifier, modifier);
    }

    @Override
    protected boolean isShaking(TuskhogEntity entity) {
        return super.isShaking(entity) || entity.isConverting();
    }

    public Identifier getTexture(TuskhogEntity tuskhog) {
        return tuskhog.getVariant() == TuskhogEntity.Type.WARM ?
                UnseenWorld.makeID("textures/entity/tuskhog/tuskhog_warm.png") :
                UnseenWorld.makeID("textures/entity/tuskhog/tuskhog_temperate.png");
    }
}