
package net.sashakyotoz.unseenworld.recipes.brewing;

import net.minecraftforge.common.brewing.IBrewingRecipe;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;


public class RedTitaniumPoisonSwordRecipeBrewingRecipe implements IBrewingRecipe {

	@Override
	public boolean isInput(ItemStack input) {
		Item inputItem = input.getItem();
		return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION) && PotionUtils.getPotion(input) == Potions.POISON;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return Ingredient.of(new ItemStack(UnseenWorldItems.RED_TITANIUM_SWORD.get())).test(ingredient);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (isInput(input) && isIngredient(ingredient)) {
			return new ItemStack(UnseenWorldItems.RED_TITANIUM_POISONOUS_SWORD.get());
		}
		return ItemStack.EMPTY;
	}
}
