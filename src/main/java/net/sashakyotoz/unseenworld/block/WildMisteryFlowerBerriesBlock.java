
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModTags;

public class WildMisteryFlowerBerriesBlock extends FlowerBlock {
	public WildMisteryFlowerBerriesBlock() {
		super(() -> MobEffects.MOVEMENT_SPEED, 200,
				BlockBehaviour.Properties.of().instabreak().randomTicks().lightLevel(s -> 8));
	}
	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 40;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return  groundState.is(Blocks.SNOW_BLOCK) || groundState.is(Blocks.POWDER_SNOW) || groundState.is(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS)
				|| groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.DIRT) || groundState.is(Blocks.PODZOL)
				|| groundState.is(Blocks.ROOTED_DIRT) || groundState.is(Blocks.BLACKSTONE) || groundState.is(UnseenWorldModBlocks.RED_OOZE.get());
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
			player.spawnAtLocation(new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get()));
		}
		level.removeBlock(pos,true);
		return InteractionResult.SUCCESS;
	}
}
