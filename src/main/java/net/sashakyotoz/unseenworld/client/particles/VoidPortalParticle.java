package net.sashakyotoz.unseenworld.client.particles;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.client.model.ModelPortalLikeParticleModel;

public class VoidPortalParticle extends AbstractModeledParticle  {
    public VoidPortalParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z, vx, vy, vz, spriteSet);
        this.lifetime = Math.max(1, 60 + (this.random.nextInt(40) - 20));
        this.hasPhysics = true;
        new PortalRenderSequence(this).start();
    }
    public static VoidPortalParticleProvider provider(SpriteSet spriteSet) {
        return new VoidPortalParticleProvider(spriteSet);
    }
    public static class VoidPortalParticleProvider extends AbstractModeledParticle.AbstractParticleProvider<VoidPortalParticle> {
        public VoidPortalParticleProvider(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public VoidPortalParticle createParticleInstance(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VoidPortalParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }

    private static class PortalRenderSequence {
        private final VoidPortalParticle particle;
        private float scale = 2.5f;

        private class VoidPortalRenderer {
            public final EntityModel<Entity> model = new ModelPortalLikeParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelPortalLikeParticleModel.LAYER_LOCATION));

            public VoidPortalRenderer() {
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void render(RenderLevelStageEvent event) {
                if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                    VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(TextureAnimator.getAnimatedTextureByName(UnseenWorldMod.MODID, "textures/particle/portal_like/", "void_portal")));
                    Vec3 camPos = event.getCamera().getPosition();
                    double x = Mth.lerp(event.getPartialTick(), particle.xo, particle.x) - camPos.x();
                    double y = Mth.lerp(event.getPartialTick(), particle.yo, particle.y) - camPos.y() + 0.1f;
                    double z = Mth.lerp(event.getPartialTick(), particle.zo, particle.z) - camPos.z();
                    event.getPoseStack().pushPose();
                    event.getPoseStack().translate(x, y, z);
                    event.getPoseStack().mulPose(Axis.XP.rotationDegrees(180));
                    event.getPoseStack().scale(scale, scale, scale);
                    model.renderToBuffer(event.getPoseStack(), consumer, particle.getLightColor(event.getPartialTick()), OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                    event.getPoseStack().popPose();
                }
            }
        }

        private final VoidPortalRenderer renderer;

        public PortalRenderSequence(VoidPortalParticle particle) {
            this.particle = particle;
            this.renderer = new VoidPortalRenderer();
        }

        public void start() {
            MinecraftForge.EVENT_BUS.register(renderer);
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void tick(TickEvent.ClientTickEvent event) {
            if (!particle.isAlive()) {
                end();
            } else {
                scale -= scale > 0.25f ? 0.05f : 0;
            }
        }
        private void end() {
            MinecraftForge.EVENT_BUS.unregister(renderer);
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}