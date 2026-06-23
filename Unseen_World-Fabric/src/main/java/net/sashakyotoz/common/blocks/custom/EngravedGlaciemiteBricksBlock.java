package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;

public class EngravedGlaciemiteBricksBlock extends HorizontalFacingBlock {
    public static final BooleanProperty GRIPPED = BooleanProperty.of("gripped");
    public static final DirectionProperty HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;

    public EngravedGlaciemiteBricksBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(GRIPPED, false).with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            ItemStack stack = player.getStackInHand(hand);
            if (state.get(GRIPPED) && stack.isOf(ModItems.GRANULATED_GRIPCRYSTAL)) {
                if (!player.getAbilities().creativeMode)
                    stack.decrement(1);
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE.value(), SoundCategory.BLOCKS, 1.1F, 0.5F);
                world.setBlockState(pos, state.with(GRIPPED, false), Block.NOTIFY_ALL);
            } else if (!state.get(GRIPPED) && stack.isOf(ModItems.GRANULATED_GRIPTONITE)) {
                if (!player.getAbilities().creativeMode)
                    stack.decrement(1);
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), SoundCategory.BLOCKS, 0.9F, 1.75F);
                world.setBlockState(pos, state.with(GRIPPED, true), Block.NOTIFY_ALL);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState frontalState = world.getBlockState(pos.offset(state.get(HORIZONTAL_FACING).getOpposite()));
        if (frontalState.isOf(ModBlocks.ENGRAVED_GLACIEMITE_BRICKS)) {
            world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), SoundCategory.BLOCKS, 1.0F, 1.5F);
            return state.with(GRIPPED, frontalState.get(GRIPPED));
        } else
            return state.with(GRIPPED, false);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        world.playSound(null, pos.up(), SoundEvents.BLOCK_BASALT_FALL, SoundCategory.BLOCKS, 1.0f, 0.5f);
        if (!world.isClient()) {
            if (entity.isSneaking()) {
                BlockPos offset = pos.offset(state.get(HORIZONTAL_FACING));
                BlockState stateToChange = world.getBlockState(offset);
                BlockState changedState = tryCycleBooleanProperty(stateToChange);
                if (changedState != null)
                    world.setBlockState(offset, changedState, Block.NOTIFY_ALL);
            } else
                world.setBlockState(pos, state.with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING).rotateYClockwise()), Block.NOTIFY_ALL);
        }
        super.onLandedUpon(world, state, pos, entity, fallDistance);
    }

    private BlockState tryCycleBooleanProperty(BlockState state) {
        for (Property<?> property : state.getProperties()) {
            if (property instanceof BooleanProperty)
                return state.cycle(property);
        }
        return null;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(GRIPPED) ? 8 : super.getWeakRedstonePower(state, world, pos, direction);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(GRIPPED, HORIZONTAL_FACING);
    }
}