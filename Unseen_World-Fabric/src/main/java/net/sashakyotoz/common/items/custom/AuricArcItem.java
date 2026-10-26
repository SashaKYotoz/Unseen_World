package net.sashakyotoz.common.items.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.custom.projectiles.ThrownAuricArc;
import net.sashakyotoz.common.items.ModTiers;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import net.sashakyotoz.common.items.combat.MeleeAbilityItem;
import net.sashakyotoz.utils.CombatHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class AuricArcItem extends TridentItem implements MeleeAbilityItem {
    public static final int THROW_TICKS = 10;
    public static final int SUNBURST_TICKS = 20;
    public static final int SUNBURST_COOLDOWN = 60;
    private static final float THROW_SPEED = 2.5F;
    private static final float BOLT_SPEED = 2.5F;
    private static final float BOLT_SPREAD_DEGREES = 10.0F;
    private static final DustParticleOptions GOLDEN_DUST = new DustParticleOptions(new Vector3f(1.0F, 0.78F, 0.25F), 1.2F);

    private final Multimap<Attribute, AttributeModifier> modifiers;

    public AuricArcItem(double attackDamage, double attackSpeed, Properties settings) {
        super(settings);
        this.modifiers = ImmutableMultimap.of(
                Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION),
                Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.modifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public int getEnchantmentValue() {
        return ModTiers.AURIC_ARC.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return ModTiers.AURIC_ARC.getRepairIngredient().test(repair);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingUseTicks) {
        if (!(entity instanceof Player player)) {
            return;
        }
        int held = this.getUseDuration(stack) - remainingUseTicks;
        if (held < THROW_TICKS) {
            return;
        }
        if (EnchantmentHelper.getRiptide(stack) > 0) {
            super.releaseUsing(stack, level, entity, remainingUseTicks);
            return;
        }
        boolean sunburst = held >= SUNBURST_TICKS;
        if (!level.isClientSide()) {
            InteractionHand hand = player.getUsedItemHand();
            AbilityHelper.damage(stack, player, sunburst ? 2 : 1, hand);
            if (stack.isEmpty()) {
                return;
            }
            ThrownAuricArc arc = ThrownAuricArc.create(level, player, stack, sunburst);
            arc.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, THROW_SPEED, 1.0F);
            if (player.getAbilities().instabuild) {
                arc.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            level.addFreshEntity(arc);
            level.playSound(null, arc, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (sunburst) {
                releaseSunburst(level, player);
            }
            if (!player.getAbilities().instabuild) {
                player.getInventory().removeItem(stack);
            }
        }
        if (sunburst) {
            player.getCooldowns().addCooldown(this, SUNBURST_COOLDOWN);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public void onPlayerAttack(Player player, Entity target) {
        if (!AbilityHelper.isCriticalHit(player) || !(player.level() instanceof ServerLevel level)) {
            return;
        }
        Vec3 start = player.position();
        Vec3 facing = Vec3.directionFromRotation(0.0F, player.getYRot());
        double length = Math.sqrt(player.distanceToSqr(target)) + 3.0D;
        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F;
        for (LivingEntity victim : CombatHelper.enemiesInLine(player, start, facing, length, 1.5D)) {
            if (victim != target) {
                victim.hurt(CombatHelper.attackSource(player), damage);
            }
        }
        for (double travelled = 1.0D; travelled <= length; travelled += 1.0D) {
            Vec3 point = start.add(facing.scale(travelled));
            level.sendParticles(GOLDEN_DUST, point.x, point.y + 1.0D, point.z, 3, 0.2D, 0.2D, 0.2D, 0.0D);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.2F, 1.4F);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 3, tooltip);
    }

    private static void releaseSunburst(Level level, Player player) {
        for (float offset : new float[]{-BOLT_SPREAD_DEGREES, BOLT_SPREAD_DEGREES}) {
            SpectralArrow bolt = new SpectralArrow(level, player);
            bolt.shootFromRotation(player, player.getXRot(), player.getYRot() + offset, 0.0F, BOLT_SPEED, 1.0F);
            bolt.pickup = AbstractArrow.Pickup.DISALLOWED;
            level.addFreshEntity(bolt);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.6F, 1.6F);
    }
}
