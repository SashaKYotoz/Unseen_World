package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public interface CrimsonvielVines {
    VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    BooleanProperty BERRIES = Properties.BERRIES;

    static ActionResult pickBerries(@Nullable Entity picker, BlockState state, World world, BlockPos pos) {
        if (state.get(BERRIES)) {
            Block.dropStack(world, pos, new ItemStack(ModItems.WARPEDVEIL_VINE_FRUIT, 1));
            float f = MathHelper.nextBetween(world.random, 0.75F, 1.25F);
            world.playSound(null, pos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, f);
            BlockState blockState = state.with(BERRIES, Boolean.FALSE);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(picker, blockState));
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    static boolean hasBerries(BlockState state) {
        return state.contains(BERRIES) && state.get(BERRIES);
    }

    static ToIntFunction<BlockState> getLuminanceSupplier(int luminance) {
        return state -> state.get(Properties.BERRIES) ? luminance : 0;
    }
}
