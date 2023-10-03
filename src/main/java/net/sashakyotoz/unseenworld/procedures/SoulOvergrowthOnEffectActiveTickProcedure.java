package net.sashakyotoz.unseenworld.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.sashakyotoz.unseenworld.init.UnseenWorldModBlocks;

public class SoulOvergrowthOnEffectActiveTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		sx = -3;
		for (int index0 = 0; index0 < 5; index0++) {
			sy = -1;
			for (int index1 = 0; index1 < 3; index1++) {
				sz = -3;
				for (int index2 = 0; index2 < 5; index2++) {
					if (((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == Blocks.GRASS_BLOCK || (world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.DARK_GRASS.get())
							&& (world.getBlockState(BlockPos.containing(x + sx, y + sy + 1, z + sz))).getBlock() == Blocks.AIR) {
						if (Math.random() < 0.025) {
							{
								Entity _entToDamage = entity;
								_entToDamage.hurt(new DamageSource(_entToDamage.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)), 1);
							}
							world.setBlock(BlockPos.containing(x + sx, y + sy + 1, z + sz), UnseenWorldModBlocks.GROWN_CRIMSERRY_SOUL_BERRY.get().defaultBlockState(), 3);
						} else if (Math.random() < 0.025) {
							world.setBlock(BlockPos.containing(x + sx, y + sy + 1, z + sz), UnseenWorldModBlocks.AMETHYST_GRASS.get().defaultBlockState(), 3);
						} else if (Math.random() < 0.035) {
							world.setBlock(BlockPos.containing(x + sx, y + sy + 1, z + sz), UnseenWorldModBlocks.TEALIVY_PLUMERIA.get().defaultBlockState(), 3);
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
