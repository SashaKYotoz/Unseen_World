package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.VexalBeetleModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsFeatureRenderer;
import net.sashakyotoz.common.entities.custom.VexalBeetleEntity;

public class VexalBeetleRenderer extends MobEntityRenderer<VexalBeetleEntity, VexalBeetleModel> {

    public VexalBeetleRenderer(EntityRendererFactory.Context context) {
        super(context, new VexalBeetleModel(context.getPart(VexalBeetleModel.VEXAL_BEETLE)), 0.5f);
        this.addFeature(new GlowingPartsFeatureRenderer<>(this, UnseenWorld.makeID("textures/entity/vexal_beetle/vexal_beetle_glowing_parts.png")));
    }

    @Override
    public Identifier getTexture(VexalBeetleEntity entity) {
        return UnseenWorld.makeID("textures/entity/vexal_beetle/vexal_beetle.png");
    }
}