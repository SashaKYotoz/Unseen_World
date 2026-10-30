package net.sashakyotoz.common.blocks.custom.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.common.blocks.ModFluids;
import org.jetbrains.annotations.Nullable;

public interface DualWaterloggedBlock extends SimpleWaterloggedBlock {
    BooleanProperty DARK_WATER = DarkWaterlogging.DARK_WATER;

    static boolean isDarkWater(FluidState fluid) {
        return fluid.getType().isSame(ModFluids.DARK_WATER);
    }

    static boolean isAnyWater(FluidState fluid) {
        return fluid.is(FluidTags.WATER) || isDarkWater(fluid);
    }

    static boolean isWaterloggingFluid(Fluid fluid) {
        return fluid == Fluids.WATER || fluid == ModFluids.DARK_WATER;
    }

    static BlockState withFluid(BlockState state, FluidState fluid) {
        boolean waterlogged = fluid.getAmount() == 8 && isAnyWater(fluid);
        return state.setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                .setValue(DARK_WATER, waterlogged && isDarkWater(fluid));
    }

    static BlockState dry(BlockState state) {
        BlockState dried = state.hasProperty(BlockStateProperties.WATERLOGGED)
                ? state.setValue(BlockStateProperties.WATERLOGGED, false)
                : state;
        return dried.hasProperty(DARK_WATER) ? dried.setValue(DARK_WATER, false) : dried;
    }

    static FluidState fluidOf(BlockState state) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED)) {
            return Fluids.EMPTY.defaultFluidState();
        }
        return state.getValue(DARK_WATER) ? ModFluids.DARK_WATER.getSource(false) : Fluids.WATER.getSource(false);
    }

    static void scheduleFluidTick(BlockState state, LevelAccessor level, BlockPos pos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            FlowingFluid fluid = state.getValue(DARK_WATER) ? ModFluids.DARK_WATER : Fluids.WATER;
            level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
        }
    }

    static boolean hasWaterNearby(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            return true;
        }
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            if (isAnyWater(level.getFluidState(cursor.setWithOffset(pos, direction)))) {
                return true;
            }
        }
        return false;
    }

    static void scheduleDeathIfDry(Block block, BlockState state, LevelAccessor level, BlockPos pos) {
        if (!hasWaterNearby(state, level, pos)) {
            level.scheduleTick(pos, block, 60 + level.getRandom().nextInt(40));
        }
    }

    static BlockState placementState(BlockState state, BlockPlaceContext context) {
        return state == null ? null : withFluid(state, context.getLevel().getFluidState(context.getClickedPos()));
    }

    static BlockState updateAttached(BlockState state, LevelAccessor level, BlockPos pos, boolean supportChanged) {
        if (supportChanged && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        scheduleFluidTick(state, level, pos);
        return state;
    }

    static BlockState updateLiving(Block block, BlockState state, LevelAccessor level, BlockPos pos, boolean supportChanged) {
        BlockState updated = updateAttached(state, level, pos, supportChanged);
        if (!updated.isAir()) {
            scheduleDeathIfDry(block, state, level, pos);
        }
        return updated;
    }

    static void dieIfDry(BlockState state, ServerLevel level, BlockPos pos, BlockState deadState) {
        if (!hasWaterNearby(state, level, pos)) {
            level.setBlock(pos, dry(deadState), 2);
        }
    }

    @Override
    default boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(BlockStateProperties.WATERLOGGED) && isWaterloggingFluid(fluid);
    }

    @Override
    default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        if (!this.canPlaceLiquid(null, level, pos, state, fluid.getType())) {
            return false;
        }
        if (!level.isClientSide()) {
            level.setBlock(pos, withFluid(state, fluid), 3);
            level.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
        }
        return true;
    }

    @Override
    default ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED)) {
            return ItemStack.EMPTY;
        }
        ItemStack bucket = new ItemStack(fluidOf(state).getType().getBucket());
        if (bucket.isEmpty()) {
            return ItemStack.EMPTY;
        }
        level.setBlock(pos, dry(state), 3);
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
        return bucket;
    }
}