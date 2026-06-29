package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.utils.ActionsUtils;

public class DarkWaterBlock extends LiquidBlock {
    public DarkWaterBlock(FlowingFluid fluid, Properties settings) {
        super(fluid, settings);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.tickCount % 20 == 0) {
            if (!(livingEntity.hasEffect(MobEffects.GLOWING)
                    || EnchantmentHelper.hasAquaAffinity(livingEntity))) {
                if (entity instanceof IGrippingEntity entity1 && entity1.getDarkeningData() < 10 && world.getFluidState(pos.above()).is(ModFluids.DARK_WATER))
                    entity1.setDarkeningData(entity1.getDarkeningData() + 1);
            }
        }
        if (entity instanceof Boat boatEntity && ActionsUtils.isMoving(boatEntity))
            boatEntity.setDeltaMovement(boatEntity.getDeltaMovement().scale(0.5f));
    }
}