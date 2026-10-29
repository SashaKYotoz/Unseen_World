package net.sashakyotoz.common.world.features.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.blocks.custom.plants.MidnightLilyPadBlock;

public class MidnightLilyPadFeature extends Feature<NoneFeatureConfiguration> {
    public static final Feature<NoneFeatureConfiguration> INSTANCE = new MidnightLilyPadFeature();

    public MidnightLilyPadFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        int radius = 2 + random.nextInt(3);
        for (int y = -2; y < 2; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    if (random.nextBoolean()){
                        BlockPos pos = origin.offset(x, y, z);
                        if ((world.getFluidState(pos).getType() == ModFluids.DARK_WATER
                                || world.getFluidState(pos).getType() == Fluids.WATER)
                                && world.getBlockState(pos.above()).isAir() && random.nextBoolean())
                            this.setBlock(world,pos.above(),ModBlocks.MIDNIGHT_LILY_PAD.defaultBlockState().setValue(MidnightLilyPadBlock.HAS_BERRY,random.nextBoolean()));
                    }
                }
            }
        }
        return true;
    }
}