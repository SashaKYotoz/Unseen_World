package net.sashakyotoz.common.blocks.custom.plants;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class FruitingVinesHeadBlock extends GrowingPlantHeadBlock implements ModPlantVines {
    @Override
    protected MapCodec<? extends GrowingPlantHeadBlock> codec() {
        return MapCodec.unit(this);
    }

    private final Supplier<Block> body;
    private final Supplier<? extends ItemLike> fruit;
    private final TagKey<Block> growableOn;

    public FruitingVinesHeadBlock(Properties settings, Supplier<Block> body, Supplier<? extends ItemLike> fruit, TagKey<Block> growableOn) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1D);
        this.body = body;
        this.fruit = fruit;
        this.growableOn = growableOn;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(FRUITS, false));
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }

    @Override
    protected Block getBodyBlock() {
        return this.body.get();
    }

    @Override
    protected BlockState updateBodyAfterConvertedFromHead(BlockState from, BlockState to) {
        return to.setValue(FRUITS, from.getValue(FRUITS));
    }

    @Override
    protected BlockState getGrowIntoState(BlockState state, RandomSource random) {
        return super.getGrowIntoState(state, random).setValue(FRUITS, random.nextFloat() < 0.11F);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return this.getFruitToDrop();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return this.pickBerries(player, state, level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRUITS);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos supportPos = pos.relative(this.growthDirection.getOpposite());
        BlockState support = level.getBlockState(supportPos);
        return this.canAttachTo(support) && (support.is(this) || support.is(this.getBodyBlock())
                || support.isFaceSturdy(level, supportPos, this.growthDirection) || support.is(this.growableOn));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(FRUITS);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(FRUITS, true), Block.UPDATE_CLIENTS);
    }

    @Override
    public ItemStack getFruitToDrop() {
        return new ItemStack(this.fruit.get());
    }
}
