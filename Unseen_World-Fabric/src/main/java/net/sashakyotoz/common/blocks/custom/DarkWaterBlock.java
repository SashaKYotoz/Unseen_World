package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.sashakyotoz.common.items.custom.ModArmorItem;

public class DarkWaterBlock extends FluidBlock {
    public DarkWaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack headStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
            ItemStack chestStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
            ItemStack leggingsStack = livingEntity.getEquippedStack(EquipmentSlot.LEGS);
            ItemStack bootsStack = livingEntity.getEquippedStack(EquipmentSlot.FEET);
            if (!(headStack.isOf(Items.TURTLE_HELMET) && livingEntity.hasStatusEffect(StatusEffects.NIGHT_VISION)
                    && ModArmorItem.isAbyssalArmorSet(headStack, chestStack, leggingsStack, bootsStack)
                    && ModArmorItem.isUnseeniumArmorSet(headStack, chestStack, leggingsStack, bootsStack))
                    && ModArmorItem.isRedTitaniumArmorSet(headStack, chestStack, leggingsStack, bootsStack)) {
                if (livingEntity.age % 20 == 0 && !livingEntity.hasStatusEffect(StatusEffects.DARKNESS)) {
                    livingEntity.setVelocity(livingEntity.getRandom().nextBoolean()
                            ? new Vec3d(1, 1, 0)
                            : new Vec3d(0, -1, -1));
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 0));
                }
            }
        }
        if (entity instanceof BoatEntity boatEntity)
            boatEntity.addVelocity(0, -0.001f, 0);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}