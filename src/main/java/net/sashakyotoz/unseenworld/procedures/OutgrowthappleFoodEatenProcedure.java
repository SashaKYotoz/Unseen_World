package net.sashakyotoz.unseenworld.procedures;

import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;

public class OutgrowthappleFoodEatenProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		world.addParticle((SimpleParticleType) (UnseenWorldModParticleTypes.REDNESS.get()), x, y, z, 0.5, 2, 0.5);
		{
			Entity _entToDamage = entity;
			_entToDamage.hurt(new DamageSource(_entToDamage.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)), 1);
		}
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 1));
	}
}
