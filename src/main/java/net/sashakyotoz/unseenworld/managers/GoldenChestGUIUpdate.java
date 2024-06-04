package net.sashakyotoz.unseenworld.managers;

import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;

public class GoldenChestGUIUpdate {
    public static void execute(Level world, double x, double y, double z) {
        BlockPos blockPos = BlockPos.containing(x, y, z);
        BlockState state = world.getBlockState(blockPos);
        if (state.getBlock().getStateDefinition().getProperty("open") instanceof BooleanProperty property)
            world.setBlock(blockPos, state.setValue(property, !state.getValue(property)), 3);
        if (!world.isClientSide()) {
            world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 1, 1);
        } else {
            world.playLocalSound(x, y, z, SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 1, 1, false);
        }
    }
}