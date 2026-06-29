package net.sashakyotoz.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class GrippingCrystalParticle extends TextureSheetParticle {
    private float field_43369;
    private final float field_43370;
    private final float field_43371;

    public GrippingCrystalParticle(ClientLevel clientWorld, double x, double y, double z, float vx, float vy, float vz) {
        super(clientWorld, x, y, z);
        this.field_43369 = (float) Math.toRadians(this.random.nextBoolean() ? -35.0 : 35.0);
        this.field_43370 = this.random.nextFloat();
        this.field_43371 = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = 200;
        this.gravity = 7.5E-4F;
        float f = this.random.nextIntBetweenInclusive(4, 8) * 0.1f;
        this.quadSize = f;
        this.setSize(f, f);
        this.friction = 0.85F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        }
        if (!this.removed) {
            float f = (float) (300 - this.lifetime);
            float g = Math.min(f / 300.0F, 1.0F);
            double d = Math.cos(Math.toRadians(this.field_43370 * 60.0F)) * 2.0 * Math.pow(g, 1.25);
            double e = Math.sin(Math.toRadians(this.field_43370 * 60.0F)) * 2.0 * Math.pow(g, 1.25);
            this.xd += d * 0.0025F;
            this.zd += e * 0.0025F;
            this.yd = this.yd - (double) this.gravity;
            this.field_43369 = this.field_43369 + this.field_43371 / 20.0F;
            this.oRoll = this.roll;
            this.roll = this.roll + this.field_43369 / 20.0F;
            this.move(this.xd, this.yd, this.zd);
            if (this.onGround || this.lifetime < 299 && (this.xd == 0.0 || this.zd == 0.0)) {
                this.remove();
            }
            if (!this.removed) {
                this.xd = this.xd * this.friction;
                this.yd = this.yd * this.friction;
                this.zd = this.zd * this.friction;
            }
        }
    }

    @Override
    public int getLightColor(float tint) {
        return 255;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
                SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            GrippingCrystalParticle particle = new GrippingCrystalParticle(world, x, y, z, (float) velocityX, (float) velocityY, (float) velocityZ);
            particle.pickSprite(spriteProvider);
            return particle;
        }
    }
}