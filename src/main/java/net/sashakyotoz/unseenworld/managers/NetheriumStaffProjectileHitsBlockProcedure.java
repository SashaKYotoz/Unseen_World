package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

public class NetheriumStaffProjectileHitsBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof Level _level && !_level.isClientSide())
			_level.explode(null, x, y, z, 1, Level.ExplosionInteraction.BLOCK);
		world.addParticle(UnseenWorldModParticleTypes.REDNESS.get(), x, y, z, 0, 1, 0);
	}
}