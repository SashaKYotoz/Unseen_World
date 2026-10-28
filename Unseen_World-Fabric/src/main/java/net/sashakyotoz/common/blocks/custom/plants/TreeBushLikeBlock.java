package net.sashakyotoz.common.blocks.custom.plants;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;

public class TreeBushLikeBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<TreeBushLikeBlock> CODEC = simpleCodec(TreeBushLikeBlock::new);

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    public static final BooleanProperty GROWN = BooleanProperty.create("grown");
    public static final EnumProperty<BushTypes> TYPE = EnumProperty.create("type", BushTypes.class);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape STEM_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    private static final VoxelShape STEM_8X_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 12.0, 10.0);
    private static final VoxelShape N_TURN_STEM_SHAPE = Shapes.or(STEM_8X_SHAPE, Block.box(0.0, 8.0, 6.0, 16.0, 12.0, 10.0));
    private static final VoxelShape S_TURN_STEM_SHAPE = Shapes.or(STEM_8X_SHAPE, Block.box(6.0, 8.0, 0.0, 10.0, 12.0, 16.0));
    private static final VoxelShape BUSH_SHAPE = Shapes.or(Block.box(6.0, 0.0, 6.0, 10.0, 8.0, 10.0), Block.box(2.0, 2.0, 2.0, 14.0, 16.0, 14.0));
    private static final VoxelShape CROOKED_TURN_SHAPE = Block.box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    private static final VoxelShape CROSSING_STEM_SHAPE = Shapes.or(N_TURN_STEM_SHAPE, S_TURN_STEM_SHAPE);

    public TreeBushLikeBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(GROWN, Boolean.FALSE).setValue(TYPE, BushTypes.BUSH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, GROWN, TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (isBushOfType(world.getBlockState(pos.relative(direction)), BushTypes.TURN_STEM)) {
                return this.defaultBlockState().setValue(FACING, direction).setValue(TYPE, BushTypes.CROOKED_STEM);
            }
        }
        BlockState floor = world.getBlockState(pos.below());
        Direction facing = ctx.getNearestLookingDirection().getOpposite();
        if (isBushOfType(floor, BushTypes.CROOKED_STEM) || isBushOfType(floor, BushTypes.STEM)) {
            return this.defaultBlockState().setValue(FACING, facing).setValue(TYPE, world.getRandom().nextBoolean() ? BushTypes.BUSH : BushTypes.STEM);
        }
        if (isBushOfType(floor, BushTypes.BUSH)) {
            world.setBlockAndUpdate(pos.below(), floor.setValue(TYPE, BushTypes.STEM));
            return this.defaultBlockState().setValue(FACING, facing).setValue(TYPE, BushTypes.STEM);
        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(TYPE, BushTypes.BUSH);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (Direction.Plane.HORIZONTAL.stream().allMatch(side -> isBushOfType(world.getBlockState(pos.relative(side)), BushTypes.CROOKED_STEM))) {
            return state.setValue(TYPE, BushTypes.CROSSING_STEM);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);
        if (random.nextInt(9) == 3 && state.getValue(TYPE) == BushTypes.BUSH)
            world.setBlockAndUpdate(pos, state.setValue(GROWN, true));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(GROWN) && state.getValue(TYPE) == BushTypes.BUSH) {
            return FruitBearing.harvest(world, pos, state.setValue(GROWN, false), new ItemStack(ModItems.GLOW_APPLE), player);
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) && !state.getValue(GROWN);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case STEM -> STEM_SHAPE;
            case CROOKED_STEM -> CROOKED_TURN_SHAPE;
            case CROSSING_STEM -> CROSSING_STEM_SHAPE;
            case TURN_STEM ->
                    (state.getValue(FACING).getOpposite() == Direction.NORTH || state.getValue(FACING).getOpposite() == Direction.SOUTH)
                            ? S_TURN_STEM_SHAPE : N_TURN_STEM_SHAPE;
            case BUSH -> BUSH_SHAPE;
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = world.getBlockState(pos.relative(direction));
            if (isBushOfType(neighbor, BushTypes.TURN_STEM) || isBushOfType(neighbor, BushTypes.CROSSING_STEM)) {
                return true;
            }
        }
        BlockState floor = world.getBlockState(pos.below());
        if (isBushOfType(floor, BushTypes.STEM) || isBushOfType(floor, BushTypes.CROOKED_STEM)
                || (isBushOfType(floor, BushTypes.BUSH) && !floor.getValue(GROWN))) {
            return true;
        }
        return super.canSurvive(state, world, pos);
    }

    private static boolean isBushOfType(BlockState state, BushTypes type) {
        return state.getBlock() instanceof TreeBushLikeBlock && state.getValue(TYPE) == type;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return state.getValue(TYPE) == BushTypes.BUSH && world.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        world.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap(registry ->
                registry.getHolder(ModConfiguredFeatures.BUSH_LIKE_TREE_PATCH)).ifPresent(reference ->
                reference.value().place(world, world.getChunkSource().getGenerator(),
                        RandomSource.create(pos.asLong()), pos));
    }

    public enum BushTypes implements StringRepresentable {
        STEM("stem"),
        CROOKED_STEM("crooked_stem"),
        TURN_STEM("turn_stem"),
        CROSSING_STEM("crossing_stem"),
        BUSH("bush");

        BushTypes(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}