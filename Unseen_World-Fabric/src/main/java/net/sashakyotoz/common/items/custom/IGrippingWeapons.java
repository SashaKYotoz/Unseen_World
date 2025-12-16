package net.sashakyotoz.common.items.custom;

import net.minecraft.item.ItemStack;

public interface IGrippingWeapons {
    static String getPhase(ItemStack stack) {
        return stack.getOrCreateNbt().getString("weapon_phase");
    }

    static void setPhase(ItemStack stack, String phase) {
        stack.getOrCreateNbt().putString("weapon_phase", phase);
    }
}