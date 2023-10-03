package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.init.UnseenWorldModMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class DarkImmuniteOnEffectActiveTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.isInLava()) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 1));
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get());
		} else if (entity instanceof LivingEntity _livEnt3 && _livEnt3.hasEffect(MobEffects.WITHER)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get());
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.WITHER);
		} else if (entity instanceof LivingEntity _livEnt6 && _livEnt6.hasEffect(UnseenWorldModMobEffects.DARK_VOID.get())) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get());
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 2));
		} else if (entity instanceof LivingEntity _livEnt9 && _livEnt9.hasEffect(MobEffects.DARKNESS)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.DARKNESS);
		}
	}
}
