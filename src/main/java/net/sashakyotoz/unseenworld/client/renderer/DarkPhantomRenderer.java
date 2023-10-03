
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelDark_Phantom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.DarkPhantomEntity;

public class DarkPhantomRenderer extends MobRenderer<DarkPhantomEntity, ModelDark_Phantom<DarkPhantomEntity>> {
	public DarkPhantomRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelDark_Phantom(context.bakeLayer(ModelDark_Phantom.LAYER_LOCATION)), 0.6f);
		this.addLayer(new EyesLayer<DarkPhantomEntity, ModelDark_Phantom<DarkPhantomEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/glow_phantom_texture.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(DarkPhantomEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/dark_phantom.png");
	}
}
