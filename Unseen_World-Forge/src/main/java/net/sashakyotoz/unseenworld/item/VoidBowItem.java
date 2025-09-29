
package net.sashakyotoz.unseenworld.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.entity.VoidArrowEntity;

public class VoidBowItem extends BowItem {
	public VoidBowItem() {
		super(new Item.Properties().durability(750));
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		boolean flag = !player.getProjectile(itemstack).isEmpty();

		InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, level, player, hand, flag);
		if (ret != null) return ret;

		if (!player.getAbilities().instabuild && !flag) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			player.startUsingItem(hand);
			return InteractionResultHolder.consume(itemstack);
		}
	}
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BOW;
	}

	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int l) {
		if (entity instanceof Player player) {
			boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
			ItemStack itemstack = player.getProjectile(stack);
			int i = this.getUseDuration(stack) - l;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
			if (i < 0)
				return;
			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}
				float f = getPowerForTime(i);
				if (!((double) f < 0.1D)) {
					boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, player));
					if (!level.isClientSide) {
						VoidArrowEntity voidArrow = VoidArrowEntity.shoot(level, player, level.getRandom(), 2.5f * f, 2.5, 1,itemstack);
						if (f == 1.0F) {
							voidArrow.setCritArrow(true);
						}
						int j = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
						if (j > 0) {
							voidArrow.setBaseDamage(voidArrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
						}
						int k = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
						if (k > 0) {
							voidArrow.setKnockback(k);
						}
						if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
							voidArrow.setSecondsOnFire(100);
						}
						stack.hurtAndBreak(1, player, (event) -> {event.broadcastBreakEvent(player.getUsedItemHand());
						});
						if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
							voidArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}
						level.addFreshEntity(voidArrow);
					}
					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !player.getAbilities().instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							player.getInventory().removeItem(itemstack);
						}
					}
					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}

	public static float getPowerForTime(int i) {
		float f = (float) i / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}
		return f;
	}
}
