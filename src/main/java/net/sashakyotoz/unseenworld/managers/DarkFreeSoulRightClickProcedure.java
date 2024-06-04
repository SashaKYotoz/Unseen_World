package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModMobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

public class DarkFreeSoulRightClickProcedure {
    public static void execute(Player player, ItemStack itemstack) {
        if (player == null)
            return;
        ItemStack itemStack = new ItemStack(UnseenWorldModItems.DARK_FREE_SOUL.get());
        player.getInventory().clearOrCountMatchingItems(p -> itemStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
        player.getCooldowns().addCooldown(itemstack.getItem(), 20);
        if (!player.level().isClientSide()) {
            if (Math.random() < 0.75)
                player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.DARK_IMMUNITE.get(), 100, 0, true, true));
            else if (Math.random() < 0.5)
                player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.REDUCED_OF_GRAVITY.get(), 80, 0, true, true));
            else if (Math.random() < 0.25)
                player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.SOUL_OVERGROWTH.get(), 60, 0, true, true));
            else if (Math.random() < 0.125)
                player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.METEORITESTROPHY.get(), 40, 0, true, true));
        }
    }
}
