package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public final class FruitBearing {
    private FruitBearing() {
    }

    public static InteractionResult harvest(Level level, BlockPos pos, BlockState harvested, ItemStack fruit, @Nullable Entity picker) {
        Block.popResource(level, pos, fruit);
        level.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, Mth.randomBetween(level.random, 0.8F, 1.2F));
        level.setBlock(pos, harvested, Block.UPDATE_ALL);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(picker, harvested));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
