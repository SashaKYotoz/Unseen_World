package net.sashakyotoz.common.world.features.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.StoneRingFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class StoneRingFeature extends Feature<StoneRingFeatureConfig> {
    public static final StoneRingFeature INSTANCE = new StoneRingFeature();

    private static final int MAX_GROUND_DIFFERENCE = 3;

    public StoneRingFeature() {
        super(StoneRingFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<StoneRingFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        StoneRingFeatureConfig config = context.config();
        BlockPos origin = context.origin();
        int radius = config.radius().sample(random);
        int count = config.pillarCount().sample(random);
        double startAngle = random.nextDouble() * Math.PI * 2.0D;
        List<BlockPos> bases = new ArrayList<>(count);
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            double angle = startAngle + Math.PI * 2.0D * i / count;
            BlockPos base = surface(level, origin.getX() + Mth.floor(Math.cos(angle) * radius + 0.5D), origin.getZ() + Mth.floor(Math.sin(angle) * radius + 0.5D));
            if (base == null) {
                return false;
            }
            bases.add(base);
            minY = Math.min(minY, base.getY());
            maxY = Math.max(maxY, base.getY());
        }
        if (maxY - minY > MAX_GROUND_DIFFERENCE) {
            return false;
        }
        for (BlockPos base : bases) {
            this.placePillar(level, random, config, base);
        }
        this.placeFlowers(level, random, config, origin, radius);
        return true;
    }

    private void placePillar(WorldGenLevel level, RandomSource random, StoneRingFeatureConfig config, BlockPos base) {
        boolean broken = random.nextFloat() < config.brokenChance();
        int height = config.pillarHeight().sample(random);
        if (broken) {
            height = Math.max(1, height - 1 - random.nextInt(2));
        }
        BlockPos.MutableBlockPos pos = base.mutable();
        for (int i = 0; i < height; i++) {
            if (!canReplace(level.getBlockState(pos))) {
                return;
            }
            level.setBlock(pos, config.pillar().getState(random, pos), 2);
            pos.move(Direction.UP);
        }
        if (!broken) {
            if (canReplace(level.getBlockState(pos))) {
                level.setBlock(pos, config.cap().getState(random, pos), 2);
            }
            return;
        }
        Direction fall = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos rubble = surface(level, base.getX() + fall.getStepX() * 2, base.getZ() + fall.getStepZ() * 2);
        if (rubble != null && canReplace(level.getBlockState(rubble))) {
            level.setBlock(rubble, config.pillar().getState(random, rubble), 2);
        }
    }

    private void placeFlowers(WorldGenLevel level, RandomSource random, StoneRingFeatureConfig config, BlockPos origin, int radius) {
        int inner = Math.max(1, radius - 1);
        for (int i = 0; i < radius * 4; i++) {
            int dx = random.nextInt(inner * 2 + 1) - inner;
            int dz = random.nextInt(inner * 2 + 1) - inner;
            if (dx * dx + dz * dz > inner * inner) {
                continue;
            }
            BlockPos pos = surface(level, origin.getX() + dx, origin.getZ() + dz);
            if (pos == null || !level.getBlockState(pos).isAir()) {
                continue;
            }
            BlockState flower = config.flower().getState(random, pos);
            if (flower.canSurvive(level, pos)) {
                level.setBlock(pos, flower, 2);
            }
        }
    }

    private static BlockPos surface(WorldGenLevel level, int x, int z) {
        BlockPos pos = new BlockPos(x, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z), z);
        BlockPos ground = pos.below();
        return level.getBlockState(ground).isFaceSturdy(level, ground, Direction.UP) ? pos : null;
    }

    private static boolean canReplace(BlockState state) {
        return state.isAir() || state.canBeReplaced();
    }
}
