package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class DishWithBerriesFoodEatenProcedure {
	public static void onEaten(LivingEntity livingEntity) {
		if (livingEntity == null)
			return;
		if (Math.random() < 0.5) {
			if (!livingEntity.level().isClientSide())
				livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 1));
			if (!livingEntity.level().isClientSide())
				livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 2));
		} else if (Math.random() < 0.25) {
				livingEntity.hurt(livingEntity.damageSources().generic(), 2);
			if (!livingEntity.level().isClientSide())
				livingEntity.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.SOUL_OVERGROWTH.get(), 60, 1));
		} else if (Math.random() < 0.125) {
			if (!livingEntity.level().isClientSide())
				livingEntity.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.DARK_VOID.get(), 40, 1));
		}
	}
}
