package net.sashakyotoz.client.environment;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class DarknessFlashState {
    private long flashSeed;
    private int offset;
    private int duration;
    private float intensity;
    private float oldIntensity;
    private float xAngle;
    private float yAngle;

    public void tick(long gametime) {
        this.calculateFlashParameters(gametime);
        this.oldIntensity = this.intensity;
        this.intensity = this.calculateIntensity(gametime);
    }

    private void calculateFlashParameters(long gametime) {
        long i = gametime / 600L;
        if (i != this.flashSeed) {
            Random random = Random.create(i);
            random.nextFloat();
            this.offset = MathHelper.nextBetween(random, 0, 200);
            this.duration = MathHelper.nextBetween(random, 100, Math.min(380, 600 - this.offset));
            this.xAngle = MathHelper.nextBetween(random, -60.0F, 10.0F);
            this.yAngle = MathHelper.nextBetween(random, -180.0F, 180.0F);
            this.flashSeed = i;
        }

    }

    private float calculateIntensity(long gametime) {
        long i = gametime % 600L;
        return i >= (long)this.offset && i <= (long)(this.offset + this.duration) ? MathHelper.sin((float)(i - (long)this.offset) * (float)Math.PI / (float)this.duration) : 0.0F;
    }

    public float getXAngle() {
        return this.xAngle;
    }

    public float getYAngle() {
        return this.yAngle;
    }

    public float getIntensity(float partialTick) {
        return MathHelper.lerp(partialTick, this.oldIntensity, this.intensity);
    }

    public boolean flashStartedThisTick() {
        return this.intensity > 0.0F && this.oldIntensity <= 0.0F;
    }
}