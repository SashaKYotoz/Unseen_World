
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModParticleTypes;

public class GreenishBurlyWoodLeavesBlock extends Block {
	public GreenishBurlyWoodLeavesBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).sound(SoundType.GRASS).noOcclusion());
	}

	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidstate) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 1;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 150;
	}
	@Override
	public void animateTick(BlockState pState, Level level, BlockPos blockPos, RandomSource source) {
		super.animateTick(pState, level, blockPos, source);
		if (source.nextInt(10) == 0) {
			BlockPos blockpos = blockPos.below();
			BlockState blockstate = level.getBlockState(blockpos);
			if (!isFaceFull(blockstate.getCollisionShape(level, blockpos), Direction.UP)) {
				ParticleUtils.spawnParticleBelow(level, blockPos, source, UnseenWorldModParticleTypes.GREENISH_PARTICLE.get());
			}
		}
	}
}
