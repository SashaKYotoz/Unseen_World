
package net.sashakyotoz.unseenworld.item;

import net.sashakyotoz.unseenworld.init.UnseenWorldModItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.AxeItem;

public class Void_ingotAxeItem extends AxeItem {
	public Void_ingotAxeItem() {
		super(new Tier() {
			public int getUses() {
				return 2350;
			}

			public float getSpeed() {
				return 13.5f;
			}

			public float getAttackDamageBonus() {
				return 12f;
			}

			public int getLevel() {
				return 5;
			}

			public int getEnchantmentValue() {
				return 30;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(UnseenWorldModItems.VOID_INGOT_INGOT.get()));
			}
		}, 1, -3f, new Item.Properties().fireResistant());
	}
}
