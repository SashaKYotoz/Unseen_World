
package net.sashakyotoz.unseenworld.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModMobEffects;

public class DarkImmuniteMobEffect extends MobEffect {
	public DarkImmuniteMobEffect() {
		super(MobEffectCategory.NEUTRAL, -16744588);
	}

	@Override
	public String getDescriptionId() {
		return "effect.unseen_world.dark_immunite";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (!entity.level().isClientSide()) {
			if (entity.isInLava()) {
				entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 1));
				entity.removeEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get());
			} else if (entity.hasEffect(MobEffects.WITHER)) {
				entity.removeEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get());
				entity.removeEffect(MobEffects.WITHER);
			} else if (entity.hasEffect(UnseenWorldModMobEffects.DARK_VOID.get())) {
				entity.removeEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get());
				entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 2));
			} else if (entity.hasEffect(MobEffects.DARKNESS)) {
				entity.removeEffect(MobEffects.DARKNESS);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
