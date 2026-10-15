package net.sashakyotoz.client.environment;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.ModDimensions;

public final class ChimericFog {
    private static final float LOW_Y = 48.0F;
    private static final float HIGH_Y = 128.0F;
    private static final float CLEAR = 0.0F;
    private static final float NORMAL = 0.5F;
    private static final float DENSE = 1.5F;
    private static final float BLEND_PER_TICK = 0.05F;
    private static final float FOG_THICKENING = 3.7F;
    private static final float SKY_FADE = 6.0F;
    private static final float DENSE_ENTER = 0.8F;
    private static final float DENSE_EXIT = 0.6F;
    private static float density = NORMAL;
    private static float previousDensity = NORMAL;
    private static boolean tracking;
    private static boolean dense;

    private ChimericFog() {
    }

    public static void tick(Minecraft minecraft) {
        ClientLevel level = minecraft.level;
        Entity camera = minecraft.getCameraEntity();
        if (level == null || camera == null || !level.dimension().equals(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY)) {
            tracking = false;
            dense = false;
            return;
        }
        float target = targetDensity(level.getBiome(BlockPos.containing(camera.getEyePosition())));
        if (!tracking) {
            tracking = true;
            density = target;
            previousDensity = target;
        } else {
            previousDensity = density;
            density += (target - density) * BLEND_PER_TICK;
        }
        float local = localDensity(heightFactor(camera.getEyeY()), 1.0F);
        if (dense) {
            dense = local > DENSE_EXIT;
        } else {
            dense = local > DENSE_ENTER;
        }
    }

    public static boolean isBossFog() {
        return Minecraft.getInstance().gui.getBossOverlay().shouldCreateWorldFog();
    }

    public static boolean hasClearView(Camera camera) {
        if (camera.getFluidInCamera() != FogType.NONE) {
            return false;
        }
        Entity entity = camera.getEntity();
        return !(entity instanceof LivingEntity living) || !(living.hasEffect(MobEffects.BLINDNESS) || living.hasEffect(MobEffects.DARKNESS));
    }

    public static float skyVisibility(float partialTick) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        float local = localDensity(heightFactor(camera.getPosition().y), partialTick);
        return 1.0F / (1.0F + Math.max(0.0F, local - NORMAL) * SKY_FADE);
    }

    public static void apply(Camera camera, float farPlaneDistance, float rainLevel, float partialTick) {
        float start;
        float end;
        if (isBossFog()) {
            start = farPlaneDistance * 0.05F;
            end = Math.min(farPlaneDistance, 192.0F) * 0.5F;
        } else {
            float height = heightFactor(camera.getPosition().y);
            float local = localDensity(height, partialTick);
            float excess = Math.max(0.0F, local - NORMAL);
            float startScale = Mth.clamp(1.0F - excess / NORMAL, 0.0F, 1.0F);
            float endScale = local < NORMAL ? Mth.lerp(Mth.clamp(local / NORMAL, 0.0F, 1.0F), 1.4F, 1.0F) : 1.0F / (1.0F + excess * FOG_THICKENING);
            start = farPlaneDistance * Mth.lerp(height, 0.0F, 0.2F) * startScale;
            end = Math.min(farPlaneDistance, farPlaneDistance * Mth.lerp(height, 0.5F, 0.9F) * endScale) * (1.0F - rainLevel * 0.25F);
        }
        RenderSystem.setShaderFogStart(start);
        RenderSystem.setShaderFogEnd(Math.max(end, start + 1.0F));
    }

    private static float targetDensity(Holder<Biome> biome) {
        if (biome.is(ModTags.Biomes.HAS_DENSE_FOG)) {
            return DENSE;
        }
        return biome.is(ModTags.Biomes.HAS_CLEAR_FOG) ? CLEAR : NORMAL;
    }

    private static float heightFactor(double y) {
        return Mth.clamp(((float) y - LOW_Y) / (HIGH_Y - LOW_Y), 0.0F, 1.0F);
    }

    private static float localDensity(float height, float partialTick) {
        float biomeDensity = Mth.lerp(partialTick, previousDensity, density);
        float groundFog = Math.max(0.0F, biomeDensity - NORMAL) * (1.0F - height);
        return Math.min(biomeDensity, NORMAL) + groundFog;
    }
}