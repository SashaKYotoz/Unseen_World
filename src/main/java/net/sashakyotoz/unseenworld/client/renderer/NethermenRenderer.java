
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelLava_Enderman;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.NethermenEntity;

public class NethermenRenderer extends MobRenderer<NethermenEntity, ModelLava_Enderman<NethermenEntity>> {
	public NethermenRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelLava_Enderman(context.bakeLayer(ModelLava_Enderman.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<NethermenEntity, ModelLava_Enderman<NethermenEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/lava_endermen_glow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(NethermenEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/lava_enderman.png");
	}
}
