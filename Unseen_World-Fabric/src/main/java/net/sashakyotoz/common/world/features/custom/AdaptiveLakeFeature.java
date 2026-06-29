package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.AdaptiveLakeFeatureConfig;

public class AdaptiveLakeFeature extends Feature<AdaptiveLakeFeatureConfig> {
    public static final Feature<AdaptiveLakeFeatureConfig> INSTANCE = new AdaptiveLakeFeature(AdaptiveLakeFeatureConfig.CODEC);

    public AdaptiveLakeFeature(Codec<AdaptiveLakeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<AdaptiveLakeFeatureConfig> context) {
        BlockState AIR = Blocks.AIR.defaultBlockState();
        BlockPos blockPos = context.origin();
        WorldGenLevel structureWorldAccess = context.level();
        RandomSource random = context.random();
        AdaptiveLakeFeatureConfig config = context.config();

        if (blockPos.getY() <= structureWorldAccess.getMinBuildHeight() + 4) {
            return false;
        } else {
            blockPos = blockPos.below(4);
            boolean[] bls = new boolean[2048];
            int i = random.nextInt(4) + 4;
            for (int j = 0; j < i; j++) {
                double d = random.nextDouble() * 6.0 + 3.0;
                double e = random.nextDouble() * 4.0 + 2.0;
                double f = random.nextDouble() * 6.0 + 3.0;
                double g = random.nextDouble() * (16.0 - d - 2.0) + 1.0 + d / 2.0;
                double h = random.nextDouble() * (8.0 - e - 4.0) + 2.0 + e / 2.0;
                double k = random.nextDouble() * (16.0 - f - 2.0) + 1.0 + f / 2.0;

                for (int l = 1; l < 15; l++) {
                    for (int m = 1; m < 15; m++) {
                        for (int n = 1; n < 7; n++) {
                            double o = (l - g) / (d / 2.0);
                            double p = (n - h) / (e / 2.0);
                            double q = (m - k) / (f / 2.0);
                            double r = o * o + p * p + q * q;
                            if (r < 1.0) {
                                bls[(l * 16 + m) * 8 + n] = true;
                            }
                        }
                    }
                }
            }

            BlockState lakeLiquidState = config.state().getState(random, blockPos);
            for (int s = 0; s < 16; s++) {
                for (int t = 0; t < 16; t++) {
                    for (int u = 0; u < 8; u++) {
                        boolean bl = !bls[(s * 16 + t) * 8 + u]
                                && (
                                s < 15 && bls[((s + 1) * 16 + t) * 8 + u]
                                        || s > 0 && bls[((s - 1) * 16 + t) * 8 + u]
                                        || t < 15 && bls[(s * 16 + t + 1) * 8 + u]
                                        || t > 0 && bls[(s * 16 + (t - 1)) * 8 + u]
                                        || u < 7 && bls[(s * 16 + t) * 8 + u + 1]
                                        || u > 0 && bls[(s * 16 + t) * 8 + (u - 1)]
                        );
                        if (bl) {
                            BlockState blockState2 = structureWorldAccess.getBlockState(blockPos.offset(s, u, t));
                            if (u >= 4 && blockState2.liquid())
                                return false;
                            if (u < 4 && !blockState2.isSolid() && structureWorldAccess.getBlockState(blockPos.offset(s, u, t)) != lakeLiquidState)
                                return false;
                        }
                    }
                }
            }
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 8; y++) {
                        if (bls[(x * 16 + z) * 8 + y]) {
                            BlockPos blockPos2 = blockPos.offset(x, y, z);
                            if (this.canReplace(structureWorldAccess.getBlockState(blockPos2))) {
                                boolean isTopHalf = y >= 4;
                                structureWorldAccess.setBlock(blockPos2, isTopHalf ? AIR : lakeLiquidState, Block.UPDATE_CLIENTS);
                                if (isTopHalf)
                                    structureWorldAccess.scheduleTick(blockPos2, AIR.getBlock(), 0);
                            }
                        }
                    }
                }
            }
            BlockState defaultBarrierState = config.barrier().getState(random, blockPos);

            if (!defaultBarrierState.isAir()) {
                for (int t = 0; t < 16; t++) {
                    for (int uxx = 0; uxx < 16; uxx++) {
                        for (int v = 7; v >= 0; v--) {
                            boolean isBorder = !bls[(t * 16 + uxx) * 8 + v]
                                    && (
                                    t < 15 && bls[((t + 1) * 16 + uxx) * 8 + v]
                                            || t > 0 && bls[((t - 1) * 16 + uxx) * 8 + v]
                                            || uxx < 15 && bls[(t * 16 + uxx + 1) * 8 + v]
                                            || uxx > 0 && bls[(t * 16 + (uxx - 1)) * 8 + v]
                                            || v < 7 && bls[(t * 16 + uxx) * 8 + v + 1]
                                            || v > 0 && bls[(t * 16 + uxx) * 8 + (v - 1)]
                            );

                            if (isBorder && (v < 4 || random.nextInt(2) != 0)) {
                                BlockPos currentPos = blockPos.offset(t, v, uxx);
                                BlockState currentState = structureWorldAccess.getBlockState(currentPos);

                                BlockPos abovePos = currentPos.above();
                                BlockState stateAbove = structureWorldAccess.getBlockState(abovePos);

                                if (stateAbove.isRedstoneConductor(structureWorldAccess, abovePos))
                                    structureWorldAccess.setBlock(currentPos, stateAbove, Block.UPDATE_CLIENTS);
                                else if (!currentState.isRedstoneConductor(structureWorldAccess, currentPos) && !currentState.liquid())
                                    structureWorldAccess.setBlock(currentPos, AIR, Block.UPDATE_CLIENTS);
                                else if (currentState.isSolid() && !currentState.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE))
                                    structureWorldAccess.setBlock(currentPos, defaultBarrierState, Block.UPDATE_CLIENTS);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean canReplace(BlockState state) {
        return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }
}