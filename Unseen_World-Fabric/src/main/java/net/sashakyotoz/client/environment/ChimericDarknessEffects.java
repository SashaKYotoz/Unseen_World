package net.sashakyotoz.client.environment;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public class ChimericDarknessEffects extends DimensionSpecialEffects {
    private static final double FOG_RED = 0.43D;
    private static final double FOG_GREEN = 0.38D;
    private static final double FOG_BLUE = 0.47D;

    public ChimericDarknessEffects() {
        super(Float.NaN, true, SkyType.NONE, false, true);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
        return color.multiply(FOG_RED, FOG_GREEN, FOG_BLUE);
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }
}