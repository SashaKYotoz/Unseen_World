
package net.sashakyotoz.unseenworld.block;

import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

public class TanzaniteBlockBuddingBlock extends Block {
	private static final Direction[] DIRECTIONS = Direction.values();

	public TanzaniteBlockBuddingBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.BUDDING_AMETHYST).randomTicks().strength(4f, 4f).requiresCorrectToolForDrops().lightLevel(s -> 5).noLootTable());
	}

	public PushReaction getPistonPushReaction(BlockState p_152733_) {
		return PushReaction.DESTROY;
	}

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
		if (source.nextInt(5) == 0) {
			Direction direction = DIRECTIONS[source.nextInt(DIRECTIONS.length)];
			BlockPos blockpos = pos.relative(direction);
			BlockState blockstate = level.getBlockState(blockpos);
			Block block = null;
			if (canClusterGrowAtState(blockstate)) {
				block = UnseenWorldModBlocks.TANZANITE_CLUSTER.get();
			}
			if (block != null) {
				BlockState blockstate1 = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
				level.setBlockAndUpdate(blockpos, blockstate1);
			}
		}
	}

	public static boolean canClusterGrowAtState(BlockState state) {
		return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
		if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
			return tieredItem.getTier().getLevel() >= 1;
		return false;
	}
}
