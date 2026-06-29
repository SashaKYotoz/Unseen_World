package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.common.blocks.custom.GrippingStoneBlock;
import org.jetbrains.annotations.Nullable;

public class GrippingSpigeliaBlock extends BushBlock {
    public static final DirectionProperty VERTICAL_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

    public GrippingSpigeliaBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(VERTICAL_DIRECTION, Direction.UP).setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_DIRECTION, AGE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(7) == 3 && state.getValue(AGE) < 2) {
            BlockState state1 = state.cycle(AGE);
            world.setBlock(pos, state1, UPDATE_ALL);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(AGE) > 0 && player instanceof ServerPlayer player1) {
            GripcrystalManaData.addMana((IEntityDataSaver) player1, state.getValue(AGE) * 4);
            player1.playNotifySound(SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.5f, 1.7f);
            world.setBlockAndUpdate(pos, state.setValue(AGE, 0));
            return InteractionResult.CONSUME;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        LevelAccessor worldAccess = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction direction = ctx.getNearestLookingVerticalDirection().getOpposite();
        Direction direction2 = getDirectionToPlaceAt(worldAccess, blockPos, direction);
        if (direction2 == null)
            return null;
        else
            return this.defaultBlockState()
                    .setValue(VERTICAL_DIRECTION, direction2);
    }

    @Nullable
    private Direction getDirectionToPlaceAt(LevelReader world, BlockPos pos, Direction direction) {
        Direction direction2;
        if (canPlaceAtWithDirection(world, pos, direction))
            direction2 = direction;
        else {
            if (!canPlaceAtWithDirection(world, pos, direction.getOpposite()))
                return null;

            direction2 = direction.getOpposite();
        }

        return direction2;
    }

    private boolean canPlaceAtWithDirection(LevelReader world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction) || ((blockState.is(Blocks.POINTED_DRIPSTONE) && blockState.getValue(VERTICAL_DIRECTION) == direction));
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.getBlock() instanceof GrippingStoneBlock;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return this.mayPlaceOn(world.getBlockState(pos.below()), world, pos.below())
                || (world.getBlockState(pos.above()).getBlock() instanceof GrippingStoneBlock
                && world.getBlockState(pos.above()).getValue(BlockStateProperties.FACING) == Direction.DOWN);
    }
}