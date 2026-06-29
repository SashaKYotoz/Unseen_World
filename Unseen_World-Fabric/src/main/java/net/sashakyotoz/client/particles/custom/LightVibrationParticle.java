package net.sashakyotoz.client.particles.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

public class LightVibrationParticle extends TextureSheetParticle {
    private final PositionSource vibration;
    private float field_28250;
    private float field_28248;
    private float field_40507;
    private float field_40508;

    LightVibrationParticle(ClientLevel world, double x, double y, double z, PositionSource vibration, int maxAge) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.quadSize = 0.75F;
        this.vibration = vibration;
        this.lifetime = maxAge;
        Optional<Vec3> optional = vibration.getPosition(world);
        if (optional.isPresent()) {
            Vec3 vec3d = optional.get();
            double d = x - vec3d.x();
            double e = y - vec3d.y();
            double f = z - vec3d.z();
            this.field_28248 = this.field_28250 = (float) Mth.atan2(d, f);
            this.field_40508 = this.field_40507 = (float) Mth.atan2(e, Math.sqrt(d * d + f * f));
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        float f = Mth.sin(((float) this.age + tickDelta - (float) (Math.PI * 2)) * 0.05F) * 2.0F;
        float g = Mth.lerp(tickDelta, this.field_28248, this.field_28250);
        float h = Mth.lerp(tickDelta, this.field_40508, this.field_40507) + (float) (Math.PI / 2);
        this.render(vertexConsumer, camera, tickDelta, rotationQuaternion -> rotationQuaternion.rotateY(g).rotateX(-h).rotateY(f));
        this.render(vertexConsumer, camera, tickDelta, rotationQuaternion -> rotationQuaternion.rotateY((float) -Math.PI + g).rotateX(h).rotateY(f));
    }

    private void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta, Consumer<Quaternionf> transforms) {
        Vec3 vec3d = camera.getPosition();
        float f = (float) (Mth.lerp(tickDelta, this.xo, this.x) - vec3d.x());
        float g = (float) (Mth.lerp(tickDelta, this.yo, this.y) - vec3d.y());
        float h = (float) (Mth.lerp(tickDelta, this.zo, this.z) - vec3d.z());
        Vector3f vector3f = new Vector3f(0.5F, 0.5F, 0.5F).normalize();
        Quaternionf quaternionf = new Quaternionf().setAngleAxis(0.0F, vector3f.x(), vector3f.y(), vector3f.z());
        transforms.accept(quaternionf);
        Vector3f[] vector3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float i = this.getQuadSize(tickDelta);

        for (int j = 0; j < 4; j++) {
            Vector3f vector3f2 = vector3fs[j];
            vector3f2.rotate(quaternionf);
            vector3f2.mul(i);
            vector3f2.add(f, g, h);
        }

        float k = this.getU0();
        float l = this.getU1();
        float m = this.getV0();
        float n = this.getV1();
        int o = this.getLightColor(tickDelta);
        vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
                .uv(l, n)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(o)
                .endVertex();
        vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
                .uv(l, m)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(o)
                .endVertex();
        vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
                .uv(k, m)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(o)
                .endVertex();
        vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
                .uv(k, n)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(o)
                .endVertex();
    }

    @Override
    public int getLightColor(float tint) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            Optional<Vec3> optional = this.vibration.getPosition(this.level);
            if (optional.isEmpty()) {
                this.remove();
            } else {
                int i = this.lifetime - this.age;
                double d = 1.0 / (double) i;
                Vec3 vec3d = optional.get();
                this.x = Mth.lerp(d, this.x, vec3d.x());
                this.y = Mth.lerp(d, this.y, vec3d.y());
                this.z = Mth.lerp(d, this.z, vec3d.z());
                double e = this.x - vec3d.x();
                double f = this.y - vec3d.y();
                double g = this.z - vec3d.z();
                this.field_28248 = this.field_28250;
                this.field_28250 = (float) Mth.atan2(e, g);
                this.field_40508 = this.field_40507;
                this.field_40507 = (float) Mth.atan2(f, Math.sqrt(e * e + g * g));
                int color = Color.HSBtoRGB(ClientTicks.getHalfTicks() / 10 % 360, 1F, 1.0F);
                rCol = (float) (color >> 16 & 255) / 255.0F;
                gCol = (float) (color >> 8 & 255) / 255.0F;
                bCol = (float) (color & 255) / 255.0F;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<LightVibrationParticleEffect> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
                LightVibrationParticleEffect vibrationParticleEffect, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i
        ) {
            LightVibrationParticle vibrationParticle = new LightVibrationParticle(
                    clientWorld, d, e, f, vibrationParticleEffect.getVibration(), vibrationParticleEffect.getArrivalInTicks()
            );
            vibrationParticle.pickSprite(this.spriteProvider);
            vibrationParticle.setAlpha(1.0F);
            return vibrationParticle;
        }
    }
}