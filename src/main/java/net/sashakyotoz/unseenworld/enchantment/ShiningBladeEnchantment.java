
package net.sashakyotoz.unseenworld.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;

public class ShiningBladeEnchantment extends Enchantment {
	public ShiningBladeEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, slots);
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return super.canEnchant(stack) || stack.is(UnseenWorldItems.TANZANITE_STAFF.get());
	}
}
