
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.Modelmoon_fish;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.procedures.MoonfishEntityShakingConditionProcedure;
import net.sashakyotoz.unseenworld.entity.MoonfishEntity;

public class MoonfishRenderer extends MobRenderer<MoonfishEntity, Modelmoon_fish<MoonfishEntity>> {
	public MoonfishRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelmoon_fish(context.bakeLayer(Modelmoon_fish.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(MoonfishEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/moon_fish" + entity.texture + ".png");
	}

	@Override
	protected boolean isShaking(MoonfishEntity _ent) {
		Entity entity = _ent;
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		return MoonfishEntityShakingConditionProcedure.execute(entity);
	}
}
