
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelGhast_Of_Tealive_Valley;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.GhastOfTealiveValleyEntity;

import com.mojang.blaze3d.vertex.PoseStack;

public class GhastOfTealiveValleyRenderer extends MobRenderer<GhastOfTealiveValleyEntity, ModelGhast_Of_Tealive_Valley<GhastOfTealiveValleyEntity>> {
	private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("unseen_world:textures/entities/ghast_of_tealive_valley0.png");
	private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation(
			"unseen_world:textures/entities/ghast_of_tealive_valley1.png");

	public GhastOfTealiveValleyRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelGhast_Of_Tealive_Valley(context.bakeLayer(ModelGhast_Of_Tealive_Valley.LAYER_LOCATION)), 0.75f);
	}

	public ResourceLocation getTextureLocation(GhastOfTealiveValleyEntity entity) {
		return entity.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
	}

	protected void scale(GhastOfTealiveValleyEntity ghast, PoseStack stack, float p_114759_) {
		stack.scale(3.0F, 3.0F, 3.0F);
	}
}
