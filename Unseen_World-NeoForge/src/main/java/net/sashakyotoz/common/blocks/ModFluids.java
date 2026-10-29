package net.sashakyotoz.common.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterFluid;
import net.sashakyotoz.common.registry.ModRegistrar;

public class ModFluids {
    public static void register() {
        UnseenWorld.log().debug("Registering Fluids for modid : " + UnseenWorld.MOD_ID);
    }

    public static final FlowingFluid DARK_FLOWING_WATER = registerFluid("dark_flowing_water", new DarkWaterFluid.Flowing());
    public static final FlowingFluid DARK_WATER = registerFluid("dark_water", new DarkWaterFluid.Still());

    private static <T extends Fluid> T registerFluid(String id, T value) {
        return ModRegistrar.register(Registries.FLUID, UnseenWorld.MOD.makeID(id), value);
    }
}