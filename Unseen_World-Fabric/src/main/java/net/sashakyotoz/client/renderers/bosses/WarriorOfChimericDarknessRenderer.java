package net.sashakyotoz.client.renderers.bosses;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.WarriorOfChimericDarknessModel;
import net.sashakyotoz.client.renderers.layers.bosses.WarriorOfDarknessEroflameLayer;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorOfChimericDarknessRenderer extends MobRenderer<WarriorOfChimericDarkness, WarriorOfChimericDarknessModel> {

    public WarriorOfChimericDarknessRenderer(EntityRendererProvider.Context context) {
        super(context, new WarriorOfChimericDarknessModel(context.bakeLayer(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS)), 0.5f);
        this.addLayer(new WarriorOfDarknessEroflameLayer<>(this));
    }

    @Override
    protected boolean isShaking(WarriorOfChimericDarkness entity) {
        return entity.isDeadOrDying();
    }

    @Override
    public ResourceLocation getTextureLocation(WarriorOfChimericDarkness entity) {
        return UnseenWorld.makeID("textures/entity/warrior_of_darkness/warrior_of_chimeric_darkness.png");
    }
}