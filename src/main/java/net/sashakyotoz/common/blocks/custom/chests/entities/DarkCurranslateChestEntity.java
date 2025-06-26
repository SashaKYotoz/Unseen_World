package net.sashakyotoz.common.blocks.custom.chests.entities;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;

public class DarkCurranslateChestEntity extends ModChestBlockEntity {
    public DarkCurranslateChestEntity(BlockPos pos, BlockState state) {
        super(ModChestBlock.ChestTypes.DARK_CURRANTSLATE, pos, state);
    }
}