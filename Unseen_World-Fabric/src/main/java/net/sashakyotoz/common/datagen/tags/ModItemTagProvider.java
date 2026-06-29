package net.sashakyotoz.common.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.sashakyotoz.common.ModRegistry;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider{
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        for (TagKey<Item> tag : ModRegistry.ITEM_TAGS.keySet()) {
            FabricTagProvider<Item>.FabricTagBuilder builder = getOrCreateTagBuilder(tag);
            for (Item item : ModRegistry.ITEM_TAGS.get(tag)) {
                builder.add(item);
            }
        }
        for(Item item : ModRegistry.ITEMS){
            if (item instanceof ArmorItem armorItem)
                this.getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR).add(armorItem);
        }
    }
}