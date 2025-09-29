package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.WarriorOfChimericDarknessModel;
import net.sashakyotoz.client.renderers.layers.bosses.WarriorOfDarknessEroflameLayer;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorOfChimericDarknessRenderer extends DeathFixedMobRenderer<WarriorOfChimericDarkness, WarriorOfChimericDarknessModel>{

    public WarriorOfChimericDarknessRenderer(EntityRendererFactory.Context context) {
        super(context, new WarriorOfChimericDarknessModel(context.getPart(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS)), 0.5f);
        this.addFeature(new WarriorOfDarknessEroflameLayer<>(this));
    }

    @Override
    protected boolean isShaking(WarriorOfChimericDarkness entity) {
        return entity.isDead();
    }

    @Override
    protected void scale(WarriorOfChimericDarkness entity, MatrixStack matrices, float amount) {
        matrices.scale(1.25f,1.25f,1.25f);
    }

    @Override
    public Identifier getTexture(WarriorOfChimericDarkness entity) {
        return UnseenWorld.makeID("textures/entity/warrior_of_darkness/warrior_of_chimeric_darkness.png");
    }
}