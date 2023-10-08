
package net.sashakyotoz.unseenworld.item;

import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;

import net.sashakyotoz.unseenworld.procedures.LightTulvarRightclickedOnBlockProcedure;
import net.sashakyotoz.unseenworld.procedures.HeavyClaymoreLivingEntityIsHitWithToolProcedure;

public class LightTulvarItem extends SwordItem {
	public LightTulvarItem() {
		super(new Tier() {
			public int getUses() {
				return 1683;
			}

			public float getSpeed() {
				return 5f;
			}

			public float getAttackDamageBonus() {
				return 4f;
			}

			public int getLevel() {
				return 5;
			}

			public int getEnchantmentValue() {
				return 20;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(UnseenWorldModItems.UNSEEN_INGOT.get()), new ItemStack(UnseenWorldModItems.DEEP_GEM.get()));
			}
		}, 3, -2f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		HeavyClaymoreLivingEntityIsHitWithToolProcedure.execute(entity, itemstack);
		return retval;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		LightTulvarRightclickedOnBlockProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), context.getPlayer(), context.getItemInHand());
		return InteractionResult.SUCCESS;
	}
}
