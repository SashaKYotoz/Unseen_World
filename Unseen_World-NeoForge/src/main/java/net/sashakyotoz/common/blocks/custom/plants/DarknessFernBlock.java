package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.sashakyotoz.common.blocks.ModBlocks;

public class DarknessFernBlock extends TallGrassBlock {
    private final boolean isLitToggleable;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public DarknessFernBlock(Properties settings, boolean isLitToggleable) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
        this.isLitToggleable = isLitToggleable;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(this.isLitToggleable ? 31 : 61) == 20 && state.getValue(LIT) && world.getBlockState(pos.above()).isAir())
            world.addParticle(this.isLitToggleable ? ParticleTypes.END_ROD : ParticleTypes.CRIT, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, random.nextIntBetweenInclusive(-1, 2) / 20f, 0.1D, random.nextIntBetweenInclusive(-1, 2) / 20f);
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        DoublePlantBlock tallPlantBlock = (DoublePlantBlock) ModBlocks.TALL_GLOOMWEED;
        if (tallPlantBlock.defaultBlockState().canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
            DoublePlantBlock.placeAt(world, tallPlantBlock.defaultBlockState(), pos, 2);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isClientSide() && this.isLitToggleable && random.nextFloat() > 0.76)
            world.setBlock(pos, state.setValue(LIT, !state.getValue(LIT)), Block.UPDATE_CLIENTS);
    }
}