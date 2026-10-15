package net.sashakyotoz.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.sashakyotoz.client.environment.weather.Grippfall;
import net.sashakyotoz.common.networking.ModMessages;

public final class ModClientMessages {
    private ModClientMessages() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.WEATHER_PACKET_ID, (client, handler, buf, responseSender) -> {
            boolean active = buf.readBoolean();
            String direction = buf.readUtf();
            client.execute(() -> {
                Grippfall.isGrippfallActive = active;
                Grippfall.direction = direction;
            });
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> client.execute(() -> Grippfall.isGrippfallActive = false));
    }
}