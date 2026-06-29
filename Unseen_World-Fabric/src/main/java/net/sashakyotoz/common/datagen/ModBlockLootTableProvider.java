package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
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
        for (Map.Entry<Block, ItemLike> entry : ModRegistry.BLOCK_DROPS.entrySet()) {
            if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB) != null && ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB).contains(entry.getKey()))
                add(entry.getKey(), createSlabItemTable(entry.getKey()));
            else if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR) != null && ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR).contains(entry.getKey()))
                add(entry.getKey(), createDoorTable(entry.getKey()));
            else if (ModRegistry.BLOCK_SILK_DROPS.containsKey(entry.getKey()))
                add(entry.getKey(), createOreDrop(entry.getKey(), entry.getValue().asItem()));
            else if (ModRegistry.TALL_BLOCK_SHEARS_DROPS.containsKey(entry.getKey())) {
                if (entry.getKey() instanceof DoublePlantBlock)
                    add(entry.getKey(), createDoublePlantWithSeedDrops(ModRegistry.TALL_BLOCK_SHEARS_DROPS.get(entry.getKey()).getB(), ModRegistry.TALL_BLOCK_SHEARS_DROPS.get(entry.getKey()).getA()));
            } else if (ModRegistry.BLOCK_SHEARS_DROPS.containsKey(entry.getKey()))
                add(entry.getKey(), createShearsOnlyDrop(entry.getValue()));
            else
                dropOther(entry.getKey(), entry.getValue());
        }
        add(ModBlocks.AMETHYST_LEAVES, createLeavesDrops(ModBlocks.AMETHYST_LEAVES, ModBlocks.AMETHYST_SAPLING, 0.1f));
        add(ModBlocks.HANGING_AMETHYST_LEAVES, createSilkTouchOrShearsDispatchTable(ModBlocks.HANGING_AMETHYST_LEAVES,
                LootItem.lootTableItem(ModBlocks.HANGING_AMETHYST_LEAVES).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.25F, 0.5F, 0.75F, 1.0F))));
        add(ModBlocks.GRIZZLY_LEAVES, createLeavesDrops(ModBlocks.GRIZZLY_LEAVES, ModBlocks.GRIZZLY_SAPLING, 0.1f));
        add(ModBlocks.CRIMSONVEIL_LEAVES, createLeavesDrops(ModBlocks.CRIMSONVEIL_LEAVES, ModBlocks.CRIMSONVEIL_SAPLING, 0.1f));
        add(ModBlocks.TEALIVE_LEAVES, createLeavesDrops(ModBlocks.TEALIVE_LEAVES, ModBlocks.TEALIVE_SAPLING, 0.1f));
        add(ModBlocks.BURLYWOOD_LEAVES, createLeavesDrops(ModBlocks.BURLYWOOD_LEAVES, ModBlocks.BURLYWOOD_SAPLING, 0.1f));
        add(ModBlocks.BURLYWOOD_HEART, createSilkTouchOnlyTable(ModBlocks.BURLYWOOD_HEART));

        dropWhenSilkTouch(ModBlocks.AMETHYST_PETALS);
        add(ModBlocks.GLOW_APPLE_BUSH, glowAppleBushDrops(ModBlocks.GLOW_APPLE_BUSH));
        add(ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE, block -> createOreDrop(block, ModItems.RAW_ABYSSAL_ORE));
        add(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE, block -> createOreDrop(block, ModItems.RAW_ABYSSAL_ORE));
        add(ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE, block -> createOreDrop(block, ModItems.RAW_UNSEENIUM_ORE));
        add(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE, block -> createOreDrop(block, ModItems.RAW_UNSEENIUM_ORE));
        add(ModBlocks.RED_TITANIUM_IN_GLACIEMITE, block -> createOreDrop(block, ModItems.RAW_RED_TITANIUM_ORE));
        add(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE, block -> createOreDrop(block, ModItems.RAW_RED_TITANIUM_ORE));
        add(ModBlocks.IRON_ORE_IN_GLACIEMITE, block -> createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(Items.IRON_NUGGET)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                )));
        add(ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE, block -> createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(Items.IRON_NUGGET)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                )));
        add(ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE, block -> createSingleItemTableWithSilkTouch(block, Items.NETHERITE_SCRAP));
        add(ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE, block -> createSingleItemTableWithSilkTouch(block, Items.NETHERITE_SCRAP));

        add(ModBlocks.AMETHYST_GRASS_BLOCK, block -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.DARK_CURRANTSLATE));
        add(ModBlocks.TEALIVY_GRASS_BLOCK, block -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.TEALIVY_DIRT));
        add(ModBlocks.GRIMWOOD_GRASS_BLOCK, block -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.DARK_CURRANTSLATE));
        add(ModBlocks.NIGHTDARK_GRASS_BLOCK, block -> this.createSingleItemTableWithSilkTouch(block, ModBlocks.NIGHTDARK_DIRT));
    }

    public static LootTable.Builder glowAppleBushDrops(Block drop) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .add(LootItem.lootTableItem(ModItems.GLOW_APPLE))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TreeBushLikeBlock.GROWN, true)))
                );
    }

    public LootTable.Builder createDoublePlantWithSeedDrops(Block tallGrass, Block grass) {
        LootPoolEntryContainer.Builder<?> builder = LootItem.lootTableItem(grass)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                .when(HAS_SHEARS)
                .otherwise(
                        ((LootPoolSingletonContainer.Builder<?>) this.applyExplosionCondition(tallGrass, LootItem.lootTableItem(Items.GLOWSTONE_DUST)))
                                .when(LootItemRandomChanceCondition.randomChance(0.1F))
                );
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .add(builder)
                                .when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(tallGrass).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))
                                )
                                .when(
                                        LocationCheck.checkLocation(
                                                LocationPredicate.Builder.location()
                                                        .setBlock(
                                                                BlockPredicate.Builder.block()
                                                                        .of(tallGrass)
                                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).build())
                                                                        .build()
                                                        ),
                                                new BlockPos(0, 1, 0)
                                        )
                                )
                )
                .withPool(
                        LootPool.lootPool()
                                .add(builder)
                                .when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(tallGrass).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))
                                )
                                .when(
                                        LocationCheck.checkLocation(
                                                LocationPredicate.Builder.location()
                                                        .setBlock(
                                                                BlockPredicate.Builder.block()
                                                                        .of(tallGrass)
                                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).build())
                                                                        .build()
                                                        ),
                                                new BlockPos(0, -1, 0)
                                        )
                                )
                );
    }
}