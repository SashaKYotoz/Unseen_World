package net.sashakyotoz.client.environment.weather;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.utils.Oscillator;

public class Grippfall implements DimensionRenderingRegistry.WeatherRenderer {
    private static final Identifier GRIPPFALL = UnseenWorld.makeID("textures/environment/grippfall.png");
    private final float[] NORMAL_LINE_DX = new float[1024];
    private final float[] NORMAL_LINE_DZ = new float[1024];

    public static boolean isGrippfallActive = false;
    public static String direction = "north";
    private int rainSoundCounter;

    public Grippfall() {
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                float f = (float) (j - 16);
                float g = (float) (i - 16);
                float h = MathHelper.sqrt(f * f + g * g);
                this.NORMAL_LINE_DX[i << 5 | j] = -g / h;
                this.NORMAL_LINE_DZ[i << 5 | j] = f / h;
            }
        }
    }

    @Override
    public void render(WorldRenderContext context) {
        if (isGrippfallActive && !MinecraftClient.getInstance().isPaused()) {
            renderWeather(context.world(), context.lightmapTextureManager(), context.tickDelta(), context.camera().getPos().x, context.camera().getPos().y, context.camera().getPos().z);
            tickGrippfallSplashing(context.world(), MinecraftClient.getInstance(), context.camera());
        }
    }

    private void renderWeather(ClientWorld world, LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        manager.enable();
        int i = MathHelper.floor(cameraX);
        int j = MathHelper.floor(cameraY);
        int k = MathHelper.floor(cameraZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int l = 3;
        if (MinecraftClient.isFancyGraphicsOrBetter())
            l = 6;

        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        int m = -1;
        RenderSystem.setShader(GameRenderer::getParticleProgram);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        float xSlope;
        float zSlope = switch (direction.toLowerCase()) {
            case "north" -> {
                xSlope = 0.0f;
                yield 1.0f;
            }
            case "south" -> {
                xSlope = 0.0f;
                yield -1.0f;
            }
            case "east" -> {
                xSlope = -1.0f;
                yield 0.0f;
            }
            case "west" -> {
                xSlope = 1.0f;
                yield 0.0f;
            }
            default -> {
                xSlope = 1.0f;
                yield 1.0f;
            }
        };

        for (int n = k - l; n <= k + l; n++) {
            for (int o = i - l; o <= i + l; o++) {
                int p = (n - k + 16) * 32 + o - i + 16;
                double d = (double) NORMAL_LINE_DX[p] * 0.5;
                double e = (double) NORMAL_LINE_DZ[p] * 0.5;
                mutable.set(o, cameraY, n);

                int q = world.getTopY(Heightmap.Type.MOTION_BLOCKING, o, n);
                int r = j - l;
                int s = j + l;

                if (r < q)
                    r = q;
                if (s < q)
                    s = q;

                int t = Math.max(q, j);

                if (r != s) {
                    Random random = Random.create((long) o * o * 3121 + o * 45238971L ^ (long) n * n * 418711 + n * 13761L);
                    if (random.nextDouble() < 0.35) {
                        mutable.set(o, r, n);
                        if (m != 0) {
                            if (m >= 0) {
                                tessellator.draw();
                            }
                            m = 0;
                            RenderSystem.setShaderTexture(0, GRIPPFALL);
                            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                        }

                        int u = ClientTicks.getTicks() + o * o * 3121 + o * 45238971 + n * n * 418711 + n * 13761 & 31;
                        float h = ((float) u + tickDelta) / 32.0F * (2.0F + random.nextFloat());

                        double v = (double) o + 0.5 - cameraX;
                        double w = (double) n + 0.5 - cameraZ;
                        float x = (float) Math.sqrt(v * v + w * w) / (float) l;
                        float y = ((1.0F - x * x) * 0.5F + 0.5F) * Math.min(Oscillator.getOscillatingValue() + 0.25f, 1);
                        y *= 0.5f;
                        mutable.set(o, t, n);
                        int z = WorldRenderer.getLightmapCoordinates(world, mutable);

                        double heightDiff = s - r;

                        double slantFactor = 0.5;
                        double slantX = heightDiff * slantFactor * xSlope;
                        double slantZ = heightDiff * slantFactor * zSlope;

                        bufferBuilder.vertex((double) o - cameraX - d + 0.5 + slantX, (double) s - cameraY, (double) n - cameraZ - e + 0.5 + slantZ)
                                .texture(0.0F, (float) r * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .light(z)
                                .next();
                        bufferBuilder.vertex((double) o - cameraX + d + 0.5 + slantX, (double) s - cameraY, (double) n - cameraZ + e + 0.5 + slantZ)
                                .texture(1.0F, (float) r * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .light(z)
                                .next();
                        bufferBuilder.vertex((double) o - cameraX + d + 0.5, (double) r - cameraY, (double) n - cameraZ + e + 0.5)
                                .texture(1.0F, (float) s * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .light(z)
                                .next();
                        bufferBuilder.vertex((double) o - cameraX - d + 0.5, (double) r - cameraY, (double) n - cameraZ - e + 0.5)
                                .texture(0.0F, (float) s * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .light(z)
                                .next();
                    }
                }
            }
        }
        if (m >= 0)
            tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        manager.disable();
    }

    public void tickGrippfallSplashing(ClientWorld world, MinecraftClient client, Camera camera) {
        Random random = world.getRandom();
        if (random.nextBoolean()) {
            BlockPos blockPos = BlockPos.ofFloored(camera.getPos());
            BlockPos blockPos2 = null;
            int i = (int) (50.0F * Oscillator.getOscillatingValue() * Oscillator.getOscillatingValue()) / (client.options.getParticles().getValue() == ParticlesMode.DECREASED ? 2 : 1);

            for (int j = 0; j < i; j++) {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockPos3 = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos.add(k, 0, l));
                if (blockPos3.getY() > world.getBottomY() && blockPos3.getY() <= blockPos.getY() + 10 && blockPos3.getY() >= blockPos.getY() - 10) {
                    blockPos2 = blockPos3.down();
                    if (client.options.getParticles().getValue() == ParticlesMode.MINIMAL)
                        break;
                    double d = random.nextDouble();
                    double e = random.nextDouble();
                    BlockState blockState = world.getBlockState(blockPos2);
                    FluidState fluidState = world.getFluidState(blockPos2);
                    VoxelShape voxelShape = blockState.getCollisionShape(world, blockPos2);
                    double g = voxelShape.getEndingCoord(Direction.Axis.Y, d, e);
                    double h = fluidState.getHeight(world, blockPos2);
                    double m = Math.max(g, h);
                    world.addParticle(ParticleTypes.BUBBLE_POP, (double) blockPos2.getX() + d, (double) blockPos2.getY() + m + 0.25f, (double) blockPos2.getZ() + e, 0.0, 0.1, 0.0);
                }
            }

            if (blockPos2 != null && random.nextInt(7) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                float rPitch = random.nextFloat() / 2f;
                if (blockPos2.getY() > blockPos.getY() + 1 && world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > blockPos.getY()) {
                    world.playSoundAtBlockCenter(blockPos2, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.WEATHER, 0.4F, 0.5F + rPitch, false);
                } else
                    world.playSoundAtBlockCenter(blockPos2, SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.WEATHER, 0.7F, 1.25F + rPitch, false);
            }
        }
    }
}