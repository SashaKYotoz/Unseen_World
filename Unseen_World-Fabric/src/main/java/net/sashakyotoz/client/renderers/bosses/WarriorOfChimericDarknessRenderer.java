package net.sashakyotoz.client.renderers.bosses;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.WarriorOfChimericDarknessModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsLayer;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorOfChimericDarknessRenderer extends BossMobRenderer<WarriorOfChimericDarkness, WarriorOfChimericDarknessModel> {
    private final ResourceLocation WARRIOR = UnseenWorld.MOD.makeID("textures/entity/warrior_of_darkness/warrior_of_chimeric_darkness.png");

    public WarriorOfChimericDarknessRenderer(EntityRendererProvider.Context context) {
        super(context, new WarriorOfChimericDarknessModel(context.bakeLayer(WarriorOfChimericDarknessModel.WARRIOR_OF_CHIMERIC_DARKNESS)), 0.5f);
        this.addLayer(new GlowingPartsLayer<>(this,
                UnseenWorld.MOD.makeID("textures/entity/warrior_of_darkness/warrior_of_darkness_glowing_parts.png"),
                null
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(WarriorOfChimericDarkness entity) {
        return WARRIOR;
    }
}