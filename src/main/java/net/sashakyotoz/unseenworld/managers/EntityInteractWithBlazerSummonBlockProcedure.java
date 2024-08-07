package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class EntityInteractWithBlazerSummonBlockProcedure {
	public static void execute(Level level, double x, double y, double z) {
		if (!(level.getDifficulty() == Difficulty.PEACEFUL)) {
			level.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			if (!level.isClientSide())
				level.explode(null, x, y, z, 2, Level.ExplosionInteraction.NONE);
			UnseenWorldMod.queueServerWork(20, () -> {
				if (level.getBlockState(BlockPos.containing(x, y + 1, z)).canOcclude()) {
						BlockPos pos = BlockPos.containing(x, y + 1, z);
						Block.dropResources(level.getBlockState(pos), level, BlockPos.containing(x, y, z), null);
						level.destroyBlock(pos, false);
				}
				if (level instanceof ServerLevel serverLevel) {
					Entity entityToSpawn = UnseenWorldEntities.THE_BLAZER.get().spawn(serverLevel, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
					if (entityToSpawn != null) {
						entityToSpawn.setYRot(level.getRandom().nextFloat() * 360F);
					}
				}
			});
		}
	}
}
