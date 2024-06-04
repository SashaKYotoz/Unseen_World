
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;

import net.sashakyotoz.unseenworld.managers.LivingEntityIsHitWithTreasureWeaponProcedure;
import net.sashakyotoz.unseenworld.managers.TreasureWeaponOnBeaconClick;

public class HeavyClaymoreItem extends SwordItem {
	public HeavyClaymoreItem() {
		super(ModTiers.HEAVY_CLAYMORE, 3, -3f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		LivingEntityIsHitWithTreasureWeaponProcedure.onHit(entity, itemstack);
		return super.hurtEnemy(itemstack, entity, sourceentity);
	}

	@Override
	public boolean isRepairable(ItemStack itemstack) {
		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		TreasureWeaponOnBeaconClick.onClick(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getItemInHand());
		return InteractionResult.SUCCESS;
	}
}
