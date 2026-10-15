package net.sashakyotoz.common.world.features.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.CoralReefFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class CoralReefFeature extends Feature<CoralReefFeatureConfig> {
    public static final CoralReefFeature INSTANCE = new CoralReefFeature();

    private static final int FLOOR_SEARCH_UP = 2;
    private static final int FLOOR_SEARCH_DEPTH = 6;

    public CoralReefFeature() {
        super(CoralReefFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<CoralReefFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        CoralReefFeatureConfig config = context.config();
        BlockPos origin = context.origin();
        if (!ChimericFreezeFeature.isDarkWaterSource(level.getBlockState(origin))) {
            return false;
        }
        int radius = config.radius().sample(random);
        int height = config.height().sample(random);
        List<BlockPos> placed = new ArrayList<>();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double distance = Math.sqrt(dx * dx + dz * dz);
                if (distance > radius + 0.5D) {
                    continue;
                }
                int columnHeight = Mth.floor(height * (1.0D - distance / (radius + 1.0D))) + random.nextInt(2);
                BlockPos base = findFloor(level, origin.offset(dx, 0, dz));
                if (columnHeight <= 0 || base == null) {
                    continue;
                }
                for (int i = 0; i < columnHeight; i++) {
                    pos.setWithOffset(base, 0, i, 0);
                    if (!ChimericFreezeFeature.isDarkWaterSource(level.getBlockState(pos))
                            || !ChimericFreezeFeature.isDarkWaterSource(level.getBlockState(pos.above()))) {
                        break;
                    }
                    level.setBlock(pos, config.block(), 2);
                    placed.add(pos.immutable());
                }
            }
        }
        for (BlockPos block : placed) {
            this.decorate(level, random, config, block);
        }
        return !placed.isEmpty();
    }

    private void decorate(WorldGenLevel level, RandomSource random, CoralReefFeatureConfig config, BlockPos block) {
        BlockPos above = block.above();
        if (ChimericFreezeFeature.isDarkWaterSource(level.getBlockState(above)) && random.nextFloat() < config.decorationChance() * 2.0F) {
            BlockState top = random.nextBoolean() ? config.plant() : config.fan();
            level.setBlock(above, waterlogged(top), 2);
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos side = block.relative(direction);
            if (random.nextFloat() < config.decorationChance() && ChimericFreezeFeature.isDarkWaterSource(level.getBlockState(side))) {
                level.setBlock(side, waterlogged(config.wallFan().trySetValue(BlockStateProperties.HORIZONTAL_FACING, direction)), 2);
            }
        }
    }

    private static BlockState waterlogged(BlockState state) {
        return state.trySetValue(BlockStateProperties.WATERLOGGED, true);
    }

    private static BlockPos findFloor(WorldGenLevel level, BlockPos start) {
        BlockPos.MutableBlockPos pos = start.mutable().move(Direction.UP, FLOOR_SEARCH_UP);
        for (int i = 0; i < FLOOR_SEARCH_DEPTH; i++) {
            BlockPos belowPos = pos.below();
            BlockState below = level.getBlockState(belowPos);
            if (!ChimericFreezeFeature.isDarkWaterSource(below)) {
                boolean valid = ChimericFreezeFeature.isDarkWaterSource(level.getBlockState(pos)) && below.isFaceSturdy(level, belowPos, Direction.UP);
                return valid ? pos.immutable() : null;
            }
            pos.move(Direction.DOWN);
        }
        return null;
    }
}
