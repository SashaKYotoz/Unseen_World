package net.sashakyotoz.common.items.combat;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public final class WeaponEvents {
    private static boolean registered;

    private WeaponEvents() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        ServerTasks.register();
        AttackEntityCallback.EVENT.register(WeaponEvents::onAttack);
    }

    private static InteractionResult onAttack(Player player, Level level, InteractionHand hand, Entity target, @Nullable EntityHitResult hitResult) {
        if (level.isClientSide || player.isSpectator() || hand != InteractionHand.MAIN_HAND || !(target instanceof LivingEntity)) {
            return InteractionResult.PASS;
        }
        if (player.getMainHandItem().getItem() instanceof MeleeAbilityItem item) {
            item.onPlayerAttack(player, target);
        }
        return InteractionResult.PASS;
    }
}
