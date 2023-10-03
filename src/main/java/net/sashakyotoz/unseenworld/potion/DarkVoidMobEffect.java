
package net.sashakyotoz.unseenworld.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.sashakyotoz.unseenworld.procedures.DarkVoidOnEffectActiveTickProcedure;

public class DarkVoidMobEffect extends MobEffect {
	public DarkVoidMobEffect() {
		super(MobEffectCategory.HARMFUL, -16777216);
	}

	@Override
	public String getDescriptionId() {
		return "effect.unseen_world.dark_void";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		DarkVoidOnEffectActiveTickProcedure.execute(entity.level(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
