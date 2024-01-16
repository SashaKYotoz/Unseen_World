
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelArmoredSkeleton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.DarkskeletonEntity;

public class DarkSkeletonRenderer extends MobRenderer<DarkskeletonEntity, ModelArmoredSkeleton<DarkskeletonEntity>> {
	public DarkSkeletonRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelArmoredSkeleton(context.bakeLayer(ModelArmoredSkeleton.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(DarkskeletonEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/armored_skeleton" + entity.texture + ".png");

	}
}
