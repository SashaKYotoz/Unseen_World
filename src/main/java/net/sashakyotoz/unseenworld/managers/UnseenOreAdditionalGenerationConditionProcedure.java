package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;

public class UnseenOreAdditionalGenerationConditionProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		double sx;
		double sy;
		double sz;
		if (!world.getBlockState(BlockPos.containing(x, y + 1, z)).canOcclude()) {
			sx = -4;
			for (int index0 = 0; index0 < (int) Mth.nextDouble(RandomSource.create(), 7, 9); index0++) {
				sy = -2;
				for (int index1 = 0; index1 < 4; index1++) {
					sz = -4;
					for (int index2 = 0; index2 < (int) Mth.nextDouble(RandomSource.create(), 7, 9); index2++) {
						if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == Blocks.DEEPSLATE) {
							{
								BlockPos _bp = BlockPos.containing(x + sx, y + sy, z + sz);
								BlockState _bs = Blocks.CALCITE.defaultBlockState();
								world.setBlock(_bp, _bs, 3);
							}
						}
						sz = sz + Mth.nextDouble(RandomSource.create(), 1, 2);
					}
					sy = sy + 1;
				}
				sx = sx + Mth.nextDouble(RandomSource.create(), 1, 2);
			}
		}
		return true;
	}
}
