package net.sashakyotoz.unseenworld.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class UnseeniumOnEffectActiveTickProcedure {
	public static void execute(LivingEntity entity) {
		if (entity == null)
			return;
		if (!entity.level().isClientSide()){
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, true, false));
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 1, true, false));
			entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 60, 1, true, false));
		}
	}
}
