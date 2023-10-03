
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.Modelspirit_of_wolf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.DarkspiritwolfEntity;

public class DarkspiritwolfRenderer extends MobRenderer<DarkspiritwolfEntity, Modelspirit_of_wolf<DarkspiritwolfEntity>> {
	public DarkspiritwolfRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelspirit_of_wolf(context.bakeLayer(Modelspirit_of_wolf.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(DarkspiritwolfEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/spirit_of_wolf.png");
	}
}
