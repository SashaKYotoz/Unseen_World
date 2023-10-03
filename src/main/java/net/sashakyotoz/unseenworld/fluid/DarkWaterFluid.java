
package net.sashakyotoz.unseenworld.fluid;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.sashakyotoz.unseenworld.init.*;

public abstract class DarkWaterFluid extends ForgeFlowingFluid {
	public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(UnseenWorldModFluidTypes.DARK_WATER_TYPE, UnseenWorldModFluids.DARK_WATER, UnseenWorldModFluids.FLOWING_DARK_WATER)
			.explosionResistance(100f).slopeFindDistance(5).bucket(UnseenWorldModItems.DARK_WATER_BUCKET).block(() -> (LiquidBlock) UnseenWorldModBlocks.DARK_WATER.get());

	private DarkWaterFluid() {
		super(PROPERTIES);
	}

	@Override
	public ParticleOptions getDripParticle() {
		return UnseenWorldModParticleTypes.BLUEVOIDPARTICLE.get();
	}

	public static class Source extends DarkWaterFluid {
		public int getAmount(FluidState state) {
			return 8;
		}

		public boolean isSource(FluidState state) {
			return true;
		}
	}

	public static class Flowing extends DarkWaterFluid {
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		public boolean isSource(FluidState state) {
			return false;
		}
	}
}
