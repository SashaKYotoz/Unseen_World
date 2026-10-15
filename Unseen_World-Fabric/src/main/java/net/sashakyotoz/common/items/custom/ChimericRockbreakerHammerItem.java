package net.sashakyotoz.common.items.custom;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.items.ModTiers;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import net.sashakyotoz.common.items.combat.FissureTask;
import net.sashakyotoz.utils.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChimericRockbreakerHammerItem extends PickaxeItem {
    public static final int CHARGE_TICKS = 20;
    public static final int CHARGED_STRIKE_COOLDOWN = 160;
    public static final int FISSURE_COOLDOWN = 200;
    private static final int USE_DURATION = 72000;
    private static final double STRIKE_RADIUS = 3.5D;
    private static final double STRIKE_PUSH = 0.6D;
    private static final double STRIKE_LAUNCH = 0.8D;
    private static final float STRIKE_DAMAGE_FACTOR = 0.5F;
    private static final int STRIKE_GRIP_SECONDS = 3;
    private static final int STRIKE_DURABILITY = 2;
    private static final int FISSURE_DURABILITY = 3;

    public ChimericRockbreakerHammerItem(int attackDamage, float attackSpeed, Properties settings) {
        super(ModTiers.ROCKBREAKER_HAMMER, attackDamage, attackSpeed, settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (AbilityHelper.deferToOffhandPlacement(player, hand)) {
            return InteractionResultHolder.pass(stack);
        }
        if (!player.isSecondaryUseActive()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        if (!player.onGround()) {
            return InteractionResultHolder.fail(stack);
        }
        if (level instanceof ServerLevel serverLevel) {
            FissureTask.start(serverLevel, player);
            AbilityHelper.damage(stack, player, FISSURE_DURABILITY, hand);
        }
        player.getCooldowns().addCooldown(this, FISSURE_COOLDOWN);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
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
        if (!level.isClientSide && this.getUseDuration(stack) - remainingUseTicks == CHARGE_TICKS) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_EQUIP_NETHERITE, SoundSource.PLAYERS, 1.0F, 0.6F);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingUseTicks) {
        if (!(entity instanceof Player player) || this.getUseDuration(stack) - remainingUseTicks < CHARGE_TICKS) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            chargedStrike(serverLevel, player);
            AbilityHelper.damage(stack, player, STRIKE_DURABILITY, player.getUsedItemHand());
        }
        player.getCooldowns().addCooldown(this, CHARGED_STRIKE_COOLDOWN);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 3, tooltip);
    }

    private static void chargedStrike(ServerLevel level, Player player) {
        Vec3 center = player.position();
        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * STRIKE_DAMAGE_FACTOR;
        for (LivingEntity target : CombatHelper.enemiesInRadius(player, center, STRIKE_RADIUS)) {
            target.hurt(CombatHelper.attackSource(player), damage);
            CombatHelper.launch(target, center, STRIKE_PUSH, STRIKE_LAUNCH, 0.0D);
            CombatHelper.grip(target, STRIKE_GRIP_SECONDS);
        }
        BlockState ground = level.getBlockState(player.blockPosition().below());
        if (!ground.isAir()) {
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ground), center.x, center.y + 0.1D, center.z, 48, STRIKE_RADIUS * 0.5D, 0.05D, STRIKE_RADIUS * 0.5D, 0.2D);
        }
        level.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y + 0.5D, center.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.6F, 1.4F);
    }
}
