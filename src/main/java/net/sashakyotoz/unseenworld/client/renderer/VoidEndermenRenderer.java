
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.Modelvoid_endermen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.VoidEndermenEntity;

public class VoidEndermenRenderer extends MobRenderer<VoidEndermenEntity, Modelvoid_endermen<VoidEndermenEntity>> {
	public VoidEndermenRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelvoid_endermen(context.bakeLayer(Modelvoid_endermen.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<VoidEndermenEntity, Modelvoid_endermen<VoidEndermenEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/void_endermen_glow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(VoidEndermenEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/void_endermen.png");
	}
}
