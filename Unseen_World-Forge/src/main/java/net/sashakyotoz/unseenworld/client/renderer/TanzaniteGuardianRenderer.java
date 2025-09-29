
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelTanzanite_Guardian;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.TanzaniteGuardianEntity;

public class TanzaniteGuardianRenderer extends MobRenderer<TanzaniteGuardianEntity, ModelTanzanite_Guardian<TanzaniteGuardianEntity>> {
	public TanzaniteGuardianRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelTanzanite_Guardian(context.bakeLayer(ModelTanzanite_Guardian.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(TanzaniteGuardianEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/tanzanite_guardian.png");
	}
}
