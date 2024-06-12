
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.managers.FierySaberRightClick;
import net.sashakyotoz.unseenworld.managers.LivingEntityIsHitWithTreasureWeaponProcedure;
import net.sashakyotoz.unseenworld.managers.TreasureWeaponOnBeaconClick;

public class FierySaberItem extends SwordItem {
	public FierySaberItem() {
		super(ModTiers.FIERY_SABER, 3, -2.4f, new Item.Properties().fireResistant());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		FierySaberRightClick.execute(world, entity, ar.getObject());
		return ar;
	}
	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		LivingEntityIsHitWithTreasureWeaponProcedure.onHit(entity, itemstack);
		return super.hurtEnemy(itemstack, entity, sourceentity);
	}
	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult result = super.useOn(context);
		TreasureWeaponOnBeaconClick.onClick(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getItemInHand());
		return result;
	}
}
