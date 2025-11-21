package net.sashakyotoz.common.world.biomes;

import net.minecraft.client.sound.MusicType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.world.carvers.ModCarvers;
import net.sashakyotoz.common.world.features.placements.ModPlacements;

public class ModBiomes {
    public static final RegistryKey<Biome> THE_DARKNESS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("the_darkness"));

    public static final RegistryKey<Biome> AMETHYST_FOREST = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("amethyst_forest"));
    public static final RegistryKey<Biome> GRIZZLY_THICKET = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("grizzly_thicket"));
    public static final RegistryKey<Biome> BURLYWOOD_JUNGLE = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("burlywood_jungle"));
    public static final RegistryKey<Biome> CRIMSONVEIL_WOODS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("crimsonveil_woods"));
    public static final RegistryKey<Biome> OLD_GROWTH_CRIMSONVEIL_WOODS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("old_growth_crimsonveil_woods"));

    public static final RegistryKey<Biome> AMETHYST_CHIMERIES = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("amethyst_chimeries"));
    public static final RegistryKey<Biome> GREENISH_VALLEY = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("greenish_valley"));
    public static final RegistryKey<Biome> GREENISH_MEADOW = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("greenish_meadow"));
    public static final RegistryKey<Biome> TEALIVY_VALLEY = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("tealivy_valley"));
    public static final RegistryKey<Biome> DARK_WASTELAND = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_wasteland"));
    public static final RegistryKey<Biome> CRIMSONVEIL_PLATEAU = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("crimsonveil_plateau"));
    public static final RegistryKey<Biome> HARMONY_GROVE = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("harmony_grove"));

    public static final RegistryKey<Biome> GRIZZLY_HIGHLANDS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("grizzly_highlands"));
    public static final RegistryKey<Biome> TEALIVY_HIGHLANDS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("tealivy_highlands"));
    public static final RegistryKey<Biome> CURRANTSLATE_PEAKS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("currantslate_peaks"));

    public static final RegistryKey<Biome> SHINY_CAVERNS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("shiny_caverns"));
    public static final RegistryKey<Biome> TANZANITE_CAVES = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("tanzanite_caves"));
    public static final RegistryKey<Biome> DEEP_GLACIEMITE_CAVES = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("deep_glaciemite_caves"));

    public static final RegistryKey<Biome> DARK_RIVER = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_river"));
    public static final RegistryKey<Biome> LUKEWARM_DARK_RIVER = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("lukewarm_dark_river"));
    public static final RegistryKey<Biome> DARK_OCEAN = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_ocean"));
    public static final RegistryKey<Biome> DARK_LIFELESS_OCEAN = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_lifeless_ocean"));

    public static final RegistryKey<Biome> GREYNISH_SHORE = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("greynish_shore"));

    public static final RegistryKey<Biome> DARK_BADLANDS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_badlands"));

    public static void boostrap(Registerable<Biome> context) {
        context.register(AMETHYST_FOREST, amethystBiome(context));
        context.register(AMETHYST_CHIMERIES, amethystBiome(context));
        context.register(DARK_OCEAN, ocean(context));
        context.register(DARK_LIFELESS_OCEAN, ocean(context));
        context.register(DARK_RIVER, ocean(context));
        context.register(LUKEWARM_DARK_RIVER, ocean(context));
        context.register(GREYNISH_SHORE, ocean(context));
        context.register(GRIZZLY_THICKET, grizzlyForestBiome(context));
        context.register(CURRANTSLATE_PEAKS, darkBiomes(context));
        context.register(GRIZZLY_HIGHLANDS, grizzlyLikeBiome(context));
        context.register(CRIMSONVEIL_WOODS, semiHotBiome(context));
        context.register(OLD_GROWTH_CRIMSONVEIL_WOODS, semiHotBiome(context));
        context.register(THE_DARKNESS, theDarkness(context));
        context.register(DARK_BADLANDS, darkBiomes(context));
        context.register(CRIMSONVEIL_PLATEAU, semiHotBiome(context));
        context.register(DARK_WASTELAND, darkBiomes(context));
        context.register(TEALIVY_VALLEY, tealivyBiome(context));
        context.register(TEALIVY_HIGHLANDS, tealivyBiome(context));
        context.register(GREENISH_VALLEY, greenishBiome(context));
        context.register(GREENISH_MEADOW, greenishBiome(context));
        context.register(BURLYWOOD_JUNGLE, greenishBiome(context));
        context.register(HARMONY_GROVE, mixedForestBiome(context));
        context.register(SHINY_CAVERNS, cavernBiome(context));
        context.register(TANZANITE_CAVES, cavernBiome(context));
        context.register(DEEP_GLACIEMITE_CAVES, cavernBiome(context));
    }

    public static Biome amethystBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ModPlacements.AMETHYST_CEILING_BOULDER)
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.AMETHYST_BUSH)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(13061821)
                        .waterFogColor(13061821)
                        .skyColor(4605011)
                        .grassColor(13061821)
                        .foliageColor(13061821)
                        .moodSound(BiomeMoodSound.CAVE)
                        .fogColor(4605011)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST.value()))).build())
                .build();
    }

    public static Biome grizzlyLikeBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.75f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(7340544)
                        .waterFogColor(7340544)
                        .skyColor(4605011)
                        .grassColor(7340544)
                        .foliageColor(7340544)
                        .moodSound(BiomeMoodSound.CAVE)
                        .fogColor(4605011)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST.value()))).build())
                .build();
    }
    public static Biome grizzlyForestBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.75f)
                .generationSettings(
                        biomeBuilder
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.GRIZZLY_TREE)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.TALL_GRIZZLY_TREE)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(7340544)
                        .waterFogColor(7340544)
                        .skyColor(4605011)
                        .grassColor(7340544)
                        .foliageColor(7340544)
                        .moodSound(BiomeMoodSound.CAVE)
                        .fogColor(4605011)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST.value()))).build())
                .build();
    }

    public static Biome ocean(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        biomeBuilder.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, ModPlacements.MIDNIGHT_LILY_PATCH);
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.25f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColor(0x000)
                        .foliageColor(0x000)
                        .fogColor(4605011)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_UNDER_WATER.value()))).build())
                .build();
    }

    public static Biome theDarkness(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.16f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.ANCIENT_DEBRIS)
                        .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .particleConfig(new BiomeParticleConfig(ParticleTypes.ASH, 0.0125f))
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColor(0x000)
                        .foliageColor(0x000)
                        .fogColor(4605011)
                        .moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 4.0))
                        .music(MusicType.createIngameMusic(RegistryEntry.of(ModSoundEvents.THE_DARKNESS_AMBIENT))).build())
                .build();
    }

    public static Biome darkBiomes(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.2f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.ANCIENT_DEBRIS)
                        .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColor(0x000)
                        .foliageColor(0x000)
                        .fogColor(4605011)
                        .moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0))
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_NETHER_BASALT_DELTAS.value()))).build())
                .build();
    }

    public static Biome semiHotBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.GLOOMWEED_PATCH)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColor(336860160)
                        .foliageColor(336860160)
                        .fogColor(4605011)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST.value()))).build())
                .build();
    }

    public static Biome tealivyBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(5013401)
                        .waterFogColor(5013401)
                        .skyColor(4605011)
                        .grassColor(5013401)
                        .foliageColor(5013401)
                        .fogColor(4605011)
                        .moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0))
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_CREDITS.value()))).build())
                .build();
    }

    public static Biome greenishBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.75f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ModPlacements.BUSH_LIKE_TREE_PATCH)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.BURLYWOOD_VIOLET_PATCH)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacements.RARE_GLOOMWEED_PATCH)
                        .feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_GRASS_JUNGLE)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(6717235)
                        .waterFogColor(6717235)
                        .skyColor(4605011)
                        .grassColor(6717235)
                        .foliageColor(6717235)
                        .fogColor(4605011)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_GAME.value()))).build())
                .build();
    }

    public static Biome cavernBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.ANCIENT_DEBRIS)
                        .feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ModPlacements.GRIPPING_GLACIEMITE_PATCH_CEILING)
                        .carver(GenerationStep.Carver.AIR, ModCarvers.SPIRAL_CAVE)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(6579300)
                        .waterFogColor(6579300)
                        .skyColor(4605011)
                        .grassColor(6579300)
                        .foliageColor(6579300)
                        .fogColor(4605011)
                        .moodSound(BiomeMoodSound.CAVE)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_OVERWORLD_LUSH_CAVES.value()))).build())
                .build();
    }

    public static Biome mixedForestBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0f)
                .generationSettings(biomeBuilder
                        .feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(6717235)
                        .waterFogColor(6579300)
                        .skyColor(4605011)
                        .grassColor(5013401)
                        .foliageColor(6579300)
                        .fogColor(4605011)
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_OVERWORLD_FOREST.value()))).build())
                .build();
    }
}