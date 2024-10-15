package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.function.ToIntFunction;

public class StandingFruitBlock extends PlantBlock implements Fertilizable {
    public ItemConvertible drop;
    public final TagKey<Block> canStayOn;
    private final VoxelShape plantShape;

    public static final BooleanProperty HAS_FLOWER = BooleanProperty.of("has_flower");
    public static final BooleanProperty HAS_FRUIT = BooleanProperty.of("has_fruit");
    public static final IntProperty LUMINANCE = IntProperty.of("luminance",0,12);
    public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = state -> (Integer)state.get(LUMINANCE);

    public StandingFruitBlock(Settings settings, ItemConvertible drop, TagKey<Block> growable_on, VoxelShape shape) {
        super(settings.ticksRandomly());
        this.drop = drop;
        this.canStayOn = growable_on;
        this.plantShape = shape;
        this.setDefaultState(this.getDefaultState().with(HAS_FRUIT, false).with(HAS_FLOWER, true).with(LUMINANCE,0));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_FRUIT).add(HAS_FLOWER).add(LUMINANCE);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextDouble() < 0.05 && this.canGrow(world, random, pos, state))
            this.grow(world, random, pos, state);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(HAS_FRUIT);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return plantShape;
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        if (state.get(HAS_FRUIT))
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.drop)));
        super.onStacksDropped(state, world, pos, tool, dropExperience);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        if (world.getBlockState(pos.up()).getBlock() instanceof StandingFruitBlock)
            world.setBlockState(pos,state.with(HAS_FLOWER,false).with(HAS_FRUIT,false).with(LUMINANCE,0));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(HAS_FRUIT)) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(this.drop)));
            world.setBlockState(pos, state.with(HAS_FRUIT, false).with(HAS_FLOWER, false));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(HAS_FRUIT) || !state.get(HAS_FLOWER);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(this.canStayOn);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !state.get(HAS_FRUIT);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (random.nextInt(3) == 0 && world.getBlockState(pos.up()).isAir() &&
                world.getBlockState(pos.down()).isIn(canStayOn)) {
            if (random.nextBoolean()) {
                world.setBlockState(pos.up(), this.getDefaultState().with(HAS_FLOWER, true).with(LUMINANCE,11));
                world.setBlockState(pos, this.getDefaultState().with(HAS_FLOWER, false));
            } else
                world.setBlockState(pos, this.getDefaultState().with(HAS_FLOWER, true).with(HAS_FRUIT, true).with(LUMINANCE,11));
        }
    }
}
