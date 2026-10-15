package net.sashakyotoz.common.items.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.combat.AbilityHelper;
import net.sashakyotoz.utils.CombatHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GrippingAbyssalBowItem extends BowItem {
    public static final String GRIPPING_SHOT_TAG = "unseen_world.gripping_shot";
    public static final String ABYSSAL_SHOT_TAG = "unseen_world.abyssal_shot";
    private static final int OVERDRAW_TICKS = 40;
    private static final float ARROW_SPEED = 3.0F;
    private static final float OVERDRAW_SPEED = 4.0F;
    private static final int GRIP_SECONDS = 2;
    private static final int OVERDRAW_GRIP_SECONDS = 4;
    private static final int OVERDRAW_EXTRA_DURABILITY = 2;
    private static final double DRAG_DOWN_SPEED = 1.0D;
    private static final boolean BETTER_COMBAT = FabricLoader.getInstance().isModLoaded("bettercombat");

    private final Multimap<Attribute, AttributeModifier> meleeModifiers = ImmutableMultimap.of(
            Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0D, AttributeModifier.Operation.ADDITION),
            Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.0D, AttributeModifier.Operation.ADDITION));

    public GrippingAbyssalBowItem(Properties settings) {
        super(settings);
    }

    public static void onArrowHurt(AbstractArrow arrow, LivingEntity target) {
        if (arrow.level().isClientSide) {
            return;
        }
        Set<String> tags = arrow.getTags();
        boolean abyssal = tags.contains(ABYSSAL_SHOT_TAG);
        if (!abyssal && !tags.contains(GRIPPING_SHOT_TAG)) {
            return;
        }
        if (arrow.getOwner() instanceof LivingEntity owner && !CombatHelper.canHit(owner, target)) {
            return;
        }
        CombatHelper.grip(target, abyssal ? OVERDRAW_GRIP_SECONDS : GRIP_SECONDS);
        if (abyssal && !target.onGround() && !CombatHelper.isBoss(target)) {
            CombatHelper.dragDown(target, DRAG_DOWN_SPEED);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (!level.isClientSide && this.getUseDuration(stack) - remainingUseTicks == OVERDRAW_TICKS) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.8F, 0.6F);
        }
    }

    @Override
    public void releaseUsing(ItemStack bow, Level level, LivingEntity entity, int remainingUseTicks) {
        if (!(entity instanceof Player player)) {
            return;
        }
        boolean creative = player.getAbilities().instabuild;
        boolean infinite = creative || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
        ItemStack ammo = player.getProjectile(bow);
        if (ammo.isEmpty() && !infinite) {
            return;
        }
        if (ammo.isEmpty()) {
            ammo = new ItemStack(Items.ARROW);
        }
        int drawn = this.getUseDuration(bow) - remainingUseTicks;
        float power = getPowerForTime(drawn);
        if (power < 0.1F) {
            return;
        }
        boolean overdrawn = drawn >= OVERDRAW_TICKS;
        boolean freeArrow = infinite && ammo.is(Items.ARROW);
        if (!level.isClientSide) {
            AbstractArrow arrow = createArrow(level, player, bow, ammo, power, overdrawn);
            if (freeArrow || creative && (ammo.is(Items.SPECTRAL_ARROW) || ammo.is(Items.TIPPED_ARROW))) {
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            AbilityHelper.damage(bow, player, overdrawn ? 1 + OVERDRAW_EXTRA_DURABILITY : 1, player.getUsedItemHand());
            level.addFreshEntity(arrow);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
        if (!freeArrow && !creative) {
            ammo.shrink(1);
            if (ammo.isEmpty()) {
                player.getInventory().removeItem(ammo);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return repair.is(ModItems.GRIPCRYSTAL) || super.isValidRepairItem(stack, repair);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return BETTER_COMBAT && slot == EquipmentSlot.MAINHAND ? this.meleeModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 2, tooltip);
    }

    private static AbstractArrow createArrow(Level level, Player player, ItemStack bow, ItemStack ammo, float power, boolean overdrawn) {
        ArrowItem arrowItem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow arrow = arrowItem.createArrow(level, ammo, player);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,
                power * (overdrawn ? OVERDRAW_SPEED : ARROW_SPEED), overdrawn ? 0.0F : 1.0F);
        if (power >= 1.0F) {
            arrow.setCritArrow(true);
        }
        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);
        if (powerLevel > 0) {
            arrow.setBaseDamage(arrow.getBaseDamage() + powerLevel * 0.5D + 0.5D);
        }
        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);
        if (punchLevel > 0) {
            arrow.setKnockback(punchLevel);
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
            arrow.setSecondsOnFire(100);
        }
        if (overdrawn) {
            arrow.setBaseDamage(arrow.getBaseDamage() * ARROW_SPEED / OVERDRAW_SPEED);
            arrow.setPierceLevel((byte) (arrow.getPierceLevel() + 1));
            arrow.addTag(ABYSSAL_SHOT_TAG);
        } else if (power >= 1.0F) {
            arrow.addTag(GRIPPING_SHOT_TAG);
        }
        return arrow;
    }
}
