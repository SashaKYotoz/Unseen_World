
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;

import net.minecraft.world.item.ItemStack;

@Mod.EventBusSubscriber
public class UnseenWorldModFuels {
	@SubscribeEvent
	public static void furnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
		ItemStack itemstack = event.getItemStack();
		if (itemstack.getItem() == UnseenWorldModItems.DARK_WATER_BUCKET.get())
			event.setBurnTime(2400);
		else if (itemstack.getItem() == UnseenWorldModBlocks.SMALL_CRIMSERRY_SOUL_BERRY.get().asItem())
			event.setBurnTime(60);
	}
}
