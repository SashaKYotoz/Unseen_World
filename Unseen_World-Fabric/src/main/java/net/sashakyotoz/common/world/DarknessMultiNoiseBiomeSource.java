package net.sashakyotoz.common.world;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DarknessMultiNoiseBiomeSource extends BiomeSource {
    private static final MapCodec<RegistryEntry<Biome>> BIOME_CODEC = Biome.REGISTRY_CODEC.fieldOf("biome");
    public static final MapCodec<MultiNoiseUtil.Entries<RegistryEntry<Biome>>> CUSTOM_CODEC = MultiNoiseUtil.Entries.createCodec(BIOME_CODEC).fieldOf("biomes");
    private static final MapCodec<RegistryEntry<MultiNoiseBiomeSourceParameterList>> PRESET_CODEC = MultiNoiseBiomeSourceParameterList.REGISTRY_CODEC.fieldOf("preset").withLifecycle(Lifecycle.stable());
    public static final Codec<DarknessMultiNoiseBiomeSource> CODEC = Codec.mapEither(CUSTOM_CODEC, PRESET_CODEC).xmap(DarknessMultiNoiseBiomeSource::new, source -> source.biomeEntries).codec();
    private final Either<MultiNoiseUtil.Entries<RegistryEntry<Biome>>, RegistryEntry<MultiNoiseBiomeSourceParameterList>> biomeEntries;

    private DarknessMultiNoiseBiomeSource(Either<MultiNoiseUtil.Entries<RegistryEntry<Biome>>, RegistryEntry<MultiNoiseBiomeSourceParameterList>> biomeEntries) {
        this.biomeEntries = biomeEntries;
    }

    public static DarknessMultiNoiseBiomeSource create(MultiNoiseUtil.Entries<RegistryEntry<Biome>> biomeEntries) {
        return new DarknessMultiNoiseBiomeSource(Either.left(biomeEntries));
    }

    public static DarknessMultiNoiseBiomeSource create(RegistryEntry<MultiNoiseBiomeSourceParameterList> biomeEntries) {
        return new DarknessMultiNoiseBiomeSource(Either.right(biomeEntries));
    }

    private MultiNoiseUtil.Entries<RegistryEntry<Biome>> getBiomeEntries() {
        return this.biomeEntries.map(entries -> entries, parameterListEntry -> parameterListEntry.value().getEntries());
    }

    @Override
    protected Stream<RegistryEntry<Biome>> biomeStream() {
        return this.getBiomeEntries().getEntries().stream().map(Pair::getSecond);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        RegistryEntry<Biome> candidate = this.getBiomeAtPoint(noise.sample(x, y, z));
        int y1 = BiomeCoords.toBlock(y);
        if (y1 < 32) {
            if (y1 < -48)
                return registryEntryFromKey(ModBiomes.DEEP_GLACIEMITE_CAVES, candidate);
            MultiNoiseUtil.NoiseValuePoint sampled = noise.sample(x, y1, z);
            float temperature = MultiNoiseUtil.toFloat(sampled.temperatureNoise());
            if (temperature > 0.0F) {
                return registryEntryFromKey(ModBiomes.TANZANITE_CAVES, candidate);
            } else
                return registryEntryFromKey(ModBiomes.SHINY_CAVERNS, candidate);
        }
        return candidate;
    }
    private RegistryEntry<Biome> registryEntryFromKey(RegistryKey<Biome> key, RegistryEntry<Biome> fallback) {
        return Optional.of(this.getBiomeEntries().getEntries().stream().filter(noiseHypercubeRegistryEntryPair -> noiseHypercubeRegistryEntryPair.getSecond().matchesKey(key)).findFirst().get().getSecond()).orElse(fallback);
    }

    @Debug
    public RegistryEntry<Biome> getBiomeAtPoint(MultiNoiseUtil.NoiseValuePoint point) {
        return this.getBiomeEntries().get(point);
    }

    @Override
    public void addDebugInfo(List<String> info, BlockPos pos, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        int i = BiomeCoords.fromBlock(pos.getX());
        int j = BiomeCoords.fromBlock(pos.getY());
        int k = BiomeCoords.fromBlock(pos.getZ());
        MultiNoiseUtil.NoiseValuePoint noiseValuePoint = noiseSampler.sample(i, j, k);
        float f = MultiNoiseUtil.toFloat(noiseValuePoint.continentalnessNoise());
        float g = MultiNoiseUtil.toFloat(noiseValuePoint.erosionNoise());
        float h = MultiNoiseUtil.toFloat(noiseValuePoint.temperatureNoise());
        float l = MultiNoiseUtil.toFloat(noiseValuePoint.humidityNoise());
        float m = MultiNoiseUtil.toFloat(noiseValuePoint.weirdnessNoise());
        double d = DensityFunctions.getPeaksValleysNoise(m);
        VanillaBiomeParameters vanillaBiomeParameters = new VanillaBiomeParameters();
        info.add("Biome builder PV: " + VanillaBiomeParameters.getPeaksValleysDescription(d) + " C: " + vanillaBiomeParameters.getContinentalnessDescription(f) + " E: " + vanillaBiomeParameters.getErosionDescription(g) + " T: " + vanillaBiomeParameters.getTemperatureDescription(h) + " H: " + vanillaBiomeParameters.getHumidityDescription(l));
    }
}