package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.DreadflapModel;
import net.sashakyotoz.common.entities.custom.DreadflapEntity;

public class DreadflapRenderer extends MobEntityRenderer<DreadflapEntity, DreadflapModel> {

    public DreadflapRenderer(EntityRendererFactory.Context context) {
        super(context, new DreadflapModel(context.getPart(DreadflapModel.DREADFLAP)), 0.5F);
    }


    public Identifier getTexture(DreadflapEntity tuskhog) {
        return UnseenWorld.makeID("textures/entity/dreadflap/dreadflap.png");
    }
}