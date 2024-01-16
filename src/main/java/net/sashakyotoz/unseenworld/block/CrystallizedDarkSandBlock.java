
package net.sashakyotoz.unseenworld.block;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class CrystallizedDarkSandBlock extends FallingBlock {
	private final int dustColor;
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public CrystallizedDarkSandBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.SAND).sound(SoundType.SAND).strength(0.5f, 0.2f).requiresCorrectToolForDrops());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.dustColor = 24001590;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public int getDustColor(BlockState p_55970_, BlockGetter p_55971_, BlockPos p_55972_) {
		return this.dustColor;
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

	@OnlyIn(Dist.CLIENT)
	public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
		event.getBlockColors().register((bs, world, pos, index) -> {
			if (world != null && pos != null) {
				assert Minecraft.getInstance().level != null;
				return Minecraft.getInstance().level.getBiome(pos).value().getSkyColor();
			} else {
				return 8562943;
			}
		}, UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND.get());
	}
}
