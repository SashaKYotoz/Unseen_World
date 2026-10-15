package net.sashakyotoz.common.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.items.ModArmorMaterials;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArmorItem extends ArmorItem {
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final int VOID_RESCUE_MARGIN = 32;
    private static final int VOID_RESCUE_GRIPPING_SECONDS = 10;
    private static final int FIRE_RESISTANCE_TICKS = 160;
    private static final int UNSEENIUM_REPAIR_INTERVAL = 200;
    private static final int UNSEENIUM_REPAIR_AMOUNT = 2;
    private static final double UNSEENIUM_SOLITUDE_RADIUS = 16.0D;

    public ModArmorItem(ArmorMaterial material, Type type, Properties settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (level.isClientSide || this.getType() != Type.HELMET || !(entity instanceof ServerPlayer player)) {
            return;
        }
        if (player.getItemBySlot(EquipmentSlot.HEAD) != stack) {
            return;
        }
        ArmorSet set = ArmorSet.of(this.getMaterial());
        if (set == null || !set.isWornBy(player)) {
            return;
        }
        switch (set) {
            case ABYSSAL -> tickAbyssal(player);
            case RED_TITANIUM -> tickRedTitanium(player);
            case UNSEENIUM -> tickUnseenium(player);
        }
    }

    private static void tickAbyssal(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        if (ModMainConfig.doAbyssalArmorSaveFromVoid.get() && player.getY() < level.getMinBuildHeight() - VOID_RESCUE_MARGIN) {
            rescueFromVoid(player, level);
        }
        if (player.hasEffect(MobEffects.DARKNESS)) {
            player.removeEffect(MobEffects.DARKNESS);
            ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, level, player.getX(), player.getY() + 1, player.getZ(), 1.5f);
            damageSet(player, 1);
        }
    }

    private static void rescueFromVoid(ServerPlayer player, ServerLevel level) {
        BlockPos surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());
        double x = surface.getX() + 0.5D;
        double y = surface.getY() + 2;
        double z = surface.getZ() + 0.5D;
        player.teleportTo(level, x, y, z, player.getYRot(), player.getXRot());
        player.fallDistance = 0.0F;
        player.setDeltaMovement(Vec3.ZERO);
        player.hurtMarked = true;
        level.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 3.0F, 2.5F);
        ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, level, x, surface.getY() + 1, z, 1.5f);
        if (player instanceof IGrippingEntity grippingEntity) {
            GrippingData.addGrippingSeconds(grippingEntity, VOID_RESCUE_GRIPPING_SECONDS);
        }
        damageSet(player, 2);
    }

    private static void tickRedTitanium(ServerPlayer player) {
        if ((player.isOnFire() || player.isInLava()) && !player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, FIRE_RESISTANCE_TICKS, 0));
            damageSet(player, 1);
        }
    }

    private static void tickUnseenium(ServerPlayer player) {
        if (player.hasEffect(MobEffects.GLOWING)) {
            player.removeEffect(MobEffects.GLOWING);
        }
        if (player.tickCount % UNSEENIUM_REPAIR_INTERVAL == 0 && isAlone(player)) {
            repairSet(player, UNSEENIUM_REPAIR_AMOUNT);
        }
    }

    private static boolean isAlone(ServerPlayer player) {
        return player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(UNSEENIUM_SOLITUDE_RADIUS), other -> other != player).isEmpty();
    }

    private static void damageSet(ServerPlayer player, int amount) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            player.getItemBySlot(slot).hurtAndBreak(amount, player, wearer -> wearer.broadcastBreakEvent(slot));
        }
    }

    private static void repairSet(ServerPlayer player, int amount) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack piece = player.getItemBySlot(slot);
            if (piece.isDamaged()) {
                piece.setDamageValue(Math.max(0, piece.getDamageValue() - amount));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(Component.translatable("item.unseen_world.armor_tips").withStyle(ChatFormatting.BLUE));
        ArmorSet set = ArmorSet.of(this.getMaterial());
        if (set != null) {
            for (String key : set.tooltipKeys) {
                tooltip.add(Component.translatable(key));
            }
        }
    }

    public static boolean isAbyssalArmorSet(LivingEntity entity) {
        return ArmorSet.ABYSSAL.isWornBy(entity);
    }

    public static boolean isRedTitaniumArmorSet(LivingEntity entity) {
        return ArmorSet.RED_TITANIUM.isWornBy(entity);
    }

    public static boolean isUnseeniumArmorSet(LivingEntity entity) {
        return ArmorSet.UNSEENIUM.isWornBy(entity);
    }

    private enum ArmorSet {
        ABYSSAL(ModArmorMaterials.ABYSSAL, "item.unseen_world.abyssal_armor_tooltip"),
        RED_TITANIUM(ModArmorMaterials.TITANIUM, "item.unseen_world.titanium_armor_tooltip"),
        UNSEENIUM(ModArmorMaterials.UNSEENIUM, "item.unseen_world.unseenium_armor_tooltip", "item.unseen_world.unseenium_armor_tooltip1");

        private final ArmorMaterial material;
        private final String[] tooltipKeys;

        ArmorSet(ArmorMaterial material, String... tooltipKeys) {
            this.material = material;
            this.tooltipKeys = tooltipKeys;
        }

        @Nullable
        private static ArmorSet of(ArmorMaterial material) {
            for (ArmorSet set : values()) {
                if (set.material == material) {
                    return set;
                }
            }
            return null;
        }

        private boolean isWornBy(LivingEntity entity) {
            for (EquipmentSlot slot : ARMOR_SLOTS) {
                if (!(entity.getItemBySlot(slot).getItem() instanceof ArmorItem armor)
                        || armor.getMaterial() != this.material
                        || armor.getEquipmentSlot() != slot) {
                    return false;
                }
            }
            return true;
        }
    }
}