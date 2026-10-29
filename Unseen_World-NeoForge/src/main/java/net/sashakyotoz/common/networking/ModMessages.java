package net.sashakyotoz.common.networking;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sashakyotoz.client.networking.ModClientMessages;

public final class ModMessages {
    private static final String PROTOCOL_VERSION = "1";

    private ModMessages() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION).optional();
        registrar.playToClient(WeatherSyncPayload.TYPE, WeatherSyncPayload.STREAM_CODEC, ModMessages::handleWeather);
    }

    private static void handleWeather(WeatherSyncPayload payload, IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            context.enqueueWork(() -> ModClientMessages.onWeather(payload));
        }
    }

    public static void sendWeather(ServerPlayer player, boolean active, String direction) {
        if (player.connection != null && player.connection.hasChannel(WeatherSyncPayload.TYPE)) {
            PacketDistributor.sendToPlayer(player, new WeatherSyncPayload(active, direction));
        }
    }
}
