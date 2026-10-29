package net.sashakyotoz.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.carvers.ModCarvers;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import net.sashakyotoz.common.world.features.placements.ModPlacements;
import net.sashakyotoz.common.world.noise.ModDensityFunctions;
import net.sashakyotoz.common.world.noise.ModNoiseSettings;
import net.sashakyotoz.common.world.noise.ModNoises;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenerator extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap)
            .add(Registries.PLACED_FEATURE, ModPlacements::boostrap)
            .add(Registries.BIOME, ModBiomes::boostrap)
            .add(Registries.CONFIGURED_CARVER, ModCarvers::boostrap)
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrap)
            .add(Registries.NOISE, ModNoises::bootstrap)
            .add(Registries.DENSITY_FUNCTION, ModDensityFunctions::bootstrap)
            .add(Registries.NOISE_SETTINGS, ModNoiseSettings::bootstrap);

    public ModWorldGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, BUILDER, Set.of(UnseenWorld.MOD_ID));
    }

    @Override
    public String getName() {
        return "World Gen";
    }
}
