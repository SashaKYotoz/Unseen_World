package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class DarkPearlRangedItemProjectileHitsLivingEntityProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        entity.teleportTo(x, (y + 0.5), z);
        if (entity instanceof ServerPlayer serverPlayer)
            serverPlayer.connection.teleport(x, (y + 0.5), z, entity.getYRot(), entity.getXRot());
        if (world instanceof ServerLevel level)
            level.sendParticles(ParticleTypes.CRIT, x, y, z, 15, 3, 3, 3, 1);
    }
}
