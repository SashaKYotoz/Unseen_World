
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelRed_Sylph;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.RedSlylfEntity;
import net.minecraft.world.entity.animal.allay.Allay;

public class RedSlylfRenderer extends MobRenderer<RedSlylfEntity, ModelRed_Sylph<RedSlylfEntity>> {
	public RedSlylfRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelRed_Sylph<>(context.bakeLayer(ModelRed_Sylph.LAYER_LOCATION)), 0.5f);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(RedSlylfEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/red_sylph.png");
	}
}
