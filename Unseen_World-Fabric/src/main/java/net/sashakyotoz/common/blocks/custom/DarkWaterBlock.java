package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.blocks.ModFluids;

public class DarkWaterBlock extends LiquidBlock {
    public DarkWaterBlock(FlowingFluid fluid, Properties settings) {
        super(fluid, settings);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide()
                && entity instanceof LivingEntity livingEntity
                && livingEntity.tickCount % 20 == 0) {
            if (!(livingEntity.hasEffect(MobEffects.GLOWING)
                    || hasAquaAffinity(livingEntity))) {
                if (entity instanceof IGrippingEntity entity1 && entity1.getDarkeningData() < 10 && level.getFluidState(pos.above()).is(ModFluids.DARK_WATER))
                    entity1.setDarkeningData(entity1.getDarkeningData() + 1);
            }
        }
    }

    private static boolean hasAquaAffinity(LivingEntity entity) {
        return entity.getAttributes().hasAttribute(Attributes.SUBMERGED_MINING_SPEED)
                && entity.getAttributeValue(Attributes.SUBMERGED_MINING_SPEED) > Attributes.SUBMERGED_MINING_SPEED.value().getDefaultValue();
    }
}