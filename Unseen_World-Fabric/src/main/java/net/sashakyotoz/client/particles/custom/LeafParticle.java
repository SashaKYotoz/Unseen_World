package net.sashakyotoz.client.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;

public class LeafParticle extends TextureSheetParticle {
    private float field_43369;
    private final float field_43370;
    private final float field_43371;

    public LeafParticle(ClientLevel clientWorld, double x, double y, double z, float r, float g, float b) {
        super(clientWorld, x, y, z);
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.field_43369 = (float) Math.toRadians(this.random.nextBoolean() ? -30.0 : 30.0);
        this.field_43370 = this.random.nextFloat();
        this.field_43371 = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.lifetime = 300;
        this.gravity = 7.5E-4F;
        float f = this.random.nextBoolean() ? 0.5F : 0.375F;
        this.quadSize = f;
        this.setSize(f, f);
        this.friction = 1.0F;
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
                this.xd = this.xd * (double) this.friction;
                this.yd = this.yd * (double) this.friction;
                this.zd = this.zd * (double) this.friction;
            }
        }
    }
    @Override
    public int getLightColor(float tint) {
        float f = ((float)this.age + tint) / (float)this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<LeafParticleEffect> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
                LeafParticleEffect effect, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i
        ) {
            LeafParticle particle = new LeafParticle(
                    clientWorld, d, e, f, effect.red(), effect.green(), effect.blue()
            );
            particle.pickSprite(this.spriteProvider);
            return particle;
        }
    }
}