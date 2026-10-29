package net.sashakyotoz.common.world.features.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.TaprootTreeFeatureConfig;

import java.util.*;

public class TaprootTreeFeature extends Feature<TaprootTreeFeatureConfig> {
    public static final TaprootTreeFeature INSTANCE = new TaprootTreeFeature();

    private static final int MIN_TRUNK = 3;
    private static final int MAX_LEAF_DISTANCE = 7;
    private static final int ANCHOR_RADIUS = 2;
    private static final float ROOT_CHANCE = 0.5F;
    private static final float HANGING_LEAVES_CHANCE = 0.12F;
    private static final float CROWN_EDGE_CHANCE = 0.6F;

    public TaprootTreeFeature() {
        super(TaprootTreeFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<TaprootTreeFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        TaprootTreeFeatureConfig config = context.config();
        BlockPos origin = context.origin();
        BlockPos ceiling = origin.above();
        if (!level.isEmptyBlock(origin) || !level.getBlockState(ceiling).isFaceSturdy(level, ceiling, Direction.DOWN)) {
            return false;
        }
        int radius = config.crownRadius().sample(random);
        int length = config.trunkLength().sample(random);
        int clearance = clearanceBelow(level, origin, length + radius + 2);
        length = Math.min(length, clearance - radius - 1);
        if (length < MIN_TRUNK) {
            return false;
        }

        Set<BlockPos> logs = new HashSet<>();
        for (int i = 0; i < length; i++) {
            BlockPos pos = origin.below(i);
            level.setBlock(pos, axis(config.trunk().getState(random, pos), Direction.Axis.Y), 2);
            logs.add(pos);
        }
        if (random.nextFloat() < config.heartChance()) {
            BlockPos heart = origin.below(1 + random.nextInt(length - 2));
            level.setBlock(heart, config.heart(), 2);
            logs.remove(heart);
        }
        this.placeCeilingRoots(level, random, config, origin, logs);
        this.placeAnchor(level, random, config, ceiling);

        BlockPos bottom = origin.below(length - 1);
        Set<BlockPos> leaves = this.collectCrown(level, random, bottom, radius);
        this.placeLeaves(level, random, config, logs, leaves);
        return true;
    }

    private void placeCeilingRoots(WorldGenLevel level, RandomSource random, TaprootTreeFeatureConfig config, BlockPos origin, Set<BlockPos> logs) {
        int count = 2 + random.nextInt(3);
        for (int i = 0; i < count; i++) {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int reach = 1 + random.nextInt(3);
            for (int step = 1; step <= reach; step++) {
                BlockPos pos = origin.relative(direction, step);
                BlockPos above = pos.above();
                if (!level.isEmptyBlock(pos) || !level.getBlockState(above).isFaceSturdy(level, above, Direction.DOWN)) {
                    break;
                }
                level.setBlock(pos, axis(config.trunk().getState(random, pos), direction.getAxis()), 2);
                logs.add(pos);
            }
        }
    }

    private void placeAnchor(WorldGenLevel level, RandomSource random, TaprootTreeFeatureConfig config, BlockPos ceiling) {
        for (int dx = -ANCHOR_RADIUS; dx <= ANCHOR_RADIUS; dx++) {
            for (int dz = -ANCHOR_RADIUS; dz <= ANCHOR_RADIUS; dz++) {
                if (dx * dx + dz * dz > ANCHOR_RADIUS * ANCHOR_RADIUS + 1) {
                    continue;
                }
                BlockPos pos = ceiling.offset(dx, 0, dz);
                BlockState state = level.getBlockState(pos);
                if (!state.is(config.anchorReplaceable()) && !state.is(BlockTags.DIRT)) {
                    continue;
                }
                level.setBlock(pos, config.anchor().getState(random, pos), 2);
                BlockPos below = pos.below();
                if (level.isEmptyBlock(below) && random.nextFloat() < ROOT_CHANCE) {
                    BlockState roots = config.roots().getState(random, below);
                    if (roots.canSurvive(level, below)) {
                        level.setBlock(below, roots, 2);
                    }
                }
            }
        }
    }

    private Set<BlockPos> collectCrown(WorldGenLevel level, RandomSource random, BlockPos bottom, int radius) {
        Set<BlockPos> leaves = new HashSet<>();
        double limit = radius * radius + 0.5D;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -radius; dy <= 1; dy++) {
                    double distance = dx * dx + dz * dz + dy * dy * 2.25D;
                    if (distance > limit || (distance > limit - 2.0D && random.nextFloat() > CROWN_EDGE_CHANCE)) {
                        continue;
                    }
                    BlockPos pos = bottom.offset(dx, dy, dz);
                    if (level.isEmptyBlock(pos)) {
                        leaves.add(pos);
                    }
                }
            }
        }
        return leaves;
    }

    private void placeLeaves(WorldGenLevel level, RandomSource random, TaprootTreeFeatureConfig config, Set<BlockPos> logs, Set<BlockPos> leaves) {
        Map<BlockPos, Integer> distances = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        for (BlockPos log : logs) {
            for (Direction direction : Direction.values()) {
                BlockPos next = log.relative(direction);
                if (leaves.contains(next) && !distances.containsKey(next)) {
                    distances.put(next, 1);
                    queue.add(next);
                }
            }
        }
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int distance = distances.get(current);
            if (distance >= MAX_LEAF_DISTANCE - 1) {
                continue;
            }
            for (Direction direction : Direction.values()) {
                BlockPos next = current.relative(direction);
                if (leaves.contains(next) && !distances.containsKey(next)) {
                    distances.put(next, distance + 1);
                    queue.add(next);
                }
            }
        }
        for (Map.Entry<BlockPos, Integer> entry : distances.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState leaf = config.leaves().getState(random, pos).trySetValue(BlockStateProperties.DISTANCE, entry.getValue());
            level.setBlock(pos, leaf, 2);
        }
        for (BlockPos pos : distances.keySet()) {
            BlockPos below = pos.below();
            if (level.isEmptyBlock(below) && random.nextFloat() < HANGING_LEAVES_CHANCE) {
                BlockState hanging = config.hangingLeaves().getState(random, below);
                if (hanging.canSurvive(level, below)) {
                    level.setBlock(below, hanging, 2);
                }
            }
        }
    }

    private static int clearanceBelow(WorldGenLevel level, BlockPos origin, int max) {
        int clearance = 0;
        while (clearance < max && level.isEmptyBlock(origin.below(clearance))) {
            clearance++;
        }
        return clearance;
    }

    private static BlockState axis(BlockState state, Direction.Axis axis) {
        return state.trySetValue(BlockStateProperties.AXIS, axis);
    }
}
