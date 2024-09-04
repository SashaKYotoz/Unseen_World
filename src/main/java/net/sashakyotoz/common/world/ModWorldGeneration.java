package net.sashakyotoz.common.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.UndergroundPlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
<<<<<<< Updated upstream
import net.sashakyotoz.common.world.biomes.ModBiomes;
=======
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import net.sashakyotoz.common.world.features.ModPlacements;
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
=======

        //trees
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.AMETHYST_LIKE_BIOMES),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.SMALL_AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.GREENISH_LIKE_BIOMES),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.BURLYWOOD_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.TEALIVE_LIKE_BIOMES),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TEALIVE_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CRIMSONVEIL_WOODS,ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.CRIMSONVEIL_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CRIMSONVEIL_WOODS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TALL_CRIMSONVEIL_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.GRIZZLY_TREE);
>>>>>>> Stashed changes
    }
}