package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;

public class EntityInteractWithWitherKnightBlockProcedure {
	public static void execute(Level level, BlockPos pos) {
		if (!(level.getDifficulty() == Difficulty.PEACEFUL)) {
			level.removeBlock(pos,true);
			EventManager.waveFlaming(UnseenWorldParticleTypes.GOLDEN.get(),level,pos);
			UnseenWorldMod.queueServerWork(20, () -> {
				if (level instanceof ServerLevel serverLevel)
					UnseenWorldEntities.THE_WITHER_KNIGHT.get().spawn(serverLevel, pos.above(), MobSpawnType.MOB_SUMMONED);
			});
		}
	}
}