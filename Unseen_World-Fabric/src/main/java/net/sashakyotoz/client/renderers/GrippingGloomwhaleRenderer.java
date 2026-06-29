package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.GloomwhaleModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.GrippingGloomwhaleEntity;

public class GrippingGloomwhaleRenderer extends MobRenderer<GrippingGloomwhaleEntity, GloomwhaleModel<GrippingGloomwhaleEntity>> {
    public GrippingGloomwhaleRenderer(EntityRendererProvider.Context context) {
        super(context, new GloomwhaleModel<>(context.bakeLayer(GloomwhaleModel.GLOOMWHALE)), 1);
        this.addLayer(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/gloomwhale/gripping_gloomwhale_glowing_parts.png")));
    }

    @Override
    protected boolean isShaking(GrippingGloomwhaleEntity entity) {
        return entity.isConverting();
    }

    @Override
    public ResourceLocation getTextureLocation(GrippingGloomwhaleEntity entity) {
        return UnseenWorld.makeID("textures/entity/gloomwhale/gripping_gloomwhale.png");
    }
}
