package net.sashakyotoz.common.world.carvers.custom;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.sashakyotoz.common.world.carvers.custom.configs.SpiralCaveCarverConfig;

import java.util.function.Function;

public class SpiralCaveCarver extends Carver<SpiralCaveCarverConfig> {
    public static final Carver<SpiralCaveCarverConfig> INSTANCE = new SpiralCaveCarver(SpiralCaveCarverConfig.CODEC);

    public SpiralCaveCarver(Codec<SpiralCaveCarverConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean carve(CarverContext context, SpiralCaveCarverConfig config, Chunk chunk, Function<BlockPos, RegistryEntry<Biome>> posToBiome, Random random, AquiferSampler aquiferSampler, ChunkPos pos, CarvingMask mask) {
        double baseRadius = Math.max(1.0, config.radius());
        int length = Math.max(1, config.length());
        double turnRate = config.turnRate();
        double radiusVar = config.radiusVariance();
        int vSpread = Math.max(1, config.verticalSpread());
        int chunkWorldX = pos.x << 4;
        int chunkWorldZ = pos.z << 4;
        double centerY = Math.max(10, chunk.getHeight());
        double startX = chunkWorldX + 8.0;
        double startZ = chunkWorldZ + 8.0;
        boolean carvedAny = false;
        for (int step = 0; step < length; step++) {
            double angle = step * turnRate;
            double r = baseRadius + (random.nextDouble() - 0.5) * radiusVar;
            double advance = step * 1.0;
            double forwardZ = 1.0;
            double offsetX = Math.cos(angle) * r;
            double offsetZ = Math.sin(angle) * r;
            double pointX = startX + advance + offsetX;
            double pointZ = startZ + forwardZ + offsetZ;
            int cx = (int) Math.floor(pointX);
            int cz = (int) Math.floor(pointZ);
            int intRadius = (int) Math.ceil(r + 1.5);
            for (int dx = -intRadius; dx <= intRadius; dx++) {
                for (int dz = -intRadius; dz <= intRadius; dz++) {
                    double nx = dx + (pointX - cx);
                    double nz = dz + (pointZ - cz);
                    double horizDistSq = nx * nx + nz * nz;
                    if (horizDistSq <= r * r + 0.9) {
                        for (int dy = -vSpread; dy <= vSpread; dy++) {
                            int wy = (int) centerY + dy;
                            int wx = cx + dx;
                            int wz = cz + dz;
                            int localX = wx & 15;
                            int localZ = wz & 15;
                            if (wy < 1 || wy > 254) continue;
                            if (mask.get(localX, wy, localZ)) continue;
                            BlockPos posBlock = new BlockPos(wx, wy, wz);
                            if (!canReplaceBlock(chunk, posBlock)) continue;
                            chunk.setBlockState(posBlock, Blocks.AIR.getDefaultState(), false);
                            mask.set(localX, wy, localZ);
                            carvedAny = true;
                        }
                    }
                }
            }
        }
        return carvedAny;
    }

    private boolean canReplaceBlock(Chunk chunk, BlockPos pos) {
        if (!chunk.getPos().equals(new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4))) return false;
        var state = chunk.getBlockState(pos);
        return !state.isOf(Blocks.BEDROCK) && state.getFluidState().isEmpty();
    }

    @Override
    public boolean shouldCarve(SpiralCaveCarverConfig config, Random random) {
        return random.nextFloat() <= config.probability;
    }
}