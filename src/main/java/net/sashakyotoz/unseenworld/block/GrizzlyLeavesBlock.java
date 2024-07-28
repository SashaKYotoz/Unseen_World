
package net.sashakyotoz.unseenworld.block;

import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class GrizzlyLeavesBlock extends LeavesBlock {
	public GrizzlyLeavesBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).sound(SoundType.NETHER_WART).strength(0.6f).requiresCorrectToolForDrops().noOcclusion());
	}
	public void animateTick(BlockState state, Level p_221375_, BlockPos p_221376_, RandomSource p_221377_) {
		if (p_221377_.nextInt(15) == 1) {
			double d0 = (double) p_221376_.getX() + p_221377_.nextDouble();
			double d1 = (double) p_221376_.getY() - 0.05D;
			double d2 = (double) p_221376_.getZ() + p_221377_.nextDouble();
			p_221375_.addParticle(UnseenWorldParticleTypes.GRIZZLY_PARTICLE.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
}
