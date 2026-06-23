package net.sashakyotoz.common.world.features.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
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
    public boolean generate(FeatureContext<SpiralSpikeFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        SpiralSpikeFeatureConfig config = context.getConfig();

        int height = config.size().get(random) + random.nextInt(5) + 5;
        int span = height + random.nextInt(height / 2);
        int radius = Math.max(1, (config.size().get(random) - 1) / 2);

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
        if (world.isRegionLoaded(origin, BlockPos.ofFloored(endX, endY, endZ))) {
            BlockState baseBlock = config.state().get(random, origin);
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
                                BlockPos pos = centerPos.add(x, y, z);

                                if (!placedPositions.add(pos)) continue;

                                if (world.getBlockState(pos).isAir() || !world.getBlockState(pos).isOpaque()) {
                                    BlockState topBlock = config.facing_state().get(random, pos);
                                    Direction direction = getFreeSide(world, pos);
                                    if (topBlock.contains(Properties.FACING))
                                        topBlock = topBlock.with(Properties.FACING, direction);
                                    this.setBlockState(world, pos, random.nextInt(3) == 1 ? topBlock : baseBlock);
                                    if (world.getBlockState(pos.offset(direction, 2)).isAir() && random.nextFloat() < 0.125) {
                                        BlockState decorativeState = config.decorating_state().get(random, pos);
                                        decorativeState = decorativeState.with(Properties.FACING, direction);
                                        this.setBlockState(world, pos.offset(direction), decorativeState);
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

    private void fillDownToGround(StructureWorldAccess world, BlockPos pos, BlockState state) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (int i = 0; i < 32; i++) {
            mutable.move(Direction.DOWN);
            if (world.getBlockState(mutable).isAir() || !world.getBlockState(mutable).isOpaque()) {
                this.setBlockState(world, mutable, state);
            } else {
                mutable.move(Direction.DOWN);
                this.setBlockState(world, mutable, state);
                break;
            }
        }
    }

    public static Direction getFreeSide(TestableWorld world, BlockPos pos) {
        AtomicReference<Direction> freeSide = new AtomicReference<>(Direction.UP);
        Direction.stream()
                .filter(direction -> direction.getAxis().isHorizontal() && world.testBlockState(pos.offset(direction),
                        AbstractBlock.AbstractBlockState::isAir))
                .findFirst().ifPresent(freeSide::set);
        return freeSide.get();
    }
}