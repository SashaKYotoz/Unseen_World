package net.sashakyotoz.client.environment.weather;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.utils.Oscillator;

public class Grippfall implements DimensionRenderingRegistry.WeatherRenderer {
    private static final ResourceLocation GRIPPFALL = UnseenWorld.makeID("textures/environment/grippfall.png");
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
                float h = Mth.sqrt(f * f + g * g);
                this.NORMAL_LINE_DX[i << 5 | j] = -g / h;
                this.NORMAL_LINE_DZ[i << 5 | j] = f / h;
            }
        }
    }

    @Override
    public void render(WorldRenderContext context) {
        if (isGrippfallActive && !Minecraft.getInstance().isPaused()) {
            renderWeather(context.world(), context.lightmapTextureManager(), context.tickDelta(), context.camera().getPosition().x, context.camera().getPosition().y, context.camera().getPosition().z);
            tickGrippfallSplashing(context.world(), Minecraft.getInstance(), context.camera());
        }
    }

    private void renderWeather(ClientLevel world, LightTexture manager, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        manager.turnOnLightLayer();
        int i = Mth.floor(cameraX);
        int j = Mth.floor(cameraY);
        int k = Mth.floor(cameraZ);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int l = 3;
        if (Minecraft.useFancyGraphics())
            l = 6;

        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        int m = -1;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

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

                int q = world.getHeight(Heightmap.Types.MOTION_BLOCKING, o, n);
                int r = j - l;
                int s = j + l;

                if (r < q)
                    r = q;
                if (s < q)
                    s = q;

                int t = Math.max(q, j);

                if (r != s) {
                    RandomSource random = RandomSource.create((long) o * o * 3121 + o * 45238971L ^ (long) n * n * 418711 + n * 13761L);
                    if (random.nextDouble() < 0.35) {
                        mutable.set(o, r, n);
                        if (m != 0) {
                            if (m >= 0) {
                                tessellator.end();
                            }
                            m = 0;
                            RenderSystem.setShaderTexture(0, GRIPPFALL);
                            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                        }

                        int u = ClientTicks.getTicks() + o * o * 3121 + o * 45238971 + n * n * 418711 + n * 13761 & 31;
                        float h = ((float) u + tickDelta) / 32.0F * (2.0F + random.nextFloat());

                        double v = (double) o + 0.5 - cameraX;
                        double w = (double) n + 0.5 - cameraZ;
                        float x = (float) Math.sqrt(v * v + w * w) / (float) l;
                        float y = ((1.0F - x * x) * 0.5F + 0.5F) * Math.min(Oscillator.getOscillatingValue() + 0.25f, 1);
                        y *= 0.5f;
                        mutable.set(o, t, n);
                        int z = LevelRenderer.getLightColor(world, mutable);

                        double heightDiff = s - r;

                        double slantFactor = 0.5;
                        double slantX = heightDiff * slantFactor * xSlope;
                        double slantZ = heightDiff * slantFactor * zSlope;

                        bufferBuilder.vertex((double) o - cameraX - d + 0.5 + slantX, (double) s - cameraY, (double) n - cameraZ - e + 0.5 + slantZ)
                                .uv(0.0F, (float) r * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .uv2(z)
                                .endVertex();
                        bufferBuilder.vertex((double) o - cameraX + d + 0.5 + slantX, (double) s - cameraY, (double) n - cameraZ + e + 0.5 + slantZ)
                                .uv(1.0F, (float) r * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .uv2(z)
                                .endVertex();
                        bufferBuilder.vertex((double) o - cameraX + d + 0.5, (double) r - cameraY, (double) n - cameraZ + e + 0.5)
                                .uv(1.0F, (float) s * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .uv2(z)
                                .endVertex();
                        bufferBuilder.vertex((double) o - cameraX - d + 0.5, (double) r - cameraY, (double) n - cameraZ - e + 0.5)
                                .uv(0.0F, (float) s * 0.25F + h)
                                .color(1.0F, 1.0F, 1.0F, y)
                                .uv2(z)
                                .endVertex();
                    }
                }
            }
        }
        if (m >= 0)
            tessellator.end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        manager.turnOffLightLayer();
    }

    public void tickGrippfallSplashing(ClientLevel world, Minecraft client, Camera camera) {
        RandomSource random = world.getRandom();
        if (random.nextBoolean()) {
            BlockPos blockPos = BlockPos.containing(camera.getPosition());
            BlockPos blockPos2 = null;
            int i = (int) (50.0F * Oscillator.getOscillatingValue() * Oscillator.getOscillatingValue()) / (client.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

            for (int j = 0; j < i; j++) {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockPos3 = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos.offset(k, 0, l));
                if (blockPos3.getY() > world.getMinBuildHeight() && blockPos3.getY() <= blockPos.getY() + 10 && blockPos3.getY() >= blockPos.getY() - 10) {
                    blockPos2 = blockPos3.below();
                    if (client.options.particles().get() == ParticleStatus.MINIMAL)
                        break;
                    double d = random.nextDouble();
                    double e = random.nextDouble();
                    BlockState blockState = world.getBlockState(blockPos2);
                    FluidState fluidState = world.getFluidState(blockPos2);
                    VoxelShape voxelShape = blockState.getCollisionShape(world, blockPos2);
                    double g = voxelShape.max(Direction.Axis.Y, d, e);
                    double h = fluidState.getHeight(world, blockPos2);
                    double m = Math.max(g, h);
                    world.addParticle(ParticleTypes.BUBBLE_POP, (double) blockPos2.getX() + d, (double) blockPos2.getY() + m + 0.25f, (double) blockPos2.getZ() + e, 0.0, 0.1, 0.0);
                }
            }

            if (blockPos2 != null && random.nextInt(7) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                float rPitch = random.nextFloat() / 2f;
                if (blockPos2.getY() > blockPos.getY() + 1 && world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).getY() > blockPos.getY()) {
                    world.playLocalSound(blockPos2, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.WEATHER, 0.4F, 0.5F + rPitch, false);
                } else
                    world.playLocalSound(blockPos2, SoundEvents.AMETHYST_BLOCK_FALL, SoundSource.WEATHER, 0.7F, 1.25F + rPitch, false);
            }
        }
    }
}