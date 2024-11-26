package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;

public class TreeBushLikeBlock extends PlantBlock {
    public static final BooleanProperty GROWN = BooleanProperty.of("grown");
    public static final EnumProperty<TreeBushLikeTypes> TYPE = EnumProperty.of("type", TreeBushLikeTypes.class);
    public static final DirectionProperty FACING = Properties.FACING;

    private static final VoxelShape STEM_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    private static final VoxelShape STEM_8X_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 12.0, 10.0);
    private static final VoxelShape N_TURN_STEM_SHAPE = VoxelShapes.union(STEM_8X_SHAPE, Block.createCuboidShape(0.0, 8.0, 6.0, 16.0, 12.0, 10.0));
    private static final VoxelShape S_TURN_STEM_SHAPE = VoxelShapes.union(STEM_8X_SHAPE, Block.createCuboidShape(6.0, 8.0, 0.0, 10.0, 12.0, 16.0));
    private static final VoxelShape BUSH_SHAPE = VoxelShapes.union(Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 8.0, 10.0), Block.createCuboidShape(2.0, 2.0, 2.0, 14.0, 16.0, 14.0));
    private static final VoxelShape CROOKED_TURN_SHAPE = Block.createCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    private static final VoxelShape CROSSING_STEM_SHAPE = VoxelShapes.union(N_TURN_STEM_SHAPE, S_TURN_STEM_SHAPE);

    public TreeBushLikeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(GROWN, Boolean.FALSE).with(TYPE, TreeBushLikeTypes.BUSH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, GROWN, TYPE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BlockState floor = world.getBlockState(pos.down());
        BlockState north = world.getBlockState(pos.north());
        BlockState south = world.getBlockState(pos.south());
        BlockState east = world.getBlockState(pos.east());
        BlockState west = world.getBlockState(pos.west());
        if (north.isOf(ModBlocks.GLOW_APPLE_BUSH) && north.get(TYPE) == TreeBushLikeTypes.TURN_STEM)
            return this.getDefaultState().with(FACING, Direction.NORTH).with(TYPE, TreeBushLikeTypes.CROOKED_STEM);
        if (south.isOf(ModBlocks.GLOW_APPLE_BUSH) && south.get(TYPE) == TreeBushLikeTypes.TURN_STEM)
            return this.getDefaultState().with(FACING, Direction.SOUTH).with(TYPE, TreeBushLikeTypes.CROOKED_STEM);
        if (east.isOf(ModBlocks.GLOW_APPLE_BUSH) && east.get(TYPE) == TreeBushLikeTypes.TURN_STEM)
            return this.getDefaultState().with(FACING, Direction.EAST).with(TYPE, TreeBushLikeTypes.CROOKED_STEM);
        if (west.isOf(ModBlocks.GLOW_APPLE_BUSH) && west.get(TYPE) == TreeBushLikeTypes.TURN_STEM)
            return this.getDefaultState().with(FACING, Direction.WEST).with(TYPE, TreeBushLikeTypes.CROOKED_STEM);
        if (floor.isOf(ModBlocks.GLOW_APPLE_BUSH) && (floor.get(TYPE) == TreeBushLikeTypes.CROOKED_STEM
                || floor.get(TYPE) == TreeBushLikeTypes.STEM))
            return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite()).with(TYPE, ctx.getPlayer().getRandom().nextBoolean() ? TreeBushLikeTypes.BUSH : TreeBushLikeTypes.STEM);
        if (floor.isOf(ModBlocks.GLOW_APPLE_BUSH) && floor.get(TYPE) == TreeBushLikeTypes.BUSH) {
            world.setBlockState(pos.down(), floor.with(TYPE, TreeBushLikeTypes.STEM));
            return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite()).with(TYPE, TreeBushLikeTypes.STEM);
        }
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite()).with(TYPE, TreeBushLikeTypes.BUSH);
    }
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        BlockState north = world.getBlockState(pos.north());
        BlockState south = world.getBlockState(pos.south());
        BlockState east = world.getBlockState(pos.east());
        BlockState west = world.getBlockState(pos.west());
        if ((north.isOf(ModBlocks.GLOW_APPLE_BUSH) && north.get(TYPE) == TreeBushLikeTypes.CROOKED_STEM)
                && (south.isOf(ModBlocks.GLOW_APPLE_BUSH) && south.get(TYPE) == TreeBushLikeTypes.CROOKED_STEM)
                && (east.isOf(ModBlocks.GLOW_APPLE_BUSH) && east.get(TYPE) == TreeBushLikeTypes.CROOKED_STEM)
                && (west.isOf(ModBlocks.GLOW_APPLE_BUSH) && west.get(TYPE) == TreeBushLikeTypes.CROOKED_STEM))
            world.setBlockState(pos, state.with(TYPE, TreeBushLikeTypes.CROSSING_STEM));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (random.nextInt(9) == 3 && state.get(TYPE) == TreeBushLikeTypes.BUSH)
            world.setBlockState(pos, state.with(GROWN, true));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(GROWN) && state.get(TYPE) == TreeBushLikeTypes.BUSH) {
            player.dropItem(ModItems.GLOW_APPLE);
            world.setBlockState(pos, state.with(GROWN, false));
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return super.hasRandomTicks(state) && !state.get(GROWN);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(TYPE)) {
            case STEM -> STEM_SHAPE;
            case CROOKED_STEM -> CROOKED_TURN_SHAPE;
            case CROSSING_STEM -> CROSSING_STEM_SHAPE;
            case TURN_STEM ->
                    (state.get(FACING).getOpposite() == Direction.NORTH || state.get(FACING).getOpposite() == Direction.SOUTH)
                            ? S_TURN_STEM_SHAPE : N_TURN_STEM_SHAPE;
            case BUSH -> BUSH_SHAPE;
        };
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos.down());
        BlockState north = world.getBlockState(pos.north());
        BlockState south = world.getBlockState(pos.south());
        BlockState east = world.getBlockState(pos.east());
        BlockState west = world.getBlockState(pos.west());
        if ((north.getBlock() instanceof TreeBushLikeBlock && north.get(TYPE) == TreeBushLikeTypes.TURN_STEM)
                || (south.getBlock() instanceof TreeBushLikeBlock && south.get(TYPE) == TreeBushLikeTypes.TURN_STEM)
                || (east.getBlock() instanceof TreeBushLikeBlock && east.get(TYPE) == TreeBushLikeTypes.TURN_STEM)
                || (west.getBlock() instanceof TreeBushLikeBlock && west.get(TYPE) == TreeBushLikeTypes.TURN_STEM))
            return true;
        if ((north.getBlock() instanceof TreeBushLikeBlock && north.get(TYPE) == TreeBushLikeTypes.CROSSING_STEM)
                || (south.getBlock() instanceof TreeBushLikeBlock && south.get(TYPE) == TreeBushLikeTypes.CROSSING_STEM)
                || (east.getBlock() instanceof TreeBushLikeBlock && east.get(TYPE) == TreeBushLikeTypes.CROSSING_STEM)
                || (west.getBlock() instanceof TreeBushLikeBlock && west.get(TYPE) == TreeBushLikeTypes.CROSSING_STEM))
            return true;
        if (floor.isOf(ModBlocks.GLOW_APPLE_BUSH)) {
            if ((floor.get(TYPE) == TreeBushLikeTypes.BUSH && !floor.get(GROWN))
                    || (floor.get(TYPE) == TreeBushLikeTypes.STEM))
                return true;
            if (floor.get(TYPE) == TreeBushLikeTypes.CROOKED_STEM)
                return true;
        }
        return super.canPlaceAt(state, world, pos);
    }

    public enum TreeBushLikeTypes implements StringIdentifiable {
        STEM("stem"),
        CROOKED_STEM("crooked_stem"),
        TURN_STEM("turn_stem"),
        CROSSING_STEM("crossing_stem"),
        BUSH("bush");

        TreeBushLikeTypes(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String asString() {
            return name;
        }
    }
}