
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.unseenworld.managers.AncientTransientBlockCloseOnBlockRightClickedProcedure;

public class AncientTransientBlockCloseBlock extends Block {
	public AncientTransientBlockCloseBlock() {
		super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.SHROOMLIGHT).requiresCorrectToolForDrops().strength(10f).lightLevel(s -> 1).hasPostProcess((bs, br, bp) -> true)
				.emissiveRendering((bs, br, bp) -> true));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
		if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem)
			return tieredItem.getTier().getLevel() >= 4;
		return false;
	}

	@Override
	public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		super.use(blockstate, world, pos, player, hand, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		AncientTransientBlockCloseOnBlockRightClickedProcedure.execute(world, x, y, z, player);
		return InteractionResult.SUCCESS;
	}
}
