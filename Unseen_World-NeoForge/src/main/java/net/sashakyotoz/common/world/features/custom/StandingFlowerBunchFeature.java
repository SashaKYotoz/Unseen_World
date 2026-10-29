package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.blocks.custom.plants.StandingFruitBlock;
import net.sashakyotoz.common.world.features.custom.configs.StandingFlowerBunchFeatureConfig;

public class StandingFlowerBunchFeature extends Feature<StandingFlowerBunchFeatureConfig> {
    public static final Feature<StandingFlowerBunchFeatureConfig> INSTANCE = new StandingFlowerBunchFeature(StandingFlowerBunchFeatureConfig.CODEC);

    public StandingFlowerBunchFeature(Codec<StandingFlowerBunchFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<StandingFlowerBunchFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        StandingFlowerBunchFeatureConfig config = context.config();
        BlockState state = config.block().getState(random, origin);
        int radius = config.radius().sample(random) + 1;
        int height = Math.round(radius / 2f);
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    if (random.nextBoolean()){
                        if (state.getBlock() instanceof StandingFruitBlock block &&
                                world.getBlockState(origin.offset(x, y, z)).is(block.canStayOn) &&
                                world.getBlockState(origin.offset(x, y, z).above()).isAir()) {
                            int sHeight = random.nextInt(height) + 1;
                            BlockPos pos = origin.offset(x, y, z);
                            for (int i = 0; i < sHeight; i++) {
                                BlockPos pos1 = pos.offset(0,i,0);
                                if (world.getBlockState(pos1).isAir() && (world.getBlockState(pos1.below()).getBlock() instanceof StandingFruitBlock
                                        || world.getBlockState(pos1.below()).is(block.canStayOn))){
                                    this.setBlock(world, pos1.above(), state.setValue(StandingFruitBlock.HAS_FRUIT,world.getBlockState(pos1.below()).getBlock() instanceof StandingFruitBlock &&
                                            random.nextBoolean()).setValue(StandingFruitBlock.HAS_FLOWER,world.getBlockState(pos1.above(2)).isAir()).setValue(StandingFruitBlock.LUMINANCE,random.nextInt(11)));
                                    this.setBlock(world, pos1, state.setValue(StandingFruitBlock.HAS_FRUIT,false).setValue(StandingFruitBlock.HAS_FLOWER,false));
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}