package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.VexalBeetleModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.VexalBeetleEntity;

public class VexalBeetleRenderer extends MobRenderer<VexalBeetleEntity, VexalBeetleModel> {

    public VexalBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, new VexalBeetleModel(context.bakeLayer(VexalBeetleModel.VEXAL_BEETLE)), 0.5f);
        this.addLayer(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/vexal_beetle/vexal_beetle_glowing_parts.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(VexalBeetleEntity entity) {
        return UnseenWorld.makeID("textures/entity/vexal_beetle/vexal_beetle.png");
    }
}