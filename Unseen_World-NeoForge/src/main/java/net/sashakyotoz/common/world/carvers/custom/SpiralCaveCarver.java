package net.sashakyotoz.common.world.carvers.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.sashakyotoz.common.world.carvers.custom.configs.SpiralCaveCarverConfig;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SpiralCaveCarver extends WorldCarver<SpiralCaveCarverConfig> {
    public static final WorldCarver<SpiralCaveCarverConfig> INSTANCE = new SpiralCaveCarver(SpiralCaveCarverConfig.CODEC);

    private static final int MAX_REACH = 112;
    private static final double MAX_STEP_LENGTH = 2.0D;
    private static final double FLAT_FLOOR_CUTOFF = -0.7D;

    private static final Aquifer DRY_CAVE = new Aquifer() {
        @Nullable
        @Override
        public BlockState computeSubstance(DensityFunction.FunctionContext context, double substance) {
            return Blocks.AIR.defaultBlockState();
        }

        @Override
        public boolean shouldScheduleFluidUpdate() {
            return false;
        }
    };

    public SpiralCaveCarver(Codec<SpiralCaveCarverConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean carve(CarvingContext context, SpiralCaveCarverConfig config, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> posToBiome, RandomSource random, Aquifer aquifer, ChunkPos pos, CarvingMask mask) {
        double baseRadius = Math.max(1.0D, config.radius());
        double radiusVariance = config.radiusVariance();
        double turnRate = config.turnRate();
        double verticalRadius = (Math.max(1, config.verticalSpread()) + 0.5D) * config.yScale.sample(random);
        double centerY = config.y.sample(random, context);
        double heading = random.nextDouble() * (Math.PI * 2.0D);
        double directionX = Math.cos(heading);
        double directionZ = Math.sin(heading);
        double stepLength = Math.min(MAX_STEP_LENGTH, baseRadius);
        int steps = (int) Math.ceil(Mth.clamp(config.length(), 1, MAX_REACH) / stepLength);
        double startX = pos.getMiddleBlockX();
        double startZ = pos.getMiddleBlockZ();
        CarveSkipChecker skipChecker = (ctx, relX, relY, relZ, y) ->
                relY <= FLAT_FLOOR_CUTOFF || relX * relX + relY * relY + relZ * relZ >= 1.0D;
        boolean carvedAny = false;
        for (int step = 0; step < steps; step++) {
            double radius = Math.max(1.0D, baseRadius + (random.nextDouble() - 0.5D) * radiusVariance);
            double distance = step * stepLength;
            double lateral = Math.sin(distance * turnRate) * baseRadius;
            double x = startX + directionX * distance - directionZ * lateral;
            double z = startZ + directionZ * distance + directionX * lateral;
            if (this.carveEllipsoid(context, config, chunk, posToBiome, DRY_CAVE, x, centerY, z, radius, verticalRadius, mask, skipChecker)) {
                carvedAny = true;
            }
        }
        return carvedAny;
    }

    @Override
    public boolean isStartChunk(SpiralCaveCarverConfig config, RandomSource random) {
        return random.nextFloat() <= config.probability;
    }
}