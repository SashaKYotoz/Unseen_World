
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;

public class CookedDustyPinkMaxorFishItem extends Item {
	public CookedDustyPinkMaxorFishItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(3f).build()));
	}

	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
		return 0F;
	}
}
