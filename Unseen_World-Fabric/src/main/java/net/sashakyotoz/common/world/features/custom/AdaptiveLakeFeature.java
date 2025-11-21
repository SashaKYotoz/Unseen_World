package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.world.features.custom.configs.AdaptiveLakeFeatureConfig;

public class AdaptiveLakeFeature extends Feature<AdaptiveLakeFeatureConfig> {
    public static final Feature<AdaptiveLakeFeatureConfig> INSTANCE = new AdaptiveLakeFeature(AdaptiveLakeFeatureConfig.CODEC);

    public AdaptiveLakeFeature(Codec<AdaptiveLakeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<AdaptiveLakeFeatureConfig> context) {
        BlockState AIR = Blocks.AIR.getDefaultState();
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        AdaptiveLakeFeatureConfig config = context.getConfig();

        if (blockPos.getY() <= structureWorldAccess.getBottomY() + 4) {
            return false;
        } else {
            blockPos = blockPos.down(4);
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

            BlockState blockState = config.state().get(random, blockPos);

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
                            BlockState blockState2 = structureWorldAccess.getBlockState(blockPos.add(s, u, t));
                            if (u >= 4 && blockState2.isLiquid()) {
                                return false;
                            }

                            if (u < 4 && !blockState2.isSolid() && structureWorldAccess.getBlockState(blockPos.add(s, u, t)) != blockState) {
                                return false;
                            }
                        }
                    }
                }
            }
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 8; y++) {
                        if (bls[(x * 16 + z) * 8 + y]) {
                            BlockPos blockPos2 = blockPos.add(x, y, z);
                            if (this.canReplace(structureWorldAccess.getBlockState(blockPos2))) {
                                boolean bl2 = y >= 4;
                                structureWorldAccess.setBlockState(blockPos2, bl2 ? AIR : blockState, Block.NOTIFY_LISTENERS);
                                if (bl2)
                                    structureWorldAccess.scheduleBlockTick(blockPos2, AIR.getBlock(), 0);
                            }
                        }
                    }
                }
            }

            BlockState blockState3 = config.barrier().get(random, blockPos);
            if (!blockState3.isAir()) {
                for (int t = 0; t < 16; t++) {
                    for (int uxx = 0; uxx < 16; uxx++) {
                        for (int v = 0; v < 8; v++) {
                            boolean bl2 = !bls[(t * 16 + uxx) * 8 + v]
                                    && (
                                    t < 15 && bls[((t + 1) * 16 + uxx) * 8 + v]
                                            || t > 0 && bls[((t - 1) * 16 + uxx) * 8 + v]
                                            || uxx < 15 && bls[(t * 16 + uxx + 1) * 8 + v]
                                            || uxx > 0 && bls[(t * 16 + (uxx - 1)) * 8 + v]
                                            || v < 7 && bls[(t * 16 + uxx) * 8 + v + 1]
                                            || v > 0 && bls[(t * 16 + uxx) * 8 + (v - 1)]
                            );
                            if (bl2 && (v < 4 || random.nextInt(2) != 0)) {
                                BlockState blockState4 = structureWorldAccess.getBlockState(blockPos.add(t, v, uxx));
                                if (blockState4.isSolid() && !blockState4.isIn(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                                    BlockPos blockPos3 = blockPos.add(t, v, uxx);
                                    structureWorldAccess.setBlockState(blockPos3, blockState3, Block.NOTIFY_LISTENERS);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    private boolean canReplace(BlockState state) {
        return !state.isIn(BlockTags.FEATURES_CANNOT_REPLACE);
    }
}