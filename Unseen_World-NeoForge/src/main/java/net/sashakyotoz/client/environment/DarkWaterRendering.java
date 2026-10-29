package net.sashakyotoz.client.environment;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModFluids;

public final class DarkWaterRendering {
    public static final ResourceLocation STILL = UnseenWorld.MOD.makeID("block/dark_water_still");
    public static final ResourceLocation FLOWING = UnseenWorld.MOD.makeID("block/dark_water_flow");
    public static final ResourceLocation OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");
    public static final int TINT = 0xFF000000 | (0x75050005 & 0x00FFFFFF);

    public static final IClientFluidTypeExtensions EXTENSIONS = new IClientFluidTypeExtensions() {
        @Override
        public ResourceLocation getStillTexture() {
            return STILL;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return FLOWING;
        }

        @Override
        public ResourceLocation getOverlayTexture() {
            return OVERLAY;
        }

        @Override
        public int getTintColor() {
            return TINT;
        }

        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return TINT;
        }
    };

    private DarkWaterRendering() {
    }

    public static boolean isDarkWater(FluidState state) {
        return state.getType().isSame(ModFluids.DARK_WATER);
    }

    public static IClientFluidTypeExtensions extensionsFor(FluidState state, IClientFluidTypeExtensions fallback) {
        return isDarkWater(state) ? EXTENSIONS : fallback;
    }
}
