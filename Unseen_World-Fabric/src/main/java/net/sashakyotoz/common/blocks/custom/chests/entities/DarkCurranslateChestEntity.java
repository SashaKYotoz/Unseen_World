package net.sashakyotoz.common.blocks.custom.chests.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;

public class DarkCurranslateChestEntity extends ModChestBlockEntity {
    public DarkCurranslateChestEntity(BlockPos pos, BlockState state) {
        super(ModChestBlock.ChestTypes.DARK_CURRANTSLATE, pos, state);
    }
}