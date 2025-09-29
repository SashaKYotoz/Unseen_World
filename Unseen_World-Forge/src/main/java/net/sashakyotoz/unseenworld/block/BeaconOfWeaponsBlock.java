package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.unseenworld.block.entity.BeaconOfWeaponsBlockEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BeaconOfWeaponsBlock extends BaseEntityBlock {
    public static final List<BlockPos> RUNE_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter((pos) -> Math.abs(pos.getX()) == 2 || Math.abs(pos.getZ()) == 2).map(BlockPos::immutable).toList();

    public BeaconOfWeaponsBlock(Properties properties) {
        super(properties);
    }

    public static boolean isValidRune(Level level, BlockPos pos, BlockPos pos1) {
        return level.getBlockState(pos.offset(pos1)).is(UnseenWorldBlocks.BEACON_RUNE.get()) && level.getBlockState(pos.offset(pos1.getX() / 2, pos1.getY(), pos1.getZ() / 2)).is(BlockTags.REPLACEABLE);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        super.animateTick(state, level, pos, source);
        for (BlockPos blockpos : RUNE_OFFSETS) {
            if (source.nextInt(12) == 0 && isValidRune(level, pos, blockpos))
                level.addParticle(level.getBiome(pos).is(Tags.Biomes.IS_HOT) ? UnseenWorldParticleTypes.REDNESS.get() : new ColorableParticleOption("sparkle",0.1f,0.25f,1f),
                        pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, ((float) blockpos.getX() + source.nextFloat()) - 0.5D, (float) blockpos.getY() - source.nextFloat() - 1.0F, ((float) blockpos.getZ() + source.nextFloat()) - 0.5D);
        }
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BeaconOfWeaponsBlockEntity(pos, state);
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof BeaconOfWeaponsBlockEntity beacon) {
            Containers.dropContents(worldIn, pos, beacon);
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return box(0, 0, 0, 16, 14, 16);
    }

    public static boolean isRuneNear(Level level, BlockPos pos) {
        int tmp = 0;
        for (int x = -3; x < 3; x++) {
            for (int z = -3; z < 3; z++) {
                BlockState tmpState = level.getBlockState(pos.offset(x, 0, z));
                if (tmpState.is(UnseenWorldBlocks.BEACON_RUNE.get()) && tmp != 4) {
                    tmp++;
                    x = -3;
                    z = -3;
                }
            }
        }
        return tmp == 4;
    }
}
