package net.sashakyotoz.unseenworld;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModInitialization {
	public ModInitialization() {
	}

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnseenWorldModCommonConfigs.SPEC, "unseenworld-common.toml"));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new ModInitialization();
	}

	@Mod.EventBusSubscriber
	private static class ForgeBusEvents {
		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void serverLoad(ServerStartingEvent event) {
			UnseenWorldModItemProperties.addCustomItemProperties();
		}
	}
}
