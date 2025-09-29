package net.sashakyotoz.client.gui.blocks;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;

public class ModScreenHandlers {
    public static ScreenHandlerType<ChestScreenHandler> DARK_CURRANTSLATE_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> GLACIEMITE_CHEST;

    public static void register() {
        DARK_CURRANTSLATE_CHEST = register("dark_currantslate_chest", (syncId, inventory) -> new ChestScreenHandler(DARK_CURRANTSLATE_CHEST, ModChestBlock.ChestTypes.DARK_CURRANTSLATE, syncId, inventory, ScreenHandlerContext.EMPTY));
        GLACIEMITE_CHEST = register("glaciemite_chest", (syncId, inventory) -> new ChestScreenHandler(GLACIEMITE_CHEST, ModChestBlock.ChestTypes.GLACIEMITE, syncId, inventory, ScreenHandlerContext.EMPTY));
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, "unseen_world:%s".formatted(id), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }
}