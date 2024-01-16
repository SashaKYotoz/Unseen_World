
package net.sashakyotoz.unseenworld.effects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.sashakyotoz.unseenworld.managers.DarkImmuniteOnEffectActiveTickProcedure;

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
		DarkImmuniteOnEffectActiveTickProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
