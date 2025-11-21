package net.sashakyotoz.common.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.tag.vanilla.VanillaBiomeTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.BiomeKeys;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends VanillaBiomeTagProvider {
    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ModTags.Biomes.TITANIUM_SPAWNS_IN).add(
                ModBiomes.DEEP_GLACIEMITE_CAVES,
                ModBiomes.TANZANITE_CAVES,
                ModBiomes.THE_DARKNESS
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.ABYSSAL_SPAWNS_IN).add(
                ModBiomes.THE_DARKNESS,
                ModBiomes.TANZANITE_CAVES,
                ModBiomes.DEEP_GLACIEMITE_CAVES,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.UNSEENIUM_SPAWNS_IN).add(
                ModBiomes.THE_DARKNESS,
                ModBiomes.TANZANITE_CAVES,
                ModBiomes.SHINY_CAVERNS,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.FLOWERS_COVERED).add(
                ModBiomes.GREENISH_MEADOW,
                ModBiomes.GREENISH_VALLEY,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.WATER_LAKE_SPAWNS_ON).add(
                ModBiomes.BURLYWOOD_JUNGLE,
                ModBiomes.GREENISH_VALLEY
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.DARK_WATER_LAKE_SPAWNS_ON).add(
                ModBiomes.AMETHYST_CHIMERIES,
                ModBiomes.DARK_WASTELAND,
                ModBiomes.THE_DARKNESS,
                ModBiomes.CRIMSONVEIL_PLATEAU
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.GLACIEMITE_BOULDER_SPAWNS_ON).add(
                ModBiomes.TEALIVY_VALLEY,
                ModBiomes.TEALIVY_HIGHLANDS,
                ModBiomes.THE_DARKNESS,
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON).add(
                ModBiomes.AMETHYST_CHIMERIES,
                ModBiomes.CURRANTSLATE_PEAKS,
                ModBiomes.GRIZZLY_HIGHLANDS,
                ModBiomes.DARK_WASTELAND,
                ModBiomes.GREENISH_MEADOW
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_BURLYWOOD_TREE).add(
                ModBiomes.BURLYWOOD_JUNGLE,
                ModBiomes.GREENISH_VALLEY,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_AMETHYST_TREE).add(
                ModBiomes.AMETHYST_FOREST,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_CRIMSONVEIL_TREE).add(
                ModBiomes.CRIMSONVEIL_WOODS,
                ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_FROST_PORTAL).add(
                BiomeKeys.SNOWY_PLAINS,
                BiomeKeys.SNOWY_TAIGA,
                BiomeKeys.SNOWY_SLOPES
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_GLEAMCARVER).add(
                ModBiomes.TANZANITE_CAVES,
                ModBiomes.DEEP_GLACIEMITE_CAVES,
                ModBiomes.SHINY_CAVERNS
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_HARMONY_WATCHER).add(
                ModBiomes.CRIMSONVEIL_WOODS,
                ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS,
                ModBiomes.AMETHYST_CHIMERIES,
                ModBiomes.TEALIVY_VALLEY,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_SABERPARD).add(
                ModBiomes.DARK_WASTELAND,
                ModBiomes.GRIZZLY_HIGHLANDS,
                ModBiomes.GRIZZLY_THICKET,
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.HARMONY_GROVE,
                ModBiomes.GREENISH_VALLEY,
                ModBiomes.BURLYWOOD_JUNGLE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_TUSKHOG).add(
                ModBiomes.GREENISH_MEADOW,
                ModBiomes.GREENISH_VALLEY,
                ModBiomes.TEALIVY_VALLEY,
                ModBiomes.HARMONY_GROVE,
                ModBiomes.DARK_WASTELAND,
                ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS,
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.CRIMSONVEIL_WOODS
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.SPAWNS_STEPPE_SABERPARD).add(
                ModBiomes.DARK_WASTELAND,
                ModBiomes.GRIZZLY_HIGHLANDS,
                ModBiomes.CRIMSONVEIL_PLATEAU
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.SPAWNS_WARM_TUSKHOG).add(
                ModBiomes.GREENISH_MEADOW,
                ModBiomes.GREENISH_VALLEY,
                ModBiomes.TEALIVY_VALLEY,
                ModBiomes.HARMONY_GROVE
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.HAS_MODIFIED_SURFACE).add(
                ModBiomes.TEALIVY_VALLEY,
                ModBiomes.GREENISH_VALLEY,
                ModBiomes.DARK_BADLANDS
        );
    }
}