package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class UnseenWorldBlockTagProvider extends BlockTagsProvider {
    public UnseenWorldBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, UnseenWorldMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Blocks.CHESTS).replace(false).add(
                UnseenWorldModBlocks.GOLDENCHEST.get()
        );
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).replace(false).add(
                UnseenWorldModBlocks.REGISTRY.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("chlorite")
                                || blockRegistryObject.get().getDescriptionId().contains("currantslate")
                                || blockRegistryObject.get().getDescriptionId().contains("reddeepslate")
                                || blockRegistryObject.get().getDescriptionId().contains("tanzanite")
                                || blockRegistryObject.get().getDescriptionId().contains("darkness_ancient"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(Tags.Blocks.ORES).replace(false).add(
                UnseenWorldModBlocks.REGISTRY.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("ore"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(BlockTags.SAND).add(
                UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND.get(),
                UnseenWorldModBlocks.RED_OOZE.get()
        );
        this.tag(BlockTags.DIRT).add(
                UnseenWorldModBlocks.DARK_GRASS.get(),
                UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(),
                UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()
        );
        this.tag(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS).add(
                UnseenWorldModBlocks.DARK_GRASS.get(),
                UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(),
                UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()
        );
        this.tag(UnseenWorldModTags.Blocks.STONE_THE_DARKNESS).add(
                UnseenWorldModBlocks.CHLORITE_SLATE_STONE.get(),
                UnseenWorldModBlocks.DARK_CURRANTSLATE.get(),
                Blocks.DEEPSLATE,
                Blocks.BLACKSTONE,
                Blocks.CALCITE
        );
    }
}
