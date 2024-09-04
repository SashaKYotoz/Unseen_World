package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.tag.vanilla.VanillaBiomeTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.biomes.ModBiomes;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends VanillaBiomeTagProvider {
    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ModTags.Biomes.AMETHYST_LIKE_BIOMES).add(
                ModBiomes.AMETHYST_FOREST,
                ModBiomes.AMETHYST_CHIMERIES
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.GREENISH_LIKE_BIOMES).add(
                ModBiomes.GREENISH_MEADOW,
                ModBiomes.GREENISH_VALLEY
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.TEALIVE_LIKE_BIOMES).add(
                ModBiomes.TEALIVY_VALLEY,
<<<<<<< Updated upstream
                ModBiomes.TEALIVY_VALLEY
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.DARK_WATERFUL_BIOMES).add(
                ModBiomes.DARK_RIVER,
=======
                ModBiomes.TEALIVY_HIGHLANDS
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.DARK_WATERFUL_BIOMES).add(
                ModBiomes.DARK_RIVER,
                ModBiomes.LUKEWARM_DARK_RIVER,
>>>>>>> Stashed changes
                ModBiomes.DARK_OCEAN,
                ModBiomes.DARK_LIFELESS_OCEAN
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.NIGHTDARK_GRASS_COVERED_BIOMES).add(
                ModBiomes.CRIMSONVEIL_PLATEAU,
                ModBiomes.CRIMSONVEIL_WOODS,
                ModBiomes.DARK_WASTELAND
        );
        this.getOrCreateTagBuilder(ModTags.Biomes.GLACIEMITE_MAIN_STONE_BIOMES).add(
                ModBiomes.TEALIVY_VALLEY,
                ModBiomes.TEALIVY_HIGHLANDS,
                ModBiomes.DARK_WASTELAND,
                ModBiomes.THE_DARKNESS
        );
    }
}