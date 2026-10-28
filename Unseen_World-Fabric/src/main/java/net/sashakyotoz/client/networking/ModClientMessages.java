package net.sashakyotoz.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.sashakyotoz.client.environment.weather.Grippfall;
import net.sashakyotoz.common.networking.WeatherSyncPayload;

public final class ModClientMessages {
    private ModClientMessages() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(WeatherSyncPayload.TYPE, (payload, context) -> {
            Grippfall.isGrippfallActive = payload.active();
            Grippfall.direction = payload.direction();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> client.execute(() -> Grippfall.isGrippfallActive = false));
    }
}
