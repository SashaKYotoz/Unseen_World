package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class TanzaniteAdapterBlock extends Block {
    public static final BooleanProperty ENABLED = BooleanProperty.of("enabled");

    public TanzaniteAdapterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ENABLED, false));
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(ENABLED) && world.getLuminance(pos.up()) == 14 ? 15 : 0;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state.with(ENABLED, world.getLuminance(pos.up()) == 14);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return state.get(ENABLED);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }
}