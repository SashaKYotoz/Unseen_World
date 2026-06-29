package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.DiskOreFeatureConfig;

public class DiskOreFeature extends Feature<DiskOreFeatureConfig> {
    public static final DiskOreFeature INSTANCE = new DiskOreFeature(DiskOreFeatureConfig.CODEC);

    public DiskOreFeature(Codec<DiskOreFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DiskOreFeatureConfig> context) {
        DiskOreFeatureConfig diskFeatureConfig = context.config();
        BlockPos blockPos = context.origin();
        WorldGenLevel structureWorldAccess = context.level();
        RandomSource random = context.random();
        boolean bl = false;
        int i = blockPos.getY();
        int j = i + diskFeatureConfig.halfHeight();
        int k = i - diskFeatureConfig.halfHeight() - 1;
        int l = diskFeatureConfig.radius().sample(random);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-l, 0, -l), blockPos.offset(l, 0, l))) {
            int m = blockPos2.getX() - blockPos.getX();
            int n = blockPos2.getZ() - blockPos.getZ();
            if (m * m + n * n <= l * l) {
                bl |= this.placeBlock(diskFeatureConfig, structureWorldAccess, random, j, k, mutable.set(blockPos2));
            }
        }

        return bl;
    }

    protected boolean placeBlock(DiskOreFeatureConfig config, WorldGenLevel world, RandomSource random, int topY, int bottomY, BlockPos.MutableBlockPos pos) {
        boolean bl = false;
        for (int i = topY; i > bottomY; i--) {
            pos.setY(i);
            if (config.target().test(world, pos)) {
                BlockState blockState = config.stateProvider().getState(world, random, pos);
                BlockState oreState = config.oreStateProvider().getState(world, random, pos);
                world.setBlock(pos, random.nextFloat() > 0.8f ? oreState : blockState, Block.UPDATE_CLIENTS);
                this.markAboveForPostProcessing(world, pos);
                bl = true;
            }
        }

        return bl;
    }
}
