package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModTags;

public class ModSaplingBlock extends SaplingBlock {
    public ModSaplingBlock(AbstractTreeGrower grower, Properties properties) {
        super(grower, properties);
    }
    @Override
    public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
        return groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.PODZOL)
                || groundState.is(Blocks.MOSS_BLOCK) || groundState.is(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS);
    }
}
