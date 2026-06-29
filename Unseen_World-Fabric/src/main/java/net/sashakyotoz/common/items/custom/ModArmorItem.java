package net.sashakyotoz.common.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArmorItem extends ArmorItem {
    public ModArmorItem(ArmorMaterial material, Type type, Properties settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chestStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack bootsStack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
            if (isAbyssalArmorSet(headStack, chestStack, leggingsStack, bootsStack)) {
                if (livingEntity.getY() < -96 && ConfigEntries.doAbyssalArmorSaveFromVoid) {
                    world.playSound(livingEntity, livingEntity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 3f, 2.5f);
                    BlockPos surfacePos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, entity.blockPosition());
                    livingEntity.teleportToWithTicket(surfacePos.getX(), surfacePos.getY() + 2, surfacePos.getZ());
                    livingEntity.fallDistance = 0;
                    ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, world, surfacePos.getX(), surfacePos.getY() + 1, surfacePos.getZ(), 1.5f);
                    if (livingEntity instanceof IGrippingEntity entity1)
                        GrippingData.addGrippingSeconds(entity1, 10);
                    headStack.hurtAndBreak(2, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.HEAD));
                    chestStack.hurtAndBreak(2, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.CHEST));
                    leggingsStack.hurtAndBreak(2, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.LEGS));
                    bootsStack.hurtAndBreak(2, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.FEET));
                }
                if (livingEntity.tickCount % 10 == 0 && livingEntity.hasEffect(MobEffects.DARKNESS)) {
                    livingEntity.removeAllEffects();
                    ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, world, livingEntity.getX(), livingEntity.getY() + 1, livingEntity.getZ(), 1.5f);
                    headStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.HEAD));
                    chestStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.CHEST));
                    leggingsStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.LEGS));
                    bootsStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.FEET));
                }
            }
            if (isRedTitaniumArmorSet(headStack, chestStack, leggingsStack, bootsStack)) {
                if ((livingEntity.isOnFire() || livingEntity.isInLava()) && !livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 160, 0));
                    headStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.HEAD));
                    chestStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.CHEST));
                    leggingsStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.LEGS));
                    bootsStack.hurtAndBreak(1, livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.FEET));
                }
            }
            if (isUnseeniumArmorSet(headStack, chestStack, leggingsStack, bootsStack)) {
                if (livingEntity.tickCount % 200 == 0) {
                    List<LivingEntity> entities = livingEntity.level().getEntitiesOfClass(LivingEntity.class, livingEntity.getBoundingBox().inflate(16), livingEntity1 -> livingEntity1 != livingEntity);
                    if (entities.isEmpty()) {
                        if (headStack.getDamageValue() < headStack.getMaxDamage())
                            headStack.setDamageValue(headStack.getDamageValue() - 2);
                        if (chestStack.getDamageValue() < chestStack.getMaxDamage())
                            chestStack.setDamageValue(chestStack.getDamageValue() - 2);
                        if (leggingsStack.getDamageValue() < leggingsStack.getMaxDamage())
                            leggingsStack.setDamageValue(leggingsStack.getDamageValue() - 2);
                        if (bootsStack.getDamageValue() < bootsStack.getMaxDamage())
                            bootsStack.setDamageValue(bootsStack.getDamageValue() - 2);
                    }
                }
                if (livingEntity.tickCount % 20 == 0 && livingEntity.hasEffect(MobEffects.GLOWING))
                    livingEntity.removeAllEffects();
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(Component.translatable("item.unseen_world.armor_tips").withStyle(ChatFormatting.BLUE));
        if (stack.is(ModItems.ABYSSAL_HELMET)
                || stack.is(ModItems.ABYSSAL_CHESTPLATE)
                || stack.is(ModItems.ABYSSAL_LEGGINGS)
                || stack.is(ModItems.ABYSSAL_BOOTS)
        )
            tooltip.add(Component.translatable("item.unseen_world.abyssal_armor_tooltip"));
        if (stack.is(ModItems.RED_TITANIUM_HELMET)
                || stack.is(ModItems.RED_TITANIUM_CHESTPLATE)
                || stack.is(ModItems.RED_TITANIUM_LEGGINGS)
                || stack.is(ModItems.RED_TITANIUM_BOOTS)
        )
            tooltip.add(Component.translatable("item.unseen_world.titanium_armor_tooltip"));
        if (stack.is(ModItems.UNSEENIUM_HELMET)
                || stack.is(ModItems.UNSEENIUM_CHESTPLATE)
                || stack.is(ModItems.UNSEENIUM_LEGGINGS)
                || stack.is(ModItems.UNSEENIUM_BOOTS)
        ) {
            tooltip.add(Component.translatable("item.unseen_world.unseenium_armor_tooltip"));
            tooltip.add(Component.translatable("item.unseen_world.unseenium_armor_tooltip1"));
        }
    }

    public static boolean isAbyssalArmorSet(ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
        return head.is(ModItems.ABYSSAL_HELMET)
                && chest.is(ModItems.ABYSSAL_CHESTPLATE)
                && legs.is(ModItems.ABYSSAL_LEGGINGS)
                && feet.is(ModItems.ABYSSAL_BOOTS);
    }

    public static boolean isAbyssalArmorSet(LivingEntity entity) {
        ItemStack headStack = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggingsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack bootsStack = entity.getItemBySlot(EquipmentSlot.FEET);
        return isAbyssalArmorSet(headStack, chestStack, leggingsStack, bootsStack);
    }

    public static boolean isRedTitaniumArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.is(ModItems.RED_TITANIUM_HELMET)
                && legsSlot.is(ModItems.RED_TITANIUM_LEGGINGS)
                && chestSlot.is(ModItems.RED_TITANIUM_CHESTPLATE)
                && headSlot.is(ModItems.RED_TITANIUM_HELMET);
    }
    public static boolean isRedTitaniumArmorSet(LivingEntity entity) {
        ItemStack headStack = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggingsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack bootsStack = entity.getItemBySlot(EquipmentSlot.FEET);
        return isRedTitaniumArmorSet(headStack, chestStack, leggingsStack, bootsStack);
    }

    public static boolean isUnseeniumArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.is(ModItems.UNSEENIUM_BOOTS)
                && legsSlot.is(ModItems.UNSEENIUM_LEGGINGS)
                && chestSlot.is(ModItems.UNSEENIUM_CHESTPLATE)
                && headSlot.is(ModItems.UNSEENIUM_HELMET);
    }
    public static boolean isUnseeniumArmorSet(LivingEntity entity) {
        ItemStack headStack = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggingsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack bootsStack = entity.getItemBySlot(EquipmentSlot.FEET);
        return isUnseeniumArmorSet(headStack, chestStack, leggingsStack, bootsStack);
    }
}