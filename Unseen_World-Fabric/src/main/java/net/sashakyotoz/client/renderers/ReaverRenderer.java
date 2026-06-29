package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ReaverModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.ReaverEntity;

public class ReaverRenderer extends MobRenderer<ReaverEntity, ReaverModel> {

    public ReaverRenderer(EntityRendererProvider.Context context) {
        super(context, new ReaverModel(context.bakeLayer(ReaverModel.REAVER)), 0.5F);
        this.addLayer(new GlowingPartsFeatureRenderer<>(this,UnseenWorld.makeID("textures/entity/reaver/reaver_glowing_parts.png")));
    }

    public ResourceLocation getTextureLocation(ReaverEntity tuskhog) {
        return UnseenWorld.makeID("textures/entity/reaver/reaver.png");
    }
}