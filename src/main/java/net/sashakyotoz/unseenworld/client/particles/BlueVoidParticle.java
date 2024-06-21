
package net.sashakyotoz.unseenworld.client.particles;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;

@OnlyIn(Dist.CLIENT)
public class BlueVoidParticle extends TextureSheetParticle {
	public static BluevoidparticleParticleProvider provider(SpriteSet spriteSet) {
		return new BluevoidparticleParticleProvider(spriteSet);
	}

	public static class BluevoidparticleParticleProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public BluevoidparticleParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new BlueVoidParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}

	private float angularVelocity;
	private final float angularAcceleration;

	protected BlueVoidParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
		super(level, x, y, z);
		this.setSize(0.2f, 0.2f);
		this.quadSize *= 0.5f;
		this.lifetime = Math.max(1, 16 + (this.random.nextInt(2) - 1));
		this.gravity = 0.1f;
		this.hasPhysics = true;
		this.xd = vx * 0.8;
		this.yd = vy * 0.8;
		this.zd = vz * 0.8;
		this.angularVelocity = 0.314f;
		this.angularAcceleration = 0.1f;
		this.setSpriteFromAge(spriteSet);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		super.tick();
		this.oRoll = this.roll;
		this.roll += this.angularVelocity;
		this.angularVelocity += this.angularAcceleration;
	}
}
