package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.GroupEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.predicate.StatePredicate;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.plants.TreeBushLikeBlock;
import net.sashakyotoz.common.items.ModItems;

import java.util.Map;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    @Override
    public void generate() {
        for (Map.Entry<Block, ItemConvertible> entry : ModRegistry.BLOCK_DROPS.entrySet()) {
            if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB) != null && ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB).contains(entry.getKey()))
                addDrop(entry.getKey(), slabDrops(entry.getKey()));
            else if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR) != null && ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR).contains(entry.getKey()))
                addDrop(entry.getKey(), doorDrops(entry.getKey()));
            else if (ModRegistry.BLOCK_SILK_DROPS.containsKey(entry.getKey()))
                oreDrops(entry.getKey(),entry.getValue().asItem());
            else
                addDrop(entry.getKey(), entry.getValue());
        }
        addDrop(ModBlocks.AMETHYST_LEAVES, leavesDrops(ModBlocks.AMETHYST_LEAVES, ModBlocks.AMETHYST_SAPLING, 0.1f));
        addDrop(ModBlocks.HANGING_AMETHYST_LEAVES,dropsWithSilkTouchOrShears(ModBlocks.HANGING_AMETHYST_LEAVES,
                new GroupEntry.Builder()));
        addDrop(ModBlocks.GRIZZLY_LEAVES, leavesDrops(ModBlocks.GRIZZLY_LEAVES, ModBlocks.GRIZZLY_SAPLING, 0.1f));
        addDrop(ModBlocks.CRIMSONVEIL_LEAVES, leavesDrops(ModBlocks.CRIMSONVEIL_LEAVES, ModBlocks.CRIMSONVEIL_SAPLING, 0.1f));
        addDrop(ModBlocks.TEALIVE_LEAVES, leavesDrops(ModBlocks.TEALIVE_LEAVES, ModBlocks.TEALIVE_SAPLING, 0.1f));
        addDrop(ModBlocks.BURLYWOOD_LEAVES, leavesDrops(ModBlocks.BURLYWOOD_LEAVES, ModBlocks.BURLYWOOD_SAPLING, 0.1f));

        addDropWithSilkTouch(ModBlocks.AMETHYST_PETALS);
        addDrop(ModBlocks.GLOW_APPLE_BUSH,glowAppleBushDrops(ModBlocks.GLOW_APPLE_BUSH));
        addDrop(ModBlocks.MIDNIGHT_LILY_PAD, dropsWithShears(ModBlocks.MIDNIGHT_LILY_PAD));
        oreDrops(ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE, ModItems.RAW_ABYSSAL_ORE).build();
        oreDrops(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE, ModItems.RAW_ABYSSAL_ORE).build();
        oreDrops(ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE, ModItems.RAW_UNSEENIUM_ORE).build();
        oreDrops(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE, ModItems.RAW_UNSEENIUM_ORE).build();
        oreDrops(ModBlocks.RED_TITANIUM_IN_GLACIEMITE, ModItems.RAW_RED_TITANIUM_ORE).build();
        oreDrops(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE, ModItems.RAW_RED_TITANIUM_ORE).build();

        addDrop(ModBlocks.AMETHYST_GRASS_BLOCK, block -> this.drops(block, ModBlocks.DARK_CURRANTSLATE));
        addDrop(ModBlocks.TEALIVY_GRASS_BLOCK, block -> this.drops(block, ModBlocks.GLACIEMITE));
        addDrop(ModBlocks.GRIMWOOD_GRASS_BLOCK, block -> this.drops(block, ModBlocks.DARK_CURRANTSLATE));
        addDrop(ModBlocks.NIGHTDARK_GRASS_BLOCK, block -> this.drops(block, Blocks.DIRT));
    }
    public static LootTable.Builder glowAppleBushDrops(Block drop) {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(ModItems.GLOW_APPLE))
                                .conditionally(BlockStatePropertyLootCondition.builder(drop).properties(StatePredicate.Builder.create().exactMatch(TreeBushLikeBlock.GROWN, true)))
                );
    }
}