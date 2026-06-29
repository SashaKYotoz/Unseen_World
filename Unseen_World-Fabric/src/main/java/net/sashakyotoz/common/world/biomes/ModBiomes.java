package net.sashakyotoz.common.world.biomes;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.world.carvers.ModCarvers;
import net.sashakyotoz.common.world.features.placements.ModPlacements;

public class ModBiomes {
    public static final ResourceKey<Biome> THE_DARKNESS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("the_darkness"));

    public static final ResourceKey<Biome> AMETHYST_FOREST = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("amethyst_forest"));
    public static final ResourceKey<Biome> GRIZZLY_THICKET = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("grizzly_thicket"));
    public static final ResourceKey<Biome> BURLYWOOD_JUNGLE = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("burlywood_jungle"));
    public static final ResourceKey<Biome> CRIMSONVEIL_WOODS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("crimsonveil_woods"));
    public static final ResourceKey<Biome> OLD_GROWTH_CRIMSONVEIL_WOODS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("old_growth_crimsonveil_woods"));

    public static final ResourceKey<Biome> AMETHYST_CHIMERIES = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("amethyst_chimeries"));
    public static final ResourceKey<Biome> GREENISH_VALLEY = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("greenish_valley"));
    public static final ResourceKey<Biome> GREENISH_MEADOW = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("greenish_meadow"));
    public static final ResourceKey<Biome> TEALIVY_VALLEY = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("tealivy_valley"));
    public static final ResourceKey<Biome> DARK_WASTELAND = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("dark_wasteland"));
    public static final ResourceKey<Biome> CRIMSONVEIL_PLATEAU = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("crimsonveil_plateau"));
    public static final ResourceKey<Biome> HARMONY_GROVE = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("harmony_grove"));

    public static final ResourceKey<Biome> GRIZZLY_HIGHLANDS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("grizzly_highlands"));
    public static final ResourceKey<Biome> TEALIVY_HIGHLANDS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("tealivy_highlands"));
    public static final ResourceKey<Biome> CURRANTSLATE_PEAKS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("currantslate_peaks"));

    public static final ResourceKey<Biome> SHINY_CAVERNS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("shiny_caverns"));
    public static final ResourceKey<Biome> TANZANITE_CAVES = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("tanzanite_caves"));
    public static final ResourceKey<Biome> DEEP_GLACIEMITE_CAVES = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("deep_glaciemite_caves"));

    public static final ResourceKey<Biome> DARK_RIVER = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("dark_river"));
    public static final ResourceKey<Biome> LUKEWARM_DARK_RIVER = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("lukewarm_dark_river"));
    public static final ResourceKey<Biome> DARK_OCEAN = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("dark_ocean"));
    public static final ResourceKey<Biome> DARK_LIFELESS_OCEAN = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("dark_lifeless_ocean"));

    public static final ResourceKey<Biome> GREYNISH_SHORE = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("greynish_shore"));

    public static final ResourceKey<Biome> DARK_BADLANDS = ResourceKey.create(Registries.BIOME,
            UnseenWorld.makeID("dark_badlands"));

    public static void boostrap(BootstapContext<Biome> context) {
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

    public static Biome amethystBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.AMETHYST_CEILING_BOULDER)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ModPlacements.AMETHYST_CLUSTER_SPIKE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.AMETHYST_BUSH)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(13061821)
                        .waterFogColor(13061821)
                        .skyColor(4605011)
                        .grassColorOverride(13061821)
                        .foliageColorOverride(13061821)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .fogColor(4605011)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST.value()))).build())
                .build();
    }

    public static Biome grizzlyLikeBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.75f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(7340544)
                        .waterFogColor(7340544)
                        .skyColor(4605011)
                        .grassColorOverride(7340544)
                        .foliageColorOverride(7340544)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .fogColor(4605011)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST.value()))).build())
                .build();
    }
    public static Biome grizzlyForestBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.75f)
                .generationSettings(
                        biomeBuilder
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.GRIZZLY_TREE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.TALL_GRIZZLY_TREE)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(7340544)
                        .waterFogColor(7340544)
                        .skyColor(4605011)
                        .grassColorOverride(7340544)
                        .foliageColorOverride(7340544)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .fogColor(4605011)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST.value()))).build())
                .build();
    }

    public static Biome ocean(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ModPlacements.MIDNIGHT_LILY_PATCH);
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.25f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColorOverride(0x000)
                        .foliageColorOverride(0x000)
                        .fogColor(4605011)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_UNDER_WATER.value()))).build())
                .build();
    }

    public static Biome theDarkness(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.16f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.ANCIENT_DEBRIS)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.GRIPCRYSTAL_WART_PATCH)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.0125f))
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColorOverride(0x000)
                        .foliageColorOverride(0x000)
                        .fogColor(4605011)
                        .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 4.0))
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(ModSoundEvents.THE_DARKNESS_AMBIENT))).build())
                .build();
    }

    public static Biome darkBiomes(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.2f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.ANCIENT_DEBRIS)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.DARK_CURRANTSLATE_CEILING_BOULDER)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColorOverride(0x000)
                        .foliageColorOverride(0x000)
                        .fogColor(4605011)
                        .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0))
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_BIOME_BASALT_DELTAS.value()))).build())
                .build();
    }

    public static Biome semiHotBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.GLOOMWEED_PATCH)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x000)
                        .waterFogColor(0x000)
                        .skyColor(4605011)
                        .grassColorOverride(336860160)
                        .foliageColorOverride(336860160)
                        .fogColor(4605011)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST.value()))).build())
                .build();
    }

    public static Biome tealivyBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(5013401)
                        .waterFogColor(5013401)
                        .skyColor(4605011)
                        .grassColorOverride(5013401)
                        .foliageColorOverride(5013401)
                        .fogColor(4605011)
                        .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 4.0))
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_CREDITS.value()))).build())
                .build();
    }

    public static Biome greenishBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.75f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModPlacements.BUSH_LIKE_TREE_PATCH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.BURLYWOOD_VIOLET_PATCH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacements.RARE_GLOOMWEED_PATCH)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_JUNGLE)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(6717235)
                        .waterFogColor(6717235)
                        .skyColor(4605011)
                        .grassColorOverride(6717235)
                        .foliageColorOverride(6717235)
                        .fogColor(4605011)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_GAME.value()))).build())
                .build();
    }

    public static Biome cavernBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.ANCIENT_DEBRIS)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacements.GRIPPING_GLACIEMITE_PATCH_CEILING)
                        .addCarver(GenerationStep.Carving.AIR, ModCarvers.SPIRAL_CAVE)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(6579300)
                        .waterFogColor(6579300)
                        .skyColor(4605011)
                        .grassColorOverride(6579300)
                        .foliageColorOverride(6579300)
                        .fogColor(4605011)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_BIOME_LUSH_CAVES.value()))).build())
                .build();
    }

    public static Biome mixedForestBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0f)
                .generationSettings(biomeBuilder
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.IRON_ORE)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(6717235)
                        .waterFogColor(6579300)
                        .skyColor(4605011)
                        .grassColorOverride(5013401)
                        .foliageColorOverride(6579300)
                        .fogColor(4605011)
                        .backgroundMusic(Musics.createGameMusic(Holder.direct(SoundEvents.MUSIC_BIOME_FOREST.value()))).build())
                .build();
    }
}