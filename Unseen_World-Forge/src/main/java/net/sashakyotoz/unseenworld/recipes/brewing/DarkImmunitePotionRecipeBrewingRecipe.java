
package net.sashakyotoz.unseenworld.recipes.brewing;

import net.minecraftforge.common.brewing.IBrewingRecipe;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

import net.sashakyotoz.unseenworld.registries.UnseenWorldPotions;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;


public class DarkImmunitePotionRecipeBrewingRecipe implements IBrewingRecipe {

	@Override
	public boolean isInput(ItemStack input) {
		return Ingredient.of(new ItemStack(UnseenWorldItems.NIGHTDEW_NECTAR_BOTTLE.get())).test(input);
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return Ingredient.of(new ItemStack(UnseenWorldItems.DARK_FREE_SOUL.get())).test(ingredient);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (isInput(input) && isIngredient(ingredient)) {
			return PotionUtils.setPotion(new ItemStack(Items.POTION), UnseenWorldPotions.DARK_IMMUNITE_POTION.get());
		}
		return ItemStack.EMPTY;
	}
}
