
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelRed_Armored_Blaze;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.RedBlazeEntity;

public class RedblazeRenderer extends MobRenderer<RedBlazeEntity, ModelRed_Armored_Blaze<RedBlazeEntity>> {
	public RedblazeRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelRed_Armored_Blaze(context.bakeLayer(ModelRed_Armored_Blaze.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(RedBlazeEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/red_armored_blaze.png");
	}
}
