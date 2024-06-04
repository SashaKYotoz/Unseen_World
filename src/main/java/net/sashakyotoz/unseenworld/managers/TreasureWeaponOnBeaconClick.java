package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.block.entity.BeaconOfWeaponsBlockEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;

public class TreasureWeaponOnBeaconClick {
    public static void onClick(Level level, BlockPos pos, Player player, ItemStack itemstack) {
        if (level.getBlockState(pos).is(UnseenWorldModBlocks.BEACON_OF_WEAPONS.get()) && !level.isClientSide()) {
            if (level.getBlockState(pos.east(2)).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get()
                    && level.getBlockState(pos.west(2)).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get() && level.getBlockState(pos.south(2)).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get()
                    && level.getBlockState(pos.north(2)).getBlock() == UnseenWorldModBlocks.BEACON_RUNE.get()) {
                if (itemstack.is(UnseenWorldModItems.HEAVY_CLAYMORE.get())) {
                    ItemStack stack = ItemStack.EMPTY;
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.VOID_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.VOID_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
                        itemstack.setHoverName(Component.literal("Void Claymore"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 3);
                        itemstack.setHoverName(Component.literal("Fire Claymore"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 2);
                        itemstack.setHoverName(Component.literal("Nature Claymore"));
                    }
                    ItemStack finalStack = stack;
                    player.getInventory().clearOrCountMatchingItems(p -> finalStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                    BeaconOfWeaponsBlockEntity.item = stack;
                    UnseenWorldMod.queueServerWork(60, () -> BeaconOfWeaponsBlockEntity.item = ItemStack.EMPTY);
                } else if (itemstack.is(UnseenWorldModItems.LIGHT_TULVAR.get())) {
                    ItemStack stack = ItemStack.EMPTY;
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.VOID_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.VOID_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
                        itemstack.setHoverName(Component.literal("Void Tulvar"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 3);
                        itemstack.setHoverName(Component.literal("Fire Tulvar"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 2);
                        itemstack.setHoverName(Component.literal("Nature Tulvar"));
                    }
                    ItemStack finalStack = stack;
                    player.getInventory().clearOrCountMatchingItems(p -> finalStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                    BeaconOfWeaponsBlockEntity.item = stack;
                    UnseenWorldMod.queueServerWork(60, () -> BeaconOfWeaponsBlockEntity.item = ItemStack.EMPTY);
                } else if (itemstack.is(UnseenWorldModItems.FIERY_SABER.get())) {
                    ItemStack stack = ItemStack.EMPTY;
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.VOID_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.VOID_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
                        itemstack.setHoverName(Component.literal("Void Saber"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 3);
                        itemstack.setHoverName(Component.literal("Fire Saber"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 2);
                        itemstack.setHoverName(Component.literal("Nature Saber"));
                    }
                    ItemStack finalStack = stack;
                    player.getInventory().clearOrCountMatchingItems(p -> finalStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                    BeaconOfWeaponsBlockEntity.item = stack;
                    UnseenWorldMod.queueServerWork(60, () -> BeaconOfWeaponsBlockEntity.item = ItemStack.EMPTY);
                } else if (itemstack.is(UnseenWorldModItems.BLASTING_LANCER.get())) {
                    ItemStack stack = ItemStack.EMPTY;
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.VOID_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.VOID_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 1);
                        itemstack.setHoverName(Component.literal("Void Blasting Lancer"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 3);
                        itemstack.setHoverName(Component.literal("Fire Blasting Lancer"));
                    }
                    if (player.getInventory().contains(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()))) {
                        stack = new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get());
                        itemstack.getOrCreateTag().putDouble("CustomModelData", 2);
                        itemstack.setHoverName(Component.literal("Nature Blasting Lancer"));
                    }
                    ItemStack finalStack = stack;
                    player.getInventory().clearOrCountMatchingItems(p -> finalStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                    BeaconOfWeaponsBlockEntity.item = stack;
                    UnseenWorldMod.queueServerWork(60, () -> BeaconOfWeaponsBlockEntity.item = ItemStack.EMPTY);
                }
            }
        }
    }
}
