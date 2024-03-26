
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.managers.FierySaberRightClickedProcedure;
import net.sashakyotoz.unseenworld.managers.TreasureWeaponOnBeaconClick;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;

public class FierySaberItem extends SwordItem {
	public FierySaberItem() {
		super(new Tier() {
			public int getUses() {
				return 1957;
			}

			public float getSpeed() {
				return 5f;
			}

			public float getAttackDamageBonus() {
				return 5f;
			}

			public int getLevel() {
				return 5;
			}

			public int getEnchantmentValue() {
				return 2;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(UnseenWorldModItems.FIRE_PEARL.get()));
			}
		}, 3, -2.4f, new Item.Properties().fireResistant());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		FierySaberRightClickedProcedure.execute(world, entity, ar.getObject());
		return ar;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult result = super.useOn(context);
		TreasureWeaponOnBeaconClick.onClick(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getItemInHand());
		return result;
	}
}
