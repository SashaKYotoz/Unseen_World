package net.sashakyotoz.common.items.combat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

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
        NeoForge.EVENT_BUS.addListener(WeaponEvents::onAttack);
    }

    private static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (player.level().isClientSide || player.isSpectator() || !(target instanceof LivingEntity)) {
            return;
        }
        if (player.getMainHandItem().getItem() instanceof MeleeAbilityItem item) {
            item.onPlayerAttack(player, target);
        }
    }
}
