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
        this.tag(BlockTags.LEAVES).replace(false).add(
                UnseenWorldModBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("leaves"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)
        );
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).replace(false).add(UnseenWorldModBlocks.DEEP_GEM_ORE.get());
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
        this.tag(BlockTags.MINEABLE_WITH_AXE).replace(false).add(
                UnseenWorldModBlocks.BLOCKS.getEntries().stream()
                        .filter(blockRegistryObject -> blockRegistryObject.get().getDescriptionId().contains("log")
                                || blockRegistryObject.get().getDescriptionId().contains("button")
                                || blockRegistryObject.get().getDescriptionId().contains("fence")
                                || blockRegistryObject.get().getDescriptionId().contains("planks")
                                || blockRegistryObject.get().getDescriptionId().contains("door")
                                || blockRegistryObject.get().getDescriptionId().contains("pressure_plate"))
                        .map(RegistryObject::get)
                        .toArray(Block[]::new)).add(
                UnseenWorldModBlocks.TEALIVY_WOOD.get(),
                UnseenWorldModBlocks.GRIZZLY_WOOD.get(),
                UnseenWorldModBlocks.DARK_CRIMSON_WOOD.get(),
                UnseenWorldModBlocks.AMETHYST_WOOD.get(),
                UnseenWorldModBlocks.GREENISH_BURLY_WOOD_BLOCK.get(),
                UnseenWorldModBlocks.TEALIVY_SLAB.get(),
                UnseenWorldModBlocks.GREENISH_BURLY_WOOD_SLAB.get(),
                UnseenWorldModBlocks.AMETHYST_SLAB.get(),
                UnseenWorldModBlocks.GRIZZLY_SLAB.get(),
                UnseenWorldModBlocks.DARK_CRIMSON_SLAB.get(),
                UnseenWorldModBlocks.GREENISH_BURLY_WOOD_STAIRS.get(),
                UnseenWorldModBlocks.GRIZZLY_STAIRS.get(),
                UnseenWorldModBlocks.AMETHYST_STAIRS.get(),
                UnseenWorldModBlocks.TEALIVY_STAIRS.get(),
                UnseenWorldModBlocks.DARK_CRIMSON_STAIRS.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_HOE).replace(false).add(
          UnseenWorldModBlocks.DARK_CRIMSON_FLOWING_AZALEA.get(),
          UnseenWorldModBlocks.DARK_CRIMSON_AZALEA.get(),
          UnseenWorldModBlocks.WILD_MISTERY_FLOWER_BERRIES.get(),
          UnseenWorldModBlocks.WILD_CRIMSERRY_SOUL_FLOWER.get(),
          UnseenWorldModBlocks.MISTERY_CROP_FLOWER.get(),
          UnseenWorldModBlocks.TEALIVY_JADE_VINE_FLOWER.get(),
          UnseenWorldModBlocks.CRIMSERRY_SOUL_CROP.get(),
          UnseenWorldModBlocks.TEALIVY_PLUMERIA.get(),
          UnseenWorldModBlocks.GLOWORCHID.get(),
          UnseenWorldModBlocks.DEEP_WATER_ANFELTSIA.get(),
          UnseenWorldModBlocks.TANZASHROOM.get(),
          UnseenWorldModBlocks.TANZASHROOM_BLOCK.get(),
          UnseenWorldModBlocks.TANZASHROOM_STEM.get(),
          UnseenWorldModBlocks.AMETHYST_GRASS.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).replace(false).add(
                UnseenWorldModBlocks.BLOCKS.getEntries().stream()
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
                UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(),
                UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.BEACON_OF_WEAPONS.get(),
                UnseenWorldModBlocks.BEACON_RUNE.get(),
                UnseenWorldModBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get(),
                UnseenWorldModBlocks.GOLDEN_CHEST.get(),
                UnseenWorldModBlocks.TOTEM_OF_GUDDY_BLAZE.get(),
                UnseenWorldModBlocks.UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS.get(),
                UnseenWorldModBlocks.BLAZER_SUMMON_BLOCK.get(),
                UnseenWorldModBlocks.THE_WITHER_KNIGHT_BLOCK.get(),
                UnseenWorldModBlocks.DEEP_GEM_BLOCK.get(),
                UnseenWorldModBlocks.UNSEEN_BLOCK.get(),
                UnseenWorldModBlocks.NATURERIUM_BLOCK.get(),
                UnseenWorldModBlocks.VOID_INGOT_BLOCK.get(),
                UnseenWorldModBlocks.RED_TITANIUM_BLOCK.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).replace(false).add(
                UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND.get(),
                UnseenWorldModBlocks.RED_OOZE.get(),
                UnseenWorldModBlocks.DARK_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()
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
        this.tag(BlockTags.SAND).replace(false).add(
                UnseenWorldModBlocks.CRYSTALLIZED_DARK_SAND.get(),
                UnseenWorldModBlocks.RED_OOZE.get()
        );
        this.tag(BlockTags.DIRT).replace(false).addTag(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS);
        this.tag(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS).add(
                UnseenWorldModBlocks.DARK_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.AMETHYST_GRASS_BLOCK.get(),
                UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get(),
                UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()
        );
        this.tag(UnseenWorldModTags.Blocks.STONE_THE_DARKNESS).replace(false).add(
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
