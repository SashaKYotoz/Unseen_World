package net.sashakyotoz.common.blocks.custom.chests.entities;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;

public class GlaciemiteChestEntity extends ModChestBlockEntity {
    public GlaciemiteChestEntity( BlockPos pos, BlockState state) {
        super(ModChestBlock.ChestTypes.GLACIEMITE, pos, state);
    }
}