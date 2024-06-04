
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModParticleTypes;

public class OutgrowthAppleItem extends Item {
	public OutgrowthAppleItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(3).saturationMod(2.5f).build()));
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 48;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level level, LivingEntity entity) {
		ItemStack stack = super.finishUsingItem(itemstack, level, entity);
		level.addParticle(UnseenWorldModParticleTypes.REDNESS.get(), entity.getX(), entity.getY(), entity.getZ(), 0.5, 2, 0.5);
		entity.hurt(entity.damageSources().generic(), 1);
		if (!entity.level().isClientSide())
			entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2));
		return stack;
	}
}
