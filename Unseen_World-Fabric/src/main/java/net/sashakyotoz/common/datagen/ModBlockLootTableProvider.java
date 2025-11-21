package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.util.math.BlockPos;
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
                addDrop(entry.getKey(), oreDrops(entry.getKey(), entry.getValue().asItem()));
            else if (ModRegistry.TALL_BLOCK_SHEARS_DROPS.containsKey(entry.getKey())) {
                if (entry.getKey() instanceof TallPlantBlock)
                    addDrop(entry.getKey(), tallGrassDrops(ModRegistry.TALL_BLOCK_SHEARS_DROPS.get(entry.getKey()).getRight(), ModRegistry.TALL_BLOCK_SHEARS_DROPS.get(entry.getKey()).getLeft()));
            } else if (ModRegistry.BLOCK_SHEARS_DROPS.containsKey(entry.getKey()))
                addDrop(entry.getKey(), dropsWithShears(entry.getValue()));
            else
                addDrop(entry.getKey(), entry.getValue());
        }
        addDrop(ModBlocks.AMETHYST_LEAVES, leavesDrops(ModBlocks.AMETHYST_LEAVES, ModBlocks.AMETHYST_SAPLING, 0.1f));
        addDrop(ModBlocks.HANGING_AMETHYST_LEAVES, dropsWithSilkTouchOrShears(ModBlocks.HANGING_AMETHYST_LEAVES,
                ItemEntry.builder(ModBlocks.HANGING_AMETHYST_LEAVES).conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, 0.25F, 0.5F, 0.75F, 1.0F))));
        addDrop(ModBlocks.GRIZZLY_LEAVES, leavesDrops(ModBlocks.GRIZZLY_LEAVES, ModBlocks.GRIZZLY_SAPLING, 0.1f));
        addDrop(ModBlocks.CRIMSONVEIL_LEAVES, leavesDrops(ModBlocks.CRIMSONVEIL_LEAVES, ModBlocks.CRIMSONVEIL_SAPLING, 0.1f));
        addDrop(ModBlocks.TEALIVE_LEAVES, leavesDrops(ModBlocks.TEALIVE_LEAVES, ModBlocks.TEALIVE_SAPLING, 0.1f));
        addDrop(ModBlocks.BURLYWOOD_LEAVES, leavesDrops(ModBlocks.BURLYWOOD_LEAVES, ModBlocks.BURLYWOOD_SAPLING, 0.1f));
        addDrop(ModBlocks.BURLYWOOD_HEART, dropsWithSilkTouch(ModBlocks.BURLYWOOD_HEART));

        addDropWithSilkTouch(ModBlocks.AMETHYST_PETALS);
        addDrop(ModBlocks.GLOW_APPLE_BUSH, glowAppleBushDrops(ModBlocks.GLOW_APPLE_BUSH));
        addDrop(ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE, block -> oreDrops(block, ModItems.RAW_ABYSSAL_ORE));
        addDrop(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE, block -> oreDrops(block, ModItems.RAW_ABYSSAL_ORE));
        addDrop(ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE, block -> oreDrops(block, ModItems.RAW_UNSEENIUM_ORE));
        addDrop(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE, block -> oreDrops(block, ModItems.RAW_UNSEENIUM_ORE));
        addDrop(ModBlocks.RED_TITANIUM_IN_GLACIEMITE, block -> oreDrops(block, ModItems.RAW_RED_TITANIUM_ORE));
        addDrop(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE, block -> oreDrops(block, ModItems.RAW_RED_TITANIUM_ORE));
        addDrop(ModBlocks.IRON_ORE_IN_GLACIEMITE, block -> dropsWithSilkTouch(
                block,
                this.applyExplosionDecay(
                        block,
                        ItemEntry.builder(Items.IRON_NUGGET)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F)))
                                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                )));
        addDrop(ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE, block -> dropsWithSilkTouch(
                block,
                this.applyExplosionDecay(
                        block,
                        ItemEntry.builder(Items.IRON_NUGGET)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F)))
                                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                )));
        addDrop(ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE, block -> drops(block, Items.NETHERITE_SCRAP));
        addDrop(ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE, block -> drops(block, Items.NETHERITE_SCRAP));

        addDrop(ModBlocks.AMETHYST_GRASS_BLOCK, block -> this.drops(block, ModBlocks.DARK_CURRANTSLATE));
        addDrop(ModBlocks.TEALIVY_GRASS_BLOCK, block -> this.drops(block, ModBlocks.TEALIVY_DIRT));
        addDrop(ModBlocks.GRIMWOOD_GRASS_BLOCK, block -> this.drops(block, ModBlocks.DARK_CURRANTSLATE));
        addDrop(ModBlocks.NIGHTDARK_GRASS_BLOCK, block -> this.drops(block, ModBlocks.NIGHTDARK_DIRT));
    }

    public static LootTable.Builder glowAppleBushDrops(Block drop) {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(ModItems.GLOW_APPLE))
                                .conditionally(BlockStatePropertyLootCondition.builder(drop).properties(StatePredicate.Builder.create().exactMatch(TreeBushLikeBlock.GROWN, true)))
                );
    }

    public LootTable.Builder tallGrassDrops(Block tallGrass, Block grass) {
        LootPoolEntry.Builder<?> builder = ItemEntry.builder(grass)
                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                .conditionally(WITH_SHEARS)
                .alternatively(
                        ((LeafEntry.Builder<?>) this.addSurvivesExplosionCondition(tallGrass, ItemEntry.builder(Items.GLOWSTONE_DUST)))
                                .conditionally(RandomChanceLootCondition.builder(0.1F))
                );
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .with(builder)
                                .conditionally(
                                        BlockStatePropertyLootCondition.builder(tallGrass).properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER))
                                )
                                .conditionally(
                                        LocationCheckLootCondition.builder(
                                                LocationPredicate.Builder.create()
                                                        .block(
                                                                BlockPredicate.Builder.create()
                                                                        .blocks(tallGrass)
                                                                        .state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER).build())
                                                                        .build()
                                                        ),
                                                new BlockPos(0, 1, 0)
                                        )
                                )
                )
                .pool(
                        LootPool.builder()
                                .with(builder)
                                .conditionally(
                                        BlockStatePropertyLootCondition.builder(tallGrass).properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER))
                                )
                                .conditionally(
                                        LocationCheckLootCondition.builder(
                                                LocationPredicate.Builder.create()
                                                        .block(
                                                                BlockPredicate.Builder.create()
                                                                        .blocks(tallGrass)
                                                                        .state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER).build())
                                                                        .build()
                                                        ),
                                                new BlockPos(0, -1, 0)
                                        )
                                )
                );
    }
}