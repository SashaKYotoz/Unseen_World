
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.client.model.ModelDarkHoglin;
import net.sashakyotoz.unseenworld.entity.DarkHoglinEntity;

import java.util.Calendar;

public class DarkHoglinRenderer extends MobRenderer<DarkHoglinEntity, ModelDarkHoglin<DarkHoglinEntity>> {
	private static final ResourceLocation christmas_texture = new ResourceLocation("unseen_world:textures/entities/dark_hoglin_christmas.png");
	private boolean xmasTexture = false;
	public DarkHoglinRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelDarkHoglin(context.bakeLayer(ModelDarkHoglin.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/dark_hoglin.png"));
			}
		});
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 20 && calendar.get(Calendar.DATE) <= 30) {
			this.xmasTexture = true;
		}
	}

	@Override
	protected void scale(DarkHoglinEntity hoglinEntity, PoseStack stack, float size) {
		if (hoglinEntity.isBaby())
			stack.scale(0.5f,0.5f,0.5f);
		else
			stack.scale(1,1,1);
		super.scale(hoglinEntity, stack, size);
	}

	@Override
	public ResourceLocation getTextureLocation(DarkHoglinEntity entity) {
		return xmasTexture ? christmas_texture : new ResourceLocation("unseen_world:textures/entities/dark_hoglin.png");
	}
}
