package net.sashakyotoz.client.environment;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.HashMap;

public class ClientTicks {
    public static HashMap<Integer, Float> halfTicks = new HashMap<>();

    public static void onEndTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        halfTicks.put(0, halfTicks.get(0) == null ? 0.5f : halfTicks.get(0) + 0.5f);

        ChimericFog.tick(client);
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