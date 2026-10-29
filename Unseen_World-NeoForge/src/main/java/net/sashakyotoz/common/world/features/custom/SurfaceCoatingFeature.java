package net.sashakyotoz.common.world.features.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.SurfaceCoatingFeatureConfig;

public class SurfaceCoatingFeature extends Feature<SurfaceCoatingFeatureConfig> {
    public static final SurfaceCoatingFeature INSTANCE = new SurfaceCoatingFeature();

    private static final int VERTICAL_SEARCH = 3;
    private static final float EDGE_FACTOR = 0.5F;

    public SurfaceCoatingFeature() {
        super(SurfaceCoatingFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<SurfaceCoatingFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        SurfaceCoatingFeatureConfig config = context.config();
        BlockPos origin = context.origin();
        int radius = config.radius().sample(random);
        int inner = (radius - 1) * (radius - 1);
        boolean placed = false;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int distance = dx * dx + dz * dz;
                if (distance > radius * radius) {
                    continue;
                }
                float chance = distance <= inner ? config.density() : config.density() * EDGE_FACTOR;
                if (random.nextFloat() >= chance) {
                    continue;
                }
                BlockPos pos = findFloor(level, origin.offset(dx, 0, dz));
                if (pos == null) {
                    continue;
                }
                BlockState coating = config.coating().getState(random, pos);
                if (coating.canSurvive(level, pos)) {
                    level.setBlock(pos, coating, 2);
                    placed = true;
                }
            }
        }
        return placed;
    }

    private static BlockPos findFloor(WorldGenLevel level, BlockPos column) {
        BlockPos.MutableBlockPos pos = column.mutable().move(0, VERTICAL_SEARCH, 0);
        for (int i = 0; i <= VERTICAL_SEARCH * 2; i++) {
            if (level.isEmptyBlock(pos) && !level.isEmptyBlock(pos.below())) {
                return pos.immutable();
            }
            pos.move(0, -1, 0);
        }
        return null;
    }
}
