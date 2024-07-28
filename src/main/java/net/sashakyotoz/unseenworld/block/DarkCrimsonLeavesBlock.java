
package net.sashakyotoz.unseenworld.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.OptionalInt;

public class DarkCrimsonLeavesBlock extends Block implements BonemealableBlock, SimpleWaterloggedBlock, net.minecraftforge.common.IForgeShearable {
	public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 9);
	public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

	public DarkCrimsonLeavesBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(DISTANCE, 8).setValue(PERSISTENT, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
	}

	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(DISTANCE) >= 8 && !state.getValue(PERSISTENT);
	}

	private boolean decaying(BlockState state) {
		return !state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 8;
	}

	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
		level.setBlock(pos, updateDistance(state, level, pos), 3);
	}
	public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos blockPos, BlockPos p_54445_) {
		if (state.getValue(WATERLOGGED))
			accessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		int i = getDistanceAt(blockState) + 1;
		if (i != 1 || state.getValue(DISTANCE) != i)
			accessor.scheduleTick(blockPos, this, 1);
		return state;
	}

	private static BlockState updateDistance(BlockState state, LevelAccessor accessor, BlockPos pos) {
		int i = 9;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for (Direction direction : Direction.values()) {
			blockpos$mutableblockpos.setWithOffset(pos, direction);
			i = Math.min(i, getDistanceAt(accessor.getBlockState(blockpos$mutableblockpos)) + 1);
			if (i == 1) {
				break;
			}
		}
		return state.setValue(DISTANCE, i);
	}

	private static int getDistanceAt(BlockState state) {
		return getOptionalDistanceAt(state).orElse(9);
	}
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
		if (this.decaying(state)) {
			dropResources(state, level, pos);
			level.removeBlock(pos, false);
		}
	}

	public static OptionalInt getOptionalDistanceAt(BlockState state) {
		if (state.is(BlockTags.LOGS)) {
			return OptionalInt.of(0);
		} else {
			return state.hasProperty(DISTANCE) ? OptionalInt.of(state.getValue(DISTANCE)) : OptionalInt.empty();
		}
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DISTANCE, PERSISTENT, WATERLOGGED,AXIS);
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		BlockState blockstate = this.defaultBlockState().setValue(PERSISTENT, Boolean.TRUE).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		return updateDistance(blockstate, context.getLevel(), context.getClickedPos());
	}
	@Override
	public boolean isValidBonemealTarget(LevelReader reader, BlockPos pos, BlockState state, boolean p_255711_) {
		return reader.getBlockState(pos.below()).isAir();
	}
	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return level.getBlockState(pos.below()).isAir();
	}
	@Override
	public void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state) {
		level.setBlock(pos.below(), UnseenWorldBlocks.DARK_CRIMSON_VINE_FLOWER.get().defaultBlockState(),3);
	}
}