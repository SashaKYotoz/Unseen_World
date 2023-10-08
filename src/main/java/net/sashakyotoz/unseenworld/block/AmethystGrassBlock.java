
package net.sashakyotoz.unseenworld.block;

import net.minecraft.world.level.block.BushBlock;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

public class AmethystGrassBlock extends BushBlock {
	public AmethystGrassBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.GRASS).strength(0f, 1f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 12)
				.noCollission().offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY));
	}

	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.DIRT_PATH) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL) || groundState.is(UnseenWorldModBlocks.RED_OOZE.get())
				|| groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get())
				|| groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(UnseenWorldModBlocks.TANZASHROOM_BLOCK.get());
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		return this.mayPlaceOn(groundState, worldIn, blockpos);
	}

	@OnlyIn(Dist.CLIENT)
	public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
		event.getBlockColors().register((bs, world, pos, index) -> world != null && pos != null ? Minecraft.getInstance().level.getBiome(pos).value().getSkyColor() : 8562943, UnseenWorldModBlocks.AMETHYST_GRASS.get());
	}
}
