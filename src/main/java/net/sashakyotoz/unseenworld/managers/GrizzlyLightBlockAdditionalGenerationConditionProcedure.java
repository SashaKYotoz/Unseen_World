package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;

public class GrizzlyLightBlockAdditionalGenerationConditionProcedure {
    public static boolean execute(LevelAccessor world, double x, double y, double z) {
        double sx;
        double sy;
        double sz;
        if ((world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == UnseenWorldModBlocks.GRIZZLY_LEAVES.get() && (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.AIR) {
            sx = -3;
            for (int index0 = 0; index0 < 6; index0++) {
                sy = -3;
                for (int index1 = 0; index1 < 6; index1++) {
                    sz = -3;
                    for (int index2 = 0; index2 < 6; index2++) {
                        if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.GRIZZLY_LEAVES.get()
                                && (world.getBlockState(BlockPos.containing(x + sx, (y + sy) - 1, z + sz))).getBlock() == Blocks.AIR) {
                            {
                                BlockPos pos = BlockPos.containing(x + sx, (y + sy) - 1, z + sz);
                                BlockState blockState = UnseenWorldModBlocks.GRIZZLY_LEAVES.get().defaultBlockState();
                                world.setBlock(pos, blockState, 3);
                            }
                            if (Math.random() < 0.25) {
                                if ((world.getBlockState(BlockPos.containing(x + sx, (y + sy) - 2, z + sz))).getBlock() == Blocks.AIR) {
                                    BlockPos blockPos = BlockPos.containing(x + sx, (y + sy) - 2, z + sz);
                                    BlockState state = UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get().defaultBlockState();
                                    world.setBlock(blockPos, state, 3);
                                }
                            }
                        }
                        sz = sz + 1;
                    }
                    sy = sy + 1;
                }
                sx = sx + 1;
            }
        }
        return true;
    }
}
