package net.sashakyotoz.common.blocks.custom.corals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseCoralPlantBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public class ModBaseCoralPlantBlock extends BaseCoralPlantBlock implements DualWaterloggedBlock {
    public ModBaseCoralPlantBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, true).setValue(DARK_WATER, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, DARK_WATER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return DualWaterloggedBlock.placementState(this.defaultBlockState(), context);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return DualWaterloggedBlock.updateAttached(state, level, pos, direction == Direction.DOWN);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return DualWaterloggedBlock.fluidOf(state);
    }
}
