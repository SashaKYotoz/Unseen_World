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
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, UnseenWorldMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Blocks.CHESTS).replace(false).add(
                UnseenWorldModBlocks.GOLDEN_CHEST.get()
        );
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(UnseenWorldModBlocks.DEEP_GEM_ORE.get());
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).replace(false).add(
                UnseenWorldModBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("chlorite")
                                || blockRegistryObject.get().getDescriptionId().contains("currantslate")
                                || blockRegistryObject.get().getDescriptionId().contains("reddeepslate")
                                || blockRegistryObject.get().getDescriptionId().contains("tanzanite")
                                || blockRegistryObject.get().getDescriptionId().contains("darkness_ancient"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).replace(false).add(
                UnseenWorldModBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("ore")
                                && !blockRegistryObject.get().getDescriptionId().contains("deep_gem"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(Tags.Blocks.ORES).replace(false).add(
                UnseenWorldModBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("ore"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(BlockTags.SAND).add(
                UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND.get(),
                UnseenWorldModBlocks.RED_OOZE.get()
        );
        this.tag(BlockTags.DIRT).add(
                UnseenWorldModBlocks.DARK_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(),
                UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()
        );
        this.tag(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS).add(
                UnseenWorldModBlocks.DARK_GRASS_BLOCK.get(),
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
        this.tag(UnseenWorldModTags.Blocks.DRIPSTONE_OF_AMETHYST_CAN_GROW_ON).add(
          UnseenWorldModBlocks.DARK_CURRANTSLATE.get(),
          UnseenWorldModBlocks.CHLORITE_SLATE_STONE.get(),
          UnseenWorldModBlocks.TANZANITE_BLOCK.get(),
          UnseenWorldModBlocks.AMETHYST_LEAVES.get(),
          Blocks.AMETHYST_BLOCK
        );
    }
}
