package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.GloomwhaleModel;
import net.sashakyotoz.common.entities.custom.GloomwhaleEntity;

public class GloomwhaleRenderer extends DeathFixedMobRenderer<GloomwhaleEntity, GloomwhaleModel> {
    public GloomwhaleRenderer(EntityRendererFactory.Context context) {
        super(context, new GloomwhaleModel(context.getPart(GloomwhaleModel.GLOOMWHALE)), 1);
    }

    @Override
    protected boolean isShaking(GloomwhaleEntity entity) {
        return entity.isConverting();
    }

    @Override
    protected void scale(GloomwhaleEntity entity, MatrixStack matrices, float amount) {
        float modifier = 1;
        if (entity.isConverting() && entity.age % 20 == 0)
            modifier -= 0.01f;
        modifier = Math.max(modifier, 0.1f);
        matrices.scale(modifier, modifier, modifier);
    }

    @Override
    public Identifier getTexture(GloomwhaleEntity entity) {
        return UnseenWorld.makeID("textures/entity/gloomwhale/gloomwhale.png");
    }
}
