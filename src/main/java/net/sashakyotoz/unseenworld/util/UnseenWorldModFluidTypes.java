package net.sashakyotoz.unseenworld.util;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.fluid.types.DarkWaterFluidType;
import net.sashakyotoz.unseenworld.fluid.types.LiquidOfChimeryFluidType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fluids.FluidType;

public class UnseenWorldModFluidTypes {
	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<FluidType> DARK_WATER_TYPE = REGISTRY.register("dark_water", () -> new DarkWaterFluidType());
	public static final RegistryObject<FluidType> LIQUID_OF_CHIMERY_TYPE = REGISTRY.register("liquid_of_chimery", () -> new LiquidOfChimeryFluidType());
}
