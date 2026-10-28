package net.sashakyotoz.common.items.combat;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.ShieldOfChimericWarriorItem;
import net.sashakyotoz.utils.CombatHelper;

import java.util.Map;
import java.util.WeakHashMap;

public final class ShieldMechanics {
    private static final int HAMMER_RESET_TICKS = 40;
    private static final Map<Player, Long> LAST_PARRY = new WeakHashMap<>();

    private ShieldMechanics() {
    }

    public static boolean onShieldBlock(Player player, LivingEntity attacker) {
        ItemStack shield = player.getUseItem();
        if (shield.isEmpty()) {
            return false;
        }
        Item weapon = attacker.getMainHandItem().getItem();
        boolean breaksShield = weapon instanceof ChimericRockbreakerHammerItem
                || weapon instanceof AxeItem && !shield.is(Items.SHIELD);
        if (breaksShield) {
            disable(player, shield.getItem());
            return true;
        }
        if (shield.getItem() instanceof ShieldOfChimericWarriorItem) {
            tryParry(player, attacker);
        }
        return false;
    }

    public static void damageCustomShield(Player player, float damage) {
        ItemStack shield = player.getUseItem();
        if (!(shield.getItem() instanceof ShieldOfChimericWarriorItem) || damage < 3.0F) {
            return;
        }
        InteractionHand hand = player.getUsedItemHand();
        shield.hurtAndBreak(1 + Mth.floor(damage), player, LivingEntity.getSlotForHand(hand));
        if (shield.isEmpty()) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            player.stopUsingItem();
            player.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);
        }
    }

    private static void disable(Player player, Item shield) {
        player.getCooldowns().addCooldown(shield, 100);
        player.stopUsingItem();
        player.level().broadcastEntityEvent(player, (byte) 30);
    }

    private static void tryParry(Player player, LivingEntity attacker) {
        if (player.getTicksUsingItem() > 10) {
            return;
        }
        long now = player.level().getGameTime();
        Long last = LAST_PARRY.get(player);
        if (last != null && now - last < 100) {
            return;
        }
        LAST_PARRY.put(player, now);
        CombatHelper.launch(attacker, player.position(), 1.5D, 0.4D, 0.0D);
        CombatHelper.grip(attacker, 1);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 0.7F, 1.3F);
        if (player.getMainHandItem().getItem() instanceof ChimericRockbreakerHammerItem hammer) {
            shortenHammerCooldown(player.getCooldowns(), hammer);
        }
    }

    private static void shortenHammerCooldown(ItemCooldowns cooldowns, Item hammer) {
        float remaining = cooldowns.getCooldownPercent(hammer, 0.0F) * ChimericRockbreakerHammerItem.CHARGED_STRIKE_COOLDOWN;
        if (remaining > HAMMER_RESET_TICKS) {
            cooldowns.removeCooldown(hammer);
            cooldowns.addCooldown(hammer, HAMMER_RESET_TICKS);
        }
    }
}
