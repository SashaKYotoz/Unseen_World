package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.*;

public class EngravedBricksBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty GRIPPED = BooleanProperty.create("gripped");
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final int SIGNAL = 8;
    private static final int MAX_NETWORK_SIZE = 256;
    private static final float ROTATING_FALL_DISTANCE = 0.6F;

    public EngravedBricksBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(GRIPPED, false).setValue(HORIZONTAL_FACING, Direction.NORTH));
    }

    public static boolean feeds(BlockState state, Direction towards) {
        return state.getBlock() instanceof EngravedBricksBlock
                && state.getValue(GRIPPED)
                && state.getValue(HORIZONTAL_FACING) == towards;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GRIPPED, HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (!level.isClientSide && (!oldState.is(this) || oldState.getValue(HORIZONTAL_FACING) != state.getValue(HORIZONTAL_FACING))) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!level.isClientSide && !newState.is(this)) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = pos.relative(direction);
                if (level.getBlockState(neighbor).is(this)) {
                    level.scheduleTick(neighbor, this, 1);
                }
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moved) {
        if (!level.isClientSide && !(block instanceof EngravedBricksBlock)) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Map<BlockPos, BlockState> network = this.collectNetwork(level, pos);
        Set<BlockPos> powered = new HashSet<>();
        for (Map.Entry<BlockPos, BlockState> entry : network.entrySet()) {
            Direction facing = entry.getValue().getValue(HORIZONTAL_FACING);
            if (isSource(level.getBlockState(entry.getKey().relative(facing.getOpposite())))) {
                BlockPos cursor = entry.getKey();
                while (network.containsKey(cursor) && powered.add(cursor)) {
                    cursor = cursor.relative(network.get(cursor).getValue(HORIZONTAL_FACING));
                }
            }
        }
        int lit = 0;
        int dimmed = 0;
        for (Map.Entry<BlockPos, BlockState> entry : network.entrySet()) {
            boolean gripped = powered.contains(entry.getKey());
            if (entry.getValue().getValue(GRIPPED) != gripped) {
                level.setBlock(entry.getKey(), entry.getValue().setValue(GRIPPED, gripped), Block.UPDATE_ALL);
                if (gripped) {
                    lit++;
                } else {
                    dimmed++;
                }
            }
        }
        if (lit > 0) {
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.BLOCKS, 0.9F, 1.5F);
        } else if (dimmed > 0) {
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_XYLOPHONE.value(), SoundSource.BLOCKS, 0.9F, 0.5F);
        }
    }

    private Map<BlockPos, BlockState> collectNetwork(Level level, BlockPos origin) {
        Map<BlockPos, BlockState> network = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        BlockState originState = level.getBlockState(origin);
        if (!originState.is(this)) {
            return network;
        }
        network.put(origin, originState);
        queue.add(origin);
        while (!queue.isEmpty() && network.size() < MAX_NETWORK_SIZE) {
            BlockPos current = queue.poll();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos next = current.relative(direction);
                if (!network.containsKey(next)) {
                    BlockState nextState = level.getBlockState(next);
                    if (nextState.is(this)) {
                        network.put(next, nextState);
                        queue.add(next);
                    }
                }
            }
        }
        return network;
    }

    private static boolean isSource(BlockState state) {
        return state.getBlock() instanceof GrippingStoneBlock;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!level.isClientSide && entity instanceof Player player && !player.isSpectator() && fallDistance > ROTATING_FALL_DISTANCE) {
            Direction facing = state.getValue(HORIZONTAL_FACING);
            Direction turned = player.isShiftKeyDown() ? facing.getCounterClockWise() : facing.getClockWise();
            level.setBlock(pos, state.setValue(HORIZONTAL_FACING, turned), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.BASALT_FALL, SoundSource.BLOCKS, 1.0F, 0.5F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(GRIPPED) && random.nextInt(4) == 0) {
            Direction facing = state.getValue(HORIZONTAL_FACING);
            double x = pos.getX() + 0.5D + facing.getStepX() * 0.3D;
            double z = pos.getZ() + 0.5D + facing.getStepZ() * 0.3D;
            level.addParticle(ParticleTypes.REVERSE_PORTAL, x, pos.getY() + 1.05D, z, facing.getStepX() * 0.02D, 0.0D, facing.getStepZ() * 0.02D);
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return feeds(state, direction.getOpposite()) ? SIGNAL : 0;
    }
}