package net.sashakyotoz.compat.accessory;

import net.lcc.sollib.platform.Services;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.common.items.custom.GrippingBundleItem;

import java.util.ArrayList;
import java.util.List;

public final class GrippingBundles {
    private GrippingBundles() {
    }

    public static List<ItemStack> worn(Player player) {
        List<ItemStack> bundles = new ArrayList<>();
        for (ItemStack stack : Services.ACCESSORY.getAccessories(player).values()) {
            if (stack != null && stack.getItem() instanceof GrippingBundleItem) {
                bundles.add(stack);
            }
        }
        return bundles;
    }

    public static List<ItemStack> find(Player player) {
        List<ItemStack> bundles = worn(player);
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.getItem() instanceof GrippingBundleItem) {
                bundles.add(stack);
            }
        }
        return bundles;
    }
}