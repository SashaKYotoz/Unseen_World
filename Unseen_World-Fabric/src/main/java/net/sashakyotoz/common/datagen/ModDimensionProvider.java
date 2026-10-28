package net.sashakyotoz.common.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.biomes.ChimericDarknessBiomeParameters;
import net.sashakyotoz.common.world.noise.ModNoiseSettings;

import java.util.concurrent.CompletableFuture;

public class ModDimensionProvider implements DataProvider {

    private final PackOutput.PathProvider paths;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public ModDimensionProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.paths = output.createPathProvider(PackOutput.Target.DATA_PACK, "dimension");
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(provider -> {
            RegistryOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            Holder<DimensionType> type = provider.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(ModDimensions.CHIMERIC_DARKNESS_TYPE);
            Holder<NoiseGeneratorSettings> settings = provider.lookupOrThrow(Registries.NOISE_SETTINGS).getOrThrow(ModNoiseSettings.CHIMERIC_DARKNESS);
            MultiNoiseBiomeSource biomeSource = MultiNoiseBiomeSource.createFromList(ChimericDarknessBiomeParameters.build(provider.lookupOrThrow(Registries.BIOME)));
            LevelStem stem = new LevelStem(type, new NoiseBasedChunkGenerator(biomeSource, settings));
            JsonElement json = LevelStem.CODEC.encodeStart(ops, stem)
                    .getOrThrow(IllegalStateException::new);
            return DataProvider.saveStable(output, json, this.paths.json(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY.location()));
        });
    }

    @Override
    public String getName() {
        return "Chimeric Darkness dimension";
    }
}