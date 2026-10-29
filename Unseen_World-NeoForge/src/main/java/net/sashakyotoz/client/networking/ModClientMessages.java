package net.sashakyotoz.client.networking;

import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.sashakyotoz.client.environment.weather.Grippfall;
import net.sashakyotoz.common.networking.WeatherSyncPayload;

public final class ModClientMessages {
    private ModClientMessages() {
    }

    public static void onWeather(WeatherSyncPayload payload) {
        Grippfall.isGrippfallActive = payload.active();
        Grippfall.direction = payload.direction();
    }

    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        Grippfall.isGrippfallActive = false;
    }
}
