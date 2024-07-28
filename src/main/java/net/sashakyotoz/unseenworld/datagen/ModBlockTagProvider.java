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
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, UnseenWorldMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Blocks.CHESTS).replace(false).add(
                UnseenWorldBlocks.GOLDEN_CHEST.get()
        );
        this.tag(BlockTags.LEAVES).replace(false).add(
                UnseenWorldBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("leaves"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).replace(false).add(UnseenWorldBlocks.DEEP_GEM_ORE.get());
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).replace(false).add(
                UnseenWorldBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("chlorite")
                                || blockRegistryObject.get().getDescriptionId().contains("currantslate")
                                || blockRegistryObject.get().getDescriptionId().contains("reddeepslate")
                                || blockRegistryObject.get().getDescriptionId().contains("tanzanite")
                                || blockRegistryObject.get().getDescriptionId().contains("darkness_ancient"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(BlockTags.MINEABLE_WITH_AXE).replace(false).add(
                UnseenWorldBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("log")
                                || blockRegistryObject.get().getDescriptionId().contains("button")
                                || blockRegistryObject.get().getDescriptionId().contains("fence")
                                || blockRegistryObject.get().getDescriptionId().contains("planks")
                                || blockRegistryObject.get().getDescriptionId().contains("door")
                                || blockRegistryObject.get().getDescriptionId().contains("pressure_plate"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)).add(
                UnseenWorldBlocks.TEALIVY_WOOD.get(),
                UnseenWorldBlocks.GRIZZLY_WOOD.get(),
                UnseenWorldBlocks.DARK_CRIMSON_WOOD.get(),
                UnseenWorldBlocks.AMETHYST_WOOD.get(),
                UnseenWorldBlocks.GREENISH_BURLY_WOOD_BLOCK.get(),
                UnseenWorldBlocks.TEALIVY_SLAB.get(),
                UnseenWorldBlocks.GREENISH_BURLY_WOOD_SLAB.get(),
                UnseenWorldBlocks.AMETHYST_SLAB.get(),
                UnseenWorldBlocks.GRIZZLY_SLAB.get(),
                UnseenWorldBlocks.DARK_CRIMSON_SLAB.get(),
                UnseenWorldBlocks.GREENISH_BURLY_WOOD_STAIRS.get(),
                UnseenWorldBlocks.GRIZZLY_STAIRS.get(),
                UnseenWorldBlocks.AMETHYST_STAIRS.get(),
                UnseenWorldBlocks.TEALIVY_STAIRS.get(),
                UnseenWorldBlocks.DARK_CRIMSON_STAIRS.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_HOE).replace(false).add(
          UnseenWorldBlocks.DARK_CRIMSON_FLOWING_AZALEA.get(),
          UnseenWorldBlocks.DARK_CRIMSON_AZALEA.get(),
          UnseenWorldBlocks.WILD_MISTERY_FLOWER_BERRIES.get(),
          UnseenWorldBlocks.WILD_CRIMSERRY_SOUL_FLOWER.get(),
          UnseenWorldBlocks.MISTERY_CROP_FLOWER.get(),
          UnseenWorldBlocks.TEALIVY_JADE_VINE_FLOWER.get(),
          UnseenWorldBlocks.CRIMSERRY_SOUL_CROP.get(),
          UnseenWorldBlocks.TEALIVY_PLUMERIA.get(),
          UnseenWorldBlocks.GLOWORCHID.get(),
          UnseenWorldBlocks.DEEP_WATER_ANFELTSIA.get(),
          UnseenWorldBlocks.TANZASHROOM.get(),
          UnseenWorldBlocks.TANZASHROOM_BLOCK.get(),
          UnseenWorldBlocks.TANZASHROOM_STEM.get(),
          UnseenWorldBlocks.AMETHYST_GRASS.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).replace(false).add(
                UnseenWorldBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("chlorite")
                                || blockRegistryObject.get().getDescriptionId().contains("currantslate")
                                || blockRegistryObject.get().getDescriptionId().contains("reddeepslate")
                                || blockRegistryObject.get().getDescriptionId().contains("tanzanite")
                                || blockRegistryObject.get().getDescriptionId().contains("darkness_ancient")
                                || blockRegistryObject.get().getDescriptionId().contains("cold")
                                || blockRegistryObject.get().getDescriptionId().contains("ancient_transient")
                                || blockRegistryObject.get().getDescriptionId().contains("ore")
                                || blockRegistryObject.get().getDescriptionId().contains("reinforced"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        ).add(
                UnseenWorldBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(),
                UnseenWorldBlocks.AMETHYST_GRASS_BLOCK.get(),
                UnseenWorldBlocks.BEACON_OF_WEAPONS.get(),
                UnseenWorldBlocks.BEACON_RUNE.get(),
                UnseenWorldBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get(),
                UnseenWorldBlocks.GOLDEN_CHEST.get(),
                UnseenWorldBlocks.TOTEM_OF_GUDDY_BLAZE.get(),
                UnseenWorldBlocks.UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS.get(),
                UnseenWorldBlocks.BLAZER_SUMMON_BLOCK.get(),
                UnseenWorldBlocks.THE_WITHER_KNIGHT_BLOCK.get(),
                UnseenWorldBlocks.DEEP_GEM_BLOCK.get(),
                UnseenWorldBlocks.UNSEEN_BLOCK.get(),
                UnseenWorldBlocks.NATURERIUM_BLOCK.get(),
                UnseenWorldBlocks.VOID_INGOT_BLOCK.get(),
                UnseenWorldBlocks.RED_TITANIUM_BLOCK.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).replace(false).add(
                UnseenWorldBlocks.CRYSTALLIZED_DARK_SAND.get(),
                UnseenWorldBlocks.RED_OOZE.get(),
                UnseenWorldBlocks.DARK_GRASS_BLOCK.get(),
                UnseenWorldBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()
        );
        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).replace(false).add(
                UnseenWorldBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("ore")
                                && !blockRegistryObject.get().getDescriptionId().contains("deep_gem"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(Tags.Blocks.ORES).replace(false).add(
                UnseenWorldBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("ore"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(BlockTags.SAND).replace(false).add(
                UnseenWorldBlocks.CRYSTALLIZED_DARK_SAND.get(),
                UnseenWorldBlocks.RED_OOZE.get()
        );
        this.tag(BlockTags.DIRT).replace(false).addTag(UnseenWorldTags.Blocks.DIRT_THE_DARKNESS);
        this.tag(UnseenWorldTags.Blocks.DIRT_THE_DARKNESS).add(
                UnseenWorldBlocks.DARK_GRASS_BLOCK.get(),
                UnseenWorldBlocks.AMETHYST_GRASS_BLOCK.get(),
                UnseenWorldBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(),
                UnseenWorldBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()
        );
        this.tag(UnseenWorldTags.Blocks.STONE_THE_DARKNESS).replace(false).add(
                UnseenWorldBlocks.CHLORITE_SLATE_STONE.get(),
                UnseenWorldBlocks.DARK_CURRANTSLATE.get(),
                Blocks.DEEPSLATE,
                Blocks.BLACKSTONE,
                Blocks.CALCITE
        );
        this.tag(UnseenWorldTags.Blocks.DRIPSTONE_OF_AMETHYST_CAN_GROW_ON).add(
          UnseenWorldBlocks.DARK_CURRANTSLATE.get(),
          UnseenWorldBlocks.CHLORITE_SLATE_STONE.get(),
          UnseenWorldBlocks.TANZANITE_BLOCK.get(),
          UnseenWorldBlocks.AMETHYST_LEAVES.get(),
          Blocks.AMETHYST_BLOCK
        );
    }
}
