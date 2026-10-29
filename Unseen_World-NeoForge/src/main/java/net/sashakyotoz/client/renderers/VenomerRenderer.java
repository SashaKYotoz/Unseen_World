package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ViolegerModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsLayer;
import net.sashakyotoz.common.entities.custom.VenomerEntity;

public class VenomerRenderer extends MobRenderer<VenomerEntity, ViolegerModel<VenomerEntity>> {

    public VenomerRenderer(EntityRendererProvider.Context context) {
        super(context, new ViolegerModel<>(context.bakeLayer(ViolegerModel.VIOLEGER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new GlowingPartsLayer<>(this, UnseenWorld.MOD.makeID("textures/entity/violegers/venomer_glowing_parts.png"), null));
    }

    @Override
    public ResourceLocation getTextureLocation(VenomerEntity entity) {
        return UnseenWorld.MOD.makeID("textures/entity/violegers/venomer.png");
    }
}