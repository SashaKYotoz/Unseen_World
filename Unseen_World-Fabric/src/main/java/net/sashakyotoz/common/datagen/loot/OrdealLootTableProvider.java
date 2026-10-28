package net.sashakyotoz.common.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.items.ModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class OrdealLootTableProvider extends SimpleFabricLootTableProvider {
    public OrdealLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.CHEST);
    }

    @Override
    public String getName() {
        return "Unseen World Ordeal Spawner Loot";
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
        for (OrdealSpawnerBlock.Type type : OrdealSpawnerBlock.Type.values()) {
            exporter.accept(type.lootTable(false), LootTable.lootTable()
                    .withPool(supplies(type)));
            exporter.accept(type.lootTable(true), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(key(type))))
                    .withPool(supplies(type))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(counted(Items.EXPERIENCE_BOTTLE, 2, 5, 1))));
        }
    }

    private static LootPool.Builder supplies(OrdealSpawnerBlock.Type type) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(2.0F, 3.0F))
                .add(counted(ModItems.GLOW_APPLE, 1, 2, 10))
                .add(counted(ModItems.CRYSTIE_APPLE, 1, 2, 8))
                .add(counted(ModItems.NIGHTBERRY, 3, 6, 10))
                .add(counted(ModItems.GLOWROOT_BULB, 2, 4, 10))
                .add(counted(ModItems.WARPEDVEIL_VINE_FRUIT, 2, 4, 8))
                .add(potion(Items.POTION, Potions.HEALING, 6))
                .add(potion(Items.POTION, Potions.REGENERATION, 4))
                .add(potion(Items.POTION, Potions.NIGHT_VISION, 5))
                .add(potion(Items.SPLASH_POTION, Potions.HEALING, 3));
        return switch (type) {
            case GRIPCRYSTAL -> pool.add(counted(ModItems.GRANULATED_GRIPCRYSTAL, 2, 4, 6));
            case ABYSSAL -> pool.add(counted(Items.ENDER_PEARL, 1, 2, 4))
                    .add(potion(Items.POTION, Potions.WATER_BREATHING, 3));
            case AURIC -> pool.add(counted(Items.GOLD_NUGGET, 3, 9, 8))
                    .add(potion(Items.POTION, Potions.FIRE_RESISTANCE, 3));
        };
    }

    private static Item key(OrdealSpawnerBlock.Type type) {
        return switch (type) {
            case GRIPCRYSTAL -> ModItems.GRIPCRYSTAL_KEY;
            case ABYSSAL -> ModItems.ABYSSAL_KEY;
            case AURIC -> ModItems.AURIC_KEY;
        };
    }

    private static LootPoolSingletonContainer.Builder<?> counted(ItemLike item, int min, int max, int weight) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }

    private static LootPoolSingletonContainer.Builder<?> potion(Item item, Holder<Potion> potion, int weight) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetPotionFunction.setPotion(potion));
    }
}