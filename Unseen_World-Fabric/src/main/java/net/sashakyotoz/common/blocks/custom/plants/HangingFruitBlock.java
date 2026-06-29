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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.tags.ModTags;

public class HangingFruitBlock extends HangingPlantBlock implements BonemealableBlock {
    public ItemLike drop;

    public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");

    public HangingFruitBlock(Properties settings, ItemLike drop, TagKey<Block> growable_on, VoxelShape shape) {
        super(settings.randomTicks(), growable_on, shape);
        this.drop = drop;
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_FRUIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_FRUIT);
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
    public void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        if (state.getValue(HAS_FRUIT))
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.drop)));
        super.spawnAfterBreak(state, world, pos, tool, dropExperience);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(HAS_FRUIT)) {
            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(this.drop)));
            world.setBlockAndUpdate(pos, state.setValue(HAS_FRUIT, false));

            return InteractionResult.SUCCESS;
        }

        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.getValue(HAS_FRUIT);
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return !state.getValue(HAS_FRUIT);
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if (random.nextInt(3) == 0 && world.getBlockState(pos.below()).isAir())
            world.setBlockAndUpdate(pos, state.setValue(HAS_FRUIT, true));

        if (state.is(ModTags.Blocks.HANGING_AMETHYST_LEAVES_GROWABLE_ON) && random.nextInt(4) == 0 && world.getBlockState(pos.below()).isAir())
            world.setBlockAndUpdate(pos.below(), random.nextBoolean() ? ModBlocks.HANGING_AMETHYST_LEAVES.defaultBlockState().setValue(HAS_FRUIT, true) :
                    this.defaultBlockState());
        if (state.is(ModTags.Blocks.CRIMSONVEIL_VINES_GROWABLE_ON) && random.nextInt(4) == 0 && world.getBlockState(pos.below()).isAir()) {
            world.setBlockAndUpdate(pos, ModBlocks.CRIMSONVEIL_VINE_PLANT.defaultBlockState());
            world.setBlockAndUpdate(pos.below(), ModBlocks.CRIMSONVEIL_VINE.defaultBlockState());
        }
        if (state.is(ModTags.Blocks.HANGING_BURLYWOOD_LEAVES_GROWABLE_ON)
                && state.hasProperty(HAS_FRUIT)
                && !state.getValue(HAS_FRUIT)
                && random.nextInt(4) == 0
                && world.getBlockState(pos.below()).isAir()) {
            if (random.nextBoolean())
                world.setBlockAndUpdate(pos, state.setValue(HAS_FRUIT, true));
            else
                world.setBlockAndUpdate(pos.below(), ModBlocks.HANGING_BURLYWOOD_LEAVES.defaultBlockState());
        }
    }
}
