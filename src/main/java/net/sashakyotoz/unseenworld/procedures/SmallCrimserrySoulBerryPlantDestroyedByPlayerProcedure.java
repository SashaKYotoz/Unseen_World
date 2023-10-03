package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.init.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.init.UnseenWorldModItems;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

public class SmallCrimserrySoulBerryPlantDestroyedByPlayerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		if ((blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _getip1 ? blockstate.getValue(_getip1) : -1) == 0) {
			if (world instanceof ServerLevel _level) {
				ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY.get()));
				entityToSpawn.setPickUpDelay(10);
				_level.addFreshEntity(entityToSpawn);
			}
		} else if ((blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _getip4 ? blockstate.getValue(_getip4) : -1) == 1) {
			for (int index0 = 0; index0 < (int) Mth.nextDouble(RandomSource.create(), 1, 2); index0++) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
			}
		} else if ((blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _getip8 ? blockstate.getValue(_getip8) : -1) == 2) {
			for (int index1 = 0; index1 < (int) Mth.nextDouble(RandomSource.create(), 2, 5); index1++) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(UnseenWorldModItems.CRIMSERRY_SOUL_BERRY_FOOD.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
			}
			for (int index2 = 0; index2 < (int) Mth.nextDouble(RandomSource.create(), 1, 3); index2++) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
			}
		}
	}
}
