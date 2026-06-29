package net.sashakyotoz.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.tags.ModTags;
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
@Mixin(SurfaceSystem.class)
public class SurfaceBuilderMixin {
    @Final
    @Shadow
    private BlockState defaultBlock;
    @Unique
    private NormalNoise cliffPillarNoise;
    @Unique
    private NormalNoise cliffPillarRoofNoise;
    @Unique
    private NormalNoise cliffSurfaceNoise;
    @Unique
    private NormalNoise darknessPillarNoise;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectNoise(RandomState noiseConfig, BlockState defaultState, int seaLevel, PositionalRandomFactory randomDeriver, CallbackInfo ci) {
        cliffPillarNoise = noiseConfig.getOrCreateNoise(ModWorldGeneration.CLIFF_PILLAR);
        cliffPillarRoofNoise = noiseConfig.getOrCreateNoise(ModWorldGeneration.CLIFF_PILLAR_ROOF);
        cliffSurfaceNoise = noiseConfig.getOrCreateNoise(ModWorldGeneration.CLIFF_SURFACE);
        darknessPillarNoise = noiseConfig.getOrCreateNoise(ModWorldGeneration.DARKNESS_CLIFF_PILLAR);
    }

    @Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/resources/ResourceKey;)Z", ordinal = 0))
    private void injectPillars(RandomState noiseConfig, BiomeManager biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, WorldGenerationContext heightContext,
                               ChunkAccess chunk, NoiseChunk chunkNoiseSampler, SurfaceRules.RuleSource materialRule,
                               CallbackInfo ci,
                               @Local Holder<Biome> registryEntry, @Local(ordinal = 2) int k, @Local(ordinal = 3) int l, @Local(ordinal = 4) int m, @Local(ordinal = 5) int n,
                               @Local BlockColumn blockColumn) {
        int o = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, k, l) + 1;
        if (registryEntry.is(ModTags.Biomes.HAS_MODIFIED_SURFACE))
            this.placeCliffPillar(blockColumn, m, n, o, chunk, registryEntry.is(ModBiomes.GREENISH_VALLEY));
        if (registryEntry.is(ModBiomes.THE_DARKNESS))
            this.placeDarknessCliffPillar(blockColumn, m, n, o, chunk);
    }

    @Unique
    private void placeCliffPillar(BlockColumn column, int x, int z, int surfaceY, LevelHeightAccessor chunk, boolean isGreenish) {
        double e = Math.min(Math.abs(cliffSurfaceNoise.getValue(x, 0.0, z) * 8.5), cliffPillarRoofNoise.getValue((double) x * 0.2, 0.0, (double) z * 0.2) * 12.0);
        if (e > -10.0) {
            double h = Math.abs(cliffPillarNoise.getValue((double) x * 0.9, 0.0, (double) z * 0.8) * 2.05);
            double i = 32.0 + Math.min(e * e * 6.75, Math.ceil(h * 30.0) + 48.0);
            int j = Mth.floor(i);
            if (surfaceY <= j) {
                int k;
                for (k = j; k >= chunk.getMinBuildHeight(); --k) {
                    BlockState blockState = column.getBlock(k);
                    if (blockState.is(this.defaultBlock.getBlock()))
                        break;
                }
                if (isGreenish) {
                    if (!defaultBlock.is(Blocks.GRASS_BLOCK)) {
                        column.setBlock(j, this.defaultBlock);
                        for (k = j - 1; k >= chunk.getMinBuildHeight() && (column.getBlock(k).isAir() || column.getBlock(k).is(ModBlocks.DARK_WATER)); --k)
                            column.setBlock(k, (k + j) % 2 == 0 ? Blocks.DIRT.defaultBlockState() : Blocks.COARSE_DIRT.defaultBlockState());
                    }
                } else
                    for (k = j; k >= chunk.getMinBuildHeight() && (column.getBlock(k).isAir() || column.getBlock(k).is(ModBlocks.DARK_WATER)); --k)
                        column.setBlock(k, this.defaultBlock);
            }
        }
    }

    @Unique
    private void placeDarknessCliffPillar(BlockColumn column, int x, int z, int surfaceY, LevelHeightAccessor chunk) {
        double e = Math.min(Math.abs(cliffSurfaceNoise.getValue(x, 0.0, z) * 8.5), cliffPillarRoofNoise.getValue((double) x * 0.2, 0.0, (double) z * 0.2) * 10.0);
        if (e > -10.0) {
            double h = Math.abs(darknessPillarNoise.getValue((double) x * 0.9, 0.0, (double) z * 0.8) * 2.05);
            double i = 32.0 + Math.min(e * e * 6.75, Math.ceil(h * 30.0) + 48.0);
            int j = Mth.floor(i);
            if (surfaceY <= j) {
                int k;
                for (k = j; k >= chunk.getMinBuildHeight(); --k) {
                    BlockState blockState = column.getBlock(k);
                    if (blockState.is(this.defaultBlock.getBlock()))
                        break;
                }
                for (k = j; k >= chunk.getMinBuildHeight() && (column.getBlock(k).isAir() || column.getBlock(k).is(ModBlocks.DARK_WATER)); --k)
                    column.setBlock(k, this.defaultBlock);
            }
        }
    }
}