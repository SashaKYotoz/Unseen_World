package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;

public class ModItemGroups {
    public static final CreativeModeTab UNSEEN_WORLD_EQUIPMENT = register("equipment",
            FabricItemGroup.builder().title(Component.translatable("itemgroup.unseen_world.items"))
                    .icon(() -> new ItemStack(ModItems.ECLIPSE_KEYSTONE))
                    .displayItems((displayContext, entries) -> ModRegistry.ITEMS.forEach(item -> {
                        if (!(item instanceof BlockItem))
                            entries.accept(item);
                    })).build());
    public static final CreativeModeTab UNSEEN_WORLD_BLOCKS = register("blocks",
            FabricItemGroup.builder().title(Component.translatable("itemgroup.unseen_world.blocks"))
                    .icon(() -> new ItemStack(ModBlocks.DARK_FROST_BRICKS))
                    .displayItems((displayContext, entries) -> ModRegistry.BLOCKS.forEach(block -> {
                        if (!block.getDescriptionId().contains("sign") && !(block instanceof LiquidBlock) && (!(block instanceof GrowingPlantBodyBlock) && (!(block instanceof GrowingPlantHeadBlock))))
                            entries.accept(block);
                    })).build());

    public static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, UnseenWorld.makeID(id), tab);
    }

    public static void register() {
        UnseenWorld.log("Registering ItemGroups for modid : " + UnseenWorld.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.WARPED_HANGING_SIGN, ModItems.AMETHYST_SIGN_ITEM);
            entries.addAfter(ModItems.AMETHYST_SIGN, ModItems.AMETHYST_HANGING_SIGN_ITEM);
            entries.addAfter(ModItems.AMETHYST_HANGING_SIGN_ITEM, ModItems.GRIZZLY_SIGN_ITEM);
            entries.addAfter(ModItems.GRIZZLY_SIGN_ITEM, ModItems.GRIZZLY_HANGING_SIGN_ITEM);
            entries.addAfter(ModItems.GRIZZLY_HANGING_SIGN_ITEM, ModItems.TEALIVE_SIGN_ITEM);
            entries.addAfter(ModItems.TEALIVE_SIGN_ITEM, ModItems.TEALIVE_HANGING_SIGN_ITEM);
            entries.addAfter(ModItems.TEALIVE_HANGING_SIGN_ITEM, ModItems.BURLYWOOD_SIGN_ITEM);
            entries.addAfter(ModItems.BURLYWOOD_SIGN_ITEM, ModItems.BURLYWOOD_HANGING_SIGN_ITEM);
            entries.addAfter(ModItems.BURLYWOOD_HANGING_SIGN_ITEM, ModItems.CRIMSONVEIL_SIGN_ITEM);
            entries.addAfter(ModItems.CRIMSONVEIL_SIGN_ITEM, ModItems.CRIMSONVEIL_HANGING_SIGN_ITEM);
        });
    }
}