
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelSpiritOfWolf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.DarkSpiritWolfEntity;

public class DarkSpiritWolfRenderer extends MobRenderer<DarkSpiritWolfEntity, ModelSpiritOfWolf<DarkSpiritWolfEntity>> {
	public DarkSpiritWolfRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelSpiritOfWolf<>(context.bakeLayer(ModelSpiritOfWolf.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(DarkSpiritWolfEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/spirit_of_wolf.png");
	}
}
