package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.registries.UnseenWorldMobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class LivingEntityIsHitWithTreasureWeaponProcedure {
	public static void onHit(LivingEntity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (!entity.level().isClientSide()){
			int customModelData = (int) itemstack.getOrCreateTag().getDouble("CustomModelData");
			switch (customModelData){
				case 1 -> entity.addEffect(new MobEffectInstance(UnseenWorldMobEffects.DARK_VOID.get(), 40, 0));
				case 2 -> entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
				case 3 -> entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
			}
		}
	}
}
