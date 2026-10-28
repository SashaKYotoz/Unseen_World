package net.sashakyotoz.common.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.lcc.sollib.datagen.SBlockLootTableProvider;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
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
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModWoodFamily;
import net.sashakyotoz.common.blocks.custom.plants.TreeBushLikeBlock;
import net.sashakyotoz.common.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends SBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        super.generate();

        ModWoodFamily.ALL.forEach(family -> this.add(family.door(), this.createDoorTable(family.door())));

        ore(ModBlocks.RIME_ICE, Items.SNOWBALL);
        ore(ModBlocks.HOARFROST, Items.SNOWBALL);
        ore(ModBlocks.GRIPCRYSTAL_WART, ModItems.GRANULATED_GRIPCRYSTAL);
        ore(ModBlocks.GRIPTONITE_CLUSTER, ModItems.GRANULATED_GRIPTONITE);

        this.add(ModBlocks.MIDNIGHT_LILY_PAD, createShearsOnlyDrop(ModBlocks.MIDNIGHT_LILY_PAD));
        this.add(ModBlocks.GLOOMWEED, createShearsOnlyDrop(ModBlocks.GLOOMWEED));
        this.add(ModBlocks.MURKTUFT, createShearsOnlyDrop(ModBlocks.MURKTUFT));
        this.add(ModBlocks.BEARFRUIT_BRAMBLE, createShearsOnlyDrop(ModItems.BEARFRUIT_BRAMBLE));
        this.add(ModBlocks.TALL_GLOOMWEED, createDoublePlantWithSeedDrops(ModBlocks.TALL_GLOOMWEED, ModBlocks.GLOOMWEED));

        leaves(ModBlocks.AMETHYST_LEAVES, ModBlocks.AMETHYST_SAPLING);
        leaves(ModBlocks.GRIZZLY_LEAVES, ModBlocks.GRIZZLY_SAPLING);
        leaves(ModBlocks.CRIMSONVEIL_LEAVES, ModBlocks.CRIMSONVEIL_SAPLING);
        leaves(ModBlocks.TEALIVE_LEAVES, ModBlocks.TEALIVE_SAPLING);
        leaves(ModBlocks.BURLYWOOD_LEAVES, ModBlocks.BURLYWOOD_SAPLING);
        this.add(ModBlocks.HANGING_AMETHYST_LEAVES, createSilkTouchOrShearsDispatchTable(ModBlocks.HANGING_AMETHYST_LEAVES,
                LootItem.lootTableItem(ModBlocks.HANGING_AMETHYST_LEAVES)
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(this.fortune(), 0.25F, 0.5F, 0.75F, 1.0F))));
        this.add(ModBlocks.BURLYWOOD_HEART, createSilkTouchOnlyTable(ModBlocks.BURLYWOOD_HEART));
        this.add(ModBlocks.GLOW_APPLE_BUSH, glowAppleBushDrops(ModBlocks.GLOW_APPLE_BUSH));
        this.dropWhenSilkTouch(ModBlocks.AMETHYST_PETALS);

        ore(ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE, ModItems.RAW_ABYSSAL_ORE);
        ore(ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE, ModItems.RAW_ABYSSAL_ORE);
        ore(ModBlocks.ABYSSAL_ORE_IN_CORONA_STONE, ModItems.RAW_ABYSSAL_ORE);
        ore(ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE, ModItems.RAW_UNSEENIUM_ORE);
        ore(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE, ModItems.RAW_UNSEENIUM_ORE);
        ore(ModBlocks.UNSEENIUM_ORE_IN_CORONA_STONE, ModItems.RAW_UNSEENIUM_ORE);
        ore(ModBlocks.RED_TITANIUM_ORE_IN_GLACIEMITE, ModItems.RAW_RED_TITANIUM_ORE);
        ore(ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE, ModItems.RAW_RED_TITANIUM_ORE);
        ore(ModBlocks.RED_TITANIUM_ORE_IN_CORONA_STONE, ModItems.RAW_RED_TITANIUM_ORE);
        ironOre(ModBlocks.IRON_ORE_IN_GLACIEMITE);
        ironOre(ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE);
        ironOre(ModBlocks.IRON_ORE_IN_CORONA_STONE);
        silkTouchOr(ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE, Items.NETHERITE_SCRAP);
        silkTouchOr(ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE, Items.NETHERITE_SCRAP);
        silkTouchOr(ModBlocks.ANCIENT_DEBRIS_IN_CORONA_STONE, Items.NETHERITE_SCRAP);

        silkTouchOr(ModBlocks.AMETHYST_GRASS_BLOCK, ModBlocks.DARK_CURRANTSLATE);
        silkTouchOr(ModBlocks.TEALIVY_GRASS_BLOCK, ModBlocks.TEALIVY_DIRT);
        silkTouchOr(ModBlocks.GRIMWOOD_GRASS_BLOCK, ModBlocks.DARK_CURRANTSLATE);
        silkTouchOr(ModBlocks.NIGHTDARK_GRASS_BLOCK, ModBlocks.NIGHTDARK_DIRT);
        silkTouchOr(ModBlocks.PENUMBRAL_GRASS_BLOCK, ModBlocks.NIGHTDARK_DIRT);

        this.dropWhenSilkTouch(ModBlocks.UMBRAL_CORAL);
        this.dropWhenSilkTouch(ModBlocks.UMBRAL_CORAL_FAN);
        this.dropWhenSilkTouch(ModBlocks.DEAD_UMBRAL_CORAL);
        this.dropWhenSilkTouch(ModBlocks.DEAD_UMBRAL_CORAL_FAN);
    }

    private Holder<Enchantment> fortune() {
        return this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
    }

    private void silkTouchOr(Block block, ItemLike drop) {
        this.add(block, this.createSingleItemTableWithSilkTouch(block, drop));
    }

    private void leaves(Block leaves, Block sapling) {
        this.add(leaves, this.createLeavesDrops(leaves, sapling, 0.1F));
    }

    private void ore(Block block, Item raw) {
        this.add(block, this.createOreDrop(block, raw));
    }

    private void ironOre(Block block) {
        this.add(block, createSilkTouchDispatchTable(block, this.applyExplosionDecay(block,
                LootItem.lootTableItem(Items.IRON_NUGGET)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(this.fortune())))));
    }

    public static LootTable.Builder glowAppleBushDrops(Block bush) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(ModItems.GLOW_APPLE))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(bush)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TreeBushLikeBlock.GROWN, true))));
    }

    public LootTable.Builder createDoublePlantWithSeedDrops(Block tallPlant, Block plant) {
        LootPoolEntryContainer.Builder<?> drop = LootItem.lootTableItem(plant)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                .when(HAS_SHEARS)
                .otherwise(((LootPoolSingletonContainer.Builder<?>) this.applyExplosionCondition(tallPlant, LootItem.lootTableItem(Items.GLOWSTONE_DUST)))
                        .when(LootItemRandomChanceCondition.randomChance(0.1F)));
        return LootTable.lootTable()
                .withPool(halfPool(tallPlant, drop, DoubleBlockHalf.LOWER, DoubleBlockHalf.UPPER, new BlockPos(0, 1, 0)))
                .withPool(halfPool(tallPlant, drop, DoubleBlockHalf.UPPER, DoubleBlockHalf.LOWER, new BlockPos(0, -1, 0)));
    }

    private static LootPool.Builder halfPool(Block tallPlant, LootPoolEntryContainer.Builder<?> drop, DoubleBlockHalf half, DoubleBlockHalf otherHalf, BlockPos otherOffset) {
        return LootPool.lootPool()
                .add(drop)
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(tallPlant)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, half)))
                .when(LocationCheck.checkLocation(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block()
                                .of(tallPlant)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, otherHalf))),
                        otherOffset));
    }
}