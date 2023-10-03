package net.sashakyotoz.unseenworld.procedures;

import net.minecraft.world.entity.Entity;

public class MoonfishEntityShakingConditionProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		boolean shaking = false;
		if (entity.isInWater()) {
			shaking = true;
		} else {
			shaking = false;
		}
		return shaking;
	}
}
