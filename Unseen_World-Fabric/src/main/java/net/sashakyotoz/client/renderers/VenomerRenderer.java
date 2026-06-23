package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ViolegerModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.VenomerEntity;

public class VenomerRenderer extends MobEntityRenderer<VenomerEntity, ViolegerModel<VenomerEntity>> {

    public VenomerRenderer(EntityRendererFactory.Context context) {
        super(context, new ViolegerModel<>(context.getPart(ViolegerModel.VIOLEGER)), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/violegers/venomer_glowing_parts.png")));
    }

    @Override
    public Identifier getTexture(VenomerEntity entity) {
        return UnseenWorld.makeID("textures/entity/violegers/venomer.png");
    }
}