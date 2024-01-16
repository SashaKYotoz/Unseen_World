package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class TealivyFireStaffProjectileHitsBlockProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        double sx;
        double sy;
        double sz;
        if (world instanceof Level level && !level.isClientSide()) {
            level.explode(null, x, y, z, 2, Level.ExplosionInteraction.BLOCK);
            sx = -3;
            for (int index0 = 0; index0 < 6; index0++) {
                sy = -3;
                for (int index1 = 0; index1 < 6; index1++) {
                    sz = -3;
                    for (int index2 = 0; index2 < 6; index2++) {
                        if ((world.getBlockState(BlockPos.containing(x + sx, y + sy + 1, z + sz))).getBlock() == Blocks.AIR && world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz)).canOcclude()) {
                            if (Math.random() < 0.25) {
                                world.setBlock(BlockPos.containing(x + sx, y + sy + 1, z + sz), Blocks.SOUL_FIRE.defaultBlockState(), 3);
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
}
