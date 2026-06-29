package net.sashakyotoz.common.world.biomes;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.NoiseRouterData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DarknessMultiNoiseBiomeSource extends BiomeSource {
    private static final MapCodec<Holder<Biome>> BIOME_CODEC = Biome.CODEC.fieldOf("biome");
    public static final MapCodec<Climate.ParameterList<Holder<Biome>>> CUSTOM_CODEC = Climate.ParameterList.codec(BIOME_CODEC).fieldOf("biomes");
    private static final MapCodec<Holder<MultiNoiseBiomeSourceParameterList>> PRESET_CODEC = MultiNoiseBiomeSourceParameterList.CODEC.fieldOf("preset").withLifecycle(Lifecycle.stable());
    public static final Codec<DarknessMultiNoiseBiomeSource> CODEC = Codec.mapEither(CUSTOM_CODEC, PRESET_CODEC).xmap(DarknessMultiNoiseBiomeSource::new, source -> source.biomeEntries).codec();
    private final Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> biomeEntries;

    private DarknessMultiNoiseBiomeSource(Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> biomeEntries) {
        this.biomeEntries = biomeEntries;
    }

    public static DarknessMultiNoiseBiomeSource create(Climate.ParameterList<Holder<Biome>> biomeEntries) {
        return new DarknessMultiNoiseBiomeSource(Either.left(biomeEntries));
    }

    public static DarknessMultiNoiseBiomeSource create(Holder<MultiNoiseBiomeSourceParameterList> biomeEntries) {
        return new DarknessMultiNoiseBiomeSource(Either.right(biomeEntries));
    }

    private Climate.ParameterList<Holder<Biome>> getBiomeEntries() {
        return this.biomeEntries.map(entries -> entries, parameterListEntry -> parameterListEntry.value().parameters());
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return this.getBiomeEntries().values().stream().map(Pair::getSecond);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler noise) {
        Holder<Biome> candidate = this.getBiomeAtPoint(noise.sample(x, y, z));
        int y1 = QuartPos.toBlock(y);
        if (y1 > 72 && candidate.is(ModBiomes.GREYNISH_SHORE))
            return registryEntryFromKey(ModBiomes.TEALIVY_VALLEY, candidate);
        if (y1 > 220)
            return this.getBiomeAtPoint(noise.sample(x, QuartPos.fromBlock(96), z));
        if (y1 < 32) {
            if (y1 < -40)
                return registryEntryFromKey(ModBiomes.DEEP_GLACIEMITE_CAVES, candidate);
            Climate.TargetPoint sampled = noise.sample(x, y1, z);
            float temperature = Climate.unquantizeCoord(sampled.temperature());
            if (temperature > 0.0F) {
                return registryEntryFromKey(ModBiomes.TANZANITE_CAVES, candidate);
            } else
                return registryEntryFromKey(ModBiomes.SHINY_CAVERNS, candidate);
        }
        return candidate;
    }

    private Holder<Biome> registryEntryFromKey(ResourceKey<Biome> key, Holder<Biome> fallback) {
        return Optional.of(this.getBiomeEntries().values().stream().filter(noiseHypercubeRegistryEntryPair -> noiseHypercubeRegistryEntryPair.getSecond().is(key)).findFirst().get().getSecond()).orElse(fallback);
    }

    @VisibleForDebug
    public Holder<Biome> getBiomeAtPoint(Climate.TargetPoint point) {
        return this.getBiomeEntries().findValue(point);
    }

    @Override
    public void addDebugInfo(List<String> info, BlockPos pos, Climate.Sampler noiseSampler) {
        int i = QuartPos.fromBlock(pos.getX());
        int j = QuartPos.fromBlock(pos.getY());
        int k = QuartPos.fromBlock(pos.getZ());
        Climate.TargetPoint noiseValuePoint = noiseSampler.sample(i, j, k);
        float f = Climate.unquantizeCoord(noiseValuePoint.continentalness());
        float g = Climate.unquantizeCoord(noiseValuePoint.erosion());
        float h = Climate.unquantizeCoord(noiseValuePoint.temperature());
        float l = Climate.unquantizeCoord(noiseValuePoint.humidity());
        float m = Climate.unquantizeCoord(noiseValuePoint.weirdness());
        double d = NoiseRouterData.peaksAndValleys(m);
        OverworldBiomeBuilder vanillaBiomeParameters = new OverworldBiomeBuilder();
        info.add("Biome builder PV: %s C: %s E: %s T: %s H: %s".formatted(OverworldBiomeBuilder.getDebugStringForPeaksAndValleys(d), vanillaBiomeParameters.getDebugStringForContinentalness(f), vanillaBiomeParameters.getDebugStringForErosion(g), vanillaBiomeParameters.getDebugStringForTemperature(h), vanillaBiomeParameters.getDebugStringForHumidity(l)));
    }
}