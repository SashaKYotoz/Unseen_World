
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.unseenworld.util.UnseenWorldModMobEffects;

public class CrimserrySoulBerryFoodItem extends Item {
	public CrimserrySoulBerryFoodItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(2).saturationMod(1f).build()));
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 24;
	}

	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
		return 0F;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack stack = super.finishUsingItem(itemstack, world, entity);
		if (!entity.level().isClientSide() && entity.getRandom().nextBoolean())
			entity.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.SOUL_OVERGROWTH.get(), 60, 1));
		return stack;
	}
}
