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
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider provider, ExistingFileHelper exFileHelper) {
        super(output, lookupProvider, provider.contentsGetter(), UnseenWorldMod.MODID, exFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(UnseenWorldTags.Items.DARK_WATER_PROTECTED_HELMETS).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("helmet"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.SWORDS).replace(false).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("sword"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        ).addTag(UnseenWorldTags.Items.TREASURE_WEAPONS);
        this.tag(ItemTags.PICKAXES).replace(false).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("pickaxe"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.AXES).replace(false).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> !itemRegistryObject.get().getDescriptionId().contains("pickaxe"))
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("axe"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.SHOVELS).replace(false).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> !itemRegistryObject.get().getDescriptionId().contains("shovel"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.HOES).replace(false).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> !itemRegistryObject.get().getDescriptionId().contains("hoe"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(UnseenWorldTags.Items.TREASURE_WEAPONS).add(
                UnseenWorldItems.FIERY_SABER.get(),
                UnseenWorldItems.HEAVY_CLAYMORE.get(),
                UnseenWorldItems.BLASTING_LANCER.get(),
                UnseenWorldItems.LIGHT_TULVAR.get()
        );
        this.tag(ItemTags.STONE_TOOL_MATERIALS).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("stone"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.PLANKS).add(
                UnseenWorldItems.ITEMS.getEntries().stream()
                        .filter(itemRegistryObject -> itemRegistryObject.get().getDescriptionId().contains("planks"))
                        .map(RegistryObject::get)
                        .toArray(Item[]::new)
        );
        this.tag(ItemTags.TRIMMABLE_ARMOR).add(
                UnseenWorldItems.DEEP_GEM_ARMOR_HELMET.get(),
                UnseenWorldItems.DEEP_GEM_ARMOR_CHESTPLATE.get(),
                UnseenWorldItems.DEEP_GEM_ARMOR_LEGGINGS.get(),
                UnseenWorldItems.DEEP_GEM_ARMOR_BOOTS.get(),
                UnseenWorldItems.UNSEEN_ARMOR_HELMET.get(),
                UnseenWorldItems.UNSEEN_ARMOR_CHESTPLATE.get(),
                UnseenWorldItems.UNSEEN_ARMOR_LEGGINGS.get(),
                UnseenWorldItems.UNSEEN_ARMOR_BOOTS.get(),
                UnseenWorldItems.NATURERIUM_ARMOR_HELMET.get(),
                UnseenWorldItems.NATURERIUM_ARMOR_CHESTPLATE.get(),
                UnseenWorldItems.NATURERIUM_ARMOR_LEGGINGS.get(),
                UnseenWorldItems.NATURERIUM_ARMOR_BOOTS.get(),
                UnseenWorldItems.VOIDINGOT_ARMOR_HELMET.get(),
                UnseenWorldItems.VOIDINGOT_ARMOR_CHESTPLATE.get(),
                UnseenWorldItems.VOIDINGOT_ARMOR_LEGGINGS.get(),
                UnseenWorldItems.VOIDINGOT_ARMOR_BOOTS.get()
        );
    }
}
