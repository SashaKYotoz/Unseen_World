
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.managers.DishWithBerriesFoodEatenProcedure;

public class DishVegetableWithPorkItem extends Item {
	public DishVegetableWithPorkItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(8).saturationMod(12f).build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 40;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack stack = new ItemStack(Items.BOWL);
		super.finishUsingItem(itemstack, world, entity);
		DishWithBerriesFoodEatenProcedure.onEaten(entity);
		if (itemstack.isEmpty()) {
			return stack;
		} else {
			if (entity instanceof Player player && !player.getAbilities().instabuild) {
				if (!player.getInventory().add(stack))
					player.drop(stack, false);
			}
			return itemstack;
		}
	}
}
