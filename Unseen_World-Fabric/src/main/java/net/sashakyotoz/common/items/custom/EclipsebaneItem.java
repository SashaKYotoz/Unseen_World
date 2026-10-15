package net.sashakyotoz.common.items.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.ModTiers;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import net.sashakyotoz.common.items.combat.MeleeAbilityItem;
import net.sashakyotoz.utils.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EclipsebaneItem extends SwordItem implements MeleeAbilityItem {
    public static final int PULSE_COOLDOWN = 160;
    public static final int DASH_COOLDOWN = 120;
    private static final int USE_DURATION = 72000;
    private static final int PULSE_CHARGE_TICKS = 20;
    private static final double PULSE_RADIUS = 4.5D;
    private static final double PULSE_PUSH = 1.4D;
    private static final double PULSE_LIFT = 0.45D;
    private static final float PULSE_DAMAGE = 4.0F;
    private static final int PULSE_DURABILITY = 1;
    private static final int PULSE_RING_POINTS = 32;
    private static final double DASH_SPEED = 1.3D;
    private static final double DASH_MAX_RISE = 0.3D;
    private static final double DASH_GROUND_LIFT = 0.6D;
    private static final int DASH_SPIN_TICKS = 12;
    private static final int DASH_DURABILITY = 2;
    private static final int DASH_GRIP_SECONDS = 3;
    private static final double SWEEP_REACH = 3.0D;
    private static final double SWEEP_HALF_ANGLE = 37.5D;
    private static final float SWEEP_MIN_RATIO = 0.5F;

    public EclipsebaneItem(int attackDamage, float attackSpeed, Properties settings) {
        super(ModTiers.ECLIPSEBANE, attackDamage, attackSpeed, settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (AbilityHelper.deferToOffhandPlacement(player, hand)) {
            return InteractionResultHolder.pass(stack);
        }
        if (player.isSecondaryUseActive()) {
            return this.dash(level, player, hand, stack);
        }
        if (!AbilityHelper.hasAmmo(player, ModItems.GRANULATED_GRIPTONITE)) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        int held = this.getUseDuration(stack) - remainingUseTicks;
        if (level.isClientSide) {
            spawnChargeParticles(level, entity, held >= PULSE_CHARGE_TICKS ? 3 : 1);
        } else if (held == PULSE_CHARGE_TICKS) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.0F, 0.5F);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingUseTicks) {
        if (!(entity instanceof Player player) || this.getUseDuration(stack) - remainingUseTicks < PULSE_CHARGE_TICKS) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            if (!AbilityHelper.consumeAmmo(player, ModItems.GRANULATED_GRIPTONITE)) {
                return;
            }
            eclipsePulse(serverLevel, player);
            AbilityHelper.damage(stack, player, PULSE_DURABILITY, player.getUsedItemHand());
        } else if (!AbilityHelper.hasAmmo(player, ModItems.GRANULATED_GRIPTONITE)) {
            return;
        }
        player.getCooldowns().addCooldown(this, PULSE_COOLDOWN);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.isAutoSpinAttack() && CombatHelper.canHit(attacker, target)) {
            CombatHelper.grip(target, DASH_GRIP_SECONDS);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void onPlayerAttack(Player player, Entity target) {
        if (!AbilityHelper.isSweepHit(player) || !(player.level() instanceof ServerLevel level)) {
            return;
        }
        float ratio = Math.max(SWEEP_MIN_RATIO, EnchantmentHelper.getSweepingDamageRatio(player));
        float damage = 1.0F + ratio * (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        for (LivingEntity victim : CombatHelper.enemiesInArc(player, SWEEP_REACH, SWEEP_HALF_ANGLE)) {
            if (victim != target && victim.hurt(CombatHelper.attackSource(player), damage)) {
                level.sendParticles(ParticleTypes.SWEEP_ATTACK, victim.getX(), victim.getY(0.5D), victim.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 3, tooltip);
    }

    private InteractionResultHolder<ItemStack> dash(Level level, Player player, InteractionHand hand, ItemStack stack) {
        Vec3 look = player.getLookAngle();
        Vec3 push = new Vec3(look.x, Mth.clamp(look.y, -DASH_MAX_RISE, DASH_MAX_RISE), look.z).normalize().scale(DASH_SPEED);
        player.push(push.x, push.y, push.z);
        player.startAutoSpinAttack(DASH_SPIN_TICKS);
        if (player.onGround()) {
            player.move(MoverType.SELF, new Vec3(0.0D, DASH_GROUND_LIFT, 0.0D));
        }
        player.getCooldowns().addCooldown(this, DASH_COOLDOWN);
        if (!level.isClientSide) {
            AbilityHelper.damage(stack, player, DASH_DURABILITY, hand);
            level.playSound(null, player, SoundEvents.TRIDENT_RIPTIDE_1, SoundSource.PLAYERS, 1.0F, 1.2F);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void eclipsePulse(ServerLevel level, Player player) {
        Vec3 center = player.position();
        for (LivingEntity target : CombatHelper.enemiesInRadius(player, center, PULSE_RADIUS)) {
            target.hurt(CombatHelper.attackSource(player), PULSE_DAMAGE);
            CombatHelper.launch(target, center, PULSE_PUSH, PULSE_LIFT, 0.0D);
        }
        for (int point = 0; point < PULSE_RING_POINTS; point++) {
            double angle = Math.PI * 2.0D * point / PULSE_RING_POINTS;
            double dirX = Math.cos(angle);
            double dirZ = Math.sin(angle);
            level.sendParticles(ParticleTypes.REVERSE_PORTAL, center.x, center.y + 0.3D, center.z, 0, dirX, 0.0D, dirZ, 0.9D);
            level.sendParticles(ParticleTypes.END_ROD, center.x, center.y + 1.1D, center.z, 0, dirX, 0.05D, dirZ, 0.35D);
        }
        level.playSound(null, center.x, center.y, center.z, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 0.7F);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 0.6F);
    }

    private static void spawnChargeParticles(Level level, LivingEntity entity, int count) {
        for (int index = 0; index < count; index++) {
            double angle = entity.getRandom().nextDouble() * Math.PI * 2.0D;
            double radius = 1.2D + entity.getRandom().nextDouble() * 0.8D;
            level.addParticle(ParticleTypes.PORTAL, entity.getX(), entity.getY(0.6D), entity.getZ(),
                    Math.cos(angle) * radius, entity.getRandom().nextDouble() - 0.3D, Math.sin(angle) * radius);
        }
    }
}