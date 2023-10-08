
package net.sashakyotoz.unseenworld.item;

import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;

import net.sashakyotoz.unseenworld.procedures.RedTitaniumPoisonousSwordLivingEntityIsHitWithToolProcedure;

public class RedTitaniumPoisonousSwordItem extends SwordItem {
	public RedTitaniumPoisonousSwordItem() {
		super(new Tier() {
			public int getUses() {
				return 2380;
			}

			public float getSpeed() {
				return 16f;
			}

			public float getAttackDamageBonus() {
				return 11f;
			}

			public int getLevel() {
				return 10;
			}

			public int getEnchantmentValue() {
				return 25;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(UnseenWorldModItems.RED_TITANIUM_INGOT.get()));
			}
		}, 3, -2.4f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		RedTitaniumPoisonousSwordLivingEntityIsHitWithToolProcedure.execute(entity);
		return retval;
	}
}
