
package net.sashakyotoz.unseenworld.block;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;

public class DarkCrimsonBloomingVineBlock extends GrowingPlantBodyBlock implements BonemealableBlock,DarkCrimsonVine {
	public DarkCrimsonBloomingVineBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.TWISTING_VINES_PLANT).strength(1f, 5f).randomTicks().requiresCorrectToolForDrops().noCollission().noOcclusion().hasPostProcess((bs, br, bp) -> true)
				.emissiveRendering((bs, br, bp) -> true).isRedstoneConductor((bs, br, bp) -> false),Direction.DOWN, SHAPE, false);
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
		return (GrowingPlantHeadBlock) UnseenWorldModBlocks.DARK_CRIMSON_VINE_FLOWER.get();
	}

	protected BlockState updateHeadAfterConvertedFromBody(BlockState p_153028_, BlockState p_153029_) {
		return p_153029_.setValue(BERRIES, p_153028_.getValue(BERRIES));
	}

	public ItemStack getCloneItemStack(BlockGetter p_153007_, BlockPos p_153008_, BlockState p_153009_) {
		return new ItemStack(UnseenWorldModItems.BERRIESFROM_BLOOMING_VINE.get());
	}

	public InteractionResult use(BlockState p_153021_, Level p_153022_, BlockPos p_153023_, Player p_153024_, InteractionHand p_153025_, BlockHitResult p_153026_) {
		return DarkCrimsonVine.use(p_153024_, p_153021_, p_153022_, p_153023_);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153031_) {
		p_153031_.add(BERRIES);
	}

	public boolean isValidBonemealTarget(LevelReader p_255942_, BlockPos p_153012_, BlockState p_153013_, boolean p_153014_) {
		return !p_153013_.getValue(BERRIES);
	}

	public boolean isBonemealSuccess(Level p_220943_, RandomSource p_220944_, BlockPos p_220945_, BlockState p_220946_) {
		return true;
	}

	public void performBonemeal(ServerLevel p_220938_, RandomSource p_220939_, BlockPos p_220940_, BlockState p_220941_) {
		p_220938_.setBlock(p_220940_, p_220941_.setValue(BERRIES, Boolean.valueOf(true)), 2);
	}

	@Override
	public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
		return true;
	}

}
