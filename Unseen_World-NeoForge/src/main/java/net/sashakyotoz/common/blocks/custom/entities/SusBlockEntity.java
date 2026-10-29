package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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