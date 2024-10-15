package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.blocks.custom.StandingFruitBlock;
import net.sashakyotoz.common.world.features.custom.configs.BoulderFeatureConfig;
import net.sashakyotoz.common.world.features.custom.configs.StandingFlowerBunchFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class StandingFlowerBunchFeature extends Feature<StandingFlowerBunchFeatureConfig> {
    public static final Feature<StandingFlowerBunchFeatureConfig> INSTANCE = new StandingFlowerBunchFeature(StandingFlowerBunchFeatureConfig.CODEC);

    public StandingFlowerBunchFeature(Codec<StandingFlowerBunchFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<StandingFlowerBunchFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        StandingFlowerBunchFeatureConfig config = context.getConfig();
        BlockState state = config.block().get(random, origin);
        int radius = config.radius().get(random) + 1;
        int height = Math.round(radius / 2f);
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    if (random.nextBoolean()){
                        if (state.getBlock() instanceof StandingFruitBlock block &&
                                world.getBlockState(origin.add(x, y, z)).isIn(block.canStayOn) &&
                                world.getBlockState(origin.add(x, y, z).up()).isAir()) {
                            int sHeight = random.nextInt(height) + 1;
                            BlockPos pos = origin.add(x, y, z);
                            for (int i = 0; i < sHeight; i++) {
                                BlockPos pos1 = pos.add(0,i,0);
                                if (world.getBlockState(pos1).isAir() && (world.getBlockState(pos1.down()).getBlock() instanceof StandingFruitBlock
                                        || world.getBlockState(pos1.down()).isIn(block.canStayOn))){
                                    this.setBlockState(world, pos1.up(), state.with(StandingFruitBlock.HAS_FRUIT,world.getBlockState(pos1.down()).getBlock() instanceof StandingFruitBlock &&
                                            random.nextBoolean()).with(StandingFruitBlock.HAS_FLOWER,world.getBlockState(pos1.up(2)).isAir()).with(StandingFruitBlock.LUMINANCE,random.nextInt(11)));
                                    this.setBlockState(world, pos1, state.with(StandingFruitBlock.HAS_FRUIT,false).with(StandingFruitBlock.HAS_FLOWER,false));
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