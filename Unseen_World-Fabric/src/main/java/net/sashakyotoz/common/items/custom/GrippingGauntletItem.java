package net.sashakyotoz.common.items.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import net.sashakyotoz.utils.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrippingGauntletItem extends Item {
    private static final int USE_DURATION = 72000;
    private static final int MIN_GRIP_TICKS = 10;
    private static final double RANGE = 24.0D;
    private static final int COOLDOWN = 100;

    public GrippingGauntletItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingUseTicks) {
        if (!(entity instanceof Player player) || !(level instanceof ServerLevel serverLevel)
                || this.getUseDuration(stack) - remainingUseTicks < MIN_GRIP_TICKS) {
            return;
        }
        CombatHelper.BeamHit hit = CombatHelper.traceBeam(player, RANGE,
                target -> CombatHelper.canHit(player, target) && hasLooseItems(target));
        if (!(hit.entity() instanceof LivingEntity target)) {
            return;
        }
        disarm(target);
        serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, target.getX(), target.getY(0.5D), target.getZ(), 16, 0.3D, 0.4D, 0.3D, 0.05D);
        serverLevel.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 0.8F, 1.6F);
        player.getCooldowns().addCooldown(this, COOLDOWN);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 1, tooltip);
    }

    private static boolean hasLooseItems(Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        if (!living.getMainHandItem().isEmpty() || !living.getOffhandItem().isEmpty()) {
            return true;
        }
        return living instanceof InventoryCarrier carrier && !carrier.getInventory().isEmpty();
    }

    private static void disarm(LivingEntity target) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = target.getItemInHand(hand);
            if (!held.isEmpty()) {
                target.setItemInHand(hand, ItemStack.EMPTY);
                target.spawnAtLocation(held, 0.5F);
            }
        }
        if (target instanceof InventoryCarrier carrier) {
            SimpleContainer inventory = carrier.getInventory();
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                ItemStack stored = inventory.removeItemNoUpdate(slot);
                if (!stored.isEmpty()) {
                    target.spawnAtLocation(stored, 0.5F);
                    return;
                }
            }
        }
    }
}
