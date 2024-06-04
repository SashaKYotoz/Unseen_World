package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModMobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ArmorAbilitiesProcedure {
    public static void execute(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity))
            return;
        ItemStack headSlot = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestSlot = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legsSlot = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feetSlot = livingEntity.getItemBySlot(EquipmentSlot.FEET);
        if (isVoidIngotArmorSet(headSlot, chestSlot, legsSlot, feetSlot) && livingEntity.hasEffect(UnseenWorldModMobEffects.DARK_VOID.get())) {
            livingEntity.removeEffect(UnseenWorldModMobEffects.DARK_VOID.get());
        } else if (isRedTitaniumArmorSet(headSlot, chestSlot, legsSlot, feetSlot) && !(livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE))) {
            if (!livingEntity.level().isClientSide()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));
            }
        } else if (isKnightArmorSet(headSlot, chestSlot, legsSlot, feetSlot)) {
            if (!(livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) && !livingEntity.level().isClientSide())
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120, 1));
            entity.setNoGravity(entity.isShiftKeyDown());
        }
    }

    private static boolean isVoidIngotArmorSet(ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
        return head.getItem() == UnseenWorldModItems.VOIDINGOT_ARMOR_HELMET.get()
                && chest.getItem() == UnseenWorldModItems.VOIDINGOT_ARMOR_CHESTPLATE.get()
                && legs.getItem() == UnseenWorldModItems.VOIDINGOT_ARMOR_LEGGINGS.get()
                && feet.getItem() == UnseenWorldModItems.VOIDINGOT_ARMOR_BOOTS.get();
    }

    private static boolean isRedTitaniumArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.getItem() == UnseenWorldModItems.RED_TITANIUM_ARMOR_BOOTS.get()
                && legsSlot.getItem() == UnseenWorldModItems.RED_TITANIUM_ARMOR_LEGGINGS.get()
                && chestSlot.getItem() == UnseenWorldModItems.RED_TITANIUM_ARMOR_CHESTPLATE.get()
                && headSlot.getItem() == UnseenWorldModItems.RED_TITANIUM_ARMOR_HELMET.get();
    }

    private static boolean isKnightArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.getItem() == UnseenWorldModItems.KNIGHT_ARMOR_BOOTS.get()
                && legsSlot.getItem() == UnseenWorldModItems.KNIGHT_ARMOR_LEGGINGS.get()
                && chestSlot.getItem() == UnseenWorldModItems.KNIGHT_ARMOR_CHESTPLATE.get()
                && headSlot.getItem() == UnseenWorldModItems.KNIGHT_ARMOR_HELMET.get();
    }
}
