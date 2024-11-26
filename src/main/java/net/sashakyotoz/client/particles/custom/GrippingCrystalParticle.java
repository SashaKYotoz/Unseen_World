package net.sashakyotoz.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class GrippingCrystalParticle extends SpriteBillboardParticle {
    private float field_43369;
    private final float field_43370;
    private final float field_43371;

    public GrippingCrystalParticle(ClientWorld clientWorld, double x, double y, double z, float vx, float vy, float vz) {
        super(clientWorld, x, y, z);
        this.field_43369 = (float) Math.toRadians(this.random.nextBoolean() ? -35.0 : 35.0);
        this.field_43370 = this.random.nextFloat();
        this.field_43371 = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        this.maxAge = 200;
        this.gravityStrength = 7.5E-4F;
        float f = this.random.nextBetween(4, 8) * 0.1f;
        this.scale = f;
        this.setBoundingBoxSpacing(f, f);
        this.velocityMultiplier = 0.85F;
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
                this.velocityX = this.velocityX * this.velocityMultiplier;
                this.velocityY = this.velocityY * this.velocityMultiplier;
                this.velocityZ = this.velocityZ * this.velocityMultiplier;
            }
        }
    }

    @Override
    public int getBrightness(float tint) {
        return 255;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
                DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            GrippingCrystalParticle particle = new GrippingCrystalParticle(world, x, y, z, (float) velocityX, (float) velocityY, (float) velocityZ);
            particle.setSprite(spriteProvider);
            return particle;
        }
    }
}