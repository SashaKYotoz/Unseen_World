package net.sashakyotoz.unseenworld.registries;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;

import net.sashakyotoz.unseenworld.fluid.LiquidOfChimeryFluid;
import net.sashakyotoz.unseenworld.fluid.DarkWaterFluid;
import net.sashakyotoz.unseenworld.fluid.types.DarkWaterFluidType;
import net.sashakyotoz.unseenworld.fluid.types.LiquidOfChimeryFluidType;

public class UnseenWorldFluids {
	//fluids
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, UnseenWorldMod.MODID);
	public static final RegistryObject<FlowingFluid> DARK_WATER = REGISTRY.register("dark_water", DarkWaterFluid.Source::new);
	public static final RegistryObject<FlowingFluid> FLOWING_DARK_WATER = REGISTRY.register("flowing_dark_water", DarkWaterFluid.Flowing::new);
	public static final RegistryObject<FlowingFluid> LIQUID_OF_CHIMERY = REGISTRY.register("liquid_of_chimery", LiquidOfChimeryFluid.Source::new);
	public static final RegistryObject<FlowingFluid> FLOWING_LIQUID_OF_CHIMERY = REGISTRY.register("flowing_liquid_of_chimery", LiquidOfChimeryFluid.Flowing::new);
	//types
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<FluidType> DARK_WATER_TYPE = FLUID_TYPES.register("dark_water", DarkWaterFluidType::new);
	public static final RegistryObject<FluidType> LIQUID_OF_CHIMERY_TYPE = FLUID_TYPES.register("liquid_of_chimery", LiquidOfChimeryFluidType::new);
	public static void register(IEventBus bus){
		REGISTRY.register(bus);
		FLUID_TYPES.register(bus);
	}
}
