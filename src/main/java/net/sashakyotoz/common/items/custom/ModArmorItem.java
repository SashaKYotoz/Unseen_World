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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.items.ModItems;
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
                if (livingEntity.getY() < -96) {
                    if (livingEntity.getWorld() instanceof ServerWorld serverWorld)
                        serverWorld.spawnParticles(ParticleTypes.DRAGON_BREATH,
                                livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                9, 0, 1, 0, 0.5);
                    world.playSound(livingEntity, livingEntity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 3f, 2.5f);
                    livingEntity.teleport(livingEntity.getX(), livingEntity.getY() + 128, livingEntity.getZ());
                    headStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                    chestStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    leggingsStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.LEGS));
                    bootsStack.damage(2, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
                if (livingEntity.age % 10 == 0 && livingEntity.hasStatusEffect(StatusEffects.DARKNESS)){
                    livingEntity.clearStatusEffects();
                    if (livingEntity.getWorld() instanceof ServerWorld serverWorld)
                        serverWorld.spawnParticles(ParticleTypes.FIREWORK,
                                livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                9, 0, 1, 0, 0.5);
                    headStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                    chestStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    leggingsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.LEGS));
                    bootsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
            }
            if (isRedTitaniumArmorSet(headStack,chestStack,leggingsStack,bootsStack)){
                if ((livingEntity.isOnFire() || livingEntity.isInLava()) && !livingEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)){
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,160,0));
                    headStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                    chestStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    leggingsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.LEGS));
                    bootsStack.damage(1, livingEntity, livingEntity1 -> livingEntity1.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
            }
            if (isUnseeniumArmorSet(headStack,chestStack,leggingsStack,bootsStack)){
                if (livingEntity.age % 200 == 0){
                    List<LivingEntity> entities = livingEntity.getWorld().getEntitiesByClass(LivingEntity.class,livingEntity.getBoundingBox().expand(16),livingEntity1 -> livingEntity1 != livingEntity);
                    if (entities.isEmpty()){
                        if (headStack.getDamage() < headStack.getMaxDamage())
                            headStack.setDamage(headStack.getDamage()-2);
                        if (chestStack.getDamage() < chestStack.getMaxDamage())
                            chestStack.setDamage(chestStack.getDamage()-2);
                        if (leggingsStack.getDamage() < leggingsStack.getMaxDamage())
                            leggingsStack.setDamage(leggingsStack.getDamage()-2);
                        if (bootsStack.getDamage() < bootsStack.getMaxDamage())
                            bootsStack.setDamage(bootsStack.getDamage()-2);
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
                || stack.isOf(ModItems.UNSEENIUM_INGOT)
        ){
            tooltip.add(Text.translatable("item.unseen_world.unseenium_armor_tooltip"));
            tooltip.add(Text.translatable("item.unseen_world.unseenium_armor_tooltip1"));
        }
    }

    public static boolean isAbyssalArmorSet(ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
        return head.getItem() == ModItems.ABYSSAL_HELMET
                && chest.getItem() == ModItems.ABYSSAL_CHESTPLATE
                && legs.getItem() == ModItems.ABYSSAL_LEGGINGS
                && feet.getItem() == ModItems.ABYSSAL_BOOTS;
    }

    public static boolean isRedTitaniumArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.getItem() == ModItems.RED_TITANIUM_HELMET
                && legsSlot.getItem() == ModItems.RED_TITANIUM_LEGGINGS
                && chestSlot.getItem() == ModItems.RED_TITANIUM_CHESTPLATE
                && headSlot.getItem() == ModItems.RED_TITANIUM_HELMET;
    }

    public static boolean isUnseeniumArmorSet(ItemStack headSlot, ItemStack chestSlot, ItemStack legsSlot, ItemStack feetSlot) {
        return feetSlot.getItem() == ModItems.UNSEENIUM_BOOTS
                && legsSlot.getItem() == ModItems.UNSEENIUM_LEGGINGS
                && chestSlot.getItem() == ModItems.UNSEENIUM_CHESTPLATE
                && headSlot.getItem() == ModItems.UNSEENIUM_HELMET;
    }
}