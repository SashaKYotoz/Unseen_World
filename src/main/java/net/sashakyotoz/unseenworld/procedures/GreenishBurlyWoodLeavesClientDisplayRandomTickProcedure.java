package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.init.UnseenWorldModParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;

public class GreenishBurlyWoodLeavesClientDisplayRandomTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.getBlockState(BlockPos.containing(x, y - 1, z)).canOcclude() && Math.random() < 0.125) {
			world.addParticle((SimpleParticleType) (UnseenWorldModParticleTypes.GREENISH_PARTICLE.get()), x, (y - 0.5), z, 0, (-0.1), 0);
		}
	}
}
