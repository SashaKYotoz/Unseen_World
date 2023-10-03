
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.block.ComposterBlock;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnseenWorldModCompostableItems {
	@SubscribeEvent
	public static void addComposterItems(FMLCommonSetupEvent event) {
		ComposterBlock.COMPOSTABLES.put(UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY.get().asItem(), 0.2f);
		ComposterBlock.COMPOSTABLES.put(UnseenWorldModBlocks.MISTERYFLOWER_SAPLING.get().asItem(), 0.2f);
	}
}
