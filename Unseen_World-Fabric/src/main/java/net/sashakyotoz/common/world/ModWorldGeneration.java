package net.sashakyotoz.common.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.*;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.features.placements.ModPlacements;

public class ModWorldGeneration {
    //NaturesSpirit copyright
    //source code: https://github.com/Team-Hibiscus/NaturesSpirit/blob/dev/src/main/java/net/hibiscus/naturespirit/registration/NSWorldGen.java
    public static final ResourceKey<NormalNoise.NoiseParameters> CLIFF_PILLAR = ResourceKey.create(Registries.NOISE, UnseenWorld.makeID("cliff_pillar"));
    public static final ResourceKey<NormalNoise.NoiseParameters> CLIFF_PILLAR_ROOF = ResourceKey.create(Registries.NOISE,
            UnseenWorld.makeID("cliff_pillar_roof"));
    public static final ResourceKey<NormalNoise.NoiseParameters> CLIFF_SURFACE = ResourceKey.create(Registries.NOISE, UnseenWorld.makeID("cliff_surface"));
    //
    public static final ResourceKey<NormalNoise.NoiseParameters> DARKNESS_CLIFF_PILLAR = ResourceKey.create(Registries.NOISE, UnseenWorld.makeID("darkness_cliff_pillar"));

    public static void register() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.ABYSSAL_SPAWNS_IN),
                GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.ABYSSAL_ORE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.TITANIUM_SPAWNS_IN),
                GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.RED_TITANIUM_ORE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.UNSEENIUM_SPAWNS_IN),
                GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.UNSEENIUM_ORE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.FLOWERS_COVERED),
                GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_MEADOW);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.FLOWERS_COVERED),
                GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.WATER_LAKE_SPAWNS_ON),
                GenerationStep.Decoration.LAKES, ModPlacements.WATER_LAKE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.DARK_WATER_LAKE_SPAWNS_ON),
                GenerationStep.Decoration.LAKES, ModPlacements.DARK_WATER_LAKE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(
                        ModBiomes.CRIMSONVEIL_WOODS, ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
                GenerationStep.Decoration.LAKES, ModPlacements.CRIMSONVEIL_DARK_WATER_LAKE);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON),
                GenerationStep.Decoration.RAW_GENERATION, ModPlacements.DARK_CURRANTSLATE_BOULDER);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON),
                GenerationStep.Decoration.RAW_GENERATION, ModPlacements.GLACIEMITE_BOULDER);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_FOREST),
                GenerationStep.Decoration.RAW_GENERATION, ModPlacements.AMETHYST_BOULDER);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_BURLYWOOD_TREE),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.BURLYWOOD_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.BURLYWOOD_JUNGLE),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.SMALL_BURLYWOOD_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_AMETHYST_TREE),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.SMALL_AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_CRIMSONVEIL_TREE),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.CRIMSONVEIL_TREE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_AMETHYST_TREE),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.TANZANITE_SPHERE);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.HAS_AMETHYST_TREE),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.AMETHYST_FLOWERS);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_CHIMERIES),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.AMETHYST_FLOWERS);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CRIMSONVEIL_WOODS),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.TALL_CRIMSONVEIL_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.AMETHYST_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.AMETHYST_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_HIGHLANDS),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.BEARFRUIT_BRAMBLE_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.BURLYWOOD_JUNGLE),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.BUSH_BURLYWOOD_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TEALIVY_VALLEY),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.TEALIVE_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.DARK_OCEAN),
                GenerationStep.Decoration.RAW_GENERATION, ModPlacements.UMBRAL_KELP);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TEALIVY_VALLEY, ModBiomes.GRIZZLY_THICKET, ModBiomes.DARK_WASTELAND),
                GenerationStep.Decoration.RAW_GENERATION, ModPlacements.GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TEALIVY_VALLEY, ModBiomes.DARK_WASTELAND, ModBiomes.CRIMSONVEIL_PLATEAU),
                GenerationStep.Decoration.RAW_GENERATION, ModPlacements.TALL_GLOOMWEED_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.THE_DARKNESS),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS, ModPlacements.DARKNESS_SPIRAL_SPIKE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.CURRANTSLATE_PEAKS),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS, ModPlacements.GRIPTONITE_CLUSTER_SPIKE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TANZANITE_CAVES),
                GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.AMETHYST_CEILING_BOULDER);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TANZANITE_CAVES, ModBiomes.SHINY_CAVERNS),
                GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.UNSEENIUM_CURRANTSLATE_DISK);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.TANZANITE_CAVES, ModBiomes.SHINY_CAVERNS),
                GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.TITANIUM_CURRANTSLATE_DISK);

        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON),
                GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR);

        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_GLEAMCARVER),
                MobCategory.AMBIENT,
                ModEntities.GLEAMCARVER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_HARMONY_WATCHER),
                MobCategory.CREATURE,
                ModEntities.HARMONY_WATCHER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_SABERPARD),
                MobCategory.AMBIENT,
                ModEntities.SABERPARD,
                4, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.HAS_TUSKHOG),
                MobCategory.AMBIENT,
                ModEntities.TUSKHOG,
                6, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.DARK_OCEAN),
                MobCategory.WATER_CREATURE,
                ModEntities.GLOOMWHALE,
                5, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.DARK_LIFELESS_OCEAN),
                MobCategory.WATER_CREATURE,
                ModEntities.GRIPPING_GLOOMWHALE,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON),
                MobCategory.MONSTER,
                ModEntities.ESPYER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.DEEP_GLACIEMITE_CAVES),
                MobCategory.MONSTER,
                ModEntities.ESPYER,
                5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.DEEP_GLACIEMITE_CAVES, ModBiomes.SHINY_CAVERNS, ModBiomes.THE_DARKNESS),
                MobCategory.AMBIENT,
                ModEntities.ELDRITCH_WATCHER,
                5, 1, 1);
        SpawnPlacements.register(ModEntities.HARMONY_WATCHER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HarmonyWatcherEntity::canWatcherSpawn);
        SpawnPlacements.register(ModEntities.GLOOMWHALE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, WhaleEntity::canWhaleSpawn);
        SpawnPlacements.register(ModEntities.GRIPPING_GLOOMWHALE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, WhaleEntity::canWhaleSpawn);
        SpawnPlacements.register(ModEntities.SHIMMER, SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, ShimmerEntity::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(ModEntities.SABERPARD, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SaberpardEntity::checkAnimalSpawnRules);
        SpawnPlacements.register(ModEntities.TUSKHOG, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TuskhogEntity::checkAnimalSpawnRules);
        SpawnPlacements.register(ModEntities.ESPYER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EspyerEntity::canEspyerSpawn);
        SpawnPlacements.register(ModEntities.ELDRITCH_WATCHER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EldritchWatcherEntity::canWatcherSpawn);
    }
}