package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class FruitingVinesBodyBlock extends GrowingPlantBodyBlock implements ModPlantVines {
    private final Supplier<Block> head;
    private final Supplier<? extends ItemLike> fruit;
    private final TagKey<Block> growableOn;

    public FruitingVinesBodyBlock(Properties settings, Supplier<Block> head, Supplier<? extends ItemLike> fruit, TagKey<Block> growableOn) {
        super(settings, Direction.DOWN, SHAPE, false);
        this.head = head;
        this.fruit = fruit;
        this.growableOn = growableOn;
        this.registerDefaultState(this.stateDefinition.any().setValue(FRUITS, false));
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) this.head.get();
    }

    @Override
    protected BlockState updateHeadAfterConvertedFromBody(BlockState from, BlockState to) {
        return to.setValue(FRUITS, from.getValue(FRUITS));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return this.getFruitToDrop();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return this.pickBerries(player, state, level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRUITS);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos supportPos = pos.relative(this.growthDirection.getOpposite());
        BlockState support = level.getBlockState(supportPos);
        return this.canAttachTo(support) && (support.is(this.getHeadBlock()) || support.is(this)
                || support.isFaceSturdy(level, supportPos, this.growthDirection) || support.is(this.growableOn));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
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
