
package net.sashakyotoz.unseenworld.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.client.model.ModelRed_Sylph;
import net.sashakyotoz.unseenworld.entity.RedSlylfEntity;

import java.util.Calendar;

public class RedSlylfRenderer extends MobRenderer<RedSlylfEntity, ModelRed_Sylph<RedSlylfEntity>> {
	private static final ResourceLocation christmas_texture = new ResourceLocation("unseen_world:textures/entities/red_sylph_christmas.png");
	private boolean xmasTexture = false;
	public RedSlylfRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelRed_Sylph<>(context.bakeLayer(ModelRed_Sylph.LAYER_LOCATION)), 0.5f);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 20 && calendar.get(Calendar.DATE) <= 30) {
			this.xmasTexture = true;
		}
	}

	@Override
	public ResourceLocation getTextureLocation(RedSlylfEntity entity) {
		return xmasTexture ? christmas_texture : new ResourceLocation("unseen_world:textures/entities/red_sylph.png");
	}
}
