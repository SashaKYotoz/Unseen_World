package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class PurpleBerriesRightClickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.GRASS_BLOCK) && blockstate.is(BlockTags.create(new ResourceLocation("minecraft:animals_spawnable_on")))
				&& world.getMaxLocalRawBrightness(BlockPos.containing(x, y, z)) <= 8) {
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
			world.setBlock(BlockPos.containing(x, y + 1, z), UnseenWorldModBlocks.MISTERYFLOWER_SAPLING.get().defaultBlockState(), 3);
		} else if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.GRASS_BLOCK && world.getMaxLocalRawBrightness(BlockPos.containing(x, y, z)) <= 12) {
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
			if (Math.random() < 0.5) {
				world.setBlock(BlockPos.containing(x, y + 1, z), UnseenWorldModBlocks.MISTERYFLOWER_SAPLING.get().defaultBlockState(), 3);
			}
		}
	}
}
