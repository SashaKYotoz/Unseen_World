package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public class VoidStaffRangedItemUsedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Player entity, ItemStack itemstack) {
        if (entity == null)
            return;
        entity.getCooldowns().addCooldown(itemstack.getItem(), 60);
        if (world instanceof ServerLevel level)
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 24, 3, 3, 3, 1);
    }
}
