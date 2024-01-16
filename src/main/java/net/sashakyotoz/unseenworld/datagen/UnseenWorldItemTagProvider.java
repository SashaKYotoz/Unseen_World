package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.util.UnseenWorldModTags;

import java.util.concurrent.CompletableFuture;

public class UnseenWorldItemTagProvider extends ItemTagsProvider {
    public UnseenWorldItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider provider, ExistingFileHelper exFileHelper) {
        super(output, lookupProvider, provider.contentsGetter(), UnseenWorldMod.MODID, exFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(UnseenWorldModTags.Items.DARK_WATER_PROTECTED_HELMETS).add(
                UnseenWorldModItems.DEEP_GEM_ARMOR_HELMET.get(),
                UnseenWorldModItems.NATURERIUM_ARMOR_HELMET.get(),
                UnseenWorldModItems.UNSEEN_ARMOR_HELMET.get(),
                UnseenWorldModItems.VOIDINGOT_ARMOR_HELMET.get(),
                UnseenWorldModItems.RED_TITANIUM_ARMOR_HELMET.get(),
                UnseenWorldModItems.KNIGHT_ARMOR_HELMET.get(),
                UnseenWorldModItems.BLAZER_HELMET.get()
        );
        this.tag(ItemTags.PLANKS).add(
                UnseenWorldModItems.REGISTRY.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("planks"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
    }
}
