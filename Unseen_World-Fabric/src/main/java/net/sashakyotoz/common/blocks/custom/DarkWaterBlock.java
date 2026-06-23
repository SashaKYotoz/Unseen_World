package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.utils.ActionsUtils;

public class DarkWaterBlock extends FluidBlock {
    public DarkWaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.age % 20 == 0) {
            if (!(livingEntity.hasStatusEffect(StatusEffects.GLOWING)
                    || EnchantmentHelper.hasAquaAffinity(livingEntity))) {
                if (entity instanceof IGrippingEntity entity1 && entity1.getDarkeningData() < 10 && world.getFluidState(pos.up()).isOf(ModFluids.DARK_WATER))
                    entity1.setDarkeningData(entity1.getDarkeningData() + 1);
            }
        }
        if (entity instanceof BoatEntity boatEntity && ActionsUtils.isMoving(boatEntity))
            boatEntity.setVelocity(boatEntity.getVelocity().multiply(0.5f));
    }
}