package net.sashakyotoz.client.environment;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.client.shaders.AuroraShader;
import org.joml.Matrix4f;

public final class ChimericDarknessSkyRenderer {
    public static final ChimericDarknessSkyRenderer INSTANCE = new ChimericDarknessSkyRenderer();

    private static final float[] ZENITH = {0.010F, 0.008F, 0.030F};
    private static final float[] BAND = {0.020F, 0.060F, 0.070F};
    private static final float[] HORIZON = {0.090F, 0.030F, 0.055F};
    private static final float[] WHITE = {1.0F, 1.0F, 1.0F};
    private static final float[] AURORA_LOW = {0.10F, 0.36F, 0.31F};
    private static final float[] AURORA_HIGH = {0.33F, 0.09F, 0.29F};
    private static final float[] RAY = {0.50F, 0.24F, 0.33F};
    private static final float[][] STAR_TINTS = {{0.86F, 0.82F, 1.00F}, {0.70F, 0.90F, 0.94F}, {1.00F, 0.74F, 0.86F}};
    private static final float[] AURORA_ROWS = {0.0F, 0.18F, 0.55F, 1.0F};
    private static final float[] AURORA_WEIGHTS = {0.0F, 1.0F, 0.45F, 0.0F};
    private static final double DOME_RADIUS = 100.0D;
    private static final double STAR_RADIUS = 90.0D;
    private static final double RAY_RADIUS = 85.0D;
    private static final double AURORA_RADIUS = 80.0D;
    private static final int STAR_ATTEMPTS = 3000;
    private static final int AURORA_COLUMNS = 240;
    private static final int RAY_COUNT = 11;

    private VertexBuffer dome;
    private VertexBuffer horizon;
    private VertexBuffer stars;
    private VertexBuffer aurora;
    private VertexBuffer rays;

    private ChimericDarknessSkyRenderer() {
    }

    public void render(ClientLevel level, PoseStack poseStack, Matrix4f projection, float partialTick) {
        if (this.dome == null) {
            this.build();
        }
        Matrix4f pose = poseStack.last().pose();
        double time = level.getGameTime() + partialTick;
        float clearSky = 1.0F - level.getRainLevel(partialTick);
        float visibility = ChimericFog.skyVisibility(partialTick);
        float[] fog = RenderSystem.getShaderFogColor();
        float fogRed = fog[0];
        float fogGreen = fog[1];
        float fogBlue = fog[2];

        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        draw(this.dome, pose, projection, 1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        float twinkle = 0.85F + 0.15F * (float) Math.sin(time * 0.05D);
        draw(this.stars, pose, projection, 1.0F, 1.0F, 1.0F, twinkle * clearSky * visibility);

        float glow = 0.75F + 0.25F * (float) Math.sin(time * 0.011D);
        draw(this.rays, pose, projection, 1.0F, 1.0F, 1.0F, glow * (0.4F + 0.6F * clearSky) * visibility);

        float auroraFade = (0.4F + 0.6F * clearSky) * visibility;
        ShaderInstance auroraShader = AuroraShader.get();
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.sin(time * 0.004D) * 2.5F));
        draw(this.aurora, poseStack.last().pose(), projection, auroraShader, 1.0F, 1.0F, 1.0F, 0.9F * auroraFade);
        poseStack.mulPose(Axis.YP.rotationDegrees(7.0F));
        draw(this.aurora, poseStack.last().pose(), projection, auroraShader, 1.0F, 1.0F, 1.0F, 0.5F * auroraFade);
        poseStack.popPose();

        RenderSystem.defaultBlendFunc();
        draw(this.horizon, pose, projection, fogRed, fogGreen, fogBlue, 1.0F);

        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
    }

    private void build() {
        this.dome = upload(buildDome());
        this.horizon = upload(buildHorizon());
        this.stars = upload(buildStars());
        this.aurora = upload(buildAurora());
        this.rays = upload(buildRays());
    }

    private static void draw(VertexBuffer buffer, Matrix4f pose, Matrix4f projection, float red, float green, float blue, float alpha) {
        draw(buffer, pose, projection, GameRenderer.getPositionColorShader(), red, green, blue, alpha);
    }

    private static void draw(VertexBuffer buffer, Matrix4f pose, Matrix4f projection, ShaderInstance shader, float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);
        buffer.bind();
        buffer.drawWithShader(pose, projection, shader);
    }

    private static VertexBuffer upload(BufferBuilder.RenderedBuffer rendered) {
        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        buffer.bind();
        buffer.upload(rendered);
        VertexBuffer.unbind();
        return buffer;
    }

    private static BufferBuilder begin() {
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        return builder;
    }

    private static BufferBuilder.RenderedBuffer buildDome() {
        BufferBuilder builder = begin();
        int rings = 24;
        int segments = 48;
        for (int ring = 0; ring < rings; ring++) {
            double low = Math.toRadians(Mth.lerp((double) ring / rings, -30.0D, 90.0D));
            double high = Math.toRadians(Mth.lerp((double) (ring + 1) / rings, -30.0D, 90.0D));
            float[] lowColor = domeColor(low);
            float[] highColor = domeColor(high);
            for (int segment = 0; segment < segments; segment++) {
                double left = Math.PI * 2.0D * segment / segments;
                double right = Math.PI * 2.0D * (segment + 1) / segments;
                vertex(builder, left, low, DOME_RADIUS, lowColor, 1.0F);
                vertex(builder, right, low, DOME_RADIUS, lowColor, 1.0F);
                vertex(builder, right, high, DOME_RADIUS, highColor, 1.0F);
                vertex(builder, left, high, DOME_RADIUS, highColor, 1.0F);
            }
        }
        return builder.end();
    }

    private static BufferBuilder.RenderedBuffer buildHorizon() {
        BufferBuilder builder = begin();
        int rings = 14;
        int segments = 48;
        for (int ring = 0; ring < rings; ring++) {
            double low = Math.toRadians(Mth.lerp((double) ring / rings, -30.0D, 40.0D));
            double high = Math.toRadians(Mth.lerp((double) (ring + 1) / rings, -30.0D, 40.0D));
            float lowAlpha = horizonAlpha(low);
            float highAlpha = horizonAlpha(high);
            for (int segment = 0; segment < segments; segment++) {
                double left = Math.PI * 2.0D * segment / segments;
                double right = Math.PI * 2.0D * (segment + 1) / segments;
                vertex(builder, left, low, DOME_RADIUS, WHITE, lowAlpha);
                vertex(builder, right, low, DOME_RADIUS, WHITE, lowAlpha);
                vertex(builder, right, high, DOME_RADIUS, WHITE, highAlpha);
                vertex(builder, left, high, DOME_RADIUS, WHITE, highAlpha);
            }
        }
        return builder.end();
    }

    private static BufferBuilder.RenderedBuffer buildStars() {
        BufferBuilder builder = begin();
        RandomSource random = RandomSource.create(10842L);
        for (int i = 0; i < STAR_ATTEMPTS; i++) {
            double x = random.nextFloat() * 2.0F - 1.0F;
            double y = random.nextFloat() * 2.0F - 1.0F;
            double z = random.nextFloat() * 2.0F - 1.0F;
            double size = 0.15F + random.nextFloat() * 0.1F;
            double spin = random.nextDouble() * Math.PI * 2.0D;
            float[] tint = STAR_TINTS[random.nextInt(STAR_TINTS.length)];
            float brightness = 0.45F + random.nextFloat() * 0.55F;
            double lengthSquared = x * x + y * y + z * z;
            if (lengthSquared >= 1.0D || lengthSquared <= 0.01D) {
                continue;
            }
            double inverse = 1.0D / Math.sqrt(lengthSquared);
            x *= inverse;
            y *= inverse;
            z *= inverse;
            if (y <= 0.0D) {
                continue;
            }
            float alpha = brightness * smoothstep(0.03F, 0.30F, (float) y);
            double centerX = x * STAR_RADIUS;
            double centerY = y * STAR_RADIUS;
            double centerZ = z * STAR_RADIUS;
            double yaw = Math.atan2(x, z);
            double yawSin = Math.sin(yaw);
            double yawCos = Math.cos(yaw);
            double pitch = Math.atan2(Math.sqrt(x * x + z * z), y);
            double pitchSin = Math.sin(pitch);
            double pitchCos = Math.cos(pitch);
            double spinSin = Math.sin(spin);
            double spinCos = Math.cos(spin);
            for (int corner = 0; corner < 4; corner++) {
                double u = ((corner & 2) - 1) * size;
                double v = ((corner + 1 & 2) - 1) * size;
                double rotatedU = u * spinCos - v * spinSin;
                double rotatedV = v * spinCos + u * spinSin;
                double offsetY = rotatedU * pitchSin;
                double radial = -rotatedU * pitchCos;
                double offsetX = radial * yawSin - rotatedV * yawCos;
                double offsetZ = rotatedV * yawSin + radial * yawCos;
                builder.vertex(centerX + offsetX, centerY + offsetY, centerZ + offsetZ).color(tint[0], tint[1], tint[2], alpha).endVertex();
            }
        }
        return builder.end();
    }

    private static BufferBuilder.RenderedBuffer buildAurora() {
        BufferBuilder builder = begin();
        RandomSource random = RandomSource.create(4096L);
        float[] intensity = new float[AURORA_COLUMNS + 1];
        for (int column = 0; column < AURORA_COLUMNS; column++) {
            intensity[column] = 0.3F + random.nextFloat() * 0.7F;
        }
        intensity[AURORA_COLUMNS] = intensity[0];
        for (int column = 0; column < AURORA_COLUMNS; column++) {
            double left = Math.PI * 2.0D * column / AURORA_COLUMNS;
            double right = Math.PI * 2.0D * (column + 1) / AURORA_COLUMNS;
            for (int row = 0; row < AURORA_ROWS.length - 1; row++) {
                auroraVertex(builder, left, row, intensity[column]);
                auroraVertex(builder, right, row, intensity[column + 1]);
                auroraVertex(builder, right, row + 1, intensity[column + 1]);
                auroraVertex(builder, left, row + 1, intensity[column]);
            }
        }
        return builder.end();
    }

    private static void auroraVertex(BufferBuilder builder, double azimuth, int row, float intensity) {
        double base = Math.toRadians(16.0D + 6.0D * Math.sin(3.0D * azimuth + 1.3D));
        double height = Math.toRadians(34.0D + 12.0D * Math.sin(5.0D * azimuth + 0.4D));
        double elevation = base + height * AURORA_ROWS[row];
        float band = 0.12F + 0.88F * smoothstep(-0.1F, 0.7F, (float) Math.sin(2.0D * azimuth + 0.4D));
        float hue = Mth.clamp(AURORA_ROWS[row] * 0.9F + 0.25F * (float) Math.sin(3.0D * azimuth), 0.0F, 1.0F);
        vertex(builder, azimuth, elevation, AURORA_RADIUS, mix(AURORA_LOW, AURORA_HIGH, hue), AURORA_WEIGHTS[row] * intensity * band * 0.6F);
    }

    private static BufferBuilder.RenderedBuffer buildRays() {
        BufferBuilder builder = begin();
        RandomSource random = RandomSource.create(7717L);
        double sourceAzimuth = Math.toRadians(225.0D);
        double sourceElevation = Math.toRadians(72.0D);
        double tip = Math.toRadians(0.4D);
        double foot = Math.toRadians(4.0D);
        for (int ray = 0; ray < RAY_COUNT; ray++) {
            double azimuth = sourceAzimuth + Math.toRadians((ray - RAY_COUNT / 2) * 11.0D + random.nextFloat() * 6.0D - 3.0D);
            double halfWidth = Math.toRadians(1.2D + random.nextFloat() * 2.2D);
            float strength = 0.03F + random.nextFloat() * 0.05F;
            vertex(builder, sourceAzimuth - tip, sourceElevation, RAY_RADIUS, RAY, strength);
            vertex(builder, sourceAzimuth + tip, sourceElevation, RAY_RADIUS, RAY, strength);
            vertex(builder, azimuth + halfWidth, foot, RAY_RADIUS, RAY, 0.0F);
            vertex(builder, azimuth - halfWidth, foot, RAY_RADIUS, RAY, 0.0F);
        }
        return builder.end();
    }

    private static void vertex(BufferBuilder builder, double azimuth, double elevation, double radius, float[] color, float alpha) {
        double horizontal = Math.cos(elevation) * radius;
        builder.vertex(Math.sin(azimuth) * horizontal, Math.sin(elevation) * radius, Math.cos(azimuth) * horizontal)
                .color(color[0], color[1], color[2], alpha)
                .endVertex();
    }

    private static float[] domeColor(double elevation) {
        float height = (float) Math.sin(elevation);
        float[] lower = mix(HORIZON, BAND, smoothstep(0.0F, 0.4F, height));
        return mix(lower, ZENITH, smoothstep(0.4F, 1.0F, height));
    }

    private static float horizonAlpha(double elevation) {
        return 1.0F - smoothstep(0.0F, 0.6F, (float) Math.sin(elevation));
    }

    private static float[] mix(float[] from, float[] to, float delta) {
        return new float[]{Mth.lerp(delta, from[0], to[0]), Mth.lerp(delta, from[1], to[1]), Mth.lerp(delta, from[2], to[2])};
    }

    private static float smoothstep(float edge0, float edge1, float value) {
        float t = Mth.clamp((value - edge0) / (edge1 - edge0), 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}