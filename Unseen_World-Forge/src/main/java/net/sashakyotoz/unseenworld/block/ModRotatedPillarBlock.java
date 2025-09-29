package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

public class ModRotatedPillarBlock extends RotatedPillarBlock {
    public ModRotatedPillarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem) {
//            if(state.is(HumbledlessWorldBlocks.TERRAQUARTZ_LOG.get())) {
//                return HumbledlessWorldBlocks.STRIPPED_TERRAQUARTZ_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
//            }
//            if(state.is(HumbledlessWorldBlocks.TERRAQUARTZ_WOOD.get())) {
//                return HumbledlessWorldBlocks.STRIPPED_TERRAQUARTZ_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
//            }
//            if(state.is(HumbledlessWorldBlocks.ZENTEREAU_LOG.get())) {
//                return HumbledlessWorldBlocks.STRIPPED_ZENTEREAU_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
//            }
//            if(state.is(HumbledlessWorldBlocks.ZENTEREAU_WOOD.get())) {
//                return HumbledlessWorldBlocks.STRIPPED_ZENTEREAU_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
//            }
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
