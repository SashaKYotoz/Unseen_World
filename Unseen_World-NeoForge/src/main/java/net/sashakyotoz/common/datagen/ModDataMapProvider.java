package net.sashakyotoz.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather() {
        Builder<Compostable, net.minecraft.world.item.Item> compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
        compost(compostables, ModBlocks.UMBRAL_KELP, 0.15F);
        compost(compostables, ModBlocks.GLOOMWEED, 0.1F);
        compost(compostables, ModBlocks.TALL_GLOOMWEED, 0.15F);
        compost(compostables, ModItems.CRYSTIE_APPLE, 0.05F);
        compost(compostables, ModItems.GLOW_APPLE, 0.05F);
        compost(compostables, ModItems.BEARFRUIT_BRAMBLE, 0.05F);
        compost(compostables, ModItems.NIGHTBERRY, 0.05F);
    }

    private static void compost(Builder<Compostable, net.minecraft.world.item.Item> builder, ItemLike item, float chance) {
        builder.add(item.asItem().builtInRegistryHolder(), new Compostable(chance), false);
    }
}
