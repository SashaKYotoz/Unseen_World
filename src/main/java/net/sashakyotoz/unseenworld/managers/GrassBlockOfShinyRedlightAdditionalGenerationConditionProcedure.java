package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class GrassBlockOfShinyRedlightAdditionalGenerationConditionProcedure {
    public static boolean execute(LevelAccessor world, double x, double y, double z) {
        double sx;
        double sy;
        double sz;
        if (!world.getBlockState(BlockPos.containing(x, y + 1, z)).canOcclude()) {
            sx = -3;
            for (int index0 = 0; index0 < 6; index0++) {
                sy = -1;
                for (int index1 = 0; index1 < 3; index1++) {
                    sz = -3;
                    for (int index2 = 0; index2 < 6; index2++) {
                        if (((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.DARK_GRASS.get()
                                || (world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get())
                                && !world.getBlockState(BlockPos.containing(x + sx, y + sy + 1, z + sz)).canOcclude()) {
                            if (Math.random() < 0.5) {
                                BlockPos blockPos = BlockPos.containing(x + sx, y + sy, z + sz);
                                BlockState _bs = UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get().defaultBlockState();
                                world.setBlock(blockPos, _bs, 3);
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
