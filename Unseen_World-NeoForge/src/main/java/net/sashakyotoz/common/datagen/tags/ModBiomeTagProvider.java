package net.sashakyotoz.common.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, ExistingFileHelper existingFileHelper) {
        super(output, registriesFuture, UnseenWorld.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.addDimensionTags();
        this.addFogTags();
        this.addBossTags();
        this.addContentTags();
    }

    private void addDimensionTags() {
        this.add(ModTags.Biomes.IS_CHIMERIC_DARKNESS, ModBiomes.ALL);
        this.add(ModTags.Biomes.IS_UNDERGROUND_BIOME, ModBiomes.CAVES);
        this.add(ModTags.Biomes.IS_COLD, List.of(
                ModBiomes.DEEP_GLACIEMITE_CAVES,
                ModBiomes.SHINY_CAVERNS,
                ModBiomes.FRIGID_SPIRE_OCEAN,
                ModBiomes.GLACIEMITE_TUNDRA,
                ModBiomes.SNOWY_GRIZZLY_THICKET,
                ModBiomes.GLACIEMITE_PEAKS));
    }

    private void addFogTags() {
        this.add(ModTags.Biomes.HAS_DENSE_FOG, List.of(
                ModBiomes.THE_DARKNESS,
                ModBiomes.DARK_WASTELAND,
                ModBiomes.GLOOMWEED_PLAINS,
                ModBiomes.ASHEN_MARSH,
                ModBiomes.CRIMSONVEIL_MIRE,
                ModBiomes.GLACIEMITE_TUNDRA));
        this.add(ModTags.Biomes.HAS_CLEAR_FOG, List.of(
                ModBiomes.GRIZZLY_HIGHLANDS,
                ModBiomes.TEALIVY_HIGHLANDS,
                ModBiomes.CURRANTSLATE_PEAKS,
                ModBiomes.GLACIEMITE_PEAKS,
                ModBiomes.AMETHYST_CHIMERIES,
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.AURIC_HEIGHTS));
    }

    private void addBossTags() {
        this.add(ModTags.Biomes.IS_BOSS_DOMAIN, ModBiomes.BOSS_DOMAINS);
        this.add(ModTags.Biomes.HAS_WARRIOR_ARENA, List.of(ModBiomes.THE_DARKNESS));
        this.add(ModTags.Biomes.HAS_WARRIOR_RUINS, List.of(ModBiomes.THE_DARKNESS, ModBiomes.GLACIEMITE_TUNDRA, ModBiomes.DARK_WASTELAND));
        this.add(ModTags.Biomes.HAS_SENTINEL_ARENA, List.of(ModBiomes.PENUMBRAL_STEPPE));
        this.add(ModTags.Biomes.HAS_SENTINEL_RUINS, List.of(ModBiomes.PENUMBRAL_STEPPE, ModBiomes.GLOOMWEED_PLAINS, ModBiomes.AMETHYST_CHIMERIES, ModBiomes.CRIMSONVEIL_PLATEAU));
        this.add(ModTags.Biomes.HAS_PATRIARCH_ARENA, List.of(ModBiomes.AURIC_HEIGHTS));
        this.add(ModTags.Biomes.HAS_PATRIARCH_RUINS, List.of(ModBiomes.AURIC_HEIGHTS, ModBiomes.CURRANTSLATE_PEAKS, ModBiomes.CRIMSONVEIL_PLATEAU));
        this.add(ModTags.Biomes.HAS_FROST_PORTAL, List.of(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_SLOPES));
    }

    private void addContentTags() {
        this.add(ModTags.Biomes.ABYSSAL_SPAWNS_IN, List.of(
                ModBiomes.DEEP_GLACIEMITE_CAVES,
                ModBiomes.GLACIEMITE_TUNDRA,
                ModBiomes.GLACIEMITE_PEAKS,
                ModBiomes.THE_DARKNESS));
        this.add(ModTags.Biomes.TITANIUM_SPAWNS_IN, List.of(
                ModBiomes.DARK_WASTELAND,
                ModBiomes.CRIMSONVEIL_MIRE,
                ModBiomes.CRIMSONVEIL_WOODS,
                ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS,
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.CURRANTSLATE_PEAKS,
                ModBiomes.PENUMBRAL_STEPPE,
                ModBiomes.AURIC_HEIGHTS));
        this.add(ModTags.Biomes.UNSEENIUM_SPAWNS_IN, List.of(
                ModBiomes.TANZANITE_CAVES,
                ModBiomes.AMETHYST_FOREST,
                ModBiomes.AMETHYST_CHIMERIES));

        this.add(ModTags.Biomes.FLOWERS_COVERED, List.of(ModBiomes.GREENISH_VALLEY));
        this.add(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON, List.of(
                ModBiomes.GLACIEMITE_TUNDRA,
                ModBiomes.SNOWY_GRIZZLY_THICKET,
                ModBiomes.THE_DARKNESS));
        this.add(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON, List.of(
                ModBiomes.DARK_WASTELAND,
                ModBiomes.GRIZZLY_THICKET,
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.GRIZZLY_HIGHLANDS,
                ModBiomes.PENUMBRAL_STEPPE,
                ModBiomes.AURIC_HEIGHTS));

        this.add(ModTags.Biomes.HAS_BURLYWOOD_TREE, List.of(ModBiomes.GREENISH_VALLEY, ModBiomes.BURLYWOOD_JUNGLE));
        this.add(ModTags.Biomes.HAS_AMETHYST_TREE, List.of(ModBiomes.AMETHYST_FOREST, ModBiomes.AMETHYST_CHIMERIES));
        this.add(ModTags.Biomes.HAS_CRIMSONVEIL_TREE, List.of(
                ModBiomes.CRIMSONVEIL_MIRE,
                ModBiomes.CRIMSONVEIL_WOODS,
                ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS,
                ModBiomes.CRIMSONVEIL_PLATEAU));
        this.add(ModTags.Biomes.SPAWNS_STEPPE_SABERPARD, List.of(
                ModBiomes.DARK_WASTELAND,
                ModBiomes.GRIZZLY_HIGHLANDS,
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.PENUMBRAL_STEPPE));
        this.add(ModTags.Biomes.SPAWNS_WARM_TUSKHOG, List.of(
                ModBiomes.DARK_WASTELAND,
                ModBiomes.GLOOMWEED_PLAINS,
                ModBiomes.PENUMBRAL_STEPPE,
                ModBiomes.CRIMSONVEIL_WOODS,
                ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS));
    }

    private void add(TagKey<Biome> tag, List<ResourceKey<Biome>> biomes) {
        TagAppender<Biome> appender = this.tag(tag);
        for (ResourceKey<Biome> biome : biomes) {
            appender.add(biome);
        }
    }
}