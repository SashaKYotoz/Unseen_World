package net.sashakyotoz.utils;

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
}