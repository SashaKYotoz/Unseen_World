
package net.sashakyotoz.unseenworld.item;

import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class NatureriumShovelItem extends ShovelItem {
	public NatureriumShovelItem() {
		super(new Tier() {
			public int getUses() {
				return 750;
			}

			public float getSpeed() {
				return 10f;
			}

			public float getAttackDamageBonus() {
				return 3.5f;
			}

			public int getLevel() {
				return 4;
			}

			public int getEnchantmentValue() {
				return 28;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()));
			}
		}, 1, -3f, new Item.Properties());
	}
}
