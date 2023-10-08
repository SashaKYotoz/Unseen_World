
package net.sashakyotoz.unseenworld.item;

import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class AmethystHammerItem extends SwordItem {
	public AmethystHammerItem() {
		super(new Tier() {
			public int getUses() {
				return 1750;
			}

			public float getSpeed() {
				return 10f;
			}

			public float getAttackDamageBonus() {
				return 11f;
			}

			public int getLevel() {
				return 5;
			}

			public int getEnchantmentValue() {
				return 18;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(UnseenWorldModItems.TANZANITE_SHARD.get()));
			}
		}, 3, -3.1f, new Item.Properties().fireResistant());
	}
}
