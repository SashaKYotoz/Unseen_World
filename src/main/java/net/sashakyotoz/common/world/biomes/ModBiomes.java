package net.sashakyotoz.common.world.biomes;

import net.minecraft.client.sound.MusicType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.sashakyotoz.UnseenWorld;

public class ModBiomes {
    public static final RegistryKey<Biome> THE_DARKNESS = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("the_darkness"));
    public static final RegistryKey<Biome> AMETHYST_FOREST = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("amethyst_forest"));
    public static final RegistryKey<Biome> AMETHYST_CHIMERIES = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("amethyst_chimeries"));
    public static final RegistryKey<Biome> DARK_OCEAN = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_ocean"));
    public static final RegistryKey<Biome> DARK_LIFELESS_OCEAN = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_lifeless_ocean"));
    public static final RegistryKey<Biome> GREYNISH_SHORE = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("greynish_shore"));
    public static final RegistryKey<Biome> GREENISH_VALLEY = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("greenish_valley"));
    public static final RegistryKey<Biome> GREENISH_MEADOW = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("greenish_meadow"));
<<<<<<< Updated upstream
=======
    public static final RegistryKey<Biome> BURLYWOOD_JUNGLE = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("burlywood_jungle"));
>>>>>>> Stashed changes
    public static final RegistryKey<Biome> TEALIVY_VALLEY = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("tealivy_valley"));
    public static final RegistryKey<Biome> GRIZZLY_THICKET  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("grizzly_thicket"));
    public static final RegistryKey<Biome> CRIMSONVEIL_WOODS  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("crimsonveil_woods"));
<<<<<<< Updated upstream
=======
    public static final RegistryKey<Biome> OLD_GROWTH_CRIMSONVEIL_WOODS  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("old_growth_crimsonveil_woods"));
>>>>>>> Stashed changes
    public static final RegistryKey<Biome> DARK_WASTELAND  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_wasteland"));
    public static final RegistryKey<Biome> CRIMSONVEIL_PLATEAU  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("crimsonveil_plateau"));
    public static final RegistryKey<Biome> GRIZZLY_HIGHLANDS  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("grizzly_highlands"));
    public static final RegistryKey<Biome> TEALIVY_HIGHLANDS  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("tealivy_highlands"));
    public static final RegistryKey<Biome> SHINY_CAVERNS  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("shiny_caverns"));
    public static final RegistryKey<Biome> TANZANITE_CAVES  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("tanzanite_caves"));
    public static final RegistryKey<Biome> DARK_RIVER  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("dark_river"));
<<<<<<< Updated upstream
=======
    public static final RegistryKey<Biome> LUKEWARM_DARK_RIVER  = RegistryKey.of(RegistryKeys.BIOME,
            UnseenWorld.makeID("lukewarm_dark_river"));
>>>>>>> Stashed changes

    public static void boostrap(Registerable<Biome> context) {
        context.register(AMETHYST_FOREST, amethystBiome(context));
        context.register(AMETHYST_CHIMERIES, amethystBiome(context));
        context.register(DARK_OCEAN, ocean(context));
        context.register(DARK_LIFELESS_OCEAN, ocean(context));
        context.register(DARK_RIVER, ocean(context));
<<<<<<< Updated upstream
=======
        context.register(LUKEWARM_DARK_RIVER, ocean(context));
>>>>>>> Stashed changes
        context.register(GREYNISH_SHORE, ocean(context));
        context.register(GRIZZLY_THICKET, theDarkness(context));
        context.register(GRIZZLY_HIGHLANDS, theDarkness(context));
        context.register(CRIMSONVEIL_WOODS, theDarkness(context));
<<<<<<< Updated upstream
=======
        context.register(OLD_GROWTH_CRIMSONVEIL_WOODS, theDarkness(context));
>>>>>>> Stashed changes
        context.register(THE_DARKNESS, theDarkness(context));
        context.register(CRIMSONVEIL_PLATEAU, theDarkness(context));
        context.register(DARK_WASTELAND, theDarkness(context));
        context.register(TEALIVY_VALLEY, tealivyBiome(context));
        context.register(TEALIVY_HIGHLANDS, tealivyBiome(context));
        context.register(GREENISH_VALLEY,greenishBiome(context));
        context.register(GREENISH_MEADOW,greenishBiome(context));
<<<<<<< Updated upstream
=======
        context.register(BURLYWOOD_JUNGLE,greenishBiome(context));
>>>>>>> Stashed changes
        context.register(SHINY_CAVERNS,cavernBiome(context));
        context.register(TANZANITE_CAVES,cavernBiome(context));
    }

    public static Biome amethystBiome(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
//        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntities.PORCUPINE, 2, 3, 5));
//
//        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 5, 4, 4));

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(13061821)
                        .waterFogColor(13061821)
<<<<<<< Updated upstream
                        .skyColor(13061821)
                        .grassColor(13061821)
                        .foliageColor(13061821)
                        .moodSound(BiomeMoodSound.CAVE)
                        .fogColor(13061821)
=======
                        .skyColor(4605011)
                        .grassColor(13061821)
                        .foliageColor(13061821)
                        .moodSound(BiomeMoodSound.CAVE)
                        .fogColor(4605011)
>>>>>>> Stashed changes
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST.value()))).build())
                .build();
    }
    public static Biome ocean(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));
        return new Biome.Builder()
                .precipitation(false)
                .downfall(0f)
                .temperature(0.25f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
<<<<<<< Updated upstream
                        .skyColor(0x000)
                        .grassColor(0x000)
                        .foliageColor(0x000)
                        .fogColor(0x000)
=======
                        .skyColor(4605011)
                        .grassColor(0x000)
                        .foliageColor(0x000)
                        .fogColor(4605011)
>>>>>>> Stashed changes
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
                .temperature(0)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
<<<<<<< Updated upstream
                        .skyColor(0x000)
                        .grassColor(0x000)
                        .foliageColor(0x000)
                        .fogColor(0x000)
=======
                        .skyColor(4605011)
                        .grassColor(0x000)
                        .foliageColor(0x000)
                        .fogColor(4605011)
>>>>>>> Stashed changes
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_END.value()))).build())
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
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(5013401)
                        .waterFogColor(5013401)
<<<<<<< Updated upstream
                        .skyColor(5013401)
                        .grassColor(5013401)
                        .foliageColor(5013401)
                        .fogColor(5013401)
=======
                        .skyColor(4605011)
                        .grassColor(5013401)
                        .foliageColor(5013401)
                        .fogColor(4605011)
>>>>>>> Stashed changes
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_MENU.value()))).build())
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
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(6717235)
                        .waterFogColor(6717235)
<<<<<<< Updated upstream
                        .skyColor(6717235)
                        .grassColor(6717235)
                        .foliageColor(6717235)
                        .fogColor(6717235)
=======
                        .skyColor(4605011)
                        .grassColor(6717235)
                        .foliageColor(6717235)
                        .fogColor(4605011)
>>>>>>> Stashed changes
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_MENU.value()))).build())
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
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(6579300)
                        .waterFogColor(6579300)
<<<<<<< Updated upstream
                        .skyColor(6579300)
                        .grassColor(6579300)
                        .foliageColor(6579300)
                        .fogColor(6579300)
=======
                        .skyColor(4605011)
                        .grassColor(6579300)
                        .foliageColor(6579300)
                        .fogColor(4605011)
>>>>>>> Stashed changes
                        .music(MusicType.createIngameMusic(RegistryEntry.of(SoundEvents.MUSIC_OVERWORLD_LUSH_CAVES.value()))).build())
                .build();
    }
}