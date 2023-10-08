package net.sashakyotoz.unseenworld.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;

import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;

public class VoidBowWhileProjectileFlyingTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.addParticle(ParticleTypes.ENCHANT, x, y, z, 0, 1, 0);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(UnseenWorldModParticleTypes.BLUEVOIDPARTICLE.get(), x, y, z, 9, 3, 3, 3, 1);
	}
}
