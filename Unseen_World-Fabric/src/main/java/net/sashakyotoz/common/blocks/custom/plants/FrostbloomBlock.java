package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.tags.ModTags;

public class FrostbloomBlock extends FlowerBlock {
    public FrostbloomBlock(Holder<MobEffect> effect, float duration, Properties properties) {
        super(effect, duration, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(Blocks.SNOW_BLOCK) || state.is(ModTags.Blocks.CHIMERIC_DARKNESS_STONES);
    }
}
