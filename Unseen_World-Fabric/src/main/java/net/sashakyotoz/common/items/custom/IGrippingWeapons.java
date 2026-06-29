package net.sashakyotoz.common.items.custom;

import net.minecraft.world.item.ItemStack;

public interface IGrippingWeapons {
    static String getPhase(ItemStack stack) {
        return stack.getOrCreateTag().getString("weapon_phase");
    }

    static void setPhase(ItemStack stack, String phase) {
        stack.getOrCreateTag().putString("weapon_phase", phase);
    }
}