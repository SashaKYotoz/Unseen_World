package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.sashakyotoz.common.blocks.custom.entities.SusBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SusBlock extends BrushableBlock {
    public SusBlock(Block baseBlock, Settings settings, SoundEvent brushingSound, SoundEvent brushingCompleteSound) {
        super(baseBlock, settings, brushingCompleteSound, brushingSound);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SusBlockEntity(pos, state);
    }
}