
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

public class FireParticle extends JsonModelParticle {
    public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(UnseenWorld.MOD_ID, "particle/fireball", "inventory");
    private int rotation = 0;

    public FireParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, MODEL_LOCATION);
        this.lifetime = Math.max(1, 40 + (this.random.nextInt(30) - 10));
        this.hasPhysics = true;
    }

    public static FireParticle.FireParticleProvider provider(SpriteSet spriteSet) {
        return new FireParticle.FireParticleProvider(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive())
            this.rotation = this.age % 360 * 5;
    }

    @Override
    public void setupTransformations(PoseStack poseStack, float v) {
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(this.rotation));
        poseStack.mulPose(Axis.YP.rotationDegrees(this.rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(this.rotation));
    }

    @Override
    public ResourceLocation getTexture(float v) {
        return AniTexLibRegs.TEXTURE_ANIMATOR.get(UnseenWorld.makeId("textures/particle/fireball/fireball_particle0.png"));
    }

    public static class FireParticleProvider implements ParticleProvider<SimpleParticleType> {
        public FireParticleProvider(SpriteSet spriteSet) {
            super();
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FireParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
