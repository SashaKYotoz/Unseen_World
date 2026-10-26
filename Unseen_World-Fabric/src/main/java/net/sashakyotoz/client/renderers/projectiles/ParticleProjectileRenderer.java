package net.sashakyotoz.client.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.projectiles.ParticleProjectileEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

public class ParticleProjectileRenderer extends EntityRenderer<ParticleProjectileEntity> {
    private static final ResourceLocation HEAD_TEXTURE = UnseenWorld.MOD.makeID("textures/entity/projectiles/projectile_star.png");
    private static final ResourceLocation TRAIL_TEXTURE = UnseenWorld.MOD.makeID("textures/entity/projectiles/projectile_trail.png");

    public ParticleProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ParticleProjectileEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isDetonating()) {
            return;
        }
        double lerpX = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double lerpY = Mth.lerp(partialTick, entity.yOld, entity.getY());
        double lerpZ = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        Vec3 currentPos = new Vec3(lerpX, lerpY, lerpZ);

        poseStack.pushPose();

        renderTrail(entity, currentPos, poseStack, buffer);
        renderHead(poseStack, buffer);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private void renderHead(PoseStack poseStack, MultiBufferSource buffer) {
        poseStack.pushPose();
        Quaternionf cameraRotation = this.entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(cameraRotation);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(HEAD_TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();

        float size = 0.5F;
        int light = 15728880;

        addVertex(consumer, matrix, normal, -size, -size, 0.0F, 0.0F, 1.0F, 255, 255, 255, 255, light);
        addVertex(consumer, matrix, normal, size, -size, 0.0F, 1.0F, 1.0F, 255, 255, 255, 255, light);
        addVertex(consumer, matrix, normal, size, size, 0.0F, 1.0F, 0.0F, 255, 255, 255, 255, light);
        addVertex(consumer, matrix, normal, -size, size, 0.0F, 0.0F, 0.0F, 255, 255, 255, 255, light);

        poseStack.popPose();
    }

    private void renderTrail(ParticleProjectileEntity entity, Vec3 currentPos, PoseStack poseStack, MultiBufferSource buffer) {
        List<Vec3> trail = entity.getTrailPositions();
        if (trail.isEmpty()) {
            return;
        }

        Vec3 cameraPos = this.entityRenderDispatcher.camera.getPosition();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TRAIL_TEXTURE));
        renderRibbonStrip(trail, currentPos, cameraPos, matrix, normal, consumer, 0.25F, 255, 255, 255, 220);
    }

    private void renderRibbonStrip(List<Vec3> trail, Vec3 currentPos, Vec3 cameraPos, Matrix4f matrix, Matrix3f normal, VertexConsumer consumer, float baseWidth, int r, int g, int b, int alpha) {
        int count = trail.size();
        if (count < 2) {
            return;
        }

        int light = 15728880;

        for (int i = 0; i < count - 1; i++) {
            Vec3 p1 = (i == 0) ? currentPos : trail.get(i);
            Vec3 p2 = trail.get(i + 1);

            float progress1 = (float) i / (float) count;
            float progress2 = (float) (i + 1) / (float) count;

            float w1 = baseWidth * (1.0F - progress1);
            float w2 = baseWidth * (1.0F - progress2);

            int a1 = (int) (alpha * (1.0F - progress1));
            int a2 = (int) (alpha * (1.0F - progress2));

            Vec3 dir = p2.subtract(p1);
            if (dir.lengthSqr() < 1.0E-6D) {
                continue;
            }
            dir = dir.normalize();

            Vec3 camDir = cameraPos.subtract(p1).normalize();
            Vec3 side = dir.cross(camDir);
            if (side.lengthSqr() < 1.0E-6D) {
                side = new Vec3(0, 1, 0);
            } else {
                side = side.normalize();
            }

            Vec3 left1 = p1.add(side.scale(w1)).subtract(currentPos);
            Vec3 right1 = p1.subtract(side.scale(w1)).subtract(currentPos);
            Vec3 left2 = p2.add(side.scale(w2)).subtract(currentPos);
            Vec3 right2 = p2.subtract(side.scale(w2)).subtract(currentPos);

            addVertex(consumer, matrix, normal, (float) left1.x, (float) left1.y, (float) left1.z, progress1, 0.0F, r, g, b, a1, light);
            addVertex(consumer, matrix, normal, (float) right1.x, (float) right1.y, (float) right1.z, progress1, 1.0F, r, g, b, a1, light);
            addVertex(consumer, matrix, normal, (float) right2.x, (float) right2.y, (float) right2.z, progress2, 1.0F, r, g, b, a2, light);
            addVertex(consumer, matrix, normal, (float) left2.x, (float) left2.y, (float) left2.z, progress2, 0.0F, r, g, b, a2, light);
        }
    }

    private void addVertex(VertexConsumer consumer, Matrix4f matrix, Matrix3f normal, float x, float y, float z, float u, float v, int r, int g, int b, int a, int light) {
        consumer.vertex(matrix, x, y, z)
                .color(r, g, b, a)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(ParticleProjectileEntity entity) {
        return HEAD_TEXTURE;
    }
}