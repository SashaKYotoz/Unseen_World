package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class LeafDroppingLeaveBlock extends LeavesBlock {
    public static final BooleanProperty OVERGROWN = BooleanProperty.of("overgrown");
    private final ParticleEffect type;

    public LeafDroppingLeaveBlock(Settings settings, ParticleEffect type) {
        super(settings);
        this.type = type;
        this.setDefaultState(
                this.stateManager.getDefaultState().with(DISTANCE, 7).with(PERSISTENT, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE).with(OVERGROWN, Boolean.FALSE)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OVERGROWN, DISTANCE, PERSISTENT, WATERLOGGED);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextInt(15) == 5 && state.get(OVERGROWN)) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (!isFaceFullSquare(blockState.getCollisionShape(world, blockPos), Direction.UP))
                ParticleUtil.spawnParticle(world, pos, random, type);
        }
    }
}