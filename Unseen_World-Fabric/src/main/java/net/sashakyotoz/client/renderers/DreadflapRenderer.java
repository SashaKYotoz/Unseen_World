package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.DreadflapModel;
import net.sashakyotoz.common.entities.custom.DreadflapEntity;

public class DreadflapRenderer extends MobRenderer<DreadflapEntity, DreadflapModel> {

    public DreadflapRenderer(EntityRendererProvider.Context context) {
        super(context, new DreadflapModel(context.bakeLayer(DreadflapModel.DREADFLAP)), 0.5F);
    }


    public ResourceLocation getTextureLocation(DreadflapEntity tuskhog) {
        return UnseenWorld.makeID("textures/entity/dreadflap/dreadflap.png");
    }
}