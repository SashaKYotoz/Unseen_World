package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;

import java.util.Map;

public class MisteryflowerWithFewBerriesUpdateTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (Math.random() < 0.05) {
			{
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockState _bs = UnseenWorldModBlocks.MISTERYFLOWER_BERRIES.get().defaultBlockState();
				world.setBlock(_bp, _bs, 3);
			}
			if (world instanceof ServerLevel _level)
				_level.sendParticles(UnseenWorldModParticleTypes.GREENISH_PARTICLE.get(), x, y, z, 12, 3, 3, 3, 1);
		}
	}
}
