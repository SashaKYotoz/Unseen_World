package net.sashakyotoz.client.renderers.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.models.EclipseSentinelModel;
import net.sashakyotoz.client.renderers.layers.bosses.EclipseSentinelEroflameLayer;
import net.sashakyotoz.client.renderers.layers.bosses.EclipseSentinelGlowingLayer;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

public class EclipseSentinelRenderer extends MobRenderer<EclipseSentinel, EclipseSentinelModel> {

    public EclipseSentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new EclipseSentinelModel(context.bakeLayer(EclipseSentinelModel.ECLIPSE_SENTINEL)), 0.5f);
        this.addLayer(new EclipseSentinelGlowingLayer<>(this));
        this.addLayer(new EclipseSentinelEroflameLayer<>(this));
    }

    private Vec3 fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = Mth.lerp(delta, entity.xOld, entity.getX());
        double e = Mth.lerp(delta, entity.yOld, entity.getY()) + yOffset;
        double f = Mth.lerp(delta, entity.zOld, entity.getZ());
        return new Vec3(d, e, f);
    }

    private Vec3 fromLerpedPosition(BlockPos pos, double yOffset, float delta) {
        double d = Mth.lerpInt(delta, pos.getX(), pos.getX());
        double e = Mth.lerpInt(delta, pos.getY(), pos.getY()) + yOffset;
        double f = Mth.lerpInt(delta, pos.getZ(), pos.getZ());
        return new Vec3(d, e, f);
    }

    @Override
    public void render(EclipseSentinel sentinel, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(sentinel, f, g, matrixStack, vertexConsumerProvider, i);
        if (sentinel.isInSentinelPose(EclipseSentinel.SentinelPose.BEAMING)) {
            BlockPos posOfViewing = sentinel.level().clip(new ClipContext(sentinel.getEyePosition(), sentinel.getEyePosition().add(sentinel.getViewVector(1f).scale(24)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, sentinel)).getBlockPos();
            float j = sentinel.level().getGameTime() + g;
            float k = j % 1.0f;
            float l = sentinel.getEyeHeight() - 0.9f;
            matrixStack.pushPose();
            matrixStack.translate(0.0F, l, 0.0F);
            Vec3 vec3d = this.fromLerpedPosition(posOfViewing, 1, g);
            Vec3 vec3d2 = this.fromLerpedPosition(sentinel, l, g);
            Vec3 vec3d3 = vec3d.subtract(vec3d2);
            float m = (float) (vec3d3.length() + 1.0);
            vec3d3 = vec3d3.normalize();
            float n = (float) Math.acos(vec3d3.y);
            float o = (float) Math.atan2(vec3d3.z, vec3d3.x);
            matrixStack.mulPose(Axis.YP.rotationDegrees((1.5707964F - o) * 57.295776F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(n * 57.295776F));
            float q = j * 0.05F * -1.5F;
            int color = Color.HSBtoRGB(ClientTicks.getHalfTicks() / 10 % 360, 1F, 1.0f);
            float r0 = (float) (color >> 16 & 255) / 255.0F;
            float g0 = (float) (color >> 8 & 255) / 255.0F;
            float b0 = (float) (color & 255) / 255.0F;
            int red = (int) (r0 * 255);
            int green = (int) (g0 * 255);
            int blue = (int) (b0 * 255);
            float x = Mth.cos(q + 2.3561945F) * 0.282F;
            float y = Mth.sin(q + 2.3561945F) * 0.282F;
            float z = Mth.cos(q + 0.7853982F) * 0.282F;
            float aa = Mth.sin(q + 0.7853982F) * 0.282F;
            float ab = Mth.cos(q + 3.926991F) * 0.282F;
            float ac = Mth.sin(q + 3.926991F) * 0.282F;
            float ad = Mth.cos(q + 5.4977875F) * 0.282F;
            float ae = Mth.sin(q + 5.4977875F) * 0.282F;
            float af = Mth.cos(q + 3.1415927F) * 0.2F;
            float ag = Mth.sin(q + 3.1415927F) * 0.2F;
            float ah = Mth.cos(q + 0.0F) * 0.2F;
            float ai = Mth.sin(q + 0.0F) * 0.2F;
            float aj = Mth.cos(q + 1.5707964F) * 0.2F;
            float ak = Mth.sin(q + 1.5707964F) * 0.2F;
            float al = Mth.cos(q + 4.712389F) * 0.2F;
            float am = Mth.sin(q + 4.712389F) * 0.2F;
            float aq = -1.0F + k;
            float ar = m * 2.5F + aq;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.armorCutoutNoCull(UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_beam.png")));
            PoseStack.Pose entry = matrixStack.last();
            Matrix4f matrix4f = entry.pose();
            Matrix3f matrix3f = entry.normal();
            vertex(vertexConsumer, matrix4f, matrix3f, af, m, ag, red, green, blue, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, af, 0.0F, ag, red, green, blue, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0F, ai, red, green, blue, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, m, ai, red, green, blue, 0.0F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, m, ak, red, green, blue, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0F, ak, red, green, blue, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, 0.0F, am, red, green, blue, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, m, am, red, green, blue, 0.0F, ar);
            float as = 0.0F;
            if (sentinel.tickCount % 2 == 0)
                as = 0.5F;
            vertex(vertexConsumer, matrix4f, matrix3f, x, m, y, red, green, blue, 0.5F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, z, m, aa, red, green, blue, 1.0F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, ad, m, ae, red, green, blue, 1.0F, as);
            vertex(vertexConsumer, matrix4f, matrix3f, ab, m, ac, red, green, blue, 0.5F, as);
            matrixStack.popPose();
        }
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(red, green, blue, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    protected boolean isShaking(EclipseSentinel entity) {
        return entity.isDeadOrDying();
    }


    @Override
    public ResourceLocation getTextureLocation(EclipseSentinel entity) {
        return entity.isExalted()
                ? UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_exalted.png")
                : UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel.png");
    }
}