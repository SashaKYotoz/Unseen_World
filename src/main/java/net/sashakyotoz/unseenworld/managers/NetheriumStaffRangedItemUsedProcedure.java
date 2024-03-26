package net.sashakyotoz.unseenworld.managers;

import net.minecraft.server.level.ServerPlayer;
import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

public class NetheriumStaffRangedItemUsedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, ServerPlayer player, ItemStack itemstack) {
        if (player == null)
            return;
        if (world instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(UnseenWorldModParticleTypes.FIRE_PARTICLE.get(), x, y, z, 24, 4, 3, 4, 1);
        player.getCooldowns().addCooldown(itemstack.getItem(), 30);
    }
}
