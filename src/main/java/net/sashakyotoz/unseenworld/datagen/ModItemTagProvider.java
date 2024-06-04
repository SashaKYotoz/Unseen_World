package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider provider, ExistingFileHelper exFileHelper) {
        super(output, lookupProvider, provider.contentsGetter(), UnseenWorldMod.MODID, exFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(UnseenWorldModTags.Items.DARK_WATER_PROTECTED_HELMETS).add(
                UnseenWorldModItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("helmet"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(UnseenWorldModTags.Items.TREASURE_WEAPONS).add(
                UnseenWorldModItems.FIERY_SABER.get(),
                UnseenWorldModItems.HEAVY_CLAYMORE.get(),
                UnseenWorldModItems.BLASTING_LANCER.get(),
                UnseenWorldModItems.LIGHT_TULVAR.get()
        );
        this.tag(ItemTags.STONE_TOOL_MATERIALS).add(
                UnseenWorldModItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("stone"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.PLANKS).add(
                UnseenWorldModItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("planks"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.TRIMMABLE_ARMOR).add(
                UnseenWorldModItems.DEEP_GEM_ARMOR_HELMET.get(),
                UnseenWorldModItems.DEEP_GEM_ARMOR_CHESTPLATE.get(),
                UnseenWorldModItems.DEEP_GEM_ARMOR_LEGGINGS.get(),
                UnseenWorldModItems.DEEP_GEM_ARMOR_BOOTS.get(),
                UnseenWorldModItems.UNSEEN_ARMOR_HELMET.get(),
                UnseenWorldModItems.UNSEEN_ARMOR_CHESTPLATE.get(),
                UnseenWorldModItems.UNSEEN_ARMOR_LEGGINGS.get(),
                UnseenWorldModItems.UNSEEN_ARMOR_BOOTS.get(),
                UnseenWorldModItems.NATURERIUM_ARMOR_HELMET.get(),
                UnseenWorldModItems.NATURERIUM_ARMOR_CHESTPLATE.get(),
                UnseenWorldModItems.NATURERIUM_ARMOR_LEGGINGS.get(),
                UnseenWorldModItems.NATURERIUM_ARMOR_BOOTS.get(),
                UnseenWorldModItems.VOIDINGOT_ARMOR_HELMET.get(),
                UnseenWorldModItems.VOIDINGOT_ARMOR_CHESTPLATE.get(),
                UnseenWorldModItems.VOIDINGOT_ARMOR_LEGGINGS.get(),
                UnseenWorldModItems.VOIDINGOT_ARMOR_BOOTS.get()
        );
    }
}
