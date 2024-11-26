package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.plants.UmbralKelpBlock;

public class UmbralKelpFeature extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> INSTANCE = new UmbralKelpFeature(DefaultFeatureConfig.CODEC);

    public UmbralKelpFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        int i = 0;
        StructureWorldAccess worldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        int j = worldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ());
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), j, blockPos.getZ());
        if (worldAccess.getBlockState(blockPos2).isOf(ModBlocks.DARK_WATER)) {
            BlockState blockState = ModBlocks.UMBRAL_KELP.getDefaultState();
            BlockState blockState2 = ModBlocks.UMBRAL_KELP_PLANT.getDefaultState();
            int k = 1 + random.nextInt(9);

            for (int l = 0; l <= k; l++) {
                if (worldAccess.getBlockState(blockPos2).isOf(ModBlocks.DARK_WATER)
                        && worldAccess.getBlockState(blockPos2.up()).isOf(ModBlocks.DARK_WATER)
                        && blockState2.canPlaceAt(worldAccess, blockPos2)) {
                    if (l == k) {
                        worldAccess.setBlockState(blockPos2, blockState.with(UmbralKelpBlock.AGE, random.nextInt(4) + 20), Block.NOTIFY_LISTENERS);
                        i++;
                    } else {
                        worldAccess.setBlockState(blockPos2, blockState2, Block.NOTIFY_LISTENERS);
                    }
                } else if (l > 0) {
                    BlockPos blockPos3 = blockPos2.down();
                    if (blockState.canPlaceAt(worldAccess, blockPos3) && !worldAccess.getBlockState(blockPos3.down()).isOf(ModBlocks.UMBRAL_KELP)) {
                        worldAccess.setBlockState(blockPos3, blockState.with(UmbralKelpBlock.AGE, random.nextInt(4) + 20), Block.NOTIFY_LISTENERS);
                        i++;
                    }
                    break;
                }

                blockPos2 = blockPos2.up();
            }
        }
        return i > 0;
    }
}