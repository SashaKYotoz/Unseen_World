
package net.sashakyotoz.unseenworld.enchantment;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class MiningbootsEnchantment extends Enchantment {
	public MiningbootsEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, slots);
	}

	@Override
	public int getDamageProtection(int level, DamageSource source) {
		return level * 1;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return List.of(Enchantments.UNBREAKING, Enchantments.MENDING, Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.THORNS).contains(ench);
	}
}
