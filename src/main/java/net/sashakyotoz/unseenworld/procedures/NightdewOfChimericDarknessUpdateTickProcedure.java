package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class NightdewOfChimericDarknessUpdateTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		if ((blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _getip1 ? blockstate.getValue(_getip1) : -1) == 0) {
			if (Math.random() < 0.05) {
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
			}
		} else if ((blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty _getip4 ? blockstate.getValue(_getip4) : -1) == 1) {
			if (Math.random() < 0.05 && (world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == Blocks.AIR
					&& !((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get()
							&& (world.getBlockState(BlockPos.containing(x, y - 2, z))).getBlock() == UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get())) {
				world.setBlock(BlockPos.containing(x, y + 1, z), UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get().defaultBlockState(), 3);
			}
		}
	}
}
