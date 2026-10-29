package net.sashakyotoz.common.world.noise;

import net.minecraft.world.level.biome.Climate;

public final class ChimericDarknessClimate {
    public static final Climate.Parameter FULL = Climate.Parameter.span(-1.0F, 1.0F);

    public static final Climate.Parameter[] TEMPERATURES = {
            Climate.Parameter.span(-1.0F, -0.45F),
            Climate.Parameter.span(-0.45F, -0.15F),
            Climate.Parameter.span(-0.15F, 0.2F),
            Climate.Parameter.span(0.2F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };

    public static final Climate.Parameter[] HUMIDITIES = {
            Climate.Parameter.span(-1.0F, -0.35F),
            Climate.Parameter.span(-0.35F, -0.1F),
            Climate.Parameter.span(-0.1F, 0.1F),
            Climate.Parameter.span(0.1F, 0.3F),
            Climate.Parameter.span(0.3F, 1.0F)
    };

    public static final Climate.Parameter[] EROSIONS = {
            Climate.Parameter.span(-1.0F, -0.78F),
            Climate.Parameter.span(-0.78F, -0.375F),
            Climate.Parameter.span(-0.375F, -0.2225F),
            Climate.Parameter.span(-0.2225F, 0.05F),
            Climate.Parameter.span(0.05F, 0.45F),
            Climate.Parameter.span(0.45F, 0.55F),
            Climate.Parameter.span(0.55F, 1.0F)
    };

    public static final Climate.Parameter ISLANDS = Climate.Parameter.span(-1.2F, -1.05F);
    public static final Climate.Parameter DEEP_OCEAN = Climate.Parameter.span(-1.05F, -0.455F);
    public static final Climate.Parameter OCEAN = Climate.Parameter.span(-0.455F, -0.19F);

    public static final int COAST = 0;
    public static final int NEAR_INLAND = 1;
    public static final int MID_INLAND = 2;
    public static final int FAR_INLAND = 3;
    public static final Climate.Parameter[] INLAND = {
            Climate.Parameter.span(-0.19F, -0.11F),
            Climate.Parameter.span(-0.11F, 0.03F),
            Climate.Parameter.span(0.03F, 0.3F),
            Climate.Parameter.span(0.3F, 1.0F)
    };

    public static final Climate.Parameter SURFACE_DEPTH = Climate.Parameter.span(0.0F, 0.16F);
    public static final Climate.Parameter CAVE_DEPTH = Climate.Parameter.span(0.2F, 0.85F);
    public static final float CAVE_COLD_MAX = -0.15F;
    public static final float CAVE_HUMID_MIN = 0.1F;
    public static final Climate.Parameter DEEP_DEPTH = Climate.Parameter.span(0.95F, 2.0F);

    public static final float VALLEY_EROSION_MIN = 0.05F;
    public static final float VALLEY_EROSION_MAX = 0.45F;
    public static final float VALLEY_CONTINENTALNESS_MIN = -0.11F;
    public static final float VALLEY_CONTINENTALNESS_MAX = 0.3F;
    public static final float RIVER_WEIRDNESS = 0.05F;
    public static final int VALLEY_EROSION_INDEX = 4;

    public static final float SPIRE_CONTINENTALNESS_MIN = -1.05F;
    public static final float SPIRE_CONTINENTALNESS_MAX = -0.19F;
    public static final float SPIRE_TEMPERATURE_MAX = -0.45F;

    public static final float MASK_FADE = 0.08F;

    private ChimericDarknessClimate() {
    }
}