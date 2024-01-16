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
        if ((UnseenWorldModItems.UNSEEN_ARMOR_HELMET.get() == (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)
                .getItem()) == ((UnseenWorldModItems.UNSEEN_ARMOR_CHESTPLATE.get() == (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
                .getItem()) == ((UnseenWorldModItems.UNSEEN_ARMOR_LEGGINGS.get() == (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)
                .getItem()) == (UnseenWorldModItems.UNSEEN_ARMOR_BOOTS.get() == (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem())))) {
            if (entity.isShiftKeyDown() && !(entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffects.INVISIBILITY))) {
                if (entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide())
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0));
                ItemStack chestplate = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY);
                if (chestplate.hurt(1, RandomSource.create(), null)) {
                    chestplate.shrink(1);
                    chestplate.setDamageValue(0);
                }
            }
            if (entity.isOnFire()) {
                if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                    _entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));
                ItemStack leggings = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY);
                if (leggings.hurt(1, RandomSource.create(), null)) {
                    leggings.shrink(1);
                    leggings.setDamageValue(0);
                }
                ItemStack boots = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY);
                if (boots.hurt(1, RandomSource.create(), null)) {
                    boots.shrink(1);
                    boots.setDamageValue(0);
                }
            }
            if (entity.getAirSupply() <= 1) {
                entity.setAirSupply(entity.getAirSupply() + 1);
                ItemStack helmet = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY);
                if (helmet.hurt(1, RandomSource.create(), null)) {
                    helmet.shrink(1);
                    helmet.setDamageValue(0);
                }
            }
        }
    }
}
