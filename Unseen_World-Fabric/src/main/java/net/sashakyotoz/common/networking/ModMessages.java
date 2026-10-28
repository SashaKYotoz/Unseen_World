package net.sashakyotoz.common.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class ModMessages {
    private ModMessages() {
    }

    public static void register() {
        PayloadTypeRegistry.playS2C().register(WeatherSyncPayload.TYPE, WeatherSyncPayload.STREAM_CODEC);
    }

    public static void sendWeather(ServerPlayer player, boolean active, String direction) {
        if (ServerPlayNetworking.canSend(player, WeatherSyncPayload.TYPE)) {
            ServerPlayNetworking.send(player, new WeatherSyncPayload(active, direction));
        }
    }
}
