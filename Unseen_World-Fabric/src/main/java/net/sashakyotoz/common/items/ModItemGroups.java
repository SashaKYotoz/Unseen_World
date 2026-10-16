package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;

import java.util.List;
import java.util.function.Consumer;

public class ModItemGroups {
    public static final CreativeModeTab UNSEEN_WORLD_EQUIPMENT = register("equipment",
            FabricItemGroup.builder().title(Component.translatable("itemgroup.unseen_world.items"))
                    .icon(() -> new ItemStack(ModItems.ECLIPSE_KEYSTONE))
                    .displayItems((displayContext, entries) -> forEachOwnItem(item -> {
                        if (!isBlockTabItem(item) && !isSign(item))
                            entries.accept(item);
                    })).build());
    public static final CreativeModeTab UNSEEN_WORLD_BLOCKS = register("blocks",
            FabricItemGroup.builder().title(Component.translatable("itemgroup.unseen_world.blocks"))
                    .icon(() -> new ItemStack(ModBlocks.DARK_FROST_BRICKS))
                    .displayItems((displayContext, entries) -> forEachOwnItem(item -> {
                        if (isBlockTabItem(item) && !isSign(item))
                            entries.accept(item);
                    })).build());

    public static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, UnseenWorld.MOD.makeID(id), tab);
    }

    public static void register() {
        UnseenWorld.log().debug("Registering ItemGroups for modid : " + UnseenWorld.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            ItemLike previous = Items.WARPED_HANGING_SIGN;
            for (Item sign : signItems()) {
                entries.addAfter(previous, sign);
                previous = sign;
            }
        });
    }

    private static void forEachOwnItem(Consumer<Item> consumer) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(UnseenWorld.MOD_ID))
                consumer.accept(item);
        }
    }

    private static boolean isBlockTabItem(Item item) {
        return item instanceof BlockItem && !(item instanceof ItemNameBlockItem);
    }

    private static boolean isSign(Item item) {
        return item instanceof SignItem || item instanceof HangingSignItem;
    }

    private static List<Item> signItems() {
        return List.of(
                ModItems.AMETHYST_SIGN_ITEM, ModItems.AMETHYST_HANGING_SIGN_ITEM,
                ModItems.GRIZZLY_SIGN_ITEM, ModItems.GRIZZLY_HANGING_SIGN_ITEM,
                ModItems.TEALIVE_SIGN_ITEM, ModItems.TEALIVE_HANGING_SIGN_ITEM,
                ModItems.BURLYWOOD_SIGN_ITEM, ModItems.BURLYWOOD_HANGING_SIGN_ITEM,
                ModItems.CRIMSONVEIL_SIGN_ITEM, ModItems.CRIMSONVEIL_HANGING_SIGN_ITEM);
    }
}