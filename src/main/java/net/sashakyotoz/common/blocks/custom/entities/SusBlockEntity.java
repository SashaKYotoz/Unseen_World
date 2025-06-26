package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.sashakyotoz.common.blocks.ModBlockEntities;

public class SusBlockEntity extends BrushableBlockEntity {
    public SusBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.SUSBLOCK;
    }

}