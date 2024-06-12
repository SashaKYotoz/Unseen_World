package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.particles.ParticleOptions;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

public class FireLikeStaffProjectileFlyingTick {
	public static void onTickParticles(ParticleOptions option, LevelAccessor accessor, double x, double y, double z, float red, float green, float blue) {
		accessor.addParticle(UnseenWorldModParticleTypes.FIRE_PARTICLE.get(), x, y, z, red, green, blue);
		if (accessor instanceof ServerLevel level)
			level.sendParticles(option, x, y, z, 5, 2, 1, 2, 1);
	}
}
