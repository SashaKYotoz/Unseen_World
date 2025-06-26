package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

public class MidnightLilyPadBlock extends PlantBlock implements Fertilizable {
    public static final BooleanProperty HAS_BERRY = BooleanProperty.of("has_berry");

    public MidnightLilyPadBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(HAS_BERRY, false));
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (world instanceof ServerWorld && entity instanceof BoatEntity)
            world.breakBlock(new BlockPos(pos), true, entity);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(HAS_BERRY)) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.NIGHTBERRY)));
            world.setBlockState(pos, state.with(HAS_BERRY, false));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_BERRY);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextDouble() < 0.05 && this.canGrow(world, random, pos, state))
            this.grow(world, random, pos, state);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(HAS_BERRY);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !state.get(HAS_BERRY);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (random.nextInt(3) == 0 && world.getBlockState(pos.up()).isAir())
            world.setBlockState(pos, this.getDefaultState().with(HAS_BERRY, true));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.5f, 15.0);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (state.get(HAS_BERRY))
            player.dropItem(new ItemStack(ModItems.NIGHTBERRY), true);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidState2 = world.getFluidState(pos.up());
        return (fluidState.getFluid() == Fluids.WATER || fluidState.getFluid() == ModFluids.DARK_WATER) && fluidState2.getFluid() == Fluids.EMPTY;
    }
}