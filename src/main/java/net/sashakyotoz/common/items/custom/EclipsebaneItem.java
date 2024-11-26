package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.sashakyotoz.common.networking.data.GripcrystalManaData;
import net.sashakyotoz.utils.IEntityDataSaver;

import java.util.List;

public class EclipsebaneItem extends SwordItem {
    public EclipsebaneItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (getItemPhase(stack).equals("absorption")
                && world.getLightLevel(entity.getBlockPos()) < 3
                && entity.age % 20 == 0
                && entity instanceof ServerPlayerEntity player
                && GripcrystalManaData.getMana((IEntityDataSaver) player) < 48) {
            GripcrystalManaData.addMana((IEntityDataSaver) player, 1);
            if (entity.age % 60 == 0 && !player.hasStatusEffect(StatusEffects.DARKNESS)
                    && !ModArmorItem.isAbyssalArmorSet(
                    player.getEquippedStack(EquipmentSlot.HEAD),
                    player.getEquippedStack(EquipmentSlot.CHEST),
                    player.getEquippedStack(EquipmentSlot.LEGS),
                    player.getEquippedStack(EquipmentSlot.FEET)
            )) {
                ((ServerPlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 1));
            }
            if (entity.getWorld() instanceof ServerWorld world1)
                world1.spawnParticles(ParticleTypes.SQUID_INK, entity.getX(), entity.getY() + 1, entity.getZ(), 9, 0, 1, 0, 0.5);
        }
        if (getItemPhase(stack).equals("blade_shield")
                && entity.age % 10 == 0
                && entity instanceof ServerPlayerEntity player) {
            List<LivingEntity> entities = player.getWorld().getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(2), LivingEntity::canTakeDamage);
            for (LivingEntity livingEntity : entities) {
                if (livingEntity != player && GripcrystalManaData.getMana((IEntityDataSaver) player) > 3) {
                    GripcrystalManaData.removeMana((IEntityDataSaver) player, 3);
                    livingEntity.damage(player.getDamageSources().playerAttack(player), 8);
                }
            }
        }
    }

    public String getItemPhase(ItemStack stack) {
        return stack.getOrCreateNbt().getString("eclipsebane_phase");
    }
}