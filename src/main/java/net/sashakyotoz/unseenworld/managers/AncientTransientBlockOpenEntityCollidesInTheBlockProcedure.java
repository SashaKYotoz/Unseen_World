package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class AncientTransientBlockOpenEntityCollidesInTheBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double sx;
		double sy;
		double sz;
		sx = -4;
		for (int index0 = 0; index0 < 8; index0++) {
			sy = -4;
			for (int index1 = 0; index1 < 8; index1++) {
				sz = -4;
				for (int index2 = 0; index2 < 8; index2++) {
					if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_OPEN.get()) {
							BlockPos _bp = BlockPos.containing(x + sx, y + sy, z + sz);
							BlockState _bs = UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_CLOSE.get().defaultBlockState();
							world.setBlock(_bp, _bs, 3);

					}
					sz = sz + 1;
				}
				sy = sy + 1;
			}
			sx = sx + 1;
		}
	}
}
