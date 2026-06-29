package net.sashakyotoz.client.gui.blocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;

public class ModScreenHandlers {
    public static MenuType<ChestScreenHandler> DARK_CURRANTSLATE_CHEST;
    public static MenuType<ChestScreenHandler> GLACIEMITE_CHEST;

    public static void register() {
        DARK_CURRANTSLATE_CHEST = register("dark_currantslate_chest", (syncId, inventory) -> new ChestScreenHandler(DARK_CURRANTSLATE_CHEST, ModChestBlock.ChestTypes.DARK_CURRANTSLATE, syncId, inventory, ContainerLevelAccess.NULL));
        GLACIEMITE_CHEST = register("glaciemite_chest", (syncId, inventory) -> new ChestScreenHandler(GLACIEMITE_CHEST, ModChestBlock.ChestTypes.GLACIEMITE, syncId, inventory, ContainerLevelAccess.NULL));
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String id, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, "unseen_world:%s".formatted(id), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }
}