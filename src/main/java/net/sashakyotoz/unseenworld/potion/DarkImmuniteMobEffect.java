
package net.sashakyotoz.unseenworld.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.sashakyotoz.unseenworld.procedures.DarkImmuniteOnEffectActiveTickProcedure;

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
