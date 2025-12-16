package net.sashakyotoz.client.environment;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.HashMap;

public class ClientTicks implements ClientTickEvents.EndTick {
    public static HashMap<Integer, Float> halfTicks = new HashMap<>();

    @Override
    public void onEndTick(MinecraftClient client) {
        halfTicks.put(0, halfTicks.get(0) == null ? 0.5f : halfTicks.get(0) + 0.5f);
    }

    public static int getTicks() {
        if (ClientTicks.halfTicks != null)
            return Math.round(halfTicks.get(0) * 2);
        else return 0;
    }

    public static float getHalfTicks() {
        if (ClientTicks.halfTicks != null)
            return halfTicks.get(0);
        else return 0;
    }
}