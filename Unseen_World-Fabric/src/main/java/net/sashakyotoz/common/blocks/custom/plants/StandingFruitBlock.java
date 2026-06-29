package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.ToIntFunction;

public class StandingFruitBlock extends BushBlock implements BonemealableBlock {
    public ItemLike drop;
    public final TagKey<Block> canStayOn;
    private final VoxelShape plantShape;

    public static final BooleanProperty HAS_FLOWER = BooleanProperty.create("has_flower");
    public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");
    public static final IntegerProperty LUMINANCE = IntegerProperty.create("luminance",0,12);
    public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = state -> (Integer)state.getValue(LUMINANCE);

    public StandingFruitBlock(Properties settings, ItemLike drop, TagKey<Block> growable_on, VoxelShape shape) {
        super(settings.randomTicks());
        this.drop = drop;
        this.canStayOn = growable_on;
        this.plantShape = shape;
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_FRUIT, false).setValue(HAS_FLOWER, true).setValue(LUMINANCE,0));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_FRUIT).add(HAS_FLOWER).add(LUMINANCE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextDouble() < 0.05 && this.isBonemealSuccess(world, random, pos, state))
            this.performBonemeal(world, random, pos, state);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(HAS_FRUIT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return plantShape;
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        if (state.getValue(HAS_FRUIT))
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.drop)));
        super.spawnAfterBreak(state, world, pos, tool, dropExperience);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
        if (world.getBlockState(pos.above()).getBlock() instanceof StandingFruitBlock)
            world.setBlockAndUpdate(pos,state.setValue(HAS_FLOWER,false).setValue(HAS_FRUIT,false).setValue(LUMINANCE,0));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(HAS_FRUIT)) {
            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(this.drop)));
            world.setBlockAndUpdate(pos, state.setValue(HAS_FRUIT, false).setValue(HAS_FLOWER, false));
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.getValue(HAS_FRUIT) || !state.getValue(HAS_FLOWER);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(this.canStayOn);
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return !state.getValue(HAS_FRUIT);
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if (random.nextInt(3) == 0 && world.getBlockState(pos.above()).isAir() &&
                world.getBlockState(pos.below()).is(canStayOn)) {
            if (random.nextBoolean()) {
                world.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(HAS_FLOWER, true).setValue(LUMINANCE,11));
                world.setBlockAndUpdate(pos, this.defaultBlockState().setValue(HAS_FLOWER, false));
            } else
                world.setBlockAndUpdate(pos, this.defaultBlockState().setValue(HAS_FLOWER, true).setValue(HAS_FRUIT, true).setValue(LUMINANCE,11));
        }
    }
}
