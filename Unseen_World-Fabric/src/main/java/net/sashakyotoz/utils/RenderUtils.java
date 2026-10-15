package net.sashakyotoz.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderUtils {
    private static final int FULL_BRIGHT = 15728880;
    private static final float CAP_RADIUS = 0.6F;
    private static final float BODY_RADIUS = 0.4F;

    public static void renderBeam(Mob entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, RenderType type, float yOffset, int color) {
        LivingEntity target = entity.getTarget();
        Vec3 end = target != null
                ? getPosition(target, target.getBbHeight() * 0.5D, partialTicks).add(0.0D, 1.0D, 0.0D)
                : getPosition(posOfViewing(entity));
        renderBeam(entity, partialTicks, poseStack, buffer, type, yOffset, color, end);
    }

    public static void renderBeam(LivingEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, RenderType type, float yOffset, int color, Vec3 end) {
        float gameTime = entity.level().getGameTime() + partialTicks;
        float timeMod = gameTime * 0.5F % 1.0F;

        poseStack.pushPose();
        poseStack.translate(0.0F, yOffset, 0.0F);

        Vec3 start = getPosition(entity, yOffset, partialTicks);
        Vec3 delta = end.subtract(start);
        float length = (float) (delta.length() + 1.0D);
        delta = delta.normalize();

        float pitch = (float) Math.acos(delta.y);
        float yaw = (float) Math.atan2(delta.z, delta.x);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - yaw * Mth.RAD_TO_DEG));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch * Mth.RAD_TO_DEG));

        float animationTime = gameTime * 0.05F * -1.5F;
        float vOffset = -1.0F + timeMod;
        float vLength = length * 2.5F + vOffset;

        VertexConsumer consumer = buffer.getBuffer(type);
        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f pose = lastPose.pose();
        Matrix3f normal = lastPose.normal();

        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        for (int plane = 0; plane < 2; plane++) {
            float startAngle = animationTime + Mth.PI * (1.0F - plane * 0.5F);
            float endAngle = startAngle + Mth.PI;
            float x0 = Mth.cos(startAngle) * BODY_RADIUS;
            float z0 = Mth.sin(startAngle) * BODY_RADIUS;
            float x1 = Mth.cos(endAngle) * BODY_RADIUS;
            float z1 = Mth.sin(endAngle) * BODY_RADIUS;
            vertex(consumer, pose, normal, x0, length, z0, red, green, blue, 0.4999F, vLength);
            vertex(consumer, pose, normal, x0, 0.0F, z0, red, green, blue, 0.4999F, vOffset);
            vertex(consumer, pose, normal, x1, 0.0F, z1, red, green, blue, 0.0F, vOffset);
            vertex(consumer, pose, normal, x1, length, z1, red, green, blue, 0.0F, vLength);
        }

        float uvTickOffset = (entity.tickCount % 2 == 0) ? 0.5F : 0.0F;
        float[][] cap = new float[4][2];
        float[] angles = {0.75F, 0.25F, 1.75F, 1.25F};
        for (int i = 0; i < 4; i++) {
            float angle = animationTime + Mth.PI * angles[i];
            cap[i][0] = Mth.cos(angle) * CAP_RADIUS;
            cap[i][1] = Mth.sin(angle) * CAP_RADIUS;
        }
        vertex(consumer, pose, normal, cap[0][0], length, cap[0][1], red, green, blue, 0.5F, uvTickOffset + 0.5F);
        vertex(consumer, pose, normal, cap[1][0], length, cap[1][1], red, green, blue, 1.0F, uvTickOffset + 0.5F);
        vertex(consumer, pose, normal, cap[2][0], length, cap[2][1], red, green, blue, 1.0F, uvTickOffset);
        vertex(consumer, pose, normal, cap[3][0], length, cap[3][1], red, green, blue, 0.5F, uvTickOffset);

        poseStack.popPose();
    }

    private static BlockPos posOfViewing(LivingEntity entity) {
        Vec3 start = entity.getEyePosition(1.0F).add(
                ActionsUtils.getXVector(0.5F, entity.getYRot()),
                0.0D,
                ActionsUtils.getZVector(0.5F, entity.getYRot()));
        Vec3 end = start.add(entity.getViewVector(1.0F).scale(32.0D));
        ClipContext context = new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity);
        return entity.level().clip(context).getBlockPos();
    }

    private static Vec3 getPosition(LivingEntity entity, double yOffset, float partialTick) {
        double x = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double y = Mth.lerp(partialTick, entity.yOld, entity.getY()) + yOffset;
        double z = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        return new Vec3(x, y, z);
    }

    private static Vec3 getPosition(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float x, float y, float z, int r, int g, int b, float u, float v) {
        consumer.vertex(pose, x, y, z)
                .color(r, g, b, 200)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(FULL_BRIGHT)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}