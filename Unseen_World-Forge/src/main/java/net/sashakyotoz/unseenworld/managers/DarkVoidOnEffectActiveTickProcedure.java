package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class DarkVoidOnEffectActiveTickProcedure {
	private static long timer;
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (timer < world.dayTime() && !world.isClientSide()) {
			timer = world.dayTime() + 20;
			if (entity instanceof LivingEntity livingEntity) {
				livingEntity.setHealth(livingEntity.getHealth() - 1);
			}
		}
	}
}
