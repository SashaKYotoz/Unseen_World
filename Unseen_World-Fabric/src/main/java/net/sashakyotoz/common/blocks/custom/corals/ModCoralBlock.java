package net.sashakyotoz.common.blocks.custom.corals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CoralBlock;

public class ModCoralBlock extends CoralBlock {
    public ModCoralBlock(Block deadBlock, Properties properties) {
        super(deadBlock, properties);
    }

    @Override
    protected boolean scanForWater(BlockGetter level, BlockPos pos) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            if (DualWaterloggedBlock.isAnyWater(level.getFluidState(cursor.setWithOffset(pos, direction)))) {
                return true;
            }
        }
        return false;
    }
}