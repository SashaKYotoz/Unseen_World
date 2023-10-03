
/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

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
	public static final RegistryObject<FlowingFluid> DARK_WATER = REGISTRY.register("dark_water", () -> new DarkWaterFluid.Source());
	public static final RegistryObject<FlowingFluid> FLOWING_DARK_WATER = REGISTRY.register("flowing_dark_water", () -> new DarkWaterFluid.Flowing());
	public static final RegistryObject<FlowingFluid> LIQUID_OF_CHIMERY = REGISTRY.register("liquid_of_chimery", () -> new LiquidOfChimeryFluid.Source());
	public static final RegistryObject<FlowingFluid> FLOWING_LIQUID_OF_CHIMERY = REGISTRY.register("flowing_liquid_of_chimery", () -> new LiquidOfChimeryFluid.Flowing());

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientSideHandler {
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			ItemBlockRenderTypes.setRenderLayer(DARK_WATER.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(FLOWING_DARK_WATER.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(LIQUID_OF_CHIMERY.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(FLOWING_LIQUID_OF_CHIMERY.get(), RenderType.translucent());
		}
	}
}
