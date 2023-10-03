
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.Modelarmored_skeleton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.DarkskeletonEntity;

public class DarkskeletonRenderer extends MobRenderer<DarkskeletonEntity, Modelarmored_skeleton<DarkskeletonEntity>> {
	public DarkskeletonRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelarmored_skeleton(context.bakeLayer(Modelarmored_skeleton.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(DarkskeletonEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/armored_skeleton" + entity.texture + ".png");

	}
}
