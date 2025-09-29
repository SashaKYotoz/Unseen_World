package net.sashakyotoz.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;

public class LeafParticle extends SpriteBillboardParticle {
    private float field_43369;
    private final float field_43370;
    private final float field_43371;

    public LeafParticle(ClientWorld clientWorld, double x, double y, double z, float r, float g, float b) {
        super(clientWorld, x, y, z);
        this.red = r;
        this.green = g;
        this.blue = b;
        this.field_43369 = (float) Math.toRadians(this.random.nextBoolean() ? -30.0 : 30.0);
        this.field_43370 = this.random.nextFloat();
        this.field_43371 = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.maxAge = 300;
        this.gravityStrength = 7.5E-4F;
        float f = this.random.nextBoolean() ? 0.5F : 0.375F;
        this.scale = f;
        this.setBoundingBoxSpacing(f, f);
        this.velocityMultiplier = 1.0F;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
        }

        if (!this.dead) {
            float f = (float) (300 - this.maxAge);
            float g = Math.min(f / 300.0F, 1.0F);
            double d = Math.cos(Math.toRadians(this.field_43370 * 60.0F)) * 2.0 * Math.pow(g, 1.25);
            double e = Math.sin(Math.toRadians(this.field_43370 * 60.0F)) * 2.0 * Math.pow(g, 1.25);
            this.velocityX += d * 0.0025F;
            this.velocityZ += e * 0.0025F;
            this.velocityY = this.velocityY - (double) this.gravityStrength;
            this.field_43369 = this.field_43369 + this.field_43371 / 20.0F;
            this.prevAngle = this.angle;
            this.angle = this.angle + this.field_43369 / 20.0F;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if (this.onGround || this.maxAge < 299 && (this.velocityX == 0.0 || this.velocityZ == 0.0)) {
                this.markDead();
            }

            if (!this.dead) {
                this.velocityX = this.velocityX * (double) this.velocityMultiplier;
                this.velocityY = this.velocityY * (double) this.velocityMultiplier;
                this.velocityZ = this.velocityZ * (double) this.velocityMultiplier;
            }
        }
    }
    @Override
    public int getBrightness(float tint) {
        float f = ((float)this.age + tint) / (float)this.maxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightness(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<LeafParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
                LeafParticleEffect effect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i
        ) {
            LeafParticle particle = new LeafParticle(
                    clientWorld, d, e, f, effect.red(), effect.green(), effect.blue()
            );
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}