package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.GloomwhaleModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.GrippingGloomwhaleEntity;

public class GrippingGloomwhaleRenderer extends MobEntityRenderer<GrippingGloomwhaleEntity, GloomwhaleModel<GrippingGloomwhaleEntity>> {
    public GrippingGloomwhaleRenderer(EntityRendererFactory.Context context) {
        super(context, new GloomwhaleModel<>(context.getPart(GloomwhaleModel.GLOOMWHALE)), 1);
        this.addFeature(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/gloomwhale/gripping_gloomwhale_glowing_parts.png")));
    }

    @Override
    protected boolean isShaking(GrippingGloomwhaleEntity entity) {
        return entity.isConverting();
    }

    @Override
    public Identifier getTexture(GrippingGloomwhaleEntity entity) {
        return UnseenWorld.makeID("textures/entity/gloomwhale/gripping_gloomwhale.png");
    }
}
