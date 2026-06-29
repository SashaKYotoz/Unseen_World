package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;

public class EngravedGlaciemiteBricksBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty GRIPPED = BooleanProperty.create("gripped");
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public EngravedGlaciemiteBricksBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(GRIPPED, false).setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            if (state.getValue(GRIPPED) && stack.is(ModItems.GRANULATED_GRIPCRYSTAL)) {
                if (!player.getAbilities().instabuild)
                    stack.shrink(1);
                world.playSound(null, pos, SoundEvents.NOTE_BLOCK_XYLOPHONE.value(), SoundSource.BLOCKS, 1.1F, 0.5F);
                world.setBlock(pos, state.setValue(GRIPPED, false), Block.UPDATE_ALL);
            } else if (!state.getValue(GRIPPED) && stack.is(ModItems.GRANULATED_GRIPTONITE)) {
                if (!player.getAbilities().instabuild)
                    stack.shrink(1);
                world.playSound(null, pos, SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.BLOCKS, 0.9F, 1.75F);
                world.setBlock(pos, state.setValue(GRIPPED, true), Block.UPDATE_ALL);
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        BlockState frontalState = world.getBlockState(pos.relative(state.getValue(HORIZONTAL_FACING).getOpposite()));
        if (frontalState.is(ModBlocks.ENGRAVED_GLACIEMITE_BRICKS)) {
            world.playSound(null, pos, SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.BLOCKS, 1.0F, 1.5F);
            return state.setValue(GRIPPED, frontalState.getValue(GRIPPED));
        } else
            return state.setValue(GRIPPED, false);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        world.playSound(null, pos.above(), SoundEvents.BASALT_FALL, SoundSource.BLOCKS, 1.0f, 0.5f);
        if (!world.isClientSide()) {
            if (entity.isShiftKeyDown()) {
                BlockPos offset = pos.relative(state.getValue(HORIZONTAL_FACING));
                BlockState stateToChange = world.getBlockState(offset);
                BlockState changedState = tryCycleBooleanProperty(stateToChange);
                if (changedState != null)
                    world.setBlock(offset, changedState, Block.UPDATE_ALL);
            } else
                world.setBlock(pos, state.setValue(HORIZONTAL_FACING, state.getValue(HORIZONTAL_FACING).getClockWise()), Block.UPDATE_ALL);
        }
        super.fallOn(world, state, pos, entity, fallDistance);
    }

    private BlockState tryCycleBooleanProperty(BlockState state) {
        for (Property<?> property : state.getProperties()) {
            if (property instanceof BooleanProperty)
                return state.cycle(property);
        }
        return null;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(GRIPPED) ? 8 : super.getSignal(state, world, pos, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GRIPPED, HORIZONTAL_FACING);
    }
}