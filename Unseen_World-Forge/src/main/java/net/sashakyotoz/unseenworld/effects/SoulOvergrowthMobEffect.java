
package net.sashakyotoz.unseenworld.effects;

import net.sashakyotoz.unseenworld.managers.SoulOvergrowthOnEffectActiveTickProcedure;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class SoulOvergrowthMobEffect extends MobEffect {
	public SoulOvergrowthMobEffect() {
		super(MobEffectCategory.HARMFUL, -1090479872);
	}

	@Override
	public String getDescriptionId() {
		return "effect.unseen_world.soul_overgrowth";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		SoulOvergrowthOnEffectActiveTickProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
