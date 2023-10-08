
package net.sashakyotoz.unseenworld.block;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModFluids;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class DeepWaterAnfeltsiaBlock extends BushBlock implements LiquidBlockContainer, net.minecraftforge.common.IForgeShearable {
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

	public DeepWaterAnfeltsiaBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.PEONY).sound(SoundType.GRASS).instabreak().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 4).noCollission());
	}

	public VoxelShape getShape(BlockState p_154525_, BlockGetter p_154526_, BlockPos p_154527_, CollisionContext p_154528_) {
		return SHAPE;
	}

	protected boolean mayPlaceOn(BlockState p_154539_, BlockGetter p_154540_, BlockPos p_154541_) {
		return p_154540_.getBlockState(p_154541_.above()).is(UnseenWorldModBlocks.DARK_WATER.get()) || p_154540_.getBlockState(p_154541_.above()).is(Blocks.WATER);
	}

	public BlockState updateShape(BlockState p_154530_, Direction p_154531_, BlockState p_154532_, LevelAccessor p_154533_, BlockPos p_154534_, BlockPos p_154535_) {
		BlockState blockstate = super.updateShape(p_154530_, p_154531_, p_154532_, p_154533_, p_154534_, p_154535_);
		return blockstate;
	}

	public FluidState getFluidState(BlockState p_154537_) {
		return UnseenWorldModFluids.DARK_WATER.get().getSource(false);
	}

	@OnlyIn(Dist.CLIENT)
	public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
		event.getBlockColors().register((bs, world, pos, index) -> world != null && pos != null ? BiomeColors.getAverageWaterColor(world, pos) : -1, UnseenWorldModBlocks.DEEP_WATER_ANFELTSIA.get());
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter p_154505_, BlockPos p_154506_, BlockState p_154507_, Fluid p_154508_) {
		return false;
	}
	@Override
	public boolean placeLiquid(LevelAccessor p_154520_, BlockPos p_154521_, BlockState p_154522_, FluidState p_154523_) {
		return false;
	}
}
