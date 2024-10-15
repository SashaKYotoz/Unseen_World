package net.sashakyotoz.common.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.sashakyotoz.common.entities.GleamcarverEntity;
import net.sashakyotoz.common.entities.HarmonyWatcherEntity;
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

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.DARK_CURRANTSLATE_BOULDER);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON),
                GenerationStep.Feature.RAW_GENERATION, ModPlacements.GLACIEMITE_BOULDER);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_BURLYWOOD_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.BURLYWOOD_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_AMETHYST_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.SMALL_AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_CRIMSONVEIL_TREE),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.CRIMSONVEIL_TREE);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CRIMSONVEIL_WOODS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TALL_CRIMSONVEIL_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.GRIZZLY_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TALL_GRIZZLY_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET,ModBiomes.GRIZZLY_HIGHLANDS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.BEARFRUIT_BRAMBLE_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TEALIVY_VALLEY),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TEALIVE_TREE);

        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_GLEAMCARVER),
                SpawnGroup.AMBIENT,
                ModEntities.GLEAMCARVER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_HARMONY_WATCHER),
                SpawnGroup.AMBIENT,
                ModEntities.HARMONY_WATCHER,
                5, 1, 1);
//        SpawnRestriction.register(ModEntities.GLEAMCARVER, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GleamcarverEntity::canGleamcarverSpawn);
        SpawnRestriction.register(ModEntities.HARMONY_WATCHER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HarmonyWatcherEntity::canWatcherSpawn);
    }
}