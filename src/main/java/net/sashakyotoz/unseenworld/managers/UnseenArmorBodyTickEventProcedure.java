package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;

import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;

public class UnseenArmorBodyTickEventProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(UnseenWorldModItems.UNSEEN_ARMOR_HELMET.get()) && livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(UnseenWorldModItems.UNSEEN_ARMOR_CHESTPLATE.get())
        && livingEntity.getItemBySlot(EquipmentSlot.LEGS).is(UnseenWorldModItems.UNSEEN_ARMOR_LEGGINGS.get()) && livingEntity.getItemBySlot(EquipmentSlot.FEET).is(UnseenWorldModItems.UNSEEN_ARMOR_BOOTS.get())) {
            if (livingEntity.isShiftKeyDown() && !livingEntity.hasEffect(MobEffects.INVISIBILITY)) {
                if (!livingEntity.level().isClientSide())
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0));
                ItemStack chestplate = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
                if (chestplate.hurt(1, RandomSource.create(), null)) {
                    chestplate.shrink(1);
                    chestplate.setDamageValue(0);
                }
            }
            if (entity.isOnFire()) {
                if (!livingEntity.level().isClientSide())
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));
                ItemStack leggings = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
                if (leggings.hurt(1, RandomSource.create(), null)) {
                    leggings.shrink(1);
                    leggings.setDamageValue(0);
                }
                ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
                if (boots.hurt(1, RandomSource.create(), null)) {
                    boots.shrink(1);
                    boots.setDamageValue(0);
                }
            }
            if (entity.getAirSupply() <= 1) {
                entity.setAirSupply(entity.getAirSupply() + 1);
                ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
                if (helmet.hurt(1, RandomSource.create(), null)) {
                    helmet.shrink(1);
                    helmet.setDamageValue(0);
                }
            }
        }
    }
}
