
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelDark_hoglin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.DarkHoglinEntity;

public class DarkHoglinRenderer extends MobRenderer<DarkHoglinEntity, ModelDark_hoglin<DarkHoglinEntity>> {
	public DarkHoglinRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelDark_hoglin(context.bakeLayer(ModelDark_hoglin.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<DarkHoglinEntity, ModelDark_hoglin<DarkHoglinEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/dark_hoglin.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(DarkHoglinEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/dark_hoglin.png");
	}
}
