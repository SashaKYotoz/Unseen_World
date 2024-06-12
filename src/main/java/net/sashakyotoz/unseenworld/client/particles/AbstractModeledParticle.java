package net.sashakyotoz.unseenworld.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractModeledParticle extends TextureSheetParticle {
    protected final SpriteSet spriteSet;

    protected AbstractModeledParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }

    public static abstract class AbstractParticleProvider<T extends AbstractModeledParticle> implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet spriteSet;

        protected AbstractParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public abstract T createParticleInstance(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            T particle = createParticleInstance(level, x, y, z, xSpeed, ySpeed, zSpeed);
            return particle;
        }
    }
}