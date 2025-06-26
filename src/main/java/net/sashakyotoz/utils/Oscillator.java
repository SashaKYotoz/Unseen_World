package net.sashakyotoz.utils;

import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.client.environment.WorldClientEventsHandler;

public class Oscillator {
    private static final int TICKS_PER_SECOND = 20;
    private static final int PERIOD_IN_SECONDS = 5;
    private static final int PERIOD_IN_TICKS = PERIOD_IN_SECONDS * TICKS_PER_SECOND;

    public static double getOscillatingValue(int tickCount) {
        double phase = (2 * Math.PI * (tickCount % PERIOD_IN_TICKS)) / PERIOD_IN_TICKS;
        return 0.5 * (1 + Math.sin(phase));
    }

    public static double getOscillatingOppositeValue(int tickCount) {
        double phase = (2 * Math.PI * (tickCount % PERIOD_IN_TICKS)) / PERIOD_IN_TICKS;
        return 0.5 * (1 + Math.cos(phase));
    }

    public static double getOscillatingWithNegativeValue(int tickCount) {
        double phase = (2 * Math.PI * (tickCount % PERIOD_IN_TICKS)) / PERIOD_IN_TICKS;
        return 0.5 * Math.sin(phase);
    }

    public static float getOscillatingValue() {
        int tickCount = 0;
        if (WorldClientEventsHandler.halfTicks != null)
            tickCount = Math.round(WorldClientEventsHandler.halfTicks.get(0) * 2);
        float phase = (float) ((2 * Math.PI * (tickCount % PERIOD_IN_TICKS)) / PERIOD_IN_TICKS);
        return 0.5F * (1 + MathHelper.sin(phase));
    }

    public static float calculateDecreasingValue(float valueToDecrease) {
        float value = 1.0f - valueToDecrease;
        return Math.max(0, Math.min(1, value));
    }
}