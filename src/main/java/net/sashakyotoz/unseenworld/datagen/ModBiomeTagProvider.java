package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {
    public static final ResourceKey<Biome> DARK_OCEAN = ResourceKey.create(Registries.BIOME, new ResourceLocation(UnseenWorldMod.MODID, "dark_ocean"));
    public static final ResourceKey<Biome> DARK_LIFELESS_OCEAN = ResourceKey.create(Registries.BIOME, new ResourceLocation(UnseenWorldMod.MODID, "dark_lifeless_ocean"));
    public static final ResourceKey<Biome> GREYNISH_BEACH = ResourceKey.create(Registries.BIOME, new ResourceLocation(UnseenWorldMod.MODID, "greynish_beach"));

    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, future, UnseenWorldMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(UnseenWorldModTags.Biomes.ICEBERGS_BLACKLIST_BIOMES).add(
                DARK_OCEAN,
                DARK_LIFELESS_OCEAN,
                GREYNISH_BEACH
        );
    }
}
