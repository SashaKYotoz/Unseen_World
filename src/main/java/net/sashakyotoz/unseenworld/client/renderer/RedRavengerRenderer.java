
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.sashakyotoz.unseenworld.client.model.ModelRed_Ravager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.RedRavengerEntity;

public class RedRavengerRenderer extends MobRenderer<RedRavengerEntity, ModelRed_Ravager<RedRavengerEntity>> {
	private static final ResourceLocation RED_RAVENGER_LOCATION = new ResourceLocation("unseen_world:textures/entities/red_ravager.png");
	private static final ResourceLocation RED_RAVENGER_SADDLED_LOCATION = new ResourceLocation("unseen_world:textures/entities/red_ravager_tamed.png");
	public RedRavengerRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelRed_Ravager(context.bakeLayer(ModelRed_Ravager.LAYER_LOCATION)), 0.8f);
	}
	protected void scale(RedRavengerEntity ravengerEntity, PoseStack stack, float p_116068_) {
		if (ravengerEntity.isBaby()) {
			stack.scale(0.5F, 0.5F, 0.5F);
			this.shadowRadius = 0.25F;
		} else {
			stack.scale(1F, 1F, 1F);
			this.shadowRadius = 0.5F;
		}
	}

	@Override
	public ResourceLocation getTextureLocation(RedRavengerEntity entity) {
		return entity.isSaddled() ? RED_RAVENGER_SADDLED_LOCATION : RED_RAVENGER_LOCATION;
	}
}
