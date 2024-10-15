package net.sashakyotoz.common.world.features.custom;

import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.MidnightLilyPadBlock;

public class MidnightLilyPadFeature extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> INSTANCE = new MidnightLilyPadFeature();

    public MidnightLilyPadFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        int radius = 2 + random.nextInt(3);
        for (int y = -2; y < 2; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    if (random.nextBoolean()){
                        BlockPos pos = origin.add(x, y, z);
                        if ((world.getFluidState(pos).getFluid() == ModBlocks.DARK_WATER
                                || world.getFluidState(pos).getFluid() == Fluids.WATER)
                                && world.getBlockState(pos.up()).isAir() && random.nextBoolean())
                            this.setBlockState(world,pos.up(),ModBlocks.MIDNIGHT_LILY_PAD.getDefaultState().with(MidnightLilyPadBlock.HAS_BERRY,random.nextBoolean()));
                    }
                }
            }
        }
        return true;
    }
}