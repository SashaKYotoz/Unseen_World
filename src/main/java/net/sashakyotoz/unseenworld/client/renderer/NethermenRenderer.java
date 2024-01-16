
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelLava_Enderman;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.NethermanEntity;

public class NethermenRenderer extends MobRenderer<NethermanEntity, ModelLava_Enderman<NethermanEntity>> {
	public NethermenRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelLava_Enderman(context.bakeLayer(ModelLava_Enderman.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<NethermanEntity, ModelLava_Enderman<NethermanEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/lava_endermen_glow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(NethermanEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/lava_enderman.png");
	}
}
