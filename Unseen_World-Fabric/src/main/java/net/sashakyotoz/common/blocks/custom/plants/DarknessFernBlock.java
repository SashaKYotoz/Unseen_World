package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.ModBlocks;

public class DarknessFernBlock extends FernBlock {
    private final boolean isLitToggleable;
    public static final BooleanProperty LIT = Properties.LIT;

    public DarknessFernBlock(Settings settings, boolean isLitToggleable) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
        this.isLitToggleable = isLitToggleable;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(this.isLitToggleable ? 31 : 61) == 20 && state.get(LIT) && world.getBlockState(pos.up()).isAir())
            world.addParticle(this.isLitToggleable ? ParticleTypes.END_ROD : ParticleTypes.CRIT, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, random.nextBetween(-1, 2) / 20f, 0.1D, random.nextBetween(-1, 2) / 20f);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        TallPlantBlock tallPlantBlock = (TallPlantBlock) ModBlocks.TALL_GLOOMWEED;
        if (tallPlantBlock.getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up())) {
            TallPlantBlock.placeAt(world, tallPlantBlock.getDefaultState(), pos, 2);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient() && this.isLitToggleable && random.nextFloat() > 0.76)
            world.setBlockState(pos, state.with(LIT, !state.get(LIT)), Block.NOTIFY_LISTENERS);
    }
}