
package net.sashakyotoz.unseenworld.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.sashakyotoz.unseenworld.procedures.MeteoritestrophyOnEffectActiveTickProcedure;
import net.sashakyotoz.unseenworld.procedures.MeteoritestrophyEffectExpiresProcedure;

public class MeteoritestrophyMobEffect extends MobEffect {
	public MeteoritestrophyMobEffect() {
		super(MobEffectCategory.HARMFUL, -5953115);
	}

	@Override
	public String getDescriptionId() {
		return "effect.unseen_world.meteoritestrophy";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		MeteoritestrophyOnEffectActiveTickProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ());
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		MeteoritestrophyEffectExpiresProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
