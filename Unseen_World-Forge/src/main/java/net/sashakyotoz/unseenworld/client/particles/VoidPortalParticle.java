package net.sashakyotoz.unseenworld.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.api.client.particle.custom.parent.JsonModelParticle;
import net.sashakyotoz.anitexlib.api.common.AniTexLibRegs;
import net.sashakyotoz.unseenworld.UnseenWorld;
import org.jetbrains.annotations.Nullable;

public class VoidPortalParticle extends JsonModelParticle {
    public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(UnseenWorld.MOD_ID, "particle/void_portal", "inventory");

    private float currentScale = 2.5f;

    public VoidPortalParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz, MODEL_LOCATION);
        this.lifetime = 70;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
    }

    public static VoidPortalParticleProvider provider(SpriteSet spriteSet) {
        return new VoidPortalParticleProvider(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            if (this.currentScale > 0.25f)
                this.currentScale -= 0.05f;
        }
    }

    @Override
    public void setupTransformations(PoseStack poseStack, float v) {
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.scale(this.currentScale, this.currentScale, this.currentScale);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
    }

    @Override
    public ResourceLocation getTexture(float v) {
        return AniTexLibRegs.TEXTURE_ANIMATOR.get(UnseenWorld.makeId("textures/particle/portal_like/void_portal0.png"));
    }

    public static class VoidPortalParticleProvider implements ParticleProvider<SimpleParticleType> {
        public VoidPortalParticleProvider(SpriteSet spriteSet) {
            super();
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VoidPortalParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}