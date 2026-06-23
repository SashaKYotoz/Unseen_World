package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.sashakyotoz.common.world.features.custom.configs.ClusterSpikeFeatureConfig;

public class ClusterSpikeFeature extends Feature<ClusterSpikeFeatureConfig> {
    public static final Feature<ClusterSpikeFeatureConfig> INSTANCE = new ClusterSpikeFeature(ClusterSpikeFeatureConfig.CODEC);

    public ClusterSpikeFeature(Codec<ClusterSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<ClusterSpikeFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        ClusterSpikeFeatureConfig config = context.getConfig();
        BlockState blockState = config.state().get(random, origin);
        int spread = config.integration_size().get(random);
        if (world.testBlockState(origin.down(), state -> !state.isAir() && !state.isReplaceable())) {
            int height = 13 + config.height().get(random);
            float baseRadius = spread / 2.0f;
            if (baseRadius < 2.0f) baseRadius = 3.5f;

            double curveDirectionAngle = random.nextDouble() * 2 * Math.PI;
            double maxCurveDistance = 8.0 + random.nextDouble() * 6.0;

            for (float x = -spread; x < spread; x++) {
                for (float z = -spread; z < spread; z++) {
                    if (random.nextFloat() >= ((x / spread) + (z / spread)) / 2f) {
                        BlockPos offset = origin.add((int) x, 0, (int) z);
                        BlockPos heightMapPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, offset);
                        if (world.testBlockState(heightMapPos, state -> !state.isAir() && !state.isReplaceable())) {
                            this.setBlockState(world, heightMapPos, blockState);
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

                BlockPos layerCenter = origin.add((int) Math.round(offsetX), y, (int) Math.round(offsetZ));

                int ceilRadius = (int) Math.ceil(currentRadius);
                for (int x = -ceilRadius; x <= ceilRadius; x++) {
                    for (int z = -ceilRadius; z <= ceilRadius; z++) {
                        if (x * x + z * z <= currentRadius * currentRadius) {
                            BlockPos blockPos = layerCenter.add(x, 0, z);

                            if (world.testBlockState(blockPos, state -> state.isAir() || state.isReplaceable())) {
                                if (y == 0) {
                                    BlockPos surfacePos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockPos);
                                    int dropDistance = blockPos.getY() - surfacePos.getY();
                                    for (int i = 1; i <= dropDistance; i++) {
                                        BlockPos fallingPos = blockPos.down(i);
                                        if (world.testBlockState(fallingPos, state -> state.isAir() || state.isReplaceable()))
                                            this.setBlockState(world, fallingPos, blockState);
                                        else
                                            break;
                                    }
                                }

                                this.setBlockState(world, blockPos, blockState);

                                if (y > height / 2 && random.nextBoolean()) {
                                    Direction direction = SpiralSpikeFeature.getFreeSide(world, blockPos);
                                    this.setBlockState(world, blockPos
                                            .offset(direction), config.cluster_state().get(random, blockPos)
                                            .withIfExists(Properties.FACING, direction));
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