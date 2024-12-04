package net.sashakyotoz.common.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.sashakyotoz.common.entities.custom.*;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.features.placements.ModPlacements;

public class ModWorldGeneration {
    public static void register() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ABYSSAL_SPAWNS_IN),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.ABYSSAL_ORE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.TITANIUM_SPAWNS_IN),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.RED_TITANIUM_ORE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.UNSEENIUM_SPAWNS_IN),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.UNSEENIUM_ORE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.GRASS_COVERED),
                GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_GRASS_JUNGLE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.FLOWERS_COVERED),
                GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_MEADOW);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.FLOWERS_COVERED),
                GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_PLAIN);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.WATER_LAKE_SPAWNS_ON),
                GenerationStep.Feature.LAKES, ModPlacements.WATER_LAKE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DARK_WATER_LAKE_SPAWNS_ON),
                GenerationStep.Feature.LAKES, ModPlacements.DARK_WATER_LAKE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
                        ModBiomes.CRIMSONVEIL_WOODS, ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
                GenerationStep.Feature.LAKES, ModPlacements.CRIMSONVEIL_DARK_WATER_LAKE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.DARK_CURRANTSLATE_BOULDER);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.GLACIEMITE_BOULDER);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_FOREST),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.AMETHYST_BOULDER);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_BURLYWOOD_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.BURLYWOOD_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.BURLYWOOD_JUNGLE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.SMALL_BURLYWOOD_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_BURLYWOOD_BUSH_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.SMALL_BUSH_LIKE_TREE_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_AMETHYST_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.SMALL_AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_CHIMERIES),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.AMETHYST_BUSH);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_CRIMSONVEIL_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.CRIMSONVEIL_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_AMETHYST_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TANZANITE_SPHERE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_AMETHYST_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.AMETHYST_FLOWERS);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_CHIMERIES),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.AMETHYST_FLOWERS);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.BURLYWOOD_JUNGLE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.BUSH_LIKE_TREE_PATCH);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CRIMSONVEIL_WOODS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TALL_CRIMSONVEIL_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.GRIZZLY_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TALL_GRIZZLY_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_HIGHLANDS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.BEARFRUIT_BRAMBLE_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TEALIVY_VALLEY),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TEALIVE_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.DARK_OCEAN),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.UMBRAL_KELP);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TEALIVY_VALLEY),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TEALIVY_VALLEY),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.TALL_GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.DARK_WASTELAND),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.DARK_WASTELAND),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.TALL_GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CRIMSONVEIL_PLATEAU),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CRIMSONVEIL_PLATEAU),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.TALL_GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.THE_DARKNESS),
                GenerationStep.Feature.LOCAL_MODIFICATIONS, ModPlacements.DARKNESS_SPIRAL_SPIKE);

        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_GLEAMCARVER),
                SpawnGroup.AMBIENT,
                ModEntities.GLEAMCARVER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_HARMONY_WATCHER),
                SpawnGroup.CREATURE,
                ModEntities.HARMONY_WATCHER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_SABERPARD),
                SpawnGroup.CREATURE,
                ModEntities.SABERPARD,
                5, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.DARK_LIFELESS_OCEAN),
                SpawnGroup.WATER_CREATURE,
                ModEntities.GLOOMWHALE,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON),
                SpawnGroup.MONSTER,
                ModEntities.ESPYER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.DEEP_GLACIEMITE_CAVES),
                SpawnGroup.MONSTER,
                ModEntities.ESPYER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.DEEP_GLACIEMITE_CAVES,ModBiomes.SHINY_CAVERNS,ModBiomes.THE_DARKNESS),
                SpawnGroup.AMBIENT,
                ModEntities.ELDRITCH_WATCHER,
                5, 1, 1);
//        SpawnRestriction.register(ModEntities.GLEAMCARVER, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GleamcarverEntity::canGleamcarverSpawn);
        SpawnRestriction.register(ModEntities.HARMONY_WATCHER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HarmonyWatcherEntity::canWatcherSpawn);
        SpawnRestriction.register(ModEntities.GLOOMWHALE, SpawnRestriction.Location.IN_WATER, Heightmap.Type.OCEAN_FLOOR, GloomwhaleEntity::canWhaleSpawn);
        SpawnRestriction.register(ModEntities.SABERPARD, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SaberpardEntity::isValidNaturalSpawn);
        SpawnRestriction.register(ModEntities.ESPYER, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EspyerEntity::canEspyerSpawn);
        SpawnRestriction.register(ModEntities.ELDRITCH_WATCHER, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EldritchWatcherEntity::canWatcherSpawn);
    }
}