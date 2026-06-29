package net.sashakyotoz.common.blocks.custom.chests.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;

public class GlaciemiteChestEntity extends ModChestBlockEntity {
    public GlaciemiteChestEntity( BlockPos pos, BlockState state) {
        super(ModChestBlock.ChestTypes.GLACIEMITE, pos, state);
    }
}