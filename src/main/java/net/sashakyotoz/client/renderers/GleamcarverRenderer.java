package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.GleamcarverModel;
import net.sashakyotoz.common.entities.GleamcarverEntity;

public class GleamcarverRenderer extends DeathFixedMobRenderer<GleamcarverEntity, GleamcarverModel> {

    public GleamcarverRenderer(EntityRendererFactory.Context context) {
        super(context, new GleamcarverModel(context.getPart(GleamcarverModel.GLEAMCARVER)), 0.5f);
    }

    @Override
    public Identifier getTexture(GleamcarverEntity entity) {
        return entity.gleamcarverType == null ? UnseenWorld.makeID("textures/entity/gleamcarver/gleamcarver_dark_currantslate.png")
                : UnseenWorld.makeID("textures/entity/gleamcarver/gleamcarver_" + entity.gleamcarverType.typeName + ".png");
    }
}