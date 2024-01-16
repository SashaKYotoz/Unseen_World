
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModTags;

public class SpikesFeature extends Feature<BlockStateConfiguration> {
    private BlockState blockState;

    public SpikesFeature() {
        super(BlockStateConfiguration.CODEC);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        blockpos = new BlockPos(blockpos.getX(), context.chunkGenerator().getSeaLevel(), blockpos.getZ());
        RandomSource randomsource = context.random();
        boolean flag = randomsource.nextDouble() > 0.7D;
        blockState = context.config().state;
        double d0 = randomsource.nextDouble() * 2.0D * Math.PI;
        int i = 11 - randomsource.nextInt(5);
        int j = 3 + randomsource.nextInt(3);
        boolean flag1 = randomsource.nextDouble() > 0.65;
        int l = flag1 ? randomsource.nextInt(7) + 6 : randomsource.nextInt(15) + 3;
        if (!flag1 && randomsource.nextDouble() > 0.9D) {
            l += randomsource.nextInt(19) + 8;
        }
        int i1 = Math.min(l + randomsource.nextInt(11), 18);
        int j1 = Math.min(l + randomsource.nextInt(7) - randomsource.nextInt(5), 11);
        int k1 = flag1 ? i : 11;
        for(int l1 = -k1; l1 < k1; ++l1) {
            for(int i2 = -k1; i2 < k1; ++i2) {
                for(int j2 = 0; j2 < l; ++j2) {
                    int k2 = flag1 ? this.heightDependentRadiusEllipse(j2, l, j1) : this.heightDependentRadiusRound(randomsource, j2, l, j1);
                    if (flag1 || l1 < k2) {
                        this.generateIcebergBlock(worldgenlevel, randomsource, blockpos, l, l1, j2, i2, k2, k1, flag1, j, d0, flag, blockState);
                    }
                }
            }
        }
        this.smooth(worldgenlevel, blockpos, j1, l, flag1, i);
        for(int i3 = -k1; i3 < k1; ++i3) {
            for(int j3 = -k1; j3 < k1; ++j3) {
                for(int k3 = -1; k3 > -i1; --k3) {
                    int l3 = flag1 ? Mth.ceil((float)k1 * (1.0F - (float)Math.pow(k3, 2.0D) / ((float)i1 * 8.0F))) : k1;
                    int l2 = this.heightDependentRadiusSteep(randomsource, -k3, i1, j1);
                    if (i3 < l2) {
                        this.generateIcebergBlock(worldgenlevel, randomsource, blockpos, i1, i3, k3, j3, l2, l3, flag1, j, d0, flag, blockState);
                    }
                }
            }
        }

        boolean flag2 = flag1 ? randomsource.nextDouble() > 0.1D : randomsource.nextDouble() > 0.7D;
        if (flag2) {
            this.generateCutOut(randomsource, worldgenlevel, j1, l, blockpos, flag1, i, d0, j);
        }

        return true;
    }

    private void generateCutOut(RandomSource randomSource, LevelAccessor accessor, int p_225102_, int p_225103_, BlockPos p_225104_, boolean p_225105_, int p_225106_, double p_225107_, int p_225108_) {
        int i = randomSource.nextBoolean() ? -1 : 1;
        int j = randomSource.nextBoolean() ? -1 : 1;
        int k = randomSource.nextInt(Math.max(p_225102_ / 2 - 2, 1));
        if (randomSource.nextBoolean()) {
            k = p_225102_ / 2 + 1 - randomSource.nextInt(Math.max(p_225102_ - p_225102_ / 2 - 1, 1));
        }

        int l = randomSource.nextInt(Math.max(p_225102_ / 2 - 2, 1));
        if (randomSource.nextBoolean()) {
            l = p_225102_ / 2 + 1 - randomSource.nextInt(Math.max(p_225102_ - p_225102_ / 2 - 1, 1));
        }

        if (p_225105_) {
            k = l = randomSource.nextInt(Math.max(p_225106_ - 5, 1));
        }

        BlockPos blockpos = new BlockPos(i * k, 0, j * l);
        double d0 = p_225105_ ? p_225107_ + (Math.PI / 2D) : randomSource.nextDouble() * 2.0D * Math.PI;

        for(int i1 = 0; i1 < p_225103_ - 3; ++i1) {
            int j1 = this.heightDependentRadiusRound(randomSource, i1, p_225103_, p_225102_);
            this.carve(j1, i1, p_225104_, accessor, false, d0, blockpos, p_225106_, p_225108_);
        }

        for(int k1 = -1; k1 > -p_225103_ + randomSource.nextInt(5); --k1) {
            int l1 = this.heightDependentRadiusSteep(randomSource, -k1, p_225103_, p_225102_);
            this.carve(l1, k1, p_225104_, accessor, true, d0, blockpos, p_225106_, p_225108_);
        }

    }

    private void carve(int p_66036_, int p_66037_, BlockPos blockPos, LevelAccessor accessor, boolean p_66040_, double p_66041_, BlockPos pos, int p_66043_, int p_66044_) {
        int i = p_66036_ + 1 + p_66043_ / 3;
        int j = Math.min(p_66036_ - 3, 3) + p_66044_ / 2 - 1;

        for(int k = -i; k < i; ++k) {
            for(int l = -i; l < i; ++l) {
                double d0 = this.signedDistanceEllipse(k, l, pos, i, j, p_66041_);
                if (d0 < 0.0D) {
                    BlockPos blockpos = blockPos.offset(k, p_66037_, l);
                    BlockState blockstate = accessor.getBlockState(blockpos);
                    if (isSpikeState(blockstate) || blockstate.is(Blocks.SNOW_BLOCK)) {
                        if (p_66040_) {
                            this.setBlock(accessor, blockpos, blockState);
                        } else {
                            this.setBlock(accessor, blockpos, Blocks.AIR.defaultBlockState());
                            this.removeFloatingSnowLayer(accessor, blockpos);
                        }
                    }
                }
            }
        }

    }

    private void removeFloatingSnowLayer(LevelAccessor accessor, BlockPos blockPos) {
        if (accessor.getBlockState(blockPos.above()).is(Blocks.SNOW)) {
            this.setBlock(accessor, blockPos.above(), Blocks.AIR.defaultBlockState());
        }

    }

    private void generateIcebergBlock(LevelAccessor accessor, RandomSource random, BlockPos blockPos, int p_225113_, int p_225114_, int p_225115_, int p_225116_, int p_225117_, int p_225118_, boolean p_225119_, int p_225120_, double p_225121_, boolean p_225122_, BlockState p_225123_) {
        double d0 = p_225119_ ? this.signedDistanceEllipse(p_225114_, p_225116_, BlockPos.ZERO, p_225118_, this.getEllipseC(p_225115_, p_225113_, p_225120_), p_225121_) : this.signedDistanceCircle(p_225114_, p_225116_, BlockPos.ZERO, p_225117_, random);
        if (d0 < 0.0D) {
            BlockPos blockpos = blockPos.offset(p_225114_, p_225115_, p_225116_);
            double d1 = p_225119_ ? -0.5D : (double)(-6 - random.nextInt(3));
            if (d0 > d1 && random.nextDouble() > 0.9D) {
                return;
            }

            this.setIcebergBlock(blockpos, accessor, random, p_225113_ - p_225115_, p_225113_, p_225119_, p_225122_, p_225123_);
        }

    }

    private void setIcebergBlock(BlockPos blockPos, LevelAccessor accessor, RandomSource random, int p_225128_, int p_225129_, boolean p_225130_, boolean p_225131_, BlockState p_225132_) {
        BlockState blockstate = accessor.getBlockState(blockPos);
        if (blockstate.isAir() || isSpikeState(blockstate)) {
            boolean flag = !p_225130_ || random.nextDouble() > 0.05D;
            int i = p_225130_ ? 3 : 2;
            if (p_225131_ && !isSpikeState(blockstate) && (double)p_225128_ <= (double)random.nextInt(Math.max(1, p_225129_ / i)) + (double)p_225129_ * 0.6D && flag) {
                this.setBlock(accessor, blockPos, blockState.is(Blocks.PACKED_ICE) ? Blocks.SNOW_BLOCK.defaultBlockState() : blockState);
            } else {
                this.setBlock(accessor, blockPos, p_225132_);
            }
        }

    }

    private int getEllipseC(int p_66019_, int p_66020_, int p_66021_) {
        int i = p_66021_;
        if (p_66019_ > 0 && p_66020_ - p_66019_ <= 3) {
            i = p_66021_ - (4 - (p_66020_ - p_66019_));
        }

        return i;
    }

    private double signedDistanceCircle(int p_225089_, int p_225090_, BlockPos blockPos, int p_225092_, RandomSource p_225093_) {
        float f = 10.0F * Mth.clamp(p_225093_.nextFloat(), 0.2F, 0.8F) / (float)p_225092_;
        return (double)f + Math.pow(p_225089_ - blockPos.getX(), 2.0D) + Math.pow(p_225090_ - blockPos.getZ(), 2.0D) - Math.pow(p_225092_, 2.0D);
    }

    private double signedDistanceEllipse(int p_66023_, int p_66024_, BlockPos blockPos, int p_66026_, int p_66027_, double p_66028_) {
        return Math.pow(((double)(p_66023_ - blockPos.getX()) * Math.cos(p_66028_) - (double)(p_66024_ - blockPos.getZ()) * Math.sin(p_66028_)) / (double)p_66026_, 2.0D) + Math.pow(((double)(p_66023_ - blockPos.getX()) * Math.sin(p_66028_) + (double)(p_66024_ - blockPos.getZ()) * Math.cos(p_66028_)) / (double)p_66027_, 2.0D) - 1.0D;
    }

    private int heightDependentRadiusRound(RandomSource random, int p_225096_, int p_225097_, int p_225098_) {
        float f = 3.5F - random.nextFloat();
        float f1 = (1.0F - (float)Math.pow(p_225096_, 2.0D) / ((float)p_225097_ * f)) * (float)p_225098_;
        if (p_225097_ > 15 + random.nextInt(5)) {
            int i = p_225096_ < 3 + random.nextInt(6) ? p_225096_ / 2 : p_225096_;
            f1 = (1.0F - (float)i / ((float)p_225097_ * f * 0.4F)) * (float)p_225098_;
        }

        return Mth.ceil(f1 / 2.0F);
    }

    private int heightDependentRadiusEllipse(int p_66110_, int p_66111_, int p_66112_) {
        float f = 1.0F;
        float f1 = (1.0F - (float)Math.pow(p_66110_, 2.0D) / ((float) p_66111_)) * (float)p_66112_;
        return Mth.ceil(f1 / 2.0F);
    }

    private int heightDependentRadiusSteep(RandomSource random, int p_225135_, int p_225136_, int p_225137_) {
        float f = 1.0F + random.nextFloat() / 2.0F;
        float f1 = (1.0F - (float)p_225135_ / ((float)p_225136_ * f)) * (float)p_225137_;
        return Mth.ceil(f1 / 2.0F);
    }

    private static boolean isSpikeState(BlockState blockState) {
        return blockState.is(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS) || blockState.is(UnseenWorldModTags.Blocks.STONE_THE_DARKNESS);
    }

    private boolean belowIsAir(BlockGetter getter, BlockPos blockPos) {
        return getter.getBlockState(blockPos.below()).isAir();
    }

    private void smooth(LevelAccessor accessor, BlockPos blockPos, int p_66054_, int p_66055_, boolean p_66056_, int p_66057_) {
        int i = p_66056_ ? p_66057_ : p_66054_ / 2;
        for(int j = -i; j <= i; ++j) {
            for(int k = -i; k <= i; ++k) {
                for(int l = 0; l <= p_66055_; ++l) {
                    BlockPos blockpos = blockPos.offset(j, l, k);
                    BlockState blockstate = accessor.getBlockState(blockpos);
                    if (isSpikeState(blockstate) || blockstate.is(Blocks.SNOW)) {
                        if (this.belowIsAir(accessor, blockpos)) {
                            this.setBlock(accessor, blockpos, Blocks.AIR.defaultBlockState());
                            this.setBlock(accessor, blockpos.above(), Blocks.AIR.defaultBlockState());
                        } else if (isSpikeState(blockstate)) {
                            BlockState[] ablockstate = new BlockState[]{accessor.getBlockState(blockpos.west()), accessor.getBlockState(blockpos.east()), accessor.getBlockState(blockpos.north()), accessor.getBlockState(blockpos.south())};
                            int i1 = 0;
                            for(BlockState blockstate1 : ablockstate) {
                                if (!isSpikeState(blockstate1)) {
                                    ++i1;
                                }
                            }
                            if (i1 >= 3) {
                                this.setBlock(accessor, blockpos, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
            }
        }
    }
}
