
package net.sashakyotoz.unseenworld.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldFluids;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;

public abstract class LiquidOfChimeryFluid extends ForgeFlowingFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(UnseenWorldFluids.LIQUID_OF_CHIMERY_TYPE::get, () -> UnseenWorldFluids.LIQUID_OF_CHIMERY.get(),
            UnseenWorldFluids.FLOWING_LIQUID_OF_CHIMERY::get).explosionResistance(100f).slopeFindDistance(6).bucket(() -> UnseenWorldItems.LIQUID_OF_CHIMERY_BUCKET.get())
            .block(() -> (LiquidBlock) UnseenWorldBlocks.LIQUID_OF_CHIMERY.get());

    private LiquidOfChimeryFluid() {
        super(PROPERTIES);
    }

    @Override
    public ParticleOptions getDripParticle() {
        return UnseenWorldParticleTypes.LIQUID_OF_CHIMERY_PARTICLE.get();
    }

    public static class Source extends LiquidOfChimeryFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends LiquidOfChimeryFluid {
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

    private void fizz(LevelAccessor p_76213_, BlockPos p_76214_) {
        p_76213_.levelEvent(1501, p_76214_, 0);
    }

    protected void spreadTo(LevelAccessor p_76220_, BlockPos p_76221_, BlockState p_76222_, Direction p_76223_, FluidState p_76224_) {
        if (p_76223_ == Direction.DOWN) {
            FluidState fluidstate = p_76220_.getFluidState(p_76221_);
            if (fluidstate.is(UnseenWorldFluids.DARK_WATER.get())) {
                if (p_76222_.getBlock() instanceof LiquidBlock) {
                    p_76220_.setBlock(p_76221_, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(p_76220_, p_76221_, p_76221_, Blocks.BASALT.defaultBlockState()), 3);
                }
                this.fizz(p_76220_, p_76221_);
                return;
            }
        }

        super.spreadTo(p_76220_, p_76221_, p_76222_, p_76223_, p_76224_);
    }

}
