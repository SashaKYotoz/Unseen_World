package net.sashakyotoz.common.blocks.custom.corals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public class ModBaseCoralWallFanBlock extends BaseCoralWallFanBlock implements DualWaterloggedBlock {
    public ModBaseCoralWallFanBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, true).setValue(DARK_WATER, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, DARK_WATER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return DualWaterloggedBlock.placementState(super.getStateForPlacement(context), context);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return DualWaterloggedBlock.updateAttached(state, level, pos, direction.getOpposite() == state.getValue(FACING));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return DualWaterloggedBlock.fluidOf(state);
    }
}
