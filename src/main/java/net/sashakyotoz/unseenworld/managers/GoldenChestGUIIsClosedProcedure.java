package net.sashakyotoz.unseenworld.managers;

import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;

public class GoldenChestGUIIsClosedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock().getStateDefinition().getProperty("open") instanceof BooleanProperty _booleanProp)
            world.setBlock(pos, blockState.setValue(_booleanProp, false), 3);
        if (world instanceof Level level) {
            if (!level.isClientSide()) {
                level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.CHEST_CLOSE, SoundSource.PLAYERS, 1, 1);
            } else {
                level.playLocalSound(x, y, z, SoundEvents.CHEST_CLOSE, SoundSource.PLAYERS, 1, 1, false);
            }
        }
    }
}
