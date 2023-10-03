package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraft.world.level.LevelAccessor;

public class AncientTransientBlockOpenEntityCollidesInTheBlockActivatorProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		UnseenWorldMod.queueServerWork(60, () -> {
			AncientTransientBlockOpenEntityCollidesInTheBlockProcedure.execute(world, x, y, z);
		});
	}
}
