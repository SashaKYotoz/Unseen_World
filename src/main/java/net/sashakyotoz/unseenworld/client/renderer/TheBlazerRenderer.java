
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.Modelthe_blazer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.TheBlazerEntity;

public class TheBlazerRenderer extends MobRenderer<TheBlazerEntity, Modelthe_blazer<TheBlazerEntity>> {
	private static final ResourceLocation THE_BLAZER_LOCATION = new ResourceLocation("unseen_world:textures/entities/the_blazer.png");
	private static final ResourceLocation THE_BLAZER_BLOCKED_LOCATION = new ResourceLocation(
			"unseen_world:textures/entities/the_blazer_is_blocking.png");

	public TheBlazerRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelthe_blazer(context.bakeLayer(Modelthe_blazer.LAYER_LOCATION)), 0.5f);
	}

	public ResourceLocation getTextureLocation(TheBlazerEntity entity) {
		return entity.isBlocked() ? THE_BLAZER_BLOCKED_LOCATION : THE_BLAZER_LOCATION;
	}
}
