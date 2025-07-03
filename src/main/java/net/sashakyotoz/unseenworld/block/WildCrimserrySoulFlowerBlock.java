
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;

public class WildCrimserrySoulFlowerBlock extends FlowerBlock {
	public WildCrimserrySoulFlowerBlock() {
		super(() -> MobEffects.MOVEMENT_SPEED, 100, BlockBehaviour.Properties.copy(Blocks.PEONY).sound(SoundType.WEEPING_VINES).lightLevel(s -> 8).instabreak().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 12).noCollission());
	}

	@Override
	public int getEffectDuration() {
		return 100;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 100;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 10;
	}

	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(Blocks.GRASS_BLOCK) || groundState.is(UnseenWorldBlocks.DARK_GRASS_BLOCK.get()) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL) || groundState.is(Blocks.ROOTED_DIRT) || groundState.is(Blocks.STONE)
				|| groundState.is(UnseenWorldBlocks.RED_OOZE.get()) || groundState.is(UnseenWorldBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(UnseenWorldBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get());
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		return this.mayPlaceOn(groundState, worldIn, blockpos);
	}
	@Override
	public InteractionResult use(BlockState blockstate, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		super.use(blockstate, level, pos, player, hand, hit);
		int randomCountOfBerries = player.getRandom().nextIntBetweenInclusive(1,4) + 1;
		for (int i = 0; i < randomCountOfBerries; i++) {
			player.spawnAtLocation(new ItemStack(UnseenWorldItems.CRIMSERRY_SOUL_BERRY.get()));
		}
		level.removeBlock(pos,true);
		return InteractionResult.SUCCESS;
	}
}
