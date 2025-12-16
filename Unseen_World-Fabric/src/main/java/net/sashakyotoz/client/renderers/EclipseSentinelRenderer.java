package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.models.EclipseSentinelModel;
import net.sashakyotoz.client.renderers.layers.bosses.EclipseSentinelEroflameLayer;
import net.sashakyotoz.client.renderers.layers.bosses.EclipseSentinelGlowingLayer;
import net.sashakyotoz.client.renderers.layers.player.BladeShieldLayer;
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;

public class EclipseSentinelRenderer extends DeathFixedMobRenderer<EclipseSentinelEntity, EclipseSentinelModel> {

    public EclipseSentinelRenderer(EntityRendererFactory.Context context) {
        super(context, new EclipseSentinelModel(context.getPart(EclipseSentinelModel.ECLIPSE_SENTINEL)), 0.5f);
        this.addFeature(new EclipseSentinelGlowingLayer<>(this));
        this.addFeature(new EclipseSentinelEroflameLayer<>(this));
        this.addFeature(new BladeShieldLayer<>(this, context));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    private Vec3d fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = MathHelper.lerp(delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp(delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp(delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }

    private Vec3d fromLerpedPosition(BlockPos pos, double yOffset, float delta) {
        double d = MathHelper.lerp(delta, pos.getX(), pos.getX());
        double e = MathHelper.lerp(delta, pos.getY(), pos.getY()) + yOffset;
        double f = MathHelper.lerp(delta, pos.getZ(), pos.getZ());
        return new Vec3d(d, e, f);
    }

    @Override
    public void render(EclipseSentinelEntity sentinel, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(sentinel, f, g, matrixStack, vertexConsumerProvider, i);
        if (sentinel.isInSentinelPose(EclipseSentinelEntity.SentinelPose.BEAMING)) {
            BlockPos posOfViewing = sentinel.getWorld().raycast(new RaycastContext(sentinel.getEyePos(), sentinel.getEyePos().add(sentinel.getRotationVec(1f).multiply(24)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, sentinel)).getBlockPos();
            float j = sentinel.getWorld().getTime() + g;
            float k = j % 1.0f;
            float l = sentinel.getStandingEyeHeight() - 0.9f;
            matrixStack.push();
            matrixStack.translate(0.0F, l, 0.0F);
            Vec3d vec3d = this.fromLerpedPosition(posOfViewing, 1, g);
            Vec3d vec3d2 = this.fromLerpedPosition(sentinel, l, g);
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            float m = (float) (vec3d3.length() + 1.0);
            vec3d3 = vec3d3.normalize();
            float n = (float) Math.acos(vec3d3.y);
            float o = (float) Math.atan2(vec3d3.z, vec3d3.x);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((1.5707964F - o) * 57.295776F));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * 57.295776F));
            float q = j * 0.05F * -1.5F;
            int color = Color.HSBtoRGB(ClientTicks.getHalfTicks() / 10 % 360, 1F, 1.0f);
            float r0 = (float) (color >> 16 & 255) / 255.0F;
            float g0 = (float) (color >> 8 & 255) / 255.0F;
            float b0 = (float) (color & 255) / 255.0F;
            int red = (int) (r0 * 255);
            int green = (int) (g0 * 255);
            int blue = (int) (b0 * 255);
            float x = MathHelper.cos(q + 2.3561945F) * 0.282F;
            float y = MathHelper.sin(q + 2.3561945F) * 0.282F;
            float z = MathHelper.cos(q + 0.7853982F) * 0.282F;
            float aa = MathHelper.sin(q + 0.7853982F) * 0.282F;
            float ab = MathHelper.cos(q + 3.926991F) * 0.282F;
            float ac = MathHelper.sin(q + 3.926991F) * 0.282F;
            float ad = MathHelper.cos(q + 5.4977875F) * 0.282F;
            float ae = MathHelper.sin(q + 5.4977875F) * 0.282F;
            float af = MathHelper.cos(q + 3.1415927F) * 0.2F;
            float ag = MathHelper.sin(q + 3.1415927F) * 0.2F;
            float ah = MathHelper.cos(q + 0.0F) * 0.2F;
            float ai = MathHelper.sin(q + 0.0F) * 0.2F;
            float aj = MathHelper.cos(q + 1.5707964F) * 0.2F;
            float ak = MathHelper.sin(q + 1.5707964F) * 0.2F;
            float al = MathHelper.cos(q + 4.712389F) * 0.2F;
            float am = MathHelper.sin(q + 4.712389F) * 0.2F;
            float aq = -1.0F + k;
            float ar = m * 2.5F + aq;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getArmorCutoutNoCull(UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_beam.png")));
            MatrixStack.Entry entry = matrixStack.peek();
            Matrix4f matrix4f = entry.getPositionMatrix();
            Matrix3f matrix3f = entry.getNormalMatrix();
            vertex(vertexConsumer, matrix4f, matrix3f, af, m, ag, red, green, blue, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, af, 0.0F, ag, red, green, blue, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0F, ai, red, green, blue, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, m, ai, red, green, blue, 0.0F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, m, ak, red, green, blue, 0.4999F, ar);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0F, ak, red, green, blue, 0.4999F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, 0.0F, am, red, green, blue, 0.0F, aq);
            vertex(vertexConsumer, matrix4f, matrix3f, al, m, am, red, green, blue, 0.0F, ar);
            float as = 0.0F;
            if (sentinel.age % 2 == 0)
                as = 0.5F;
            vertex(vertexConsumer, matrix4f, matrix3f, x, m, y, red, green, blue, 0.5F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, z, m, aa, red, green, blue, 1.0F, as + 0.5F);
            vertex(vertexConsumer, matrix4f, matrix3f, ad, m, ae, red, green, blue, 1.0F, as);
            vertex(vertexConsumer, matrix4f, matrix3f, ab, m, ac, red, green, blue, 0.5F, as);
            matrixStack.pop();
        }
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(red, green, blue, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    @Override
    protected boolean isShaking(EclipseSentinelEntity entity) {
        return entity.isDead();
    }

    @Override
    protected void scale(EclipseSentinelEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(1.25f, 1.25f, 1.25f);
    }

    @Override
    public Identifier getTexture(EclipseSentinelEntity entity) {
        return entity.isExalted()
                ? UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_exalted.png")
                : UnseenWorld.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel.png");
    }
}