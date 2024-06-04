
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelNetherman;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.NethermanEntity;

public class NethermanRenderer extends MobRenderer<NethermanEntity, ModelNetherman<NethermanEntity>> {
	public NethermanRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelNetherman(context.bakeLayer(ModelNetherman.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/endermen/netherman_glow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(NethermanEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/endermen/netherman.png");
	}
}
