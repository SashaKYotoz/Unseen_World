package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.MapColor;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ReaverModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsLayer;
import net.sashakyotoz.client.renderers.layers.OwnedOutlineLayer;
import net.sashakyotoz.common.entities.custom.ReaverEntity;

public class ReaverRenderer extends MobRenderer<ReaverEntity, ReaverModel> {

    public ReaverRenderer(EntityRendererProvider.Context context) {
        super(context, new ReaverModel(context.bakeLayer(ReaverModel.REAVER)), 0.5F);
        this.addLayer(new GlowingPartsLayer<>(this, UnseenWorld.MOD.makeID("textures/entity/reaver/reaver_glowing_parts.png"), null));
        this.addLayer(new OwnedOutlineLayer<>(this, MapColor.COLOR_ORANGE.col));
    }

    public ResourceLocation getTextureLocation(ReaverEntity tuskhog) {
        return UnseenWorld.MOD.makeID("textures/entity/reaver/reaver.png");
    }
}