
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelSavageSmallBlaze;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.SavageSmallBlazeEntity;

public class SavageSmallBlazeRenderer extends MobRenderer<SavageSmallBlazeEntity, ModelSavageSmallBlaze<SavageSmallBlazeEntity>> {
	public SavageSmallBlazeRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelSavageSmallBlaze(context.bakeLayer(ModelSavageSmallBlaze.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/savage_small_blaze.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(SavageSmallBlazeEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/savage_small_blaze.png");
	}
}
