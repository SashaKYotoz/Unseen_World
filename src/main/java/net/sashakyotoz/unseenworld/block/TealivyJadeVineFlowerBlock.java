
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.items.ItemHandlerHelper;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;

public class TealivyJadeVineFlowerBlock extends DoublePlantBlock {
	public TealivyJadeVineFlowerBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).sound(SoundType.ROOTS).instabreak().lightLevel(s -> 10).noCollission().offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY));
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 40;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 20;
	}

	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(UnseenWorldModBlocks.DARK_GRASS_BLOCK.get()) || groundState.is(UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get()) || groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get())
				|| groundState.is(UnseenWorldModBlocks.RED_OOZE.get()) || groundState.is(Blocks.MOSS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL);
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		if (blockstate.getValue(HALF) == DoubleBlockHalf.UPPER)
			return groundState.is(this) && groundState.getValue(HALF) == DoubleBlockHalf.LOWER;
		else
			return this.mayPlaceOn(groundState, worldIn, blockpos);
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return PlantType.CAVE;
	}

	@Override
	public InteractionResult use(BlockState blockstate, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		super.use(blockstate, level, pos, player, hand, hit);
		if (player.getMainHandItem().is(Items.GLASS_BOTTLE)) {
			ItemStack stack = new ItemStack(Items.GLASS_BOTTLE);
			player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
			if (player.getInventory().contains(ItemStack.EMPTY)) {
				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(UnseenWorldModItems.NIGHTDEW_NECTAR_BOTTLE.get()));
			} else
				player.spawnAtLocation(new ItemStack(UnseenWorldModItems.NIGHTDEW_NECTAR_BOTTLE.get()));
			Block.dropResources(level.getBlockState(pos), level, pos, null);
			level.destroyBlock(pos, false);
		}
		return InteractionResult.SUCCESS;
	}
}
