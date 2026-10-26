package net.sashakyotoz.common.blocks.custom.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.common.blocks.ModFluids;
import org.jetbrains.annotations.Nullable;

public final class DarkWaterlogging {
    public static final BooleanProperty DARK_WATER = BooleanProperty.create("dark_water");
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private DarkWaterlogging() {
    }

    public static boolean supports(BlockState state) {
        return state.hasProperty(WATERLOGGED) && state.hasProperty(DARK_WATER);
    }

    public static boolean isDarkLogged(BlockState state) {
        return supports(state) && state.getValue(WATERLOGGED) && state.getValue(DARK_WATER);
    }

    public static boolean isDarkWater(FluidState fluid) {
        return fluid.getType().isSame(ModFluids.DARK_WATER);
    }

    public static boolean isDarkSource(FluidState fluid) {
        return fluid.isSource() && isDarkWater(fluid);
    }

    public static FluidState source() {
        return ModFluids.DARK_WATER.getSource(false);
    }

    @Nullable
    public static BlockState placementState(@Nullable BlockState state, BlockGetter level, BlockPos pos) {
        if (state == null || !supports(state)) {
            return state;
        }
        boolean dark = isDarkSource(level.getFluidState(pos));
        if (dark && !state.getValue(WATERLOGGED) && acceptsWater(state, level, pos)) {
            return state.setValue(WATERLOGGED, true).setValue(DARK_WATER, true);
        }
        return state.setValue(DARK_WATER, dark && state.getValue(WATERLOGGED));
    }

    public static boolean canFill(BlockState state, BlockGetter level, BlockPos pos) {
        return supports(state) && !state.getValue(WATERLOGGED) && acceptsWater(state, level, pos);
    }

    public static boolean fill(LevelAccessor level, BlockPos pos, BlockState state) {
        if (!canFill(state, level, pos)) {
            return false;
        }
        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(DARK_WATER, true), Block.UPDATE_ALL);
            level.scheduleTick(pos, ModFluids.DARK_WATER, ModFluids.DARK_WATER.getTickDelay(level));
        }
        return true;
    }

    public static ItemStack drain(LevelAccessor level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(WATERLOGGED, false).setValue(DARK_WATER, false), Block.UPDATE_ALL_IMMEDIATE);
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
        return new ItemStack(ModFluids.DARK_WATER.getBucket());
    }

    public static BlockState matchFluid(BlockState state, BlockGetter level, BlockPos pos) {
        if (!supports(state)) {
            return state;
        }
        return state.setValue(DARK_WATER, state.getValue(WATERLOGGED) && isDarkSource(level.getFluidState(pos)));
    }

    public static boolean hasDarkWaterNearby(BlockGetter level, BlockPos pos) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            if (isDarkWater(level.getFluidState(cursor.setWithOffset(pos, direction)))) {
                return true;
            }
        }
        return false;
    }

    private static boolean acceptsWater(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof LiquidBlockContainer container && container.canPlaceLiquid(level, pos, state, Fluids.WATER);
    }
}