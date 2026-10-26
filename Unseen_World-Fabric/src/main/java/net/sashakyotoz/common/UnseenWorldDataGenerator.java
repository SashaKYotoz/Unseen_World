package net.sashakyotoz.common;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.lcc.sollib.datagen.SEntityTagProvider;
import net.lcc.sollib.datagen.SItemTagProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.sashakyotoz.common.datagen.*;
import net.sashakyotoz.common.datagen.loot.ModArchaeologyLootTableProvider;
import net.sashakyotoz.common.datagen.loot.ModBlockLootTableProvider;
import net.sashakyotoz.common.datagen.loot.ModChestLootTableProvider;
import net.sashakyotoz.common.datagen.loot.OrdealLootTableProvider;
import net.sashakyotoz.common.datagen.recipes.ModRecipeProvider;
import net.sashakyotoz.common.datagen.tags.ModBiomeTagProvider;
import net.sashakyotoz.common.datagen.tags.ModBlockTagProvider;
import net.sashakyotoz.common.datagen.tags.ModFluidTagProvider;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.carvers.ModCarvers;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import net.sashakyotoz.common.world.features.placements.ModPlacements;
import net.sashakyotoz.common.world.noise.ModDensityFunctions;
import net.sashakyotoz.common.world.noise.ModNoiseSettings;
import net.sashakyotoz.common.world.noise.ModNoises;

public class UnseenWorldDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModAdvancementProvider::new);

        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModFluidTagProvider::new);
        pack.addProvider(ModBiomeTagProvider::new);
        pack.addProvider(SItemTagProvider::new);
        pack.addProvider(SEntityTagProvider::new);
        pack.addProvider(ModGameEventTagProvider::new);

        pack.addProvider(ModBlockLootTableProvider::new);
        pack.addProvider(OrdealLootTableProvider::new);
        pack.addProvider(ModChestLootTableProvider::new);
        pack.addProvider(ModArchaeologyLootTableProvider::new);

        pack.addProvider(ModModelProvider::new);

        pack.addProvider(ModWorldGenerator::new);
        pack.addProvider(ModDimensionProvider::new);

        pack.addProvider(ModLanguageProvider::new);
        pack.addProvider(ModRecipeProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacements::boostrap);
        registryBuilder.add(Registries.BIOME, ModBiomes::boostrap);
        registryBuilder.add(Registries.CONFIGURED_CARVER, ModCarvers::boostrap);
        registryBuilder.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrap);
        registryBuilder.add(Registries.NOISE, ModNoises::bootstrap);
        registryBuilder.add(Registries.DENSITY_FUNCTION, ModDensityFunctions::bootstrap);
        registryBuilder.add(Registries.NOISE_SETTINGS, ModNoiseSettings::bootstrap);
    }
}