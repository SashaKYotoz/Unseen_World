
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelTealive_Skeleton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.TealiveSkeletonEntity;

public class TealiveSkeletonRenderer extends MobRenderer<TealiveSkeletonEntity, ModelTealive_Skeleton<TealiveSkeletonEntity>> {
	public TealiveSkeletonRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelTealive_Skeleton(context.bakeLayer(ModelTealive_Skeleton.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/glow_tealive_skeleton.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(TealiveSkeletonEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/tealive_skeleton.png");
	}
}
