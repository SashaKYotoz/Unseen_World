
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.sashakyotoz.unseenworld.network.MiningBootsAbilityMessage;
import net.sashakyotoz.unseenworld.network.BlazerHelmetAbilityMessage;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class UnseenWorldModKeyMappings {
	public static final KeyMapping BLAZER_HELMET_ABILITY = new KeyMapping("key.unseen_world.blazer_helmet_ability", GLFW.GLFW_KEY_UP, "key.categories.abilities") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				BLAZER_HELMET_ABILITY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - BLAZER_HELMET_ABILITY_LASTPRESS);
				UnseenWorldMod.PACKET_HANDLER.sendToServer(new BlazerHelmetAbilityMessage(1, dt));
				BlazerHelmetAbilityMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping MINING_BOOTS_ABILITY = new KeyMapping("key.unseen_world.mining_boots_ability", GLFW.GLFW_KEY_DOWN, "key.categories.abilities") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				UnseenWorldMod.PACKET_HANDLER.sendToServer(new MiningBootsAbilityMessage(0, 0));
				MiningBootsAbilityMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	private static long BLAZER_HELMET_ABILITY_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(BLAZER_HELMET_ABILITY);
		event.register(MINING_BOOTS_ABILITY);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				BLAZER_HELMET_ABILITY.consumeClick();
				MINING_BOOTS_ABILITY.consumeClick();
			}
		}
	}
}
