
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelDustyPinkMaxorFish;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.DustyPinkMaxorFishEntity;

public class DustyPinkMaxorFishRenderer extends MobRenderer<DustyPinkMaxorFishEntity, ModelDustyPinkMaxorFish<DustyPinkMaxorFishEntity>> {
	public DustyPinkMaxorFishRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelDustyPinkMaxorFish(context.bakeLayer(ModelDustyPinkMaxorFish.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(DustyPinkMaxorFishEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/dusty_pink_maxor_fish.png");
	}
}
