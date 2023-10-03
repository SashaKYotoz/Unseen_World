
package net.sashakyotoz.unseenworld.block;

import net.sashakyotoz.unseenworld.init.UnseenWorldModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class GrizzlyLeavesBlock extends LeavesBlock {
	public GrizzlyLeavesBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).sound(SoundType.NETHER_WART).strength(0.6000000000000001f).requiresCorrectToolForDrops()
				.noOcclusion());
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 1;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 90;
	}

	protected boolean decaying(BlockState p_221386_) {
		return !p_221386_.getValue(PERSISTENT) && p_221386_.getValue(DISTANCE) == 9;
	}

	private static int getDistanceAt(BlockState p_54464_) {
		if (p_54464_.is(BlockTags.LOGS)) {
			return 0;
		} else {
			return p_54464_.getBlock() instanceof LeavesBlock ? p_54464_.getValue(DISTANCE) : 9;
		}
	}

	public void animateTick(BlockState p_221374_, Level p_221375_, BlockPos p_221376_, RandomSource p_221377_) {
		if (p_221377_.nextInt(15) == 1) {
			BlockPos blockpos = p_221376_.below();
			BlockState blockstate = p_221375_.getBlockState(blockpos);
			double d0 = (double) p_221376_.getX() + p_221377_.nextDouble();
			double d1 = (double) p_221376_.getY() - 0.05D;
			double d2 = (double) p_221376_.getZ() + p_221377_.nextDouble();
			p_221375_.addParticle(UnseenWorldModParticleTypes.GRIZZLY_PARTICLE.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
		if (player.getInventory().getSelected().getItem() instanceof TieredItem tieredItem)
			return tieredItem.getTier().getLevel() >= 0;
		return false;
	}
}
