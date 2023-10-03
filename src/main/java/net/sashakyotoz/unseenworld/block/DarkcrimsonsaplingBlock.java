
package net.sashakyotoz.unseenworld.block;

import net.sashakyotoz.unseenworld.init.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.procedures.DarkcrimsonsaplingUpdateTickProcedure;
import net.sashakyotoz.unseenworld.procedures.SaplingManagerProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

public class DarkcrimsonsaplingBlock extends FlowerBlock implements BonemealableBlock {
	public DarkcrimsonsaplingBlock() {
		super(() -> MobEffects.MOVEMENT_SPEED, 100, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).randomTicks().sound(SoundType.GRASS).instabreak().noCollission());
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
		return 60;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(UnseenWorldModBlocks.RED_OOZE.get())
				|| groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get());
	}
	@Override
	public boolean isValidBonemealTarget(LevelReader p_256559_, BlockPos p_50898_, BlockState p_50899_, boolean p_50900_) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level p_220878_, RandomSource p_220879_, BlockPos p_220880_, BlockState p_220881_) {
		return (double)p_220878_.random.nextFloat() < 0.35D;
	}

	@Override
	public void performBonemeal(ServerLevel p_220874_, RandomSource p_220875_, BlockPos p_220876_, BlockState p_220877_) {
		SaplingManagerProcedure.execute(p_220874_, p_220876_.getX(), p_220876_.getY(), p_220876_.getZ());
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		return this.mayPlaceOn(groundState, worldIn, blockpos);
	}

	@Override
	public void randomTick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		DarkcrimsonsaplingUpdateTickProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}
}
