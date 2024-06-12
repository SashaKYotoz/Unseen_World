
package net.sashakyotoz.unseenworld.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.client.model.ModelFireParticle;

public class FireParticle extends AbstractModeledParticle {
	private static double red,green,blue;
	public FireParticle(ClientLevel level, double x, double y, double z, double redC, double greenC, double blueC, SpriteSet spriteSet) {
		super(level, x, y, z,red,green,blue,spriteSet);
		this.lifetime = Math.max(1, 40 + (this.random.nextInt(30) - 10));
		this.hasPhysics = true;
		red = redC;
		green = greenC;
		blue = blueC;
		new FireballRenderSequence(this).start();
	}
	public static FireParticle.FireParticleProvider provider(SpriteSet spriteSet) {
		return new FireParticle.FireParticleProvider(spriteSet);
	}
	public static class FireParticleProvider extends AbstractModeledParticle.AbstractParticleProvider<FireParticle> {
		public FireParticleProvider(SpriteSet spriteSet) {
			super(spriteSet);
		}

		@Override
		public FireParticle createParticleInstance(ClientLevel level, double x, double y, double z, double red, double green, double blue) {
			return new FireParticle(level, x, y, z, red, green, blue, spriteSet);
		}
	}

	private static class FireballRenderSequence {
		private final FireParticle particle;
		private int rotation = 0;

		private class FireballRenderer {
			public final EntityModel<Entity> model = new ModelFireParticle<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelFireParticle.LAYER_LOCATION));

			public FireballRenderer() {
				MinecraftForge.EVENT_BUS.register(this);
			}

			@SubscribeEvent
			public void render(RenderLevelStageEvent event) {
				if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
					VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(TextureAnimator.getAnimatedTextureByName(UnseenWorldMod.MODID, "textures/particle/fireball/", "fireball_particle")));
					Vec3 camPos = event.getCamera().getPosition();
					double x = Mth.lerp(event.getPartialTick(), particle.xo, particle.x) - camPos.x();
					double y = Mth.lerp(event.getPartialTick(), particle.yo, particle.y) - camPos.y();
					double z = Mth.lerp(event.getPartialTick(), particle.zo, particle.z) - camPos.z();
					event.getPoseStack().pushPose();
					event.getPoseStack().scale(1.5f,1.5f,1.5f);
					event.getPoseStack().translate(x, y, z);
					event.getPoseStack().rotateAround(Axis.XP.rotationDegrees(rotation), (float) x, (float) y, (float) z);
					event.getPoseStack().rotateAround(Axis.YP.rotationDegrees(rotation), (float) x, (float) y, (float) z);
					event.getPoseStack().rotateAround(Axis.ZP.rotationDegrees(rotation), (float) x, (float) y, (float) z);
					model.renderToBuffer(event.getPoseStack(), consumer, particle.getLightColor(event.getPartialTick()), OverlayTexture.NO_OVERLAY, (float) red, (float) blue, (float) green, 1);
					event.getPoseStack().popPose();
				}
			}
		}

		private final FireParticle.FireballRenderSequence.FireballRenderer renderer;

		public FireballRenderSequence(FireParticle particle) {
			this.particle = particle;
			this.renderer = new FireParticle.FireballRenderSequence.FireballRenderer();
		}

		public void start() {
			MinecraftForge.EVENT_BUS.register(renderer);
			MinecraftForge.EVENT_BUS.register(this);
		}

		@SubscribeEvent
		public void tick(TickEvent.ClientTickEvent event) {
			if (!particle.isAlive())
				end();
			else{
				rotation = this.particle.age % 360 * 5;
			}
		}
		private void end() {
			MinecraftForge.EVENT_BUS.unregister(renderer);
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
}
