package net.sashakyotoz.common.items.combat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.sashakyotoz.common.items.custom.GrippingBundleItem;

import java.util.List;

public final class AbilityHelper {
    private static final double SURVIVAL_REACH = 4.5D;
    private static final double CREATIVE_REACH = 5.0D;

    private AbilityHelper() {
    }

    public static boolean deferToOffhandPlacement(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND || !(player.getOffhandItem().getItem() instanceof BlockItem)) {
            return false;
        }
        HitResult hit = player.pick(player.isCreative() ? CREATIVE_REACH : SURVIVAL_REACH, 1.0F, false);
        return hit.getType() == HitResult.Type.BLOCK;
    }

    public static void damage(ItemStack stack, LivingEntity user, int amount, InteractionHand hand) {
        stack.hurtAndBreak(amount, user, entity -> entity.broadcastBreakEvent(hand));
    }

    public static boolean hasAmmo(Player player, Item ammo) {
        if (player.getAbilities().instabuild) {
            return true;
        }
        if (holdsAmmo(player.getOffhandItem(), ammo)) {
            return true;
        }
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (holdsAmmo(inventory.getItem(slot), ammo)) {
                return true;
            }
        }
        return false;
    }

    public static boolean consumeAmmo(Player player, Item ammo) {
        if (player.getAbilities().instabuild) {
            return true;
        }
        if (takeAmmo(player.getOffhandItem(), ammo)) {
            return true;
        }
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (takeAmmo(inventory.getItem(slot), ammo)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFullStrength(Player player) {
        return player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    public static boolean isCriticalHit(Player player) {
        return isFullStrength(player)
                && player.fallDistance > 0.0F
                && !player.onGround()
                && !player.onClimbable()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger()
                && !player.isSprinting();
    }

    public static boolean isSweepHit(Player player) {
        return isFullStrength(player)
                && !isCriticalHit(player)
                && !player.isSprinting()
                && player.onGround()
                && (double) (player.walkDist - player.walkDistO) < (double) player.getSpeed();
    }

    public static void appendAbilityLines(String descriptionId, int abilityCount, List<Component> tooltip) {
        for (int index = 0; index < abilityCount; index++) {
            tooltip.add(Component.translatable(descriptionId + ".ability." + index).withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.translatable(descriptionId + ".lore").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
    }

    private static boolean holdsAmmo(ItemStack stack, Item ammo) {
        return stack.is(ammo) || stack.getItem() instanceof GrippingBundleItem && GrippingBundleItem.contains(stack, ammo);
    }

    private static boolean takeAmmo(ItemStack stack, Item ammo) {
        if (stack.is(ammo)) {
            stack.shrink(1);
            return true;
        }
        return stack.getItem() instanceof GrippingBundleItem && GrippingBundleItem.removeOne(stack, ammo);
    }
}