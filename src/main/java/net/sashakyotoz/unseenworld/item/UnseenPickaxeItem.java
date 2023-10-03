
package net.sashakyotoz.unseenworld.item;

import net.sashakyotoz.unseenworld.init.UnseenWorldModItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class UnseenPickaxeItem extends PickaxeItem {
	public UnseenPickaxeItem() {
		super(new Tier() {
			public int getUses() {
				return 1200;
			}

			public float getSpeed() {
				return 12f;
			}

			public float getAttackDamageBonus() {
				return 5f;
			}

			public int getLevel() {
				return 4;
			}

			public int getEnchantmentValue() {
				return 35;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(UnseenWorldModItems.UNSEEN_INGOT.get()));
			}
		}, 1, -2.8f, new Item.Properties().fireResistant());
	}
}
