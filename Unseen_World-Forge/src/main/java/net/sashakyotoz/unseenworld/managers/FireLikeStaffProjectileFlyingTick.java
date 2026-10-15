package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;

public class FireLikeStaffProjectileFlyingTick {
    public static void onTickParticles(ParticleOptions option, Level level, double x, double y, double z, float red, float green, float blue, int tickCount) {
        if (tickCount % 10 == 0) {
            if(level.isClientSide())
                level.addParticle(UnseenWorldParticleTypes.FIRE_PARTICLE.get(), x, y, z, red, green, blue);
            if (level instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(option, x, y, z, 3, 0, 1, 0, 1);
        }
    }
}
