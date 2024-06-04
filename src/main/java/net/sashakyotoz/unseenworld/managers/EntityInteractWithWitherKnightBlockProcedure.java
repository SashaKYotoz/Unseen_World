package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.sashakyotoz.unseenworld.entity.TheWitherKnightEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.UnseenWorldMod;

public class EntityInteractWithWitherKnightBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!(world.getDifficulty() == Difficulty.PEACEFUL)) {
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			world.addParticle(ParticleTypes.ANGRY_VILLAGER, x, y, z, 0, 1, 0);
			UnseenWorldMod.queueServerWork(10, () -> {
				if (world instanceof ServerLevel level) {
					TheWitherKnightEntity entityToSpawn = UnseenWorldModEntities.THE_WITHER_KNIGHT.get().spawn(level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                }
			});
		}
	}
}