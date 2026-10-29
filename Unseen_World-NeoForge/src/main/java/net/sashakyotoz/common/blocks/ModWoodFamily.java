package net.sashakyotoz.common.blocks;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;

import java.util.List;

public record ModWoodFamily(Block planks, Block log, Block wood, Block strippedLog, Block strippedWood,
                            Block door, Block trapdoor,
                            Block sign, Block wallSign, Item signItem,
                            Block hangingSign, Block wallHangingSign, Item hangingSignItem,
                            TagKey<Item> logs) {
    public static final List<ModWoodFamily> ALL = List.of(
            new ModWoodFamily(ModBlocks.AMETHYST_PLANKS, ModBlocks.AMETHYST_LOG, ModBlocks.AMETHYST_WOOD, ModBlocks.STRIPPED_AMETHYST_LOG, ModBlocks.STRIPPED_AMETHYST_WOOD,
                    ModBlocks.AMETHYST_DOOR, ModBlocks.AMETHYST_TRAPDOOR,
                    ModItems.AMETHYST_SIGN, ModItems.AMETHYST_WALL_SIGN, ModItems.AMETHYST_SIGN_ITEM,
                    ModItems.AMETHYST_HANGING_SIGN, ModItems.AMETHYST_WALL_HANGING_SIGN, ModItems.AMETHYST_HANGING_SIGN_ITEM,
                    ModTags.Items.AMETHYST_LOGS),
            new ModWoodFamily(ModBlocks.GRIZZLY_PLANKS, ModBlocks.GRIZZLY_LOG, ModBlocks.GRIZZLY_WOOD, ModBlocks.STRIPPED_GRIZZLY_LOG, ModBlocks.STRIPPED_GRIZZLY_WOOD,
                    ModBlocks.GRIZZLY_DOOR, ModBlocks.GRIZZLY_TRAPDOOR,
                    ModItems.GRIZZLY_SIGN, ModItems.GRIZZLY_WALL_SIGN, ModItems.GRIZZLY_SIGN_ITEM,
                    ModItems.GRIZZLY_HANGING_SIGN, ModItems.GRIZZLY_WALL_HANGING_SIGN, ModItems.GRIZZLY_HANGING_SIGN_ITEM,
                    ModTags.Items.GRIZZLY_LOGS),
            new ModWoodFamily(ModBlocks.TEALIVE_PLANKS, ModBlocks.TEALIVE_LOG, ModBlocks.TEALIVE_WOOD, ModBlocks.STRIPPED_TEALIVE_LOG, ModBlocks.STRIPPED_TEALIVE_WOOD,
                    ModBlocks.TEALIVE_DOOR, ModBlocks.TEALIVE_TRAPDOOR,
                    ModItems.TEALIVE_SIGN, ModItems.TEALIVE_WALL_SIGN, ModItems.TEALIVE_SIGN_ITEM,
                    ModItems.TEALIVE_HANGING_SIGN, ModItems.TEALIVE_WALL_HANGING_SIGN, ModItems.TEALIVE_HANGING_SIGN_ITEM,
                    ModTags.Items.TEALIVE_LOGS),
            new ModWoodFamily(ModBlocks.BURLYWOOD_PLANKS, ModBlocks.BURLYWOOD_LOG, ModBlocks.BURLYWOOD_WOOD, ModBlocks.STRIPPED_BURLYWOOD_LOG, ModBlocks.STRIPPED_BURLYWOOD_WOOD,
                    ModBlocks.BURLYWOOD_DOOR, ModBlocks.BURLYWOOD_TRAPDOOR,
                    ModItems.BURLYWOOD_SIGN, ModItems.BURLYWOOD_WALL_SIGN, ModItems.BURLYWOOD_SIGN_ITEM,
                    ModItems.BURLYWOOD_HANGING_SIGN, ModItems.BURLYWOOD_WALL_HANGING_SIGN, ModItems.BURLYWOOD_HANGING_SIGN_ITEM,
                    ModTags.Items.BURLYWOOD_LOGS),
            new ModWoodFamily(ModBlocks.CRIMSONVEIL_PLANKS, ModBlocks.CRIMSONVEIL_LOG, ModBlocks.CRIMSONVEIL_WOOD, ModBlocks.STRIPPED_CRIMSONVEIL_LOG, ModBlocks.STRIPPED_CRIMSONVEIL_WOOD,
                    ModBlocks.CRIMSONVEIL_DOOR, ModBlocks.CRIMSONVEIL_TRAPDOOR,
                    ModItems.CRIMSONVEIL_SIGN, ModItems.CRIMSONVEIL_WALL_SIGN, ModItems.CRIMSONVEIL_SIGN_ITEM,
                    ModItems.CRIMSONVEIL_HANGING_SIGN, ModItems.CRIMSONVEIL_WALL_HANGING_SIGN, ModItems.CRIMSONVEIL_HANGING_SIGN_ITEM,
                    ModTags.Items.CRIMSONVEIL_LOGS)
    );
}
