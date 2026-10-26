package net.sashakyotoz.client.renderers.fx;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.util.BossParticles;
import org.joml.Matrix4f;

public final class FxDraw {
    private static final Vec3 UP = new Vec3(0.0D, 1.0D, 0.0D);
    private static final Vec3 EAST = new Vec3(1.0D, 0.0D, 0.0D);
    private static final Vec3 SOUTH = new Vec3(0.0D, 0.0D, 1.0D);

    private FxDraw() {
    }

    public static int argb(int rgb, float alpha) {
        int a = Mth.clamp((int) (alpha * 255.0F), 0, 255);
        return (a << 24) | (rgb & 0xFFFFFF);
    }

    public static int mix(int from, int to, float t) {
        float clamped = Mth.clamp(t, 0.0F, 1.0F);
        int a = (int) Mth.lerp(clamped, (from >>> 24) & 0xFF, (to >>> 24) & 0xFF);
        int r = (int) Mth.lerp(clamped, (from >> 16) & 0xFF, (to >> 16) & 0xFF);
        int g = (int) Mth.lerp(clamped, (from >> 8) & 0xFF, (to >> 8) & 0xFF);
        int b = (int) Mth.lerp(clamped, from & 0xFF, to & 0xFF);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, Vec3 pos, int argb) {
        consumer.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z)
                .color((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, (argb >>> 24) & 0xFF)
                .endVertex();
    }

    public static void quad(VertexConsumer consumer, Matrix4f matrix, Vec3 a, Vec3 b, Vec3 c, Vec3 d, int colorA, int colorB, int colorC, int colorD) {
        vertex(consumer, matrix, a, colorA);
        vertex(consumer, matrix, b, colorB);
        vertex(consumer, matrix, c, colorC);
        vertex(consumer, matrix, d, colorD);
        vertex(consumer, matrix, d, colorD);
        vertex(consumer, matrix, c, colorC);
        vertex(consumer, matrix, b, colorB);
        vertex(consumer, matrix, a, colorA);
    }

    public static void ring(VertexConsumer consumer, Matrix4f matrix, Vec3 center, Vec3 axisU, Vec3 axisV, float innerRadius, float outerRadius, int segments, int innerColor, int outerColor, float rotation) {
        for (int i = 0; i < segments; i++) {
            double a0 = rotation + i * (Math.PI * 2.0D / segments);
            double a1 = rotation + (i + 1) * (Math.PI * 2.0D / segments);
            Vec3 d0 = axisU.scale(Math.cos(a0)).add(axisV.scale(Math.sin(a0)));
            Vec3 d1 = axisU.scale(Math.cos(a1)).add(axisV.scale(Math.sin(a1)));
            quad(consumer, matrix,
                    center.add(d0.scale(innerRadius)), center.add(d0.scale(outerRadius)),
                    center.add(d1.scale(outerRadius)), center.add(d1.scale(innerRadius)),
                    innerColor, outerColor, outerColor, innerColor);
        }
    }

    public static void flatRing(VertexConsumer consumer, Matrix4f matrix, Vec3 center, float innerRadius, float outerRadius, int segments, int innerColor, int outerColor, float rotation) {
        ring(consumer, matrix, center, EAST, SOUTH, innerRadius, outerRadius, segments, innerColor, outerColor, rotation);
    }

    public static void runes(VertexConsumer consumer, Matrix4f matrix, Vec3 center, float radius, int count, float size, int color, float rotation) {
        for (int i = 0; i < count; i++) {
            double angle = rotation + i * (Math.PI * 2.0D / count);
            Vec3 radial = new Vec3(Math.cos(angle), 0.0D, Math.sin(angle));
            Vec3 tangent = new Vec3(-radial.z, 0.0D, radial.x);
            Vec3 middle = center.add(radial.scale(radius));
            Vec3 inner = middle.subtract(radial.scale(size));
            Vec3 outer = middle.add(radial.scale(size));
            Vec3 left = middle.add(tangent.scale(size * 0.45D));
            Vec3 right = middle.subtract(tangent.scale(size * 0.45D));
            quad(consumer, matrix, inner, left, outer, right, color, color, color, color);
        }
    }

    public static void spokes(VertexConsumer consumer, Matrix4f matrix, Vec3 center, Vec3 axisU, Vec3 axisV, int count, float innerRadius, float outerRadius, float width, int innerColor, int outerColor, float rotation) {
        for (int i = 0; i < count; i++) {
            double angle = rotation + i * (Math.PI * 2.0D / count);
            Vec3 radial = axisU.scale(Math.cos(angle)).add(axisV.scale(Math.sin(angle)));
            Vec3 tangent = axisU.scale(-Math.sin(angle)).add(axisV.scale(Math.cos(angle)));
            float reach = (i % 2 == 0) ? outerRadius : innerRadius + (outerRadius - innerRadius) * 0.6F;
            Vec3 base = center.add(radial.scale(innerRadius));
            Vec3 tip = center.add(radial.scale(reach));
            quad(consumer, matrix, base.add(tangent.scale(width)), tip, tip, base.subtract(tangent.scale(width)), innerColor, outerColor, outerColor, innerColor);
        }
    }

    public static void rays(VertexConsumer consumer, Matrix4f matrix, Vec3 center, int count, float length, float width, int coreColor, long seed, float spin) {
        RandomSource random = RandomSource.create(seed);
        int tipColor = coreColor & 0x00FFFFFF;
        for (int i = 0; i < count; i++) {
            Vec3 direction = BossParticles.randomUnit(random).yRot(spin * (0.5F + random.nextFloat())).xRot(spin * 0.3F);
            float rayLength = length * (0.55F + random.nextFloat() * 0.45F);
            Vec3 side = direction.cross(Math.abs(direction.y) > 0.9D ? EAST : UP).normalize().scale(width * rayLength * 0.12F);
            Vec3 tip = center.add(direction.scale(rayLength));
            Vec3 shoulder = center.add(direction.scale(rayLength * 0.75D));
            quad(consumer, matrix, center, shoulder.add(side), tip, shoulder.subtract(side), coreColor, tipColor, tipColor, tipColor);
        }
    }

    public static void beam(VertexConsumer consumer, Matrix4f matrix, Vec3 from, Vec3 to, float radius, int startColor, int endColor) {
        Vec3 axis = to.subtract(from);
        if (axis.lengthSqr() < 1.0E-6D) {
            return;
        }
        Vec3 direction = axis.normalize();
        Vec3 sideA = direction.cross(Math.abs(direction.y) > 0.95D ? EAST : UP).normalize().scale(radius);
        Vec3 sideB = direction.cross(sideA).normalize().scale(radius);
        quad(consumer, matrix, from.add(sideA), to.add(sideA), to.subtract(sideA), from.subtract(sideA), startColor, endColor, endColor, startColor);
        quad(consumer, matrix, from.add(sideB), to.add(sideB), to.subtract(sideB), from.subtract(sideB), startColor, endColor, endColor, startColor);
    }

    public static void sphere(VertexConsumer consumer, Matrix4f matrix, Vec3 center, float radius, int latitudeBands, int longitudeBands, int color, float shimmer, boolean upperHalfOnly) {
        int bands = upperHalfOnly ? latitudeBands / 2 : latitudeBands;
        for (int lat = 0; lat < bands; lat++) {
            double theta0 = Math.PI * lat / latitudeBands;
            double theta1 = Math.PI * (lat + 1) / latitudeBands;
            float wave = 0.65F + 0.35F * Mth.sin(shimmer + lat * 0.9F);
            int bandColor = argb(color, ((color >>> 24) & 0xFF) / 255.0F * wave);
            for (int lon = 0; lon < longitudeBands; lon++) {
                double phi0 = Math.PI * 2.0D * lon / longitudeBands;
                double phi1 = Math.PI * 2.0D * (lon + 1) / longitudeBands;
                quad(consumer, matrix,
                        spherePoint(center, radius, theta0, phi0), spherePoint(center, radius, theta1, phi0),
                        spherePoint(center, radius, theta1, phi1), spherePoint(center, radius, theta0, phi1),
                        bandColor, bandColor, bandColor, bandColor);
            }
        }
    }

    public static void rimDome(VertexConsumer consumer, Matrix4f matrix, Vec3 center, float radius, int bands, int segments, Vec3 eye, int color, float shimmer) {
        float baseAlpha = ((color >>> 24) & 0xFF) / 255.0F;
        for (int lat = 0; lat < bands; lat++) {
            double theta0 = Math.PI * 0.5D * lat / bands;
            double theta1 = Math.PI * 0.5D * (lat + 1) / bands;
            float wave = 0.75F + 0.25F * Mth.sin(shimmer + lat * 0.9F);
            for (int lon = 0; lon < segments; lon++) {
                double phi0 = Math.PI * 2.0D * lon / segments;
                double phi1 = Math.PI * 2.0D * (lon + 1) / segments;
                Vec3 a = spherePoint(center, radius, theta0, phi0);
                Vec3 b = spherePoint(center, radius, theta1, phi0);
                Vec3 c = spherePoint(center, radius, theta1, phi1);
                Vec3 d = spherePoint(center, radius, theta0, phi1);
                quad(consumer, matrix, a, b, c, d,
                        rimColor(color, baseAlpha * wave, a, center, eye),
                        rimColor(color, baseAlpha * wave, b, center, eye),
                        rimColor(color, baseAlpha * wave, c, center, eye),
                        rimColor(color, baseAlpha * wave, d, center, eye));
            }
        }
    }

    private static int rimColor(int color, float alpha, Vec3 point, Vec3 center, Vec3 eye) {
        Vec3 normal = point.subtract(center);
        Vec3 view = eye.subtract(point);
        double lengths = normal.length() * view.length();
        if (lengths < 1.0E-6D) {
            return argb(color, 0.0F);
        }
        float facing = (float) Math.abs(normal.dot(view) / lengths);
        float rim = 1.0F - facing;
        return argb(color, alpha * rim * rim * rim);
    }

    private static Vec3 spherePoint(Vec3 center, float radius, double theta, double phi) {
        return center.add(Math.sin(theta) * Math.cos(phi) * radius, Math.cos(theta) * radius, Math.sin(theta) * Math.sin(phi) * radius);
    }
}