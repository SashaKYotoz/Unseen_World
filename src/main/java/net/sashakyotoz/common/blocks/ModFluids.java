package net.sashakyotoz.common.blocks;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterFluid;

public class ModFluids {
    public static void register() {
        UnseenWorld.log("Registering Fluids for modid : " + UnseenWorld.MOD_ID);
    }

    public static final FlowableFluid DARK_FLOWING_WATER = registerFluid("dark_flowing_water", new DarkWaterFluid.Flowing());
    public static final FlowableFluid DARK_WATER = registerFluid("dark_water", new DarkWaterFluid.Still());

    private static <T extends Fluid> T registerFluid(String id, T value) {
        return Registry.register(Registries.FLUID, UnseenWorld.makeID(id), value);
    }
}