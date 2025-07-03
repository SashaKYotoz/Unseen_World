
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.client.model.ModelStrederWithSaddle;
import net.sashakyotoz.unseenworld.entity.StrederEntity;

public class StrederRenderer extends MobRenderer<StrederEntity, ModelStrederWithSaddle<StrederEntity>> {
	private static final ResourceLocation STREDER_LOCATION = new ResourceLocation("unseen_world:textures/entities/streder.png");
	private static final ResourceLocation STREDER_SADDLED_LOCATION = new ResourceLocation("unseen_world:textures/entities/streder_with_saddle.png");

	public StrederRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelStrederWithSaddle(context.bakeLayer(ModelStrederWithSaddle.LAYER_LOCATION)), 0.5f);
	}

	protected void scale(StrederEntity entity, PoseStack stack, float p_116068_) {
		if (entity.isBaby()) {
			stack.scale(0.5F, 0.5F, 0.5F);
			this.shadowRadius = 0.25F;
		} else {
			this.shadowRadius = 0.5F;
		}
	}

	public ResourceLocation getTextureLocation(StrederEntity entity) {
		return entity.isSaddled() ? STREDER_SADDLED_LOCATION : STREDER_LOCATION;
	}
}
