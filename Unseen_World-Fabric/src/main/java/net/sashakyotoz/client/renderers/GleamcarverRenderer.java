package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.GleamcarverModel;
import net.sashakyotoz.common.entities.custom.GleamcarverEntity;

public class GleamcarverRenderer extends MobRenderer<GleamcarverEntity, GleamcarverModel> {

    public GleamcarverRenderer(EntityRendererProvider.Context context) {
        super(context, new GleamcarverModel(context.bakeLayer(GleamcarverModel.GLEAMCARVER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(GleamcarverEntity entity) {
        return entity.gleamcarverType == null ? UnseenWorld.makeID("textures/entity/gleamcarver/gleamcarver_dark_currantslate.png")
                : UnseenWorld.makeID("textures/entity/gleamcarver/gleamcarver_" + entity.gleamcarverType.typeName + ".png");
    }
}