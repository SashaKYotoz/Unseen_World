package net.sashakyotoz.unseenworld.procedures;

import net.minecraft.world.item.Items;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.block.entity.BeaconOfWeaponsBlockEntity;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

public class LightTulvarRightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UnseenWorldModBlocks.BEACON_OF_WEAPONS.get() && (world.getBlockState(BlockPos.containing(x + 2, y, z))).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get()
				&& (world.getBlockState(BlockPos.containing(x - 2, y, z))).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get() && (world.getBlockState(BlockPos.containing(x, y, z - 2))).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get()
				&& (world.getBlockState(BlockPos.containing(x, y, z + 2))).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get()) {
			if (itemstack.getItem() == UnseenWorldModItems.LIGHT_TULVAR.get() && (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(UnseenWorldModItems.VOID_INGOT_INGOT.get())) : false)) {
				if (entity instanceof Player _player) {
					ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.VOID_INGOT_INGOT.get());
					_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
					BeaconOfWeaponsBlockEntity.item = _stktoremove;
					UnseenWorldMod.queueServerWork(60,()->{
						BeaconOfWeaponsBlockEntity.item = new ItemStack(Items.AIR);
					});
				}
				itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
				itemstack.setHoverName(Component.literal("Void Tulvar"));
			} else if (itemstack.getItem() == UnseenWorldModItems.LIGHT_TULVAR.get() && (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get())) : false)) {
				if (entity instanceof Player _player) {
					ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get());
					_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
					BeaconOfWeaponsBlockEntity.item = _stktoremove;
					UnseenWorldMod.queueServerWork(60,()->{
						BeaconOfWeaponsBlockEntity.item = new ItemStack(Items.AIR);
					});
				}
				itemstack.getOrCreateTag().putDouble("CustomModelData", 2);
				itemstack.setHoverName(Component.literal("Nature Tulvar"));
			} else if (itemstack.getItem() == UnseenWorldModItems.LIGHT_TULVAR.get() && (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get())) : false)) {
				if (entity instanceof Player _player) {
					ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get());
					_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
					BeaconOfWeaponsBlockEntity.item = _stktoremove;
					UnseenWorldMod.queueServerWork(60,()->{
						BeaconOfWeaponsBlockEntity.item = new ItemStack(Items.AIR);
					});
				}
				itemstack.getOrCreateTag().putDouble("CustomModelData", 3);
				itemstack.setHoverName(Component.literal("Fire Tulvar"));
			}
		}
	}
}
