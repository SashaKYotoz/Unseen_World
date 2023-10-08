package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;

import java.util.OptionalInt;

public class AmethystLeavesBlock extends Block implements BonemealableBlock, SimpleWaterloggedBlock, net.minecraftforge.common.IForgeShearable {
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 9);
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AmethystLeavesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 8).setValue(PERSISTENT, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    public boolean isRandomlyTicking(BlockState p_54449_) {
        return p_54449_.getValue(DISTANCE) == 9 && !p_54449_.getValue(PERSISTENT);
    }

    private boolean decaying(BlockState p_221386_) {
        return !p_221386_.getValue(PERSISTENT) && p_221386_.getValue(DISTANCE) == 9;
    }

    public void tick(BlockState p_221369_, ServerLevel p_221370_, BlockPos p_221371_, RandomSource p_221372_) {
        p_221370_.setBlock(p_221371_, updateDistance(p_221369_, p_221370_, p_221371_), 3);
    }

    public int getLightBlock(BlockState p_54460_, BlockGetter p_54461_, BlockPos p_54462_) {
        return 1;
    }

    public BlockState updateShape(BlockState p_54440_, Direction p_54441_, BlockState p_54442_, LevelAccessor p_54443_, BlockPos p_54444_, BlockPos p_54445_) {
        if (p_54440_.getValue(WATERLOGGED)) {
            p_54443_.scheduleTick(p_54444_, Fluids.WATER, Fluids.WATER.getTickDelay(p_54443_));
        }

        int i = getDistanceAt(p_54442_) + 1;
        if (i != 1 || p_54440_.getValue(DISTANCE) != i) {
            p_54443_.scheduleTick(p_54444_, this, 1);
        }

        return p_54440_;
    }

    private static BlockState updateDistance(BlockState p_54436_, LevelAccessor p_54437_, BlockPos p_54438_) {
        int i = 9;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(p_54438_, direction);
            i = Math.min(i, getDistanceAt(p_54437_.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }

        return p_54436_.setValue(DISTANCE, Integer.valueOf(i));
    }

    private static int getDistanceAt(BlockState p_54464_) {
        return getOptionalDistanceAt(p_54464_).orElse(9);
    }

    public static OptionalInt getOptionalDistanceAt(BlockState p_277868_) {
        if (p_277868_.is(BlockTags.LOGS) || p_277868_.is(UnseenWorldModBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get()) || p_277868_.is(Blocks.AMETHYST_BLOCK)) {
            return OptionalInt.of(0);
        } else {
            return p_277868_.hasProperty(DISTANCE) ? OptionalInt.of(p_277868_.getValue(DISTANCE)) : OptionalInt.empty();
        }
    }

    public FluidState getFluidState(BlockState p_221384_) {
        return p_221384_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_221384_);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54447_) {
        p_54447_.add(DISTANCE, PERSISTENT, WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_54424_) {
        FluidState fluidstate = p_54424_.getLevel().getFluidState(p_54424_.getClickedPos());
        BlockState blockstate = this.defaultBlockState().setValue(PERSISTENT, Boolean.valueOf(true)).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
        return updateDistance(blockstate, p_54424_.getLevel(), p_54424_.getClickedPos());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256559_, BlockPos p_50898_, BlockState p_50899_, boolean p_50900_) {
        return p_256559_.getBlockState(p_50898_.below()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level p_220878_, RandomSource p_220879_, BlockPos p_220880_, BlockState p_220881_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel p_221427_, RandomSource p_221428_, BlockPos p_221429_, BlockState p_221430_) {
        p_221427_.setBlock(p_221429_.below(), UnseenWorldModBlocks.LUMINOUSAMETHYSTVINE.get().defaultBlockState(), 3);
    }
}
