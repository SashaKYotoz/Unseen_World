
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.sashakyotoz.unseenworld.client.model.ModelStreder_with_Saddle;
import net.sashakyotoz.unseenworld.entity.StrederEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StrederRenderer extends MobRenderer<StrederEntity, ModelStreder_with_Saddle<StrederEntity>> {
	private static final ResourceLocation STREDER_LOCATION = new ResourceLocation("unseen_world:textures/entities/streder.png");
	private static final ResourceLocation STREDER_SADDLED_LOCATION = new ResourceLocation("unseen_world:textures/entities/streder_with_saddle.png");

	public StrederRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelStreder_with_Saddle(context.bakeLayer(ModelStreder_with_Saddle.LAYER_LOCATION)), 0.5f);
	}

	protected void scale(StrederEntity p_116066_, PoseStack p_116067_, float p_116068_) {
		if (p_116066_.isBaby()) {
			p_116067_.scale(0.5F, 0.5F, 0.5F);
			this.shadowRadius = 0.25F;
		} else {
			this.shadowRadius = 0.5F;
		}
	}

	public ResourceLocation getTextureLocation(StrederEntity entity) {
		return entity.isSaddled() ? STREDER_SADDLED_LOCATION : STREDER_LOCATION;
	}
}
