
package net.sashakyotoz.unseenworld.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.UnseenWorldModConfigs;

import java.util.List;

public class LifeSteelEnchantment extends Enchantment {
	public LifeSteelEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, slots);
	}
	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public void doPostAttack(LivingEntity livingEntity, Entity entity, int amount) {
		UnseenWorldMod.queueServerWork(10,()->{
			if(entity instanceof LivingEntity livingEntity1){
				livingEntity1.hurt(livingEntity.damageSources().mobAttack(livingEntity1),1+ UnseenWorldModConfigs.LIFE_STEELING_POWER.get());
			}
		});
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return List.of(Enchantments.SHARPNESS, Enchantments.UNBREAKING, Enchantments.MENDING, Enchantments.MOB_LOOTING).contains(ench);
	}
}
