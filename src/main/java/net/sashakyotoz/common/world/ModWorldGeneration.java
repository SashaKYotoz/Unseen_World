package net.sashakyotoz.common.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.UndergroundPlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.sashakyotoz.common.world.biomes.ModBiomes;

public class ModWorldGeneration {
    public static void register() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GREENISH_VALLEY),
                GenerationStep.Feature.VEGETAL_DECORATION, MiscPlacedFeatures.SPRING_WATER);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GREENISH_VALLEY),
                GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_GRASS_JUNGLE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GREENISH_VALLEY),
                GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_WATERLILY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GREENISH_VALLEY),
                GenerationStep.Feature.VEGETAL_DECORATION, UndergroundPlacedFeatures.LUSH_CAVES_VEGETATION);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_CHIMERIES),
                GenerationStep.Feature.UNDERGROUND_ORES, UndergroundPlacedFeatures.AMETHYST_GEODE);
    }
}