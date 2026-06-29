package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ViolegerModel;
import net.sashakyotoz.common.entities.custom.ViolegerEntity;

public class ViolegerRenderer extends MobRenderer<ViolegerEntity, ViolegerModel<ViolegerEntity>> {

    public ViolegerRenderer(EntityRendererProvider.Context context) {
        super(context, new ViolegerModel<>(context.bakeLayer(ViolegerModel.VIOLEGER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(ViolegerEntity entity) {
        return UnseenWorld.makeID("textures/entity/violegers/violeger.png");
    }
}