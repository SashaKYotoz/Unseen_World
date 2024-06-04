package net.sashakyotoz.unseenworld.registries;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

import net.sashakyotoz.unseenworld.fluid.LiquidOfChimeryFluid;
import net.sashakyotoz.unseenworld.fluid.DarkWaterFluid;

public class UnseenWorldModFluids {
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, UnseenWorldMod.MODID);
	public static final RegistryObject<FlowingFluid> DARK_WATER = REGISTRY.register("dark_water", DarkWaterFluid.Source::new);
	public static final RegistryObject<FlowingFluid> FLOWING_DARK_WATER = REGISTRY.register("flowing_dark_water", DarkWaterFluid.Flowing::new);
	public static final RegistryObject<FlowingFluid> LIQUID_OF_CHIMERY = REGISTRY.register("liquid_of_chimery", LiquidOfChimeryFluid.Source::new);
	public static final RegistryObject<FlowingFluid> FLOWING_LIQUID_OF_CHIMERY = REGISTRY.register("flowing_liquid_of_chimery", LiquidOfChimeryFluid.Flowing::new);
}
