
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelDark_Phantom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.DarkPhantomEntity;

import java.util.Calendar;

public class DarkPhantomRenderer extends MobRenderer<DarkPhantomEntity, ModelDark_Phantom<DarkPhantomEntity>> {
	private static final ResourceLocation christmas_texture = new ResourceLocation("unseen_world:textures/entities/dark_phantom_christmas.png");
	private boolean xmasTexture = false;
	public DarkPhantomRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelDark_Phantom(context.bakeLayer(ModelDark_Phantom.LAYER_LOCATION)), 0.6f);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/glow_phantom_texture.png"));
			}
		});
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 20 && calendar.get(Calendar.DATE) <= 30) {
			this.xmasTexture = true;
		}
	}

	@Override
	public ResourceLocation getTextureLocation(DarkPhantomEntity entity) {
		return xmasTexture ? christmas_texture : new ResourceLocation("unseen_world:textures/entities/dark_phantom.png");
	}
}
