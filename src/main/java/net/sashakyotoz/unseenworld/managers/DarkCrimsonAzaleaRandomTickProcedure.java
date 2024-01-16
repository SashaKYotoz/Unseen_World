package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class DarkCrimsonAzaleaRandomTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        if (Math.random() < 0.0025) {
            world.addParticle(UnseenWorldModParticleTypes.GREENISH_PARTICLE.get(), x, y, z, 0.25, 1, 0.25);
            BlockPos _bp = BlockPos.containing(x, y, z);
            BlockState _bs = UnseenWorldModBlocks.DARK_CRIMSON_FLOWING_AZALIA.get().defaultBlockState();
            world.setBlock(_bp, _bs, 3);
        }
    }
}
