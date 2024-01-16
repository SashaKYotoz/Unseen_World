
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelAmethystGolem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.AmethystGolemEntity;

import com.mojang.blaze3d.vertex.PoseStack;

public class AmethystGolemRenderer extends MobRenderer<AmethystGolemEntity, ModelAmethystGolem<AmethystGolemEntity>> {
	public AmethystGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelAmethystGolem(context.bakeLayer(ModelAmethystGolem.LAYER_LOCATION)), 0.6f);
	}

	protected void scale(AmethystGolemEntity entity, PoseStack stack, float p_115985_) {
		stack.scale(1.5F, 1.5F, 1.5F);
		stack.translate(0.0D, (double) 0.001F, 0.0D);
	}

	@Override
	public ResourceLocation getTextureLocation(AmethystGolemEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/amethyst_golem.png");
	}
}
