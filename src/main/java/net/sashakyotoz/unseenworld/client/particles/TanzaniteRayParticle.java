package net.sashakyotoz.unseenworld.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.anitexlib.client.particles.parents.GlowingLikeParticle;
import net.sashakyotoz.anitexlib.client.particles.parents.rendertypes.GlowingParticleRenderType;

public class TanzaniteRayParticle extends GlowingLikeParticle {
    public static TanzaniteRayParticle.TanzaniteParticleProvider provider(SpriteSet spriteSet) {
        return new TanzaniteRayParticle.TanzaniteParticleProvider(spriteSet);
    }

    public static class TanzaniteParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public TanzaniteParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TanzaniteRayParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
    private float angularVelocity;
    private final float angularAcceleration;

    protected TanzaniteRayParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z,1f,0,0.75f,spriteSet);
        this.setSize(0.3f, 0.3f);
        this.lifetime = 50;
        this.hasPhysics = true;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.quadSize = 0.35f;
        this.angularVelocity = 0.1f;
        this.angularAcceleration = 0f;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return GlowingParticleRenderType.INSTANCE;
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        this.roll += this.angularVelocity;
        this.angularVelocity += this.angularAcceleration;
        this.move(this.xd, this.yd, this.zd);
        fadeOut();
    }
    private void fadeOut(){
        this.rCol = this.rCol + (RandomSource.create().nextInt(16)-12)/10f;
        this.bCol = this.bCol + (RandomSource.create().nextInt(16)-12)/10f;
        this.gCol = this.gCol + (RandomSource.create().nextInt(16)-12)/10f;
        if(!this.removed && quadSize < 1)
            this.quadSize = 0.35f + (float) age/20;
    }
}
