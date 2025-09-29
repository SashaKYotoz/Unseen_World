
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;

public class DarkCrimsonBloomingVineBlock extends GrowingPlantBodyBlock implements BonemealableBlock,DarkCrimsonVine {
	public DarkCrimsonBloomingVineBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.TWISTING_VINES_PLANT).strength(1f, 5f).randomTicks().noCollission().noOcclusion().hasPostProcess((bs, br, bp) -> true)
				.emissiveRendering((bs, br, bp) -> true),Direction.DOWN, SHAPE, false);
		this.registerDefaultState(this.stateDefinition.any().setValue(BERRIES, Boolean.FALSE));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	protected GrowingPlantHeadBlock getHeadBlock() {
		return (GrowingPlantHeadBlock) UnseenWorldBlocks.DARK_CRIMSON_VINE_FLOWER.get();
	}

	protected BlockState updateHeadAfterConvertedFromBody(BlockState state, BlockState state1) {
		return state1.setValue(BERRIES, state.getValue(BERRIES));
	}

	public ItemStack getCloneItemStack(BlockGetter p_153007_, BlockPos p_153008_, BlockState p_153009_) {
		return new ItemStack(UnseenWorldItems.BERRIES_OF_BLOOMING_VINE.get());
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand p_153025_, BlockHitResult p_153026_) {
		return DarkCrimsonVine.use(player, state, level, pos);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BERRIES);
	}

	public boolean isValidBonemealTarget(LevelReader p_255942_, BlockPos p_153012_, BlockState state, boolean p_153014_) {
		return !state.getValue(BERRIES);
	}

	public boolean isBonemealSuccess(Level p_220943_, RandomSource p_220944_, BlockPos p_220945_, BlockState p_220946_) {
		return true;
	}

	public void performBonemeal(ServerLevel level, RandomSource p_220939_, BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(BERRIES, Boolean.TRUE), 2);
	}

	@Override
	public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
		return true;
	}

}
