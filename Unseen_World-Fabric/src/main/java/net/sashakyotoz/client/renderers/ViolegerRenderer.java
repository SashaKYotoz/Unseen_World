package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ViolegerModel;
import net.sashakyotoz.common.entities.custom.ViolegerEntity;

public class ViolegerRenderer extends MobEntityRenderer<ViolegerEntity, ViolegerModel<ViolegerEntity>> {

    public ViolegerRenderer(EntityRendererFactory.Context context) {
        super(context, new ViolegerModel<>(context.getPart(ViolegerModel.VIOLEGER)), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(ViolegerEntity entity) {
        return UnseenWorld.makeID("textures/entity/violegers/violeger.png");
    }
}