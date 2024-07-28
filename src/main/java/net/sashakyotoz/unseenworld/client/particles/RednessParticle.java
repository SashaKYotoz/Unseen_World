
package net.sashakyotoz.unseenworld.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.sashakyotoz.anitexlib.client.particles.parents.GlowingLikeParticle;
import net.sashakyotoz.anitexlib.client.particles.parents.rendertypes.GlowingParticleRenderType;
import org.antlr.v4.runtime.misc.Triple;

@OnlyIn(Dist.CLIENT)
public class RednessParticle extends GlowingLikeParticle {
	public static Triple<Float, Float, Float> COLOR = new Triple<>(1.0F, 0.0F, 0.0F);
	public static RednessParticleProvider provider(SpriteSet spriteSet) {
		return new RednessParticleProvider(spriteSet);
	}

	public static class RednessParticleProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public RednessParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new RednessParticle(worldIn, x, y, z, COLOR.a, COLOR.b, COLOR.c, this.spriteSet);
		}
	}

	private final SpriteSet spriteSet;

	protected RednessParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, SpriteSet spriteSet) {
		super(world, x, y, z, r, g, b, spriteSet);
		this.spriteSet = spriteSet;
		this.LIFETIME_VARIANTS[0] = 40;
		this.LIFETIME_VARIANTS[1] = 60;
		this.LIFETIME_VARIANTS[2] = 90;
		this.lifetime = this.LIFETIME_VARIANTS[RandomSource.create().nextIntBetweenInclusive(0, this.LIFETIME_VARIANTS.length - 1)];
		this.setSize(0.25f, 0.25f);
		this.gravity = 0.1f;
		this.hasPhysics = true;
		this.setAlpha(0.75f);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return GlowingParticleRenderType.INSTANCE;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.lifetime % 4 == 0)
			this.setSpriteFromAge(this.spriteSet);
	}
}
