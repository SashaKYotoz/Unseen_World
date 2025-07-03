package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

public class AncientTransientBlockOpenEntityCollidesInTheBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double sx;
		double sy;
		double sz;
		sx = -4;
		for (int i = 0; i < 8; i++) {
			sy = -4;
			for (int j = 0; j < 8; j++) {
				sz = -4;
				for (int k = 0; k < 8; k++) {
					if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldBlocks.ANCIENT_TRANSIENT_BLOCK_OPEN.get()) {
							BlockPos _bp = BlockPos.containing(x + sx, y + sy, z + sz);
							BlockState _bs = UnseenWorldBlocks.ANCIENT_TRANSIENT_BLOCK_CLOSE.get().defaultBlockState();
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
