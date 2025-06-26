package net.sashakyotoz.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.BlockColumn;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.ModWorldGeneration;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//NaturesSpirit copyright
//source code: https://github.com/Team-Hibiscus/NaturesSpirit/blob/dev/src/main/java/net/hibiscus/naturespirit/mixin/SurfaceBuilderMixin.java
@Mixin(SurfaceBuilder.class)
public class SurfaceBuilderMixin {
    @Final
    @Shadow
    private BlockState defaultState;
    @Unique
    private DoublePerlinNoiseSampler cliffPillarNoise;
    @Unique
    private DoublePerlinNoiseSampler cliffPillarRoofNoise;
    @Unique
    private DoublePerlinNoiseSampler cliffSurfaceNoise;
    @Unique
    private DoublePerlinNoiseSampler darknessPillarNoise;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectNoise(NoiseConfig noiseConfig, BlockState defaultState, int seaLevel, RandomSplitter randomDeriver, CallbackInfo ci) {
        cliffPillarNoise = noiseConfig.getOrCreateSampler(ModWorldGeneration.CLIFF_PILLAR);
        cliffPillarRoofNoise = noiseConfig.getOrCreateSampler(ModWorldGeneration.CLIFF_PILLAR_ROOF);
        cliffSurfaceNoise = noiseConfig.getOrCreateSampler(ModWorldGeneration.CLIFF_SURFACE);
        darknessPillarNoise = noiseConfig.getOrCreateSampler(ModWorldGeneration.DARKNESS_CLIFF_PILLAR);
    }

    @Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/entry/RegistryEntry;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z", ordinal = 0))
    private void injectPillars(NoiseConfig noiseConfig, BiomeAccess biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, HeightContext heightContext,
                               Chunk chunk, ChunkNoiseSampler chunkNoiseSampler, MaterialRules.MaterialRule materialRule,
                               CallbackInfo ci,
                               @Local RegistryEntry<Biome> registryEntry, @Local(ordinal = 2) int k, @Local(ordinal = 3) int l, @Local(ordinal = 4) int m, @Local(ordinal = 5) int n,
                               @Local BlockColumn blockColumn) {
        int o = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, k, l) + 1;
        if (registryEntry.matchesKey(ModBiomes.TEALIVY_VALLEY) || registryEntry.matchesKey(ModBiomes.GREENISH_VALLEY))
            this.placeCliffPillar(blockColumn, m, n, o, chunk, registryEntry.matchesKey(ModBiomes.GREENISH_VALLEY));
        if (registryEntry.matchesKey(ModBiomes.THE_DARKNESS))
            this.placeDarknessCliffPillar(blockColumn, m, n, o, chunk);
    }

    @Unique
    private void placeCliffPillar(BlockColumn column, int x, int z, int surfaceY, HeightLimitView chunk, boolean isGreenish) {
        double e = Math.min(Math.abs(cliffSurfaceNoise.sample(x, 0.0, z) * 8.5), cliffPillarRoofNoise.sample((double) x * 0.2, 0.0, (double) z * 0.2) * 12.0);
        if (e > -10.0) {
            double h = Math.abs(cliffPillarNoise.sample((double) x * 0.9, 0.0, (double) z * 0.8) * 2.05);
            double i = 32.0 + Math.min(e * e * 6.75, Math.ceil(h * 30.0) + 48.0);
            int j = MathHelper.floor(i);
            if (surfaceY <= j) {
                int k;
                for (k = j; k >= chunk.getBottomY(); --k) {
                    BlockState blockState = column.getState(k);
                    if (blockState.isOf(this.defaultState.getBlock()))
                        break;
                }
                if (isGreenish) {
                    column.setState(j, this.defaultState);
                    for (k = j - 1; k >= chunk.getBottomY() && (column.getState(k).isAir() || column.getState(k).isOf(ModBlocks.DARK_WATER)); --k)
                        column.setState(k, k % 3 == 0 ? Blocks.DIRT.getDefaultState() : Blocks.COARSE_DIRT.getDefaultState());
                } else
                    for (k = j; k >= chunk.getBottomY() && (column.getState(k).isAir() || column.getState(k).isOf(ModBlocks.DARK_WATER)); --k)
                        column.setState(k, this.defaultState);
            }
        }
    }

    @Unique
    private void placeDarknessCliffPillar(BlockColumn column, int x, int z, int surfaceY, HeightLimitView chunk) {
        double e = Math.min(Math.abs(cliffSurfaceNoise.sample(x, 0.0, z) * 8.5), cliffPillarRoofNoise.sample((double) x * 0.2, 0.0, (double) z * 0.2) * 10.0);
        if (e > -10.0) {
            double h = Math.abs(darknessPillarNoise.sample((double) x * 0.9, 0.0, (double) z * 0.8) * 2.05);
            double i = 32.0 + Math.min(e * e * 6.75, Math.ceil(h * 30.0) + 48.0);
            int j = MathHelper.floor(i);
            if (surfaceY <= j) {
                int k;
                for (k = j; k >= chunk.getBottomY(); --k) {
                    BlockState blockState = column.getState(k);
                    if (blockState.isOf(this.defaultState.getBlock()))
                        break;
                }
                for (k = j; k >= chunk.getBottomY() && (column.getState(k).isAir() || column.getState(k).isOf(ModBlocks.DARK_WATER)); --k)
                    column.setState(k, this.defaultState);
            }
        }
    }
}