
package net.sashakyotoz.unseenworld.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class RedTitaniumPoisonousSwordItem extends SwordItem {
	public RedTitaniumPoisonousSwordItem() {
		super(ModTiers.TITANIUM, 3, -2.4f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceEntity) {
		if(!entity.level().isClientSide())
			entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 3));
		return super.hurtEnemy(itemstack, entity, sourceEntity);
	}
}
