
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
import net.sashakyotoz.anitexlib.client.particles.parents.FluidParticleRenderType;

@OnlyIn(Dist.CLIENT)
public class GoldenParticle extends TextureSheetParticle {
	public static GoldenParticleProvider provider(SpriteSet spriteSet) {
		return new GoldenParticleProvider(spriteSet);
	}

	public static class GoldenParticleProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public GoldenParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new GoldenParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}

	private final SpriteSet spriteSet;
	private float angularVelocity;
	private final float angularAcceleration;

	protected GoldenParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
		super(world, x, y, z);
		this.spriteSet = spriteSet;
		this.setSize(0.25f, 0.25f);
		this.quadSize *= 0.5f;
		this.lifetime = Math.max(1, 35 + (this.random.nextInt(10) - 5));
		this.gravity = -0.1f;
		this.hasPhysics = true;
		this.xd = vx * 1;
		this.yd = vy * 1;
		this.zd = vz * 1;
		this.angularVelocity = 0.25f;
		this.angularAcceleration = 0.1f;
	}

	@Override
	public int getLightColor(float partialTick) {
		return 15728880;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return FluidParticleRenderType.INSTANCE;
	}

	@Override
	public void tick() {
		super.tick();
		this.oRoll = this.roll;
		this.roll += this.angularVelocity;
		this.angularVelocity += this.angularAcceleration;
		if (this.lifetime % 3 == 0)
			this.setSpriteFromAge(spriteSet);
	}
}
