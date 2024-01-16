package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.sashakyotoz.unseenworld.util.UnseenWorldModMobEffects;

public class VoidBowProjectileHitsPlayerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		double xRadius = 0.5;
		double loop = 0;
		double zRadius = 0.5;
		double particleAmount = 8;
		if (Math.random() < 0.5) {
			if (!(entity == sourceentity)) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(UnseenWorldModMobEffects.DARK_VOID.get(), 20, (int) Mth.nextDouble(RandomSource.create(), 0, 2)));
			}
		}
		while (loop < particleAmount) {
			world.addParticle(UnseenWorldModParticleTypes.BLUEVOIDPARTICLE.get(), (x + Math.cos(((Math.PI * 2) / particleAmount) * loop) * xRadius), y, (z + Math.sin(((Math.PI * 2) / particleAmount) * loop) * zRadius), 0, 0.05,
					0);
			loop = loop + 1;
		}
	}
}