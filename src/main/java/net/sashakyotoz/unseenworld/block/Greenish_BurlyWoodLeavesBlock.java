
package net.sashakyotoz.unseenworld.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;

public class Greenish_BurlyWoodLeavesBlock extends Block {
	public Greenish_BurlyWoodLeavesBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).sound(SoundType.GRASS).strength(1f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
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

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState p_272714_, Level p_272837_, BlockPos p_273218_, RandomSource p_273360_) {
		super.animateTick(p_272714_, p_272837_, p_273218_, p_273360_);
		if (p_273360_.nextInt(10) == 0) {
			BlockPos blockpos = p_273218_.below();
			BlockState blockstate = p_272837_.getBlockState(blockpos);
			if (!isFaceFull(blockstate.getCollisionShape(p_272837_, blockpos), Direction.UP)) {
				ParticleUtils.spawnParticleBelow(p_272837_, p_273218_, p_273360_, UnseenWorldModParticleTypes.GREENISH_PARTICLE.get());
			}
		}
	}
}
