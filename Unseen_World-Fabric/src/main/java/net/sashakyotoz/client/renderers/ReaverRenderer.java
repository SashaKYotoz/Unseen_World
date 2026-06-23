package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ReaverModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.ReaverEntity;

public class ReaverRenderer extends MobEntityRenderer<ReaverEntity, ReaverModel> {

    public ReaverRenderer(EntityRendererFactory.Context context) {
        super(context, new ReaverModel(context.getPart(ReaverModel.REAVER)), 0.5F);
        this.addFeature(new GlowingPartsFeatureRenderer<>(this,UnseenWorld.makeID("textures/entity/reaver/reaver_glowing_parts.png")));
    }

    public Identifier getTexture(ReaverEntity tuskhog) {
        return UnseenWorld.makeID("textures/entity/reaver/reaver.png");
    }
}