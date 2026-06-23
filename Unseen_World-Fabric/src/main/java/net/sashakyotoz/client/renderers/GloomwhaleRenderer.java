package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.GloomwhaleModel;
import net.sashakyotoz.common.entities.custom.GloomwhaleEntity;

public class GloomwhaleRenderer extends MobEntityRenderer<GloomwhaleEntity, GloomwhaleModel<GloomwhaleEntity>> {
    public GloomwhaleRenderer(EntityRendererFactory.Context context) {
        super(context, new GloomwhaleModel<>(context.getPart(GloomwhaleModel.GLOOMWHALE)), 1);
    }

    @Override
    public Identifier getTexture(GloomwhaleEntity entity) {
        return UnseenWorld.makeID("textures/entity/gloomwhale/gloomwhale.png");
    }
}
