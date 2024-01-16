
package net.sashakyotoz.unseenworld.network;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import net.sashakyotoz.unseenworld.managers.BlazerHelmetHelmetTickEventProcedure;

import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlazerHelmetAbilityMessage {
	int type, pressedms;

	public BlazerHelmetAbilityMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public BlazerHelmetAbilityMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(BlazerHelmetAbilityMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(BlazerHelmetAbilityMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(Objects.requireNonNull(context.getSender()), message.type, message.pressedms);
		});
		context.setPacketHandled(true);
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 1) {

			BlazerHelmetHelmetTickEventProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		UnseenWorldMod.addNetworkMessage(BlazerHelmetAbilityMessage.class, BlazerHelmetAbilityMessage::buffer, BlazerHelmetAbilityMessage::new, BlazerHelmetAbilityMessage::handler);
	}
}
