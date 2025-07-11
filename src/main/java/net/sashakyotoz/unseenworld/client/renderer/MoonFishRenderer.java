
package net.sashakyotoz.unseenworld.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.client.model.ModelMoonFish;
import net.sashakyotoz.unseenworld.entity.MoonFishEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

public class MoonFishRenderer extends MobRenderer<MoonFishEntity, ModelMoonFish<MoonFishEntity>> {
	public MoonFishRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelMoonFish(context.bakeLayer(ModelMoonFish.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(MoonFishEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/fish/moon_fish" + entity.texture + ".png");
	}

	@Override
	protected boolean isShaking(MoonFishEntity entity) {
		return !(entity.isInWater() || entity.level().getBlockState(entity.getOnPos()).is(UnseenWorldBlocks.DARK_WATER.get()));
	}
}
