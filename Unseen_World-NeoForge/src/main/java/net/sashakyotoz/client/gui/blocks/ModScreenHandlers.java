package net.sashakyotoz.client.gui.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;
import net.sashakyotoz.common.registry.ModRegistrar;

public class ModScreenHandlers {
    public static MenuType<ChestMenu> DARK_CURRANTSLATE_CHEST;
    public static MenuType<ChestMenu> GLACIEMITE_CHEST;
    public static MenuType<ChestMenu> CORONA_STONE_CHEST;

    public static void register() {
        DARK_CURRANTSLATE_CHEST = register("dark_currantslate_chest", (syncId, inventory) -> ChestScreenHandler.client(DARK_CURRANTSLATE_CHEST, ModChestBlock.ChestTypes.DARK_CURRANTSLATE, syncId, inventory));
        GLACIEMITE_CHEST = register("glaciemite_chest", (syncId, inventory) -> ChestScreenHandler.client(GLACIEMITE_CHEST, ModChestBlock.ChestTypes.GLACIEMITE, syncId, inventory));
        CORONA_STONE_CHEST = register("corona_stone_chest", (syncId, inventory) -> ChestScreenHandler.client(CORONA_STONE_CHEST, ModChestBlock.ChestTypes.CORONA_STONE, syncId, inventory));
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String id, MenuType.MenuSupplier<T> factory) {
        return ModRegistrar.register(Registries.MENU, UnseenWorld.MOD.makeID(id), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }
}
