package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public interface ModPlantVines {
    VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    BooleanProperty FRUITS = BlockStateProperties.BERRIES;

    default InteractionResult pickBerries(@Nullable Entity picker, BlockState state, Level world, BlockPos pos) {
        if (!state.getValue(FRUITS)) {
            return InteractionResult.PASS;
        }
        return FruitBearing.harvest(world, pos, state.setValue(FRUITS, false), this.getFruitToDrop(), picker);
    }

    ItemStack getFruitToDrop();

    static ToIntFunction<BlockState> getLuminanceSupplier(int luminance) {
        return state -> state.getValue(BlockStateProperties.BERRIES) ? luminance : 0;
    }
}
