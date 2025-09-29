
package net.sashakyotoz.unseenworld.client.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.client.model.ModelVoidEndermen;
import net.sashakyotoz.unseenworld.entity.VoidEndermanEntity;

public class VoidEndermenRenderer extends MobRenderer<VoidEndermanEntity, ModelVoidEndermen<VoidEndermanEntity>> {
	public VoidEndermenRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelVoidEndermen(context.bakeLayer(ModelVoidEndermen.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/endermen/void_enderman_glow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(VoidEndermanEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/endermen/void_enderman.png");
	}
}
