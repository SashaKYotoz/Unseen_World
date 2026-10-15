package net.sashakyotoz.common.world.features.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.sashakyotoz.common.world.features.custom.configs.NestFeatureConfig;

public class NestFeature extends Feature<NestFeatureConfig> {
    public static final NestFeature INSTANCE = new NestFeature();

    private static final int HEADROOM = 3;
    private static final int FLOOR_SEARCH = 2;
    private static final float TALL_SHELL_CHANCE = 0.5F;

    public NestFeature() {
        super(NestFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NestFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        NestFeatureConfig config = context.config();
        BlockPos center = context.origin();
        if (!isOpen(level, center) || !isGround(level, center.below())) {
            return false;
        }
        for (int i = 1; i < HEADROOM; i++) {
            if (!isOpen(level, center.above(i))) {
                return false;
            }
        }
        int radius = config.radius().sample(random);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double distance = Math.sqrt(dx * dx + dz * dz);
                if (distance > radius + 0.5D) {
                    continue;
                }
                BlockPos column = surface(level, center.offset(dx, 0, dz));
                if (column == null) {
                    continue;
                }
                if (distance < radius - 1.0D) {
                    BlockPos ground = column.below();
                    level.setBlock(ground, config.floor().getState(random, ground), 2);
                } else {
                    this.placeShell(level, random, config, column, distance < radius - 0.5D);
                }
            }
        }
        BlockPos coreGround = center.below();
        level.setBlock(coreGround, config.floor().getState(random, coreGround), 2);
        placeIfSurvives(level, center, config.core().getState(random, center));
        this.spawnDwellers(level, random, config, center);
        return true;
    }

    private void placeShell(WorldGenLevel level, RandomSource random, NestFeatureConfig config, BlockPos column, boolean inner) {
        int height = inner && random.nextFloat() < TALL_SHELL_CHANCE ? 2 : 1;
        BlockPos.MutableBlockPos pos = column.mutable();
        for (int i = 0; i < height; i++) {
            if (!isOpen(level, pos)) {
                return;
            }
            level.setBlock(pos, config.shell().getState(random, pos), 2);
            pos.move(Direction.UP);
        }
        if (isOpen(level, pos) && random.nextFloat() < config.decorationChance()) {
            placeIfSurvives(level, pos.immutable(), config.decoration().getState(random, pos));
        }
    }

    private void spawnDwellers(WorldGenLevel level, RandomSource random, NestFeatureConfig config, BlockPos center) {
        int count = config.dwellerCount().sample(random);
        for (int i = 0; i < count; i++) {
            Entity entity = config.dweller().create(level.getLevel());
            if (!(entity instanceof Mob mob)) {
                return;
            }
            mob.moveTo(center.getX() + 0.5D, center.getY(), center.getZ() + 0.5D, random.nextFloat() * 360.0F, 0.0F);
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(center), MobSpawnType.STRUCTURE, null, null);
            if (mob instanceof NestDweller dweller) {
                dweller.bindToNest(center);
            }
            level.addFreshEntityWithPassengers(mob);
        }
    }

    private static void placeIfSurvives(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (state.canSurvive(level, pos)) {
            level.setBlock(pos, state, 2);
        }
    }

    private static BlockPos surface(WorldGenLevel level, BlockPos column) {
        BlockPos.MutableBlockPos pos = column.mutable().move(Direction.UP, FLOOR_SEARCH);
        for (int i = 0; i <= FLOOR_SEARCH * 2; i++) {
            if (isOpen(level, pos) && isGround(level, pos.below())) {
                return pos.immutable();
            }
            pos.move(Direction.DOWN);
        }
        return null;
    }

    private static boolean isOpen(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || (state.canBeReplaced() && state.getFluidState().isEmpty());
    }

    private static boolean isGround(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).isFaceSturdy(level, pos, Direction.UP);
    }
}
