package net.sashakyotoz.common.blocks.custom.corals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CoralWallFanBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public class ModCoralWallFanBlock extends CoralWallFanBlock implements DualWaterloggedBlock {
    private final Block deadBlock;

    public ModCoralWallFanBlock(Block deadBlock, Properties properties) {
        super(deadBlock, properties);
        this.deadBlock = deadBlock;
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
    protected void tryScheduleDieTick(BlockState state, LevelAccessor level, BlockPos pos) {
        DualWaterloggedBlock.scheduleDeathIfDry(this, state, level, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        DualWaterloggedBlock.dieIfDry(state, level, pos, this.deadBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return DualWaterloggedBlock.updateLiving(this, state, level, pos, direction.getOpposite() == state.getValue(FACING));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return DualWaterloggedBlock.fluidOf(state);
    }
}
