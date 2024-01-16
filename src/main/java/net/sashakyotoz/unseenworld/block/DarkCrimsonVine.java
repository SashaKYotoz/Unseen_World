package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;

import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public interface DarkCrimsonVine {
    VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    BooleanProperty BERRIES = BlockStateProperties.BERRIES;

    static InteractionResult use(@Nullable Entity p_270738_, BlockState p_270772_, Level level, BlockPos blockPos) {
        if (p_270772_.getValue(BERRIES)) {
            Block.popResource(level, blockPos, new ItemStack(UnseenWorldModItems.BERRIESFROM_BLOOMING_VINE.get(), RandomSource.create().nextInt(2,4)));
            float f = Mth.randomBetween(level.random, 0.8F, 1.2F);
            level.playSound(null, blockPos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);
            BlockState blockstate = p_270772_.setValue(BERRIES, Boolean.FALSE);
            level.setBlock(blockPos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(p_270738_, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    static boolean hasBerries(BlockState p_152952_) {
        return p_152952_.hasProperty(BERRIES) && p_152952_.getValue(BERRIES);
    }

    static ToIntFunction<BlockState> emission(int p_181218_) {
        return (p_181216_) -> {
            return p_181216_.getValue(BlockStateProperties.BERRIES) ? p_181218_ : 0;
        };
    }
}
