package net.sashakyotoz.common.world.biomes;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.world.carvers.ModCarvers;
import net.sashakyotoz.common.world.features.placements.ModPlacements;

import java.util.*;

public class ModBiomes {
    public static final ResourceKey<Biome> DEEP_GLACIEMITE_CAVES = key("deep_glaciemite_caves");
    public static final ResourceKey<Biome> TANZANITE_CAVES = key("tanzanite_caves");
    public static final ResourceKey<Biome> SHINY_CAVERNS = key("shiny_caverns");
    public static final ResourceKey<Biome> BURLYWOOD_HOLLOWS = key("burlywood_hollows");

    public static final ResourceKey<Biome> DARK_OCEAN = key("dark_ocean");
    public static final ResourceKey<Biome> DARK_LIFELESS_OCEAN = key("dark_lifeless_ocean");
    public static final ResourceKey<Biome> FRIGID_SPIRE_OCEAN = key("frigid_spire_ocean");
    public static final ResourceKey<Biome> DARK_RIVER = key("dark_river");
    public static final ResourceKey<Biome> LUKEWARM_DARK_RIVER = key("lukewarm_dark_river");
    public static final ResourceKey<Biome> GREYNISH_SHORE = key("greynish_shore");

    public static final ResourceKey<Biome> DARK_WASTELAND = key("dark_wasteland");
    public static final ResourceKey<Biome> GLOOMWEED_PLAINS = key("gloomweed_plains");
    public static final ResourceKey<Biome> ASHEN_MARSH = key("ashen_marsh");
    public static final ResourceKey<Biome> CRIMSONVEIL_MIRE = key("crimsonveil_mire");
    public static final ResourceKey<Biome> GLACIEMITE_TUNDRA = key("glaciemite_tundra");

    public static final ResourceKey<Biome> GREENISH_VALLEY = key("greenish_valley");
    public static final ResourceKey<Biome> TEALIVY_VALLEY = key("tealivy_valley");

    public static final ResourceKey<Biome> AMETHYST_FOREST = key("amethyst_forest");
    public static final ResourceKey<Biome> GRIZZLY_THICKET = key("grizzly_thicket");
    public static final ResourceKey<Biome> SNOWY_GRIZZLY_THICKET = key("snowy_grizzly_thicket");
    public static final ResourceKey<Biome> CRIMSONVEIL_WOODS = key("crimsonveil_woods");
    public static final ResourceKey<Biome> OLD_GROWTH_CRIMSONVEIL_WOODS = key("old_growth_crimsonveil_woods");
    public static final ResourceKey<Biome> BURLYWOOD_JUNGLE = key("burlywood_jungle");
    public static final ResourceKey<Biome> AMETHYST_CHIMERIES = key("amethyst_chimeries");
    public static final ResourceKey<Biome> CRIMSONVEIL_PLATEAU = key("crimsonveil_plateau");

    public static final ResourceKey<Biome> GRIZZLY_HIGHLANDS = key("grizzly_highlands");
    public static final ResourceKey<Biome> TEALIVY_HIGHLANDS = key("tealivy_highlands");
    public static final ResourceKey<Biome> CURRANTSLATE_PEAKS = key("currantslate_peaks");
    public static final ResourceKey<Biome> GLACIEMITE_PEAKS = key("glaciemite_peaks");

    public static final ResourceKey<Biome> THE_DARKNESS = key("the_darkness");
    public static final ResourceKey<Biome> PENUMBRAL_STEPPE = key("penumbral_steppe");
    public static final ResourceKey<Biome> AURIC_HEIGHTS = key("auric_heights");

    public static final List<ResourceKey<Biome>> CAVES = List.of(DEEP_GLACIEMITE_CAVES, TANZANITE_CAVES, SHINY_CAVERNS, BURLYWOOD_HOLLOWS);
    public static final List<ResourceKey<Biome>> OCEANS = List.of(DARK_OCEAN, DARK_LIFELESS_OCEAN, FRIGID_SPIRE_OCEAN);
    public static final List<ResourceKey<Biome>> RIVERS = List.of(DARK_RIVER, LUKEWARM_DARK_RIVER);
    public static final List<ResourceKey<Biome>> LOWLANDS = List.of(GREYNISH_SHORE, DARK_WASTELAND, GLOOMWEED_PLAINS, ASHEN_MARSH, CRIMSONVEIL_MIRE, GLACIEMITE_TUNDRA);
    public static final List<ResourceKey<Biome>> VALLEYS = List.of(GREENISH_VALLEY, TEALIVY_VALLEY);
    public static final List<ResourceKey<Biome>> MIDLANDS = List.of(AMETHYST_FOREST, GRIZZLY_THICKET, SNOWY_GRIZZLY_THICKET, CRIMSONVEIL_WOODS, OLD_GROWTH_CRIMSONVEIL_WOODS, BURLYWOOD_JUNGLE, AMETHYST_CHIMERIES, CRIMSONVEIL_PLATEAU);
    public static final List<ResourceKey<Biome>> HIGHLANDS = List.of(GRIZZLY_HIGHLANDS, TEALIVY_HIGHLANDS, CURRANTSLATE_PEAKS, GLACIEMITE_PEAKS);
    public static final List<ResourceKey<Biome>> BOSS_DOMAINS = List.of(THE_DARKNESS, PENUMBRAL_STEPPE, AURIC_HEIGHTS);
    public static final List<ResourceKey<Biome>> ALL = concat(CAVES, OCEANS, RIVERS, LOWLANDS, VALLEYS, MIDLANDS, HIGHLANDS, BOSS_DOMAINS);

    private static final int SKY = 4605011;
    private static final int FOG = 4605011;

    private static final Map<ResourceKey<PlacedFeature>, GenerationStep.Decoration> FEATURE_ORDER = new LinkedHashMap<>();
    private static final Map<ResourceKey<PlacedFeature>, Integer> FEATURE_INDEX = new LinkedHashMap<>();

    static {
        order(GenerationStep.Decoration.RAW_GENERATION, ModPlacements.DARK_CURRANTSLATE_BOULDER, ModPlacements.GLACIEMITE_BOULDER, ModPlacements.AMETHYST_BOULDER,
                ModPlacements.CORONA_STONE_BOULDER, ModPlacements.GILDED_CURRANTSLATE_BOULDER);
        order(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.AMETHYST_GEODE, ModPlacements.DARKNESS_SPIRAL_SPIKE, ModPlacements.GRIPTONITE_CLUSTER_SPIKE, ModPlacements.AMETHYST_CLUSTER_SPIKE, ModPlacements.MIDNIGHT_LILY_PATCH);
        order(GenerationStep.Decoration.SURFACE_STRUCTURES, MiscOverworldPlacements.ICE_SPIKE, ModPlacements.CORONA_STONE_RING);
        order(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.IRON_ORE, ModPlacements.IRON_ORE_UPPER, ModPlacements.ANCIENT_DEBRIS, ModPlacements.ABYSSAL_ORE, ModPlacements.RED_TITANIUM_ORE, ModPlacements.UNSEENIUM_ORE);
        order(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.AMETHYST_CEILING_BOULDER, ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER, ModPlacements.UNSEENIUM_CURRANTSLATE_DISK, ModPlacements.TITANIUM_CURRANTSLATE_DISK, ModPlacements.GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR, ModPlacements.GRIPPING_GLACIEMITE_PATCH_CEILING,
                ModPlacements.SHINY_ICE_CEILING_BOULDER, ModPlacements.FROZEN_CAVE_POOL);
        order(GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacements.HOLLOWS_CEILING_MOSS, ModPlacements.TAPROOT_TREE, ModPlacements.GLOWROOT_VINES, ModPlacements.HOLLOWS_FLOOR_MOSS,
                ModPlacements.HOLLOWS_BURLYWOOD_TREE, ModPlacements.HOLLOWS_BURLYWOOD_BUSH, CavePlacements.SPORE_BLOSSOM, ModPlacements.SHINY_FROSTBLOOM, ModPlacements.HOARFROST_PATCH,
                ModPlacements.HOLLOWS_GLEAMCARVER_NEST, ModPlacements.TANZANITE_GLEAMCARVER_NEST, ModPlacements.SHINY_GLEAMCARVER_NEST,
                ModPlacements.UMBRAL_CORAL_REEF, ModPlacements.DEAD_UMBRAL_CORAL_REEF,
                ModPlacements.AMETHYST_TREE, ModPlacements.SMALL_AMETHYST_TREE, ModPlacements.TANZANITE_SPHERE,
                ModPlacements.GRIZZLY_TREE, ModPlacements.TALL_GRIZZLY_TREE,
                ModPlacements.TALL_CRIMSONVEIL_TREE, ModPlacements.CRIMSONVEIL_TREE,
                ModPlacements.BURLYWOOD_TREE, ModPlacements.SMALL_BURLYWOOD_TREE, ModPlacements.BUSH_BURLYWOOD_TREE,
                ModPlacements.TEALIVE_TREE,
                ModPlacements.UMBRAL_KELP,
                ModPlacements.AMETHYST_FLOWERS, ModPlacements.AMETHYST_BUSH,
                ModPlacements.BEARFRUIT_BRAMBLE_PATCH, ModPlacements.BURLYWOOD_VIOLET_PATCH,
                VegetationPlacements.FLOWER_MEADOW, VegetationPlacements.PATCH_GRASS_PLAIN, VegetationPlacements.PATCH_GRASS_JUNGLE,
                ModPlacements.GLOOMWEED_PATCH, ModPlacements.TALL_GLOOMWEED_PATCH, ModPlacements.RARE_GLOOMWEED_PATCH,
                ModPlacements.FROSTBLOOM_PATCH, ModPlacements.CORONA_BLOOM_PATCH, ModPlacements.ASHREED_PATCH,
                ModPlacements.GRIPCRYSTAL_WART_PATCH, ModPlacements.HOLLOWS_GLOW_LICHEN);
        order(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModPlacements.BUSH_LIKE_TREE_PATCH, ModPlacements.CHIMERIC_FREEZE);
    }

    public static void boostrap(BootstapContext<Biome> context) {
        context.register(DEEP_GLACIEMITE_CAVES, deepGlaciemiteCaves(context));
        context.register(TANZANITE_CAVES, tanzaniteCaves(context));
        context.register(SHINY_CAVERNS, shinyCaverns(context));
        context.register(BURLYWOOD_HOLLOWS, burlywoodHollows(context));

        context.register(DARK_OCEAN, darkOcean(context));
        context.register(DARK_LIFELESS_OCEAN, lifelessOcean(context));
        context.register(FRIGID_SPIRE_OCEAN, frigidSpireOcean(context));
        context.register(DARK_RIVER, river(context, 0.4F));
        context.register(LUKEWARM_DARK_RIVER, river(context, 0.8F));
        context.register(GREYNISH_SHORE, greynishShore(context));

        context.register(DARK_WASTELAND, darkWasteland(context));
        context.register(GLOOMWEED_PLAINS, gloomweedPlains(context));
        context.register(ASHEN_MARSH, ashenMarsh(context));
        context.register(CRIMSONVEIL_MIRE, crimsonveilMire(context));
        context.register(GLACIEMITE_TUNDRA, glaciemiteTundra(context));

        context.register(GREENISH_VALLEY, greenishValley(context));
        context.register(TEALIVY_VALLEY, tealivyValley(context));

        context.register(AMETHYST_FOREST, amethystForest(context));
        context.register(AMETHYST_CHIMERIES, amethystChimeries(context));
        context.register(GRIZZLY_THICKET, grizzlyThicket(context));
        context.register(SNOWY_GRIZZLY_THICKET, snowyGrizzlyThicket(context));
        context.register(CRIMSONVEIL_WOODS, crimsonveilWoods(context, false));
        context.register(OLD_GROWTH_CRIMSONVEIL_WOODS, crimsonveilWoods(context, true));
        context.register(CRIMSONVEIL_PLATEAU, crimsonveilPlateau(context));
        context.register(BURLYWOOD_JUNGLE, burlywoodJungle(context));

        context.register(GRIZZLY_HIGHLANDS, grizzlyHighlands(context));
        context.register(TEALIVY_HIGHLANDS, tealivyHighlands(context));
        context.register(CURRANTSLATE_PEAKS, currantslatePeaks(context));
        context.register(GLACIEMITE_PEAKS, glaciemitePeaks(context));

        context.register(THE_DARKNESS, theDarkness(context));
        context.register(PENUMBRAL_STEPPE, penumbralSteppe(context));
        context.register(AURIC_HEIGHTS, auricHeights(context));
    }

    private static Biome deepGlaciemiteCaves(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.MONSTER, ModEntities.ESPYER, 6, 1, 1);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.ELDRITCH_WATCHER, 5, 1, 1);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.DREADFLAP, 6, 1, 2);
        return biome(-0.4F, true, spawns,
                generation(context, true, ModPlacements.IRON_ORE, ModPlacements.ANCIENT_DEBRIS, ModPlacements.ABYSSAL_ORE, ModPlacements.GRIPPING_GLACIEMITE_PATCH_CEILING, ModPlacements.FROZEN_CAVE_POOL),
                new Look().water(6579300).foliage(6579300).grass(6579300)
                        .music(SoundEvents.MUSIC_BIOME_DEEP_DARK)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
                        .particle(ParticleTypes.WHITE_ASH, 0.006F));
    }

    private static Biome tanzaniteCaves(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, ModEntities.GLEAMCARVER, 5, 1, 1);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.DREADFLAP, 6, 1, 2);
        return biome(0.5F, false, spawns,
                generation(context, true, CavePlacements.AMETHYST_GEODE, ModPlacements.IRON_ORE, ModPlacements.UNSEENIUM_ORE, ModPlacements.RED_TITANIUM_ORE,
                        ModPlacements.AMETHYST_CEILING_BOULDER, ModPlacements.UNSEENIUM_CURRANTSLATE_DISK, ModPlacements.TITANIUM_CURRANTSLATE_DISK,
                        ModPlacements.TANZANITE_GLEAMCARVER_NEST),
                new Look().grass(13061821).foliage(13061821).music(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES));
    }

    private static Biome shinyCaverns(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.MONSTER, ModEntities.ESPYER, 4, 1, 1);
        spawn(spawns, ModEntities.GLEAMCARVER, 5, 1, 1);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.ELDRITCH_WATCHER, 3, 1, 1);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.DREADFLAP, 6, 1, 2);
        return biome(-0.3F, true, spawns,
                generation(context, true, ModPlacements.IRON_ORE, ModPlacements.ABYSSAL_ORE, ModPlacements.UNSEENIUM_ORE,
                        ModPlacements.SHINY_ICE_CEILING_BOULDER, ModPlacements.UNSEENIUM_CURRANTSLATE_DISK, ModPlacements.TITANIUM_CURRANTSLATE_DISK,
                        ModPlacements.GRIPPING_GLACIEMITE_PATCH_CEILING, ModPlacements.FROZEN_CAVE_POOL, ModPlacements.SHINY_FROSTBLOOM, ModPlacements.HOARFROST_PATCH, ModPlacements.SHINY_GLEAMCARVER_NEST),
                new Look().water(6579300).grass(6579300).foliage(6579300).music(SoundEvents.MUSIC_BIOME_FROZEN_PEAKS).particle(ParticleTypes.SNOWFLAKE, 0.002F));
    }

    private static Biome burlywoodHollows(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, ModEntities.GLEAMCARVER, 4, 1, 1);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.DREADFLAP, 4, 1, 2);
        return biome(0.7F, false, spawns,
                generation(context, true, ModPlacements.IRON_ORE, ModPlacements.RED_TITANIUM_ORE,
                        ModPlacements.HOLLOWS_CEILING_MOSS, ModPlacements.TAPROOT_TREE, ModPlacements.GLOWROOT_VINES, ModPlacements.HOLLOWS_FLOOR_MOSS,
                        ModPlacements.HOLLOWS_BURLYWOOD_TREE, ModPlacements.HOLLOWS_BURLYWOOD_BUSH, CavePlacements.SPORE_BLOSSOM, ModPlacements.HOLLOWS_GLEAMCARVER_NEST, ModPlacements.HOLLOWS_GLOW_LICHEN),
                new Look().grass(6717235).foliage(6717235).music(SoundEvents.MUSIC_BIOME_LUSH_CAVES).particle(ParticleTypes.SPORE_BLOSSOM_AIR, 0.004F));
    }

    private static Biome darkOcean(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, ModEntities.SHIMMER, 8, 1, 2);
        return biome(0.8F, false, spawns,
                generation(context, false, ModPlacements.MIDNIGHT_LILY_PATCH, ModPlacements.UMBRAL_KELP, ModPlacements.UMBRAL_CORAL_REEF),
                new Look().music(SoundEvents.MUSIC_UNDER_WATER));
    }

    private static Biome lifelessOcean(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, ModEntities.GLOOMWHALE, 8, 1, 3);
        spawn(spawns, ModEntities.GRIPPING_GLOOMWHALE, 3, 1, 1);
        return biome(0.3F, false, spawns,
                generation(context, false, ModPlacements.DEAD_UMBRAL_CORAL_REEF),
                new Look().music(SoundEvents.MUSIC_UNDER_WATER).mood(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 4.0D)));
    }

    private static Biome frigidSpireOcean(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, ModEntities.GRIPPING_GLOOMWHALE, 8, 1, 2);
        spawn(spawns, ModEntities.GLOOMWHALE, 2, 1, 2);
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(-0.5F)
                .temperatureAdjustment(Biome.TemperatureModifier.FROZEN)
                .downfall(0.5F)
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation(context, false, ModPlacements.CHIMERIC_FREEZE))
                .specialEffects(new Look().water(6579300).music(SoundEvents.MUSIC_UNDER_WATER).particle(ParticleTypes.SNOWFLAKE, 0.004F).build())
                .build();
    }

    private static Biome river(BootstapContext<Biome> context, float temperature) {
        return biome(temperature, false, new MobSpawnSettings.Builder(),
                generation(context, false, ModPlacements.MIDNIGHT_LILY_PATCH, ModPlacements.UMBRAL_KELP),
                new Look().music(SoundEvents.MUSIC_UNDER_WATER));
    }

    private static Biome greynishShore(BootstapContext<Biome> context) {
        return biome(0.5F, false, new MobSpawnSettings.Builder(),
                generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.MIDNIGHT_LILY_PATCH),
                new Look().music(SoundEvents.MUSIC_UNDER_WATER));
    }

    private static Biome darkWasteland(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.TUSKHOG, 6, 2, 4);
        spawn(spawns, MobCategory.CREATURE, ModEntities.SABERPARD, 3, 1, 1);
        return biome(1.0F, false, spawns,
                generation(context, true, ModPlacements.DARK_CURRANTSLATE_BOULDER, ModPlacements.IRON_ORE_UPPER,
                        ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER, ModPlacements.GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR, ModPlacements.GLOOMWEED_PATCH, ModPlacements.TALL_GLOOMWEED_PATCH),
                new Look().music(SoundEvents.MUSIC_BIOME_BADLANDS)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0D))
                        .particle(ParticleTypes.ASH, 0.01F));
    }

    private static Biome gloomweedPlains(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.TUSKHOG, 8, 2, 4);
        spawn(spawns, MobCategory.CREATURE, ModEntities.HARMONY_WATCHER, 4, 1, 1);
        return biome(0.55F, false, spawns,
                generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.GLOOMWEED_PATCH, ModPlacements.TALL_GLOOMWEED_PATCH, ModPlacements.RARE_GLOOMWEED_PATCH),
                new Look().grass(336860160).foliage(336860160).music(SoundEvents.MUSIC_BIOME_MEADOW)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0D)));
    }

    private static Biome ashenMarsh(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.TUSKHOG, 3, 1, 2);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.DREADFLAP, 6, 1, 3);
        return biome(0.45F, false, spawns,
                generation(context, true, ModPlacements.MIDNIGHT_LILY_PATCH, ModPlacements.IRON_ORE_UPPER, ModPlacements.ASHREED_PATCH, ModPlacements.GLOOMWEED_PATCH, ModPlacements.TALL_GLOOMWEED_PATCH),
                new Look().water(3355443).grass(5592405).foliage(5592405).music(SoundEvents.MUSIC_BIOME_SWAMP)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 4.0D))
                        .particle(ParticleTypes.WHITE_ASH, 0.02F));
    }

    private static Biome crimsonveilMire(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.AMBIENT, ModEntities.DREADFLAP, 6, 1, 3);
        return biome(0.8F, false, spawns,
                generation(context, true, ModPlacements.MIDNIGHT_LILY_PATCH, ModPlacements.IRON_ORE_UPPER,
                        ModPlacements.CRIMSONVEIL_TREE, ModPlacements.GLOOMWEED_PATCH, ModPlacements.TALL_GLOOMWEED_PATCH),
                new Look().grass(336860160).foliage(336860160).music(SoundEvents.MUSIC_BIOME_SWAMP)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 4.0D))
                        .particle(ParticleTypes.CRIMSON_SPORE, 0.01F));
    }

    private static Biome glaciemiteTundra(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.MONSTER, ModEntities.ESPYER, 5, 1, 1);
        spawn(spawns, MobCategory.CREATURE, ModEntities.RIMEHORN, 6, 2, 5);
        return biome(-0.6F, true, spawns,
                generation(context, true, ModPlacements.GLACIEMITE_BOULDER, MiscOverworldPlacements.ICE_SPIKE, ModPlacements.IRON_ORE_UPPER, ModPlacements.FROSTBLOOM_PATCH, ModPlacements.CHIMERIC_FREEZE),
                new Look().water(6579300).grass(6579300).foliage(6579300).music(SoundEvents.MUSIC_BIOME_SNOWY_SLOPES).particle(ParticleTypes.SNOWFLAKE, 0.012F));
    }

    private static Biome theDarkness(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.MONSTER, ModEntities.ESPYER, 3, 1, 1);
        spawn(spawns, MobCategory.AMBIENT, ModEntities.ELDRITCH_WATCHER, 5, 1, 1);
        return biome(0.16F, false, spawns,
                generation(context, true, ModPlacements.GLACIEMITE_BOULDER, ModPlacements.DARKNESS_SPIRAL_SPIKE, ModPlacements.IRON_ORE_UPPER,
                        ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER, ModPlacements.GRIPCRYSTAL_WART_PATCH),
                new Look().particle(ParticleTypes.ASH, 0.0125F)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 4.0D))
                        .music(ModSoundEvents.THE_DARKNESS_AMBIENT));
    }

    private static Biome greenishValley(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.HARMONY_WATCHER, 5, 1, 1);
        return biome(0.75F, false, spawns,
                generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.BURLYWOOD_TREE, ModPlacements.BURLYWOOD_VIOLET_PATCH,
                        VegetationPlacements.FLOWER_MEADOW, VegetationPlacements.PATCH_GRASS_PLAIN, ModPlacements.RARE_GLOOMWEED_PATCH, ModPlacements.BUSH_LIKE_TREE_PATCH),
                new Look().water(6717235).grass(6717235).foliage(6717235).music(SoundEvents.MUSIC_GAME));
    }

    private static Biome tealivyValley(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.HARMONY_WATCHER, 4, 1, 1);
        return biome(0.5F, false, spawns,
                generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.TEALIVE_TREE, ModPlacements.GLOOMWEED_PATCH, ModPlacements.TALL_GLOOMWEED_PATCH),
                new Look().water(5013401).grass(5013401).foliage(5013401).music(SoundEvents.MUSIC_CREDITS)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0D)));
    }

    private static Biome amethystForest(BootstapContext<Biome> context) {
        return biome(0.5F, false, new MobSpawnSettings.Builder(),
                generation(context, true, ModPlacements.AMETHYST_BOULDER, ModPlacements.IRON_ORE_UPPER, ModPlacements.AMETHYST_CEILING_BOULDER,
                        ModPlacements.AMETHYST_TREE, ModPlacements.SMALL_AMETHYST_TREE, ModPlacements.TANZANITE_SPHERE, ModPlacements.AMETHYST_FLOWERS, ModPlacements.AMETHYST_BUSH),
                new Look().water(13061821).grass(13061821).foliage(13061821).music(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST));
    }

    private static Biome amethystChimeries(BootstapContext<Biome> context) {
        return biome(0.5F, false, new MobSpawnSettings.Builder(),
                generation(context, true, ModPlacements.AMETHYST_CLUSTER_SPIKE, ModPlacements.IRON_ORE_UPPER, ModPlacements.AMETHYST_CEILING_BOULDER,
                        ModPlacements.SMALL_AMETHYST_TREE, ModPlacements.AMETHYST_FLOWERS, ModPlacements.AMETHYST_BUSH),
                new Look().water(13061821).grass(13061821).foliage(13061821).music(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST));
    }

    private static Biome grizzlyThicket(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.SABERPARD, 4, 1, 2);
        return biome(0.3F, false, spawns,
                generation(context, true, ModPlacements.DARK_CURRANTSLATE_BOULDER, ModPlacements.IRON_ORE_UPPER, ModPlacements.GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR,
                        ModPlacements.GRIZZLY_TREE, ModPlacements.TALL_GRIZZLY_TREE, ModPlacements.BEARFRUIT_BRAMBLE_PATCH, ModPlacements.GLOOMWEED_PATCH),
                new Look().water(7340544).grass(7340544).foliage(7340544).music(SoundEvents.MUSIC_BIOME_FOREST));
    }

    private static Biome snowyGrizzlyThicket(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.RIMEHORN, 6, 2, 5);
        spawn(spawns, MobCategory.MONSTER, ModEntities.ESPYER, 3, 1, 1);
        return biome(-0.5F, true, spawns,
                generation(context, true, ModPlacements.GLACIEMITE_BOULDER, ModPlacements.IRON_ORE_UPPER, ModPlacements.GRIZZLY_TREE, ModPlacements.BEARFRUIT_BRAMBLE_PATCH, ModPlacements.FROSTBLOOM_PATCH, ModPlacements.CHIMERIC_FREEZE),
                new Look().water(6579300).grass(7340544).foliage(7340544).music(SoundEvents.MUSIC_BIOME_GROVE));
    }

    private static Biome crimsonveilWoods(BootstapContext<Biome> context, boolean oldGrowth) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.TUSKHOG, oldGrowth ? 3 : 5, 2, 3);
        BiomeGenerationSettings settings = oldGrowth
                ? generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.TALL_CRIMSONVEIL_TREE, ModPlacements.CRIMSONVEIL_TREE, ModPlacements.TALL_GLOOMWEED_PATCH)
                : generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.CRIMSONVEIL_TREE, ModPlacements.GLOOMWEED_PATCH);
        return biome(0.8F, false, spawns, settings,
                new Look().grass(336860160).foliage(336860160).music(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 4.0D)));
    }

    private static Biome crimsonveilPlateau(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.SABERPARD, 4, 1, 2);
        return biome(0.9F, false, spawns,
                generation(context, true, ModPlacements.DARK_CURRANTSLATE_BOULDER, ModPlacements.GRIPTONITE_CLUSTER_SPIKE, ModPlacements.IRON_ORE_UPPER,
                        ModPlacements.CRIMSONVEIL_TREE, ModPlacements.TALL_GLOOMWEED_PATCH),
                new Look().grass(336860160).foliage(336860160).music(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST));
    }

    private static Biome burlywoodJungle(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.SABERPARD, 5, 1, 2);
        return biome(0.95F, false, spawns,
                generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.BURLYWOOD_TREE, ModPlacements.SMALL_BURLYWOOD_TREE, ModPlacements.BUSH_BURLYWOOD_TREE,
                        ModPlacements.BURLYWOOD_VIOLET_PATCH, VegetationPlacements.PATCH_GRASS_JUNGLE, ModPlacements.RARE_GLOOMWEED_PATCH, ModPlacements.BUSH_LIKE_TREE_PATCH),
                new Look().water(6717235).grass(6717235).foliage(6717235).music(SoundEvents.MUSIC_BIOME_JUNGLE));
    }

    private static Biome grizzlyHighlands(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.SABERPARD, 3, 1, 1);
        return biome(0.3F, false, spawns,
                generation(context, true, ModPlacements.DARK_CURRANTSLATE_BOULDER, ModPlacements.IRON_ORE_UPPER, ModPlacements.BEARFRUIT_BRAMBLE_PATCH, ModPlacements.GLOOMWEED_PATCH, ModPlacements.CHIMERIC_FREEZE),
                new Look().water(7340544).grass(7340544).foliage(7340544).music(SoundEvents.MUSIC_BIOME_MEADOW));
    }

    private static Biome tealivyHighlands(BootstapContext<Biome> context) {
        return biome(0.35F, false, new MobSpawnSettings.Builder(),
                generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.GLOOMWEED_PATCH, ModPlacements.CHIMERIC_FREEZE),
                new Look().water(5013401).grass(5013401).foliage(5013401).music(SoundEvents.MUSIC_CREDITS));
    }

    private static Biome currantslatePeaks(BootstapContext<Biome> context) {
        return biome(0.4F, false, new MobSpawnSettings.Builder(),
                generation(context, true, ModPlacements.GRIPTONITE_CLUSTER_SPIKE, ModPlacements.IRON_ORE_UPPER, ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER),
                new Look().music(SoundEvents.MUSIC_BIOME_STONY_PEAKS)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0D)));
    }

    private static Biome glaciemitePeaks(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.MONSTER, ModEntities.ESPYER, 6, 1, 1);
        return biome(-0.7F, true, spawns,
                generation(context, true, ModPlacements.IRON_ORE_UPPER, ModPlacements.GRIPPING_GLACIEMITE_PATCH_CEILING, ModPlacements.CHIMERIC_FREEZE),
                new Look().water(6579300).grass(6579300).foliage(6579300).music(SoundEvents.MUSIC_BIOME_JAGGED_PEAKS).particle(ParticleTypes.SNOWFLAKE, 0.02F));
    }

    private static Biome penumbralSteppe(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawn(spawns, MobCategory.CREATURE, ModEntities.TUSKHOG, 4, 2, 3);
        spawn(spawns, MobCategory.CREATURE, ModEntities.SABERPARD, 2, 1, 1);
        return biome(0.6F, false, spawns,
                generation(context, true, ModPlacements.CORONA_STONE_BOULDER, ModPlacements.CORONA_STONE_RING, ModPlacements.IRON_ORE_UPPER,
                        ModPlacements.CORONA_BLOOM_PATCH, ModPlacements.GLOOMWEED_PATCH, ModPlacements.RARE_GLOOMWEED_PATCH),
                new Look().grass(3618615).foliage(3618615).music(SoundEvents.MUSIC_BIOME_BADLANDS)
                        .mood(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
                        .particle(ParticleTypes.WHITE_ASH, 0.004F));
    }

    private static Biome auricHeights(BootstapContext<Biome> context) {
        return biome(0.7F, false, new MobSpawnSettings.Builder(),
                generation(context, true, ModPlacements.GILDED_CURRANTSLATE_BOULDER, ModPlacements.IRON_ORE_UPPER,
                        ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER),
                new Look().grass(12951120).foliage(12951120).music(SoundEvents.MUSIC_BIOME_JAGGED_PEAKS));
    }

    private static Biome biome(float temperature, boolean precipitation, MobSpawnSettings.Builder spawns, BiomeGenerationSettings generation, Look look) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(precipitation)
                .temperature(temperature)
                .downfall(precipitation ? 0.5F : 0.0F)
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation)
                .specialEffects(look.build())
                .build();
    }

    @SafeVarargs
    private static BiomeGenerationSettings generation(BootstapContext<Biome> context, boolean spiralCaves, ResourceKey<PlacedFeature>... features) {
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        List<ResourceKey<PlacedFeature>> sorted = new ArrayList<>();
        for (ResourceKey<PlacedFeature> feature : features) {
            if (!FEATURE_INDEX.containsKey(feature)) {
                throw new IllegalArgumentException("Placed feature " + feature.location() + " has no slot in ModBiomes feature order");
            }
            if (!sorted.contains(feature)) {
                sorted.add(feature);
            }
        }
        sorted.sort(Comparator.comparingInt(FEATURE_INDEX::get));
        for (ResourceKey<PlacedFeature> feature : sorted) {
            builder.addFeature(FEATURE_ORDER.get(feature), feature);
        }
        if (spiralCaves) {
            builder.addCarver(GenerationStep.Carving.AIR, ModCarvers.SPIRAL_CAVE);
        }
        return builder.build();
    }

    private static void spawn(MobSpawnSettings.Builder builder, EntityType<?> type, int weight, int min, int max) {
        spawn(builder, type.getCategory(), type, weight, min, max);
    }

    private static void spawn(MobSpawnSettings.Builder builder, MobCategory category, EntityType<?> type, int weight, int min, int max) {
        builder.addSpawn(category, new MobSpawnSettings.SpawnerData(type, weight, min, max));
    }

    @SafeVarargs
    private static void order(GenerationStep.Decoration step, ResourceKey<PlacedFeature>... features) {
        for (ResourceKey<PlacedFeature> feature : features) {
            if (FEATURE_ORDER.putIfAbsent(feature, step) != null) {
                throw new IllegalStateException("Placed feature " + feature.location() + " is ordered twice");
            }
            FEATURE_INDEX.put(feature, FEATURE_INDEX.size());
        }
    }

    @SafeVarargs
    private static List<ResourceKey<Biome>> concat(List<ResourceKey<Biome>>... groups) {
        List<ResourceKey<Biome>> all = new ArrayList<>();
        for (List<ResourceKey<Biome>> group : groups) {
            all.addAll(group);
        }
        return List.copyOf(all);
    }

    private static ResourceKey<Biome> key(String name) {
        return ResourceKey.create(Registries.BIOME, UnseenWorld.MOD.makeID(name));
    }

    private static final class Look {
        private int water;
        private int grass;
        private int foliage;
        private Music music = Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_GAME.value()));
        private AmbientMoodSettings mood = AmbientMoodSettings.LEGACY_CAVE_SETTINGS;
        private AmbientParticleSettings particle;

        Look water(int color) {
            this.water = color;
            return this;
        }

        Look grass(int color) {
            this.grass = color;
            return this;
        }

        Look foliage(int color) {
            this.foliage = color;
            return this;
        }

        Look music(Holder<SoundEvent> sound) {
            this.music = Musics.createGameMusic(Holder.direct(sound.value()));
            return this;
        }

        Look music(SoundEvent sound) {
            this.music = Musics.createGameMusic(Holder.direct(sound));
            return this;
        }

        Look mood(AmbientMoodSettings settings) {
            this.mood = settings;
            return this;
        }

        Look particle(ParticleOptions options, float probability) {
            this.particle = new AmbientParticleSettings(options, probability);
            return this;
        }

        BiomeSpecialEffects build() {
            BiomeSpecialEffects.Builder builder = new BiomeSpecialEffects.Builder()
                    .waterColor(this.water)
                    .waterFogColor(this.water)
                    .skyColor(SKY)
                    .fogColor(FOG)
                    .grassColorOverride(this.grass)
                    .foliageColorOverride(this.foliage)
                    .ambientMoodSound(this.mood)
                    .backgroundMusic(this.music);
            if (this.particle != null) {
                builder.ambientParticle(this.particle);
            }
            return builder.build();
        }
    }
}
