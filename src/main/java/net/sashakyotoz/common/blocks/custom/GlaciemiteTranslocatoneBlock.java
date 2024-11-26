package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.entities.TranslocatoneBlockEntity;

public class GlaciemiteTranslocatoneBlock extends BlockWithEntity {
    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;
    private final VoxelShape STONE_SHAPE = VoxelShapes.union(
            GlaciemiteTranslocatoneBlock.createCuboidShape(0, 0, 0, 16, 3, 16),
            GlaciemiteTranslocatoneBlock.createCuboidShape(1, 4, 1, 15, 5, 15),
            GlaciemiteTranslocatoneBlock.createCuboidShape(3, 5, 3, 13, 11, 13),
            GlaciemiteTranslocatoneBlock.createCuboidShape(1, 11, 1, 12, 13, 12),
            GlaciemiteTranslocatoneBlock.createCuboidShape(0, 13, 0, 16, 16, 16));

    public GlaciemiteTranslocatoneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(TRIGGERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TranslocatoneBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getOutlineShape(state,world,pos,context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(TRIGGERED) ? VoxelShapes.empty() : STONE_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof TranslocatoneBlockEntity entity) {
            world.playSound(player, pos, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 2.5f, 2);
            world.setBlockState(pos, state.with(TRIGGERED, false));
            player.teleport(entity.pos.getX(),entity.pos.getY(),entity.pos.getZ());
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(TRIGGERED) ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.TRANSLOCATONE, TranslocatoneBlockEntity::tick);
    }
}