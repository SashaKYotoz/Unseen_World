package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.SpiralSpikeFeatureConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class SpiralSpikeFeature extends Feature<SpiralSpikeFeatureConfig> {
    public static final Feature<SpiralSpikeFeatureConfig> INSTANCE = new SpiralSpikeFeature(SpiralSpikeFeatureConfig.CODEC);

    public SpiralSpikeFeature(Codec<SpiralSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpiralSpikeFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        SpiralSpikeFeatureConfig config = context.config();

        int height = config.size().sample(random) + random.nextInt(5) + 5;
        int span = height + random.nextInt(height / 2);
        int radius = Math.max(1, (config.size().sample(random) - 1) / 2);

        double angle = random.nextDouble() * Math.PI * 2;
        double startX = origin.getX();
        double startY = origin.getY();
        double startZ = origin.getZ();

        double endX = startX + (Math.cos(angle) * span);
        double endZ = startZ + (Math.sin(angle) * span);
        double endY = origin.getY();

        double controlX = (startX + endX) / 2.0;
        double controlZ = (startZ + endZ) / 2.0;
        double controlY = startY + height * 1.5;
        if (world.hasChunksAt(origin, BlockPos.containing(endX, endY, endZ))) {
            BlockState baseBlock = config.state().getState(random, origin);
            Set<BlockPos> placedPositions = new HashSet<>();

            int steps = span * 4;

            for (int i = 0; i <= steps; i++) {
                double t = (double) i / steps;

                double u = 1 - t;
                double tt = t * t;
                double uu = u * u;

                double currentX = (uu * startX) + (2 * u * t * controlX) + (tt * endX);
                double currentY = (uu * startY) + (2 * u * t * controlY) + (tt * endY);
                double currentZ = (uu * startZ) + (2 * u * t * controlZ) + (tt * endZ);

                double noiseIntensity = 1.2;
                if (t < 0.1 || t > 0.9) noiseIntensity = 0.1;

                currentX += (random.nextDouble() - 0.5) * noiseIntensity;
                currentY += (random.nextDouble() - 0.5) * noiseIntensity;
                currentZ += (random.nextDouble() - 0.5) * noiseIntensity;

                BlockPos centerPos = new BlockPos((int) currentX, (int) currentY, (int) currentZ);

                int currentRadius = radius;
                if (t < 0.1 || t > 0.9) currentRadius = Math.max(1, radius - 1);

                for (int x = -currentRadius; x <= currentRadius; x++) {
                    for (int y = -currentRadius; y <= currentRadius; y++) {
                        for (int z = -currentRadius; z <= currentRadius; z++) {
                            if (x * x + y * y + z * z <= currentRadius * currentRadius) {
                                BlockPos pos = centerPos.offset(x, y, z);

                                if (!placedPositions.add(pos)) continue;

                                if (world.getBlockState(pos).isAir() || !world.getBlockState(pos).canOcclude()) {
                                    BlockState topBlock = config.facing_state().getState(random, pos);
                                    Direction direction = getFreeSide(world, pos);
                                    if (topBlock.hasProperty(BlockStateProperties.FACING))
                                        topBlock = topBlock.setValue(BlockStateProperties.FACING, direction);
                                    this.setBlock(world, pos, random.nextInt(3) == 1 ? topBlock : baseBlock);
                                    if (world.getBlockState(pos.relative(direction, 2)).isAir() && random.nextFloat() < 0.125) {
                                        BlockState decorativeState = config.decorating_state().getState(random, pos);
                                        decorativeState = decorativeState.setValue(BlockStateProperties.FACING, direction);
                                        this.setBlock(world, pos.relative(direction), decorativeState);
                                    }
                                }
                            }
                        }
                    }
                }

                if (t > 0.95) {
                    fillDownToGround(world, centerPos, baseBlock);
                }
            }

            fillDownToGround(world, origin, baseBlock);
        }
        return true;
    }

    private void fillDownToGround(WorldGenLevel world, BlockPos pos, BlockState state) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        for (int i = 0; i < 32; i++) {
            mutable.move(Direction.DOWN);
            if (world.getBlockState(mutable).isAir() || !world.getBlockState(mutable).canOcclude()) {
                this.setBlock(world, mutable, state);
            } else {
                mutable.move(Direction.DOWN);
                this.setBlock(world, mutable, state);
                break;
            }
        }
    }

    public static Direction getFreeSide(LevelSimulatedReader world, BlockPos pos) {
        AtomicReference<Direction> freeSide = new AtomicReference<>(Direction.UP);
        Direction.stream()
                .filter(direction -> direction.getAxis().isHorizontal() && world.isStateAtPosition(pos.relative(direction),
                        BlockBehaviour.BlockStateBase::isAir))
                .findFirst().ifPresent(freeSide::set);
        return freeSide.get();
    }
}