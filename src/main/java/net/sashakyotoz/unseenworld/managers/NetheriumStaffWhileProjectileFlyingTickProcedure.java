package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class NetheriumStaffWhileProjectileFlyingTickProcedure {
	public static void onTickParticles(LevelAccessor world, double x, double y, double z) {
		world.addParticle(UnseenWorldModParticleTypes.FIRE_PARTICLE.get(), x, y, z, 0, 0, 0);
		if (world instanceof ServerLevel level)
			level.sendParticles(ParticleTypes.FALLING_LAVA, x, y, z, 5, 2, 1, 2, 1);
	}
}
