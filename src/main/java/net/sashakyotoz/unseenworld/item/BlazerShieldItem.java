
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class BlazerShieldItem extends ShieldItem {
	public BlazerShieldItem() {
		super(new Item.Properties().durability(1240).fireResistant().rarity(Rarity.UNCOMMON));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BLOCK;
	}

	@Override
	public int getEnchantmentValue() {
		return 8;
	}
}
