
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.sashakyotoz.unseenworld.managers.DarkFreeSoulRightClickProcedure;

public class DarkFreeSoulItem extends Item {
	public DarkFreeSoulItem() {
		super(new Item.Properties().stacksTo(4).fireResistant().rarity(Rarity.UNCOMMON));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.EAT;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		ItemStack itemstack = ar.getObject();
		DarkFreeSoulRightClickProcedure.execute(entity, itemstack);
		return ar;
	}
}
