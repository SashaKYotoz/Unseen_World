package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.GroupEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.sashakyotoz.common.ModRegistry;

import java.util.Map;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    public void addSelfDropOr(Block self, ItemConvertible loot) {
        addSelfDropOr(self, loot, ConstantLootNumberProvider.create(1));
    }

    public void addSelfDropOr(Block self, ItemConvertible loot, LootNumberProvider amount) {
        addDrop(self, dropsWithSilkTouch(self,
                new GroupEntry.Builder()).pool(LootPool.builder().conditionally(WITHOUT_SILK_TOUCH)
                .rolls(amount)
                .with(ItemEntry.builder(loot))));
    }
    @Override
    public void generate() {
        for (Map.Entry<Block, ItemConvertible> entry : ModRegistry.BLOCK_DROPS.entrySet()) {
            if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB) != null && ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.SLAB).contains(entry.getKey()))
                addDrop(entry.getKey(), slabDrops(entry.getKey()));
            else if (ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR) != null && ModRegistry.BLOCK_MODELS.get(ModRegistry.Models.DOOR).contains(entry.getKey()))
                addDrop(entry.getKey(), doorDrops(entry.getKey()));
            else if (ModRegistry.BLOCK_SILK_DROPS.containsKey(entry.getKey()))
                addSelfDropOr(entry.getKey(),entry.getValue());
            else
                addDrop(entry.getKey(), entry.getValue());
        }
    }
}