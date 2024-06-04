
package net.sashakyotoz.unseenworld.block;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;

public class TanzashroomStemBlock extends Block {
	public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 3);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public TanzashroomStemBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.CRIMSON_STEM)
				.strength(1.5f, 5f).lightLevel(s -> 3));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STATE, 0));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 12;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		builder.add(STATE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
		super.neighborChanged(state, level, pos, neighborBlock, fromPos, moving);
		if (level.getBlockState(pos.above()).canOcclude() && !level.getBlockState(pos.below()).canOcclude()) {
			if (state.getBlock().getStateDefinition().getProperty("state") instanceof IntegerProperty property){
				switch (state.getValue(property)){
					case 0 -> level.setBlock(pos, state.setValue(property, 1), 3);
					case 1 -> level.setBlock(pos, state.setValue(property, 2), 3);
					case 2 -> level.setBlock(pos, state.setValue(property, 3), 3);
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
		event.getBlockColors().register((bs, world, pos, index) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.get(0.5D, 1.0D), UnseenWorldModBlocks.TANZASHROOM_STEM.get());
	}

	@OnlyIn(Dist.CLIENT)
	public static void itemColorLoad(RegisterColorHandlersEvent.Item event) {
		event.getItemColors().register((stack, index) -> GrassColor.get(0.5D, 1.0D), UnseenWorldModBlocks.TANZASHROOM_STEM.get());
	}
}
