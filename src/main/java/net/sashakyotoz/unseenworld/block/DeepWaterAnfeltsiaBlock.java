
package net.sashakyotoz.unseenworld.block;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.*;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModFluids;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class DeepWaterAnfeltsiaBlock extends BushBlock implements SimpleWaterloggedBlock,net.minecraftforge.common.IForgeShearable {
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

	public DeepWaterAnfeltsiaBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).replaceable().noCollission().instabreak().sound(SoundType.WET_GRASS).pushReaction(PushReaction.DESTROY).lightLevel(l->4));
	}

	public VoxelShape getShape(BlockState p_154525_, BlockGetter p_154526_, BlockPos p_154527_, CollisionContext p_154528_) {
		return SHAPE;
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
		return (blockGetter.getBlockState(blockPos.above()).is(UnseenWorldModBlocks.DARK_WATER.get()) || blockGetter.getBlockState(blockPos.above()).is(Blocks.WATER)) && blockGetter.getBlockState(blockPos.below()).isSolid();
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState p_154532_, LevelAccessor p_154533_, BlockPos p_154534_, BlockPos p_154535_) {
		BlockState blockstate = super.updateShape(state, direction, p_154532_, p_154533_, p_154534_, p_154535_);
		if (!blockstate.isAir()) {
			p_154533_.scheduleTick(p_154534_, UnseenWorldModFluids.DARK_WATER.get(), UnseenWorldModFluids.DARK_WATER.get().getTickDelay(p_154533_));
		}
		return blockstate;
	}
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56388_) {
		p_56388_.add(WATERLOGGED);
	}
	public FluidState getFluidState(BlockState p_154537_) {
		return UnseenWorldModFluids.DARK_WATER.get().getSource(false);
	}

	@OnlyIn(Dist.CLIENT)
	public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
		event.getBlockColors().register((bs, world, pos, index) -> world != null && pos != null ? BiomeColors.getAverageWaterColor(world, pos) : -1, UnseenWorldModBlocks.DEEP_WATER_ANFELTSIA.get());
	}
}
