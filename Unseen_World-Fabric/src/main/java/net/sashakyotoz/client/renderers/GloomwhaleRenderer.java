package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.GloomwhaleModel;
import net.sashakyotoz.common.entities.custom.GloomwhaleEntity;

public class GloomwhaleRenderer extends MobRenderer<GloomwhaleEntity, GloomwhaleModel<GloomwhaleEntity>> {
    public GloomwhaleRenderer(EntityRendererProvider.Context context) {
        super(context, new GloomwhaleModel<>(context.bakeLayer(GloomwhaleModel.GLOOMWHALE)), 1);
    }

    @Override
    public ResourceLocation getTextureLocation(GloomwhaleEntity entity) {
        return UnseenWorld.makeID("textures/entity/gloomwhale/gloomwhale.png");
    }
}
