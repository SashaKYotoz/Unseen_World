
package net.sashakyotoz.unseenworld.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class GravitySpikeEnchantment extends Enchantment {
	public GravitySpikeEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, slots);
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return List.of(Enchantments.UNBREAKING, Enchantments.MENDING, Enchantments.ALL_DAMAGE_PROTECTION).contains(ench);
	}
}
