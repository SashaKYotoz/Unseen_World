
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class TanzaniteClusterBlock extends AmethystBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	protected final VoxelShape northAabb;
	protected final VoxelShape southAabb;
	protected final VoxelShape eastAabb;
	protected final VoxelShape westAabb;
	protected final VoxelShape upAabb;
	protected final VoxelShape downAabb;

	public TanzaniteClusterBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).strength(1f, 4f).lightLevel(s -> 2).requiresCorrectToolForDrops().noCollission().noOcclusion().hasPostProcess((bs, br, bp) -> true)
				.emissiveRendering((bs, br, bp) -> true).isRedstoneConductor((bs, br, bp) -> false));
		int p_152015_ = 7;
		int p_152016_ = 3;
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.UP));
		this.upAabb = Block.box(p_152016_, 0.0D, p_152016_, 16 - p_152016_, p_152015_, 16 - p_152016_);
		this.downAabb = Block.box(p_152016_, 16 - p_152015_, p_152016_, 16 - p_152016_, 16.0D, 16 - p_152016_);
		this.northAabb = Block.box(p_152016_, p_152016_, 16 - p_152015_, 16 - p_152016_, 16 - p_152016_, 16.0D);
		this.southAabb = Block.box(p_152016_, p_152016_, 0.0D, 16 - p_152016_, 16 - p_152016_, p_152015_);
		this.eastAabb = Block.box(0.0D, p_152016_, p_152016_, p_152015_, 16 - p_152016_, 16 - p_152016_);
		this.westAabb = Block.box(16 - p_152015_, p_152016_, p_152016_, 16.0D, 16 - p_152016_, 16 - p_152016_);
	}

	public VoxelShape getShape(BlockState state, BlockGetter p_152022_, BlockPos p_152023_, CollisionContext p_152024_) {
		Direction direction = state.getValue(FACING);
		switch (direction) {
			case NORTH :
				return this.northAabb;
			case SOUTH :
				return this.southAabb;
			case EAST :
				return this.eastAabb;
			case WEST :
				return this.westAabb;
			case DOWN :
				return this.downAabb;
			case UP :
			default :
				return this.upAabb;
		}
	}

	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos p_152028_) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = p_152028_.relative(direction.getOpposite());
		return reader.getBlockState(blockpos).isFaceSturdy(reader, blockpos, direction);
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState p_152038_, LevelAccessor p_152039_, BlockPos p_152040_, BlockPos p_152041_) {
		if (state.getValue(WATERLOGGED)) {
			p_152039_.scheduleTick(p_152040_, Fluids.WATER, Fluids.WATER.getTickDelay(p_152039_));
		}
		return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(p_152039_, p_152040_) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, p_152038_, p_152039_, p_152040_, p_152041_);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		LevelAccessor levelaccessor = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(FACING, context.getClickedFace());
	}

	public BlockState rotate(BlockState state, Rotation p_152034_) {
		return state.setValue(FACING, p_152034_.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror p_152031_) {
		return state.rotate(p_152031_.getRotation(state.getValue(FACING)));
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, FACING);
	}

	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}
}
