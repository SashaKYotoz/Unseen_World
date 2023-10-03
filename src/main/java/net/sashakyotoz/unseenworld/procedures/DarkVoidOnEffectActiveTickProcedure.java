package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.network.UnseenWorldModVariables;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class DarkVoidOnEffectActiveTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (UnseenWorldModVariables.WorldVariables.get(world).timer < world.dayTime()) {
			UnseenWorldModVariables.WorldVariables.get(world).timer = world.dayTime() + 30;
			UnseenWorldModVariables.WorldVariables.get(world).syncData(world);
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) - 1));
		}
	}
}
