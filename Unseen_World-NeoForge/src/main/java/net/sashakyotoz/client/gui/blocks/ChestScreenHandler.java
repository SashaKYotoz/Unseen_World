package net.sashakyotoz.client.gui.blocks;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;

public class ChestScreenHandler extends ChestMenu {
    private final ModChestBlock.ChestTypes chestType;

    public ChestScreenHandler(MenuType<?> type, ModChestBlock.ChestTypes chestType, int syncId, Inventory playerInventory, Container container) {
        super(type, syncId, playerInventory, container, chestType.getRowCount());
        this.chestType = chestType;
    }

    public static ChestScreenHandler client(MenuType<?> type, ModChestBlock.ChestTypes chestType, int syncId, Inventory playerInventory) {
        return new ChestScreenHandler(type, chestType, syncId, playerInventory, new SimpleContainer(chestType.size));
    }

    public ModChestBlock.ChestTypes getChestType() {
        return this.chestType;
    }
}
