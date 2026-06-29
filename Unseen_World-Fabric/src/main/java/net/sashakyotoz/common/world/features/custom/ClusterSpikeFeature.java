package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.ClusterSpikeFeatureConfig;

public class ClusterSpikeFeature extends Feature<ClusterSpikeFeatureConfig> {
    public static final Feature<ClusterSpikeFeatureConfig> INSTANCE = new ClusterSpikeFeature(ClusterSpikeFeatureConfig.CODEC);

    public ClusterSpikeFeature(Codec<ClusterSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ClusterSpikeFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        ClusterSpikeFeatureConfig config = context.config();
        BlockState blockState = config.state().getState(random, origin);
        int spread = config.integration_size().sample(random);
        if (world.isStateAtPosition(origin.below(), state -> !state.isAir() && !state.canBeReplaced())) {
            int height = 13 + config.height().sample(random);
            float baseRadius = spread / 2.0f;
            if (baseRadius < 2.0f) baseRadius = 3.5f;

            double curveDirectionAngle = random.nextDouble() * 2 * Math.PI;
            double maxCurveDistance = 8.0 + random.nextDouble() * 6.0;

            for (float x = -spread; x < spread; x++) {
                for (float z = -spread; z < spread; z++) {
                    if (random.nextFloat() >= ((x / spread) + (z / spread)) / 2f) {
                        BlockPos offset = origin.offset((int) x, 0, (int) z);
                        BlockPos heightMapPos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, offset);
                        if (world.isStateAtPosition(heightMapPos, state -> !state.isAir() && !state.canBeReplaced())) {
                            this.setBlock(world, heightMapPos, blockState);
                        }
                    }
                }
            }

            for (int y = 0; y < height; y++) {
                float progress = (float) y / height;
                float currentRadius = baseRadius * (1.0f - progress);

                double curveOffset = Math.pow(progress, 2) * maxCurveDistance;
                double offsetX = Math.cos(curveDirectionAngle) * curveOffset;
                double offsetZ = Math.sin(curveDirectionAngle) * curveOffset;

                BlockPos layerCenter = origin.offset((int) Math.round(offsetX), y, (int) Math.round(offsetZ));

                int ceilRadius = (int) Math.ceil(currentRadius);
                for (int x = -ceilRadius; x <= ceilRadius; x++) {
                    for (int z = -ceilRadius; z <= ceilRadius; z++) {
                        if (x * x + z * z <= currentRadius * currentRadius) {
                            BlockPos blockPos = layerCenter.offset(x, 0, z);

                            if (world.isStateAtPosition(blockPos, state -> state.isAir() || state.canBeReplaced())) {
                                if (y == 0) {
                                    BlockPos surfacePos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos);
                                    int dropDistance = blockPos.getY() - surfacePos.getY();
                                    for (int i = 1; i <= dropDistance; i++) {
                                        BlockPos fallingPos = blockPos.below(i);
                                        if (world.isStateAtPosition(fallingPos, state -> state.isAir() || state.canBeReplaced()))
                                            this.setBlock(world, fallingPos, blockState);
                                        else
                                            break;
                                    }
                                }

                                this.setBlock(world, blockPos, blockState);

                                if (y > height / 2 && random.nextBoolean()) {
                                    Direction direction = SpiralSpikeFeature.getFreeSide(world, blockPos);
                                    this.setBlock(world, blockPos
                                            .relative(direction), config.cluster_state().getState(random, blockPos)
                                            .trySetValue(BlockStateProperties.FACING, direction));
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}