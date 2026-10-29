package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.ModBlocks;

public class ReedBlock extends SugarCaneBlock {
    public ReedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.below());
        if (blockState.is(this)) {
            return true;
        } else {
            return blockState.is(BlockTags.DIRT) || blockState.is(BlockTags.SAND) || blockState.is(ModBlocks.ASHEN_OOZE);
        }
    }
}