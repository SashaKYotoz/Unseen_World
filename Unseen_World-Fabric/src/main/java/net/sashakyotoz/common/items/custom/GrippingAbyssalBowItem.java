package net.sashakyotoz.common.items.custom;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
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

    public GrippingAbyssalBowItem(Properties settings) {
        super(BETTER_COMBAT ? settings.attributes(meleeAttributes()) : settings);
    }

    private static ItemAttributeModifiers meleeAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 5.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
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
        if (!level.isClientSide && this.getUseDuration(stack, entity) - remainingUseTicks == OVERDRAW_TICKS) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.8F, 0.6F);
        }
    }

    @Override
    public void releaseUsing(ItemStack bow, Level level, LivingEntity entity, int remainingUseTicks) {
        if (!(entity instanceof Player player)) {
            return;
        }
        ItemStack ammo = player.getProjectile(bow);
        if (ammo.isEmpty()) {
            return;
        }
        int drawn = this.getUseDuration(bow, entity) - remainingUseTicks;
        float power = getPowerForTime(drawn);
        if (power < 0.1F) {
            return;
        }
        boolean overdrawn = drawn >= OVERDRAW_TICKS;
        List<ItemStack> projectiles = draw(bow, ammo, player);
        if (level instanceof ServerLevel serverLevel && !projectiles.isEmpty()) {
            InteractionHand hand = player.getUsedItemHand();
            this.shoot(serverLevel, player, hand, bow, projectiles,
                    power * (overdrawn ? OVERDRAW_SPEED : ARROW_SPEED), overdrawn ? 0.0F : 1.0F, power >= 1.0F, null);
            if (overdrawn && !bow.isEmpty()) {
                AbilityHelper.damage(bow, player, OVERDRAW_EXTRA_DURABILITY, hand);
            }
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
        if (!(projectile instanceof AbstractArrow arrow)) {
            return;
        }
        if (velocity > ARROW_SPEED) {
            arrow.setBaseDamage(arrow.getBaseDamage() * ARROW_SPEED / OVERDRAW_SPEED);
            arrow.setPierceLevel((byte) (arrow.getPierceLevel() + 1));
            arrow.addTag(ABYSSAL_SHOT_TAG);
        } else if (arrow.isCritArrow()) {
            arrow.addTag(GRIPPING_SHOT_TAG);
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return repair.is(ModItems.GRIPCRYSTAL) || super.isValidRepairItem(stack, repair);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        AbilityHelper.appendAbilityLines(this.getDescriptionId(), 2, tooltip);
    }
}
