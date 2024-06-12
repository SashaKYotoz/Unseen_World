package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;

public class SoulOvergrowthOnEffectActiveTickProcedure {
	public static void execute(LevelAccessor level, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double sx, sy, sz;
		sx = -3;
		for (int i = 0; i < 5; i++) {
			sy = -1;
			for (int j = 0; j < 3; j++) {
				sz = -3;
				for (int k = 0; k < 5; k++) {
					if (((level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).is(Blocks.GRASS_BLOCK) || (level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).is(UnseenWorldModBlocks.DARK_GRASS_BLOCK.get()))
							&& (level.getBlockState(BlockPos.containing(x + sx, y + sy + 1, z + sz))).isAir()) {
						if (Math.random() < 0.025) {
								entity.hurt(entity.damageSources().generic(), 1);
							level.setBlock(BlockPos.containing(x + sx, y + sy + 1, z + sz), UnseenWorldModBlocks.WILD_CRIMSERRY_SOUL_FLOWER.get().defaultBlockState(), 3);
						} else if (Math.random() < 0.025) {
							level.setBlock(BlockPos.containing(x + sx, y + sy + 1, z + sz), UnseenWorldModBlocks.AMETHYST_GRASS.get().defaultBlockState(), 3);
						} else if (Math.random() < 0.035) {
							level.setBlock(BlockPos.containing(x + sx, y + sy + 1, z + sz), UnseenWorldModBlocks.TEALIVY_PLUMERIA.get().defaultBlockState(), 3);
						}
					}
					sz = sz + 1;
				}
				sy = sy + 1;
			}
			sx = sx + 1;
		}
	}
}
