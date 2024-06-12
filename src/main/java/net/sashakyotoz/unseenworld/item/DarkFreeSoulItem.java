
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.sashakyotoz.unseenworld.registries.UnseenWorldModMobEffects;

public class DarkFreeSoulItem extends Item {
	public DarkFreeSoulItem() {
		super(new Item.Properties().stacksTo(4).fireResistant().rarity(Rarity.UNCOMMON));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.EAT;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemStack = this.getDefaultInstance();
		player.getInventory().clearOrCountMatchingItems(p -> itemStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
		player.getCooldowns().addCooldown(itemStack.getItem(), 20);
		if (!player.level().isClientSide()) {
			if (Math.random() < 0.75)
				player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.DARK_IMMUNITE.get(), 100, 0, true, true));
			else if (Math.random() < 0.5)
				player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.REDUCED_OF_GRAVITY.get(), 80, 0, true, true));
			else if (Math.random() < 0.25)
				player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.SOUL_OVERGROWTH.get(), 60, 0, true, true));
			else if (Math.random() < 0.125)
				player.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.METEORITESTROPHY.get(), 40, 0, true, true));
		}
		return super.use(world, player, hand);
	}
}
