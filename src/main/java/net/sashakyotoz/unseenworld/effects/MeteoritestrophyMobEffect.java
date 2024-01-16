
package net.sashakyotoz.unseenworld.effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.sashakyotoz.unseenworld.managers.MeteoritestrophyOnEffectActiveTickProcedure;

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
		if(!entity.level().isClientSide())
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
