
package net.sashakyotoz.unseenworld.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;

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
		Level level = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (Math.random() < 0.25) {
			for (int i = 0; i < (int) Mth.nextDouble(RandomSource.create(), 1, 3); i++) {
				if (Math.random() < 0.125) {
					if (!level.isClientSide())
						level.explode(null, (x + Mth.nextDouble(RandomSource.create(), -5, 5)), y, (z + Mth.nextDouble(RandomSource.create(), -5, 5)), 2, Level.ExplosionInteraction.BLOCK);
					if (level instanceof ServerLevel serverLevel)
						serverLevel.sendParticles(UnseenWorldParticleTypes.REDNESS.get(), (x + Mth.nextDouble(RandomSource.create(), -5, 5)), (y + 5), (z + Mth.nextDouble(RandomSource.create(), -5, 5)), 9, 5, 5, 5, 1);
				}
			}
		}
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
