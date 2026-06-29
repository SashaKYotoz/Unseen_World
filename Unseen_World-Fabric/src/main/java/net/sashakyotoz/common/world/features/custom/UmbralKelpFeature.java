package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.plants.UmbralKelpBlock;

public class UmbralKelpFeature extends Feature<NoneFeatureConfiguration> {
    public static final Feature<NoneFeatureConfiguration> INSTANCE = new UmbralKelpFeature(NoneFeatureConfiguration.CODEC);

    public UmbralKelpFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        int i = 0;
        WorldGenLevel worldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        int j = worldAccess.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ());
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), j, blockPos.getZ());
        if (worldAccess.getBlockState(blockPos2).is(ModBlocks.DARK_WATER)) {
            BlockState blockState = ModBlocks.UMBRAL_KELP.defaultBlockState();
            BlockState blockState2 = ModBlocks.UMBRAL_KELP_PLANT.defaultBlockState();
            int k = 1 + random.nextInt(9);

            for (int l = 0; l <= k; l++) {
                if (worldAccess.getBlockState(blockPos2).is(ModBlocks.DARK_WATER)
                        && worldAccess.getBlockState(blockPos2.above()).is(ModBlocks.DARK_WATER)
                        && blockState2.canSurvive(worldAccess, blockPos2)) {
                    if (l == k) {
                        worldAccess.setBlock(blockPos2, blockState.setValue(UmbralKelpBlock.AGE, random.nextInt(4) + 20), Block.UPDATE_CLIENTS);
                        i++;
                    } else {
                        worldAccess.setBlock(blockPos2, blockState2, Block.UPDATE_CLIENTS);
                    }
                } else if (l > 0) {
                    BlockPos blockPos3 = blockPos2.below();
                    if (blockState.canSurvive(worldAccess, blockPos3) && !worldAccess.getBlockState(blockPos3.below()).is(ModBlocks.UMBRAL_KELP)) {
                        worldAccess.setBlock(blockPos3, blockState.setValue(UmbralKelpBlock.AGE, random.nextInt(4) + 20), Block.UPDATE_CLIENTS);
                        i++;
                    }
                    break;
                }

                blockPos2 = blockPos2.above();
            }
        }
        return i > 0;
    }
}