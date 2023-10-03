
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;

public class ChimericBluePepperItem extends Item {
	public ChimericBluePepperItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(2).saturationMod(2f).build()));
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 20;
	}
}
