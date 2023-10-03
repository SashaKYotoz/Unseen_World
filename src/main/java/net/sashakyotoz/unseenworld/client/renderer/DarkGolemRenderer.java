
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelWarrior_of_the_Chimeric_Darkness;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.entity.DarkGolemEntity;

public class DarkGolemRenderer extends MobRenderer<DarkGolemEntity, ModelWarrior_of_the_Chimeric_Darkness<DarkGolemEntity>> {
	public DarkGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelWarrior_of_the_Chimeric_Darkness(context.bakeLayer(ModelWarrior_of_the_Chimeric_Darkness.LAYER_LOCATION)), 0.6f);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(DarkGolemEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/warrior_of_the_chimeric_darkness.png");
	}
}
