package net.sashakyotoz.common.items.combat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface MeleeAbilityItem {
    void onPlayerAttack(Player player, Entity target);
}