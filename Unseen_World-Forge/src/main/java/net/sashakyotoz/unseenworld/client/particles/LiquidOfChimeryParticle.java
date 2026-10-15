
package net.sashakyotoz.unseenworld.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LiquidOfChimeryParticle extends TextureSheetParticle {
	public static LiquidOfChimeryParticleParticleProvider provider(SpriteSet spriteSet) {
		return new LiquidOfChimeryParticleParticleProvider(spriteSet);
	}

	public static class LiquidOfChimeryParticleParticleProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public LiquidOfChimeryParticleParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new LiquidOfChimeryParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}

	private final SpriteSet spriteSet;
	private float angularVelocity;
	private final float angularAcceleration;

	protected LiquidOfChimeryParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
		super(world, x, y, z);
		this.spriteSet = spriteSet;
		this.setSize(0.3f, 0.3f);
		this.quadSize *= 2f;
		this.lifetime = Math.max(1, 50 + (this.random.nextInt(6) - 3));
		this.gravity = -0.1f;
		this.hasPhysics = false;
		this.xd = vx * 1.2;
		this.yd = vy * 1.2;
		this.zd = vz * 1.2;
		this.angularVelocity = 0.1f;
		this.angularAcceleration = 0.01f;
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
		if (!this.removed) {
			this.setSprite(this.spriteSet.get((this.age / 2) % 7 + 1, 7));
		}
	}
}
