package net.sashakyotoz.common.items.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArmorItem extends ArmorItem {
    public ModArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack headStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
            ItemStack chestStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
            ItemStack leggingsStack = livingEntity.getEquippedStack(EquipmentSlot.LEGS);
            ItemStack bootsStack = livingEntity.getEquippedStack(EquipmentSlot.FEET);
            if (isAbyssalArmorSet(headStack, chestStack, leggingsStack, bootsStack)) {
                if (livingEntity.getY() < -96 && ConfigEntries.doAbyssalArmorSaveFromVoid) {
                    world.playSound(livingEntity, livingEntity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 3f, 2.5f);
                    BlockPos surfacePos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, entity.getBlockPos());
                    livingEntity.teleport(surfacePos.getX(), surfacePos.getY() + 2, surfacePos.getZ());
                    livingEntity.fallDistance = 0;
                    ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, world, surfacePos.getX(), surfacePos.getY() + 1, surfacePos.getZ(), 1.5f);
                    if (livingEntity instanceof IGrippingEntity entity1)
                        GrippingData.addGrippingSeconds(entity1, 10);
                    headStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                    chestStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    leggingsStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.LEGS));
                    bootsStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
                if (livingEntity.age % 10 == 0 && livingEntity.hasStatusEffect(StatusEffects.DARKNESS)) {
                    livingEntity.clearStatusEffects();
                    ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, world, livingEntity.getX(), livingEntity.getY() + 1, livingEntity.getZ(), 1.5f);
                    headStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                    chestStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    leggingsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.LEGS));
                    bootsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
            }
            if (isRedTitaniumArmorSet(headStack, chestStack, leggingsStack, bootsStack)) {
                if ((livingEntity.isOnFire() || livingEntity.isInLava()) && !livingEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 160, 0));
                    headStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                    chestStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    leggingsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.LEGS));
                    bootsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
            }
            if (isUnseeniumArmorSet(headStack, chestStack, leggingsStack, bootsStack)) {
                if (livingEntity.age % 200 == 0) {
                    List<LivingEntity> entities = livingEntity.getWorld().getEntitiesByClass(LivingEntity.class, livingEntity.getBoundingBox().expand(16), livingEntity1 -> livingEntity1 != livingEntity);
                    if (entities.isEmpty()) {
                        if (headStack.getDamage() < headStack.getMaxDamage())
                            headStack.setDamage(headStack.getDamage() - 2);
                        if (chestStack.getDamage() < chestStack.getMaxDamage())
                            chestStack.setDamage(chestStack.getDamage() - 2);
                        if (leggingsStack.getDamage() < leggingsStack.getMaxDamage())
                            leggingsStack.setDamage(leggingsStack.getDamage() - 2);
                        if (bootsStack.getDamage() < bootsStack.getMaxDamage())
                            bootsStack.setDamage(bootsStack.getDamage() - 2);
                    }
                }
                if (livingEntity.age % 20 == 0 && livingEntity.hasStatusEffect(StatusEffects.GLOWING))
                    livingEntity.clearStatusEffects();
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.unseen_world.armor_tips").formatted(Formatting.BLUE));
        if (stack.isOf(ModItems.ABYSSAL_HELMET)
                || stack.isOf(ModItems.ABYSSAL_CHESTPLATE)
                || stack.isOf(ModItems.ABYSSAL_LEGGINGS)
                || stack.isOf(ModItems.ABYSSAL_BOOTS)
        )
            tooltip.add(Text.translatable("item.unseen_world.abyssal_armor_tooltip"));
        if (stack.isOf(ModItems.RED_TITANIUM_HELMET)
                || stack.isOf(ModItems.RED_TITANIUM_CHESTPLATE)
                || stack.isOf(ModItems.RED_TITANIUM_LEGGINGS)
                || stack.isOf(ModItems.RED_TITANIUM_BOOTS)
        )
            tooltip.add(Text.translatable("item.unseen_world.titanium_armor_tooltip"));
        if (stack.isOf(ModItems.UNSEENIUM_HELMET)
                || stack.isOf(ModItems.UNSEENIUM_CHESTPLATE)
                || stack.isOf(ModItems.UNSEENIUM_LEGGINGS)
                || stack.isOf(ModItems.UNSEENIUM_BOOTS)
        ) {
            tooltip.add(Text.translatable("item.unseen_world.unseenium_armor_tooltip"));
            tooltip.add(Text.translatable("item.unseen_world.unseenium_armor_tooltip1"));
        }
    }

    public static boolean isAbyssalArmorSet(ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
        return head.isOf(ModItems.ABYSSAL_HELMET)
                && chest.isOf(ModItems.ABYSSAL_CHESTPLATE)
                && legs.isOf(ModItems.ABYSSAL_LEGGINGS)
                && feet.isOf(ModItems.ABYSSAL_BOOTS);
    }

    public static boolean isAbyssalArmorSet(LivingEntity entity) {
        ItemStack headStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack leggingsStack = entity.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack bootsStack = entity.getEquippedStack(EquipmentSlot.FEET);
        return isAbyssalArmorSet(headStack, chestStack, leggingsStack, bootsStack);
    }

    public static boolean isRedTitaniumArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.isOf(ModItems.RED_TITANIUM_HELMET)
                && legsSlot.isOf(ModItems.RED_TITANIUM_LEGGINGS)
                && chestSlot.isOf(ModItems.RED_TITANIUM_CHESTPLATE)
                && headSlot.isOf(ModItems.RED_TITANIUM_HELMET);
    }
    public static boolean isRedTitaniumArmorSet(LivingEntity entity) {
        ItemStack headStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack leggingsStack = entity.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack bootsStack = entity.getEquippedStack(EquipmentSlot.FEET);
        return isRedTitaniumArmorSet(headStack, chestStack, leggingsStack, bootsStack);
    }

    public static boolean isUnseeniumArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.isOf(ModItems.UNSEENIUM_BOOTS)
                && legsSlot.isOf(ModItems.UNSEENIUM_LEGGINGS)
                && chestSlot.isOf(ModItems.UNSEENIUM_CHESTPLATE)
                && headSlot.isOf(ModItems.UNSEENIUM_HELMET);
    }
    public static boolean isUnseeniumArmorSet(LivingEntity entity) {
        ItemStack headStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack leggingsStack = entity.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack bootsStack = entity.getEquippedStack(EquipmentSlot.FEET);
        return isUnseeniumArmorSet(headStack, chestStack, leggingsStack, bootsStack);
    }
}