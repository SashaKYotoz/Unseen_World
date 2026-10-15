package net.sashakyotoz.client.renderers.fx;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.renderers.types.FxRenderType;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.StateMachineBoss;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.entities.custom.AureateVexEntity;
import net.sashakyotoz.common.entities.custom.ReaverEntity;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

public final class BossFxRenderer {
    private static final Set<Entity> TRACKED = Collections.newSetFromMap(new IdentityHashMap<>());
    private static final double MAX_DISTANCE_SQR = 160.0D * 160.0D;
    private static final int SHOCKWAVE_TICKS = 12;
    private static final Vec3 UP = new Vec3(0.0D, 1.0D, 0.0D);

    private BossFxRenderer() {
    }

    public static void track(Entity entity) {
        if (entity instanceof StateMachineBoss<?> || entity instanceof ReaverEntity || entity instanceof AureateVexEntity) {
            TRACKED.add(entity);
        }
    }

    public static void untrack(Entity entity) {
        TRACKED.remove(entity);
    }

    public static void render(WorldRenderContext context) {
        if (TRACKED.isEmpty()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource buffers = context.consumers();
        if (buffers == null || minecraft.level == null) {
            return;
        }
        PoseStack poseStack = context.matrixStack();
        Vec3 camera = context.camera().getPosition();
        float partialTick = context.tickDelta();
        VertexConsumer consumer = buffers.getBuffer(FxRenderType.glow());

        Iterator<Entity> iterator = TRACKED.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity.isRemoved() || entity.level() != minecraft.level) {
                iterator.remove();
                continue;
            }
            if (entity.isInvisible() || entity.distanceToSqr(camera) > MAX_DISTANCE_SQR) {
                continue;
            }
            Vec3 base = lerpedPosition(entity, partialTick);
            poseStack.pushPose();
            poseStack.translate(base.x - camera.x, base.y - camera.y, base.z - camera.z);
            Matrix4f matrix = poseStack.last().pose();
            float time = entity.tickCount + partialTick;
            if (entity instanceof AuricPatriarch patriarch) {
                renderPatriarch(consumer, matrix, patriarch, partialTick, time);
            } else if (entity instanceof EclipseSentinel sentinel) {
                renderSentinel(consumer, matrix, sentinel, base, partialTick, time);
            } else if (entity instanceof WarriorOfChimericDarkness warrior) {
                renderWarrior(consumer, matrix, warrior, base, camera, partialTick, time);
            } else {
                renderTether(consumer, matrix, entity, base, partialTick, time);
            }
            poseStack.popPose();
        }
    }

    private static Vec3 lerpedPosition(Entity entity, float partialTick) {
        return new Vec3(Mth.lerp(partialTick, entity.xo, entity.getX()), Mth.lerp(partialTick, entity.yo, entity.getY()), Mth.lerp(partialTick, entity.zo, entity.getZ()));
    }

    private static Vec3 forward(LivingEntity entity, float partialTick) {
        float yaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot) * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
    }

    private static Vec3 left(Vec3 forward) {
        return new Vec3(forward.z, 0.0D, -forward.x);
    }

    private static float pulse(float time, float speed, float min, float max) {
        return min + (max - min) * (0.5F + 0.5F * Mth.sin(time * speed));
    }

    private static void renderShockwave(VertexConsumer consumer, Matrix4f matrix, StateMachineBoss<?> boss, Vec3 base, float partialTick, float maxRadius, int color) {
        float age = boss.ticksSinceImpact(partialTick);
        if (age < 0.0F || age > SHOCKWAVE_TICKS) {
            return;
        }
        float progress = age / SHOCKWAVE_TICKS;
        float radius = 0.5F + maxRadius * (1.0F - (1.0F - progress) * (1.0F - progress));
        Vec3 center = boss.getImpactPos().subtract(base).add(0.0D, 0.08D, 0.0D);
        float alpha = (1.0F - progress) * 0.85F;
        FxDraw.flatRing(consumer, matrix, center, Math.max(0.0F, radius - 0.9F), radius, 48, FxDraw.argb(color, 0.0F), FxDraw.argb(color, alpha), 0.0F);
        FxDraw.flatRing(consumer, matrix, center, radius, radius + 0.25F, 48, FxDraw.argb(0xFFFFFF, alpha * 0.8F), FxDraw.argb(color, 0.0F), 0.0F);
    }

    private static void renderDeathRays(VertexConsumer consumer, Matrix4f matrix, StateMachineBoss<?> boss, float partialTick, float time, int color, float scale) {
        if (!boss.isDeadOrDying()) {
            return;
        }
        float progress = boss.getDeathProgress(partialTick);
        Vec3 chest = new Vec3(0.0D, boss.getBbHeight() * 0.6D, 0.0D);
        int count = 6 + (int) (progress * 22.0F);
        float fade = progress > 0.9F ? (1.0F - progress) * 10.0F : 1.0F;
        FxDraw.rays(consumer, matrix, chest, count, (1.0F + progress * 6.0F) * scale, 1.0F, FxDraw.argb(color, 0.9F * fade), 432L + boss.getId(), time * 0.01F);
    }

    private static void renderPatriarch(VertexConsumer consumer, Matrix4f matrix, AuricPatriarch patriarch, float partialTick, float time) {
        Vec3 forward = forward(patriarch, partialTick);
        Vec3 left = left(forward);
        AuricPatriarch.PatriarchState state = patriarch.getPatriarchState();

        int summonTicks = patriarch.clientStateTicks;
        boolean summoning = (state == AuricPatriarch.PatriarchState.AUREATE_UNDEAD && summonTicks < 70)
                || (state == AuricPatriarch.PatriarchState.VEX_TIME && summonTicks < 40);
        if (summoning) {
            float fadeIn = Mth.clamp(summonTicks / 10.0F, 0.0F, 1.0F);
            float fadeOut = state == AuricPatriarch.PatriarchState.AUREATE_UNDEAD ? Mth.clamp((70 - summonTicks) / 15.0F, 0.0F, 1.0F) : Mth.clamp((40 - summonTicks) / 10.0F, 0.0F, 1.0F);
            float alpha = fadeIn * fadeOut;
            Vec3 ground = new Vec3(0.0D, 0.06D, 0.0D);
            float spin = time * 0.03F;
            FxDraw.flatRing(consumer, matrix, ground, 2.75F, 3.0F, 64, FxDraw.argb(AuricPatriarch.GOLD, 0.9F * alpha), FxDraw.argb(AuricPatriarch.AMBER, 0.2F * alpha), spin);
            FxDraw.flatRing(consumer, matrix, ground, 1.6F, 1.72F, 48, FxDraw.argb(AuricPatriarch.PALE_GOLD, 0.8F * alpha), FxDraw.argb(AuricPatriarch.GOLD, 0.4F * alpha), -spin);
            FxDraw.runes(consumer, matrix, ground, 2.2F, 8, 0.28F, FxDraw.argb(AuricPatriarch.PALE_GOLD, 0.85F * alpha), -spin * 1.5F);
            FxDraw.spokes(consumer, matrix, ground, new Vec3(1.0D, 0.0D, 0.0D), new Vec3(0.0D, 0.0D, 1.0D), 16, 1.75F, 2.7F, 0.04F, FxDraw.argb(AuricPatriarch.GOLD, 0.5F * alpha), FxDraw.argb(AuricPatriarch.GOLD, 0.0F), spin * 0.5F);
            if (state == AuricPatriarch.PatriarchState.AUREATE_UNDEAD && summonTicks >= 25 && summonTicks < 45) {
                float pillar = 1.0F - (summonTicks - 25) / 20.0F;
                for (int i = 0; i < 4; i++) {
                    Vec3 spot = forward.scale(i + 1).add(0.0D, 0.05D, 0.0D);
                    FxDraw.beam(consumer, matrix, spot, spot.add(0.0D, 4.0D, 0.0D), 0.35F * pillar, FxDraw.argb(AuricPatriarch.PALE_GOLD, 0.7F * pillar), FxDraw.argb(AuricPatriarch.GOLD, 0.0F));
                }
            }
        }

        if (state == AuricPatriarch.PatriarchState.WING_KNOCKBACK && summonTicks >= 25 && summonTicks <= 37) {
            float progress = (summonTicks - 25 + partialTick) / 12.0F;
            float radius = 1.0F + progress * 8.0F;
            FxDraw.flatRing(consumer, matrix, new Vec3(0.0D, 0.1D, 0.0D), radius - 0.8F, radius, 48, FxDraw.argb(AuricPatriarch.PALE_GOLD, 0.0F), FxDraw.argb(AuricPatriarch.PALE_GOLD, (1.0F - progress) * 0.8F), 0.0F);
        }

        if (state == AuricPatriarch.PatriarchState.SUDDEN_BEAM && summonTicks > 10 && summonTicks < 50) {
            float charge = (summonTicks - 10 + partialTick) / 40.0F;
            Vec3 hand = forward.scale(0.9D).add(left.scale(-0.6D)).add(0.0D, patriarch.getBbHeight() * 0.65D, 0.0D);
            FxDraw.sphere(consumer, matrix, hand, 0.1F + 0.3F * charge, 8, 12, FxDraw.argb(AuricPatriarch.PALE_GOLD, 0.35F + 0.4F * charge), time * 0.5F, false);
        }

        renderDeathRays(consumer, matrix, patriarch, partialTick, time, AuricPatriarch.GOLD, 1.0F);
    }

    private static void renderSentinel(VertexConsumer consumer, Matrix4f matrix, EclipseSentinel sentinel, Vec3 base, float partialTick, float time) {
        EclipseSentinel.SentinelPose state = sentinel.getSentinelPose();
        int ticks = sentinel.clientStateTicks;

        if (state == EclipseSentinel.SentinelPose.LASER) {
            Vec3 origin = sentinel.laserOrigin(partialTick).subtract(base);
            Vec3 end = sentinel.getAbilityPos().subtract(base);
            float beamPulse = pulse(time, 1.7F, 0.8F, 1.0F);
            FxDraw.beam(consumer, matrix, origin, end, 0.09F * beamPulse, FxDraw.argb(0xFFFFFF, 0.95F), FxDraw.argb(EclipseSentinel.CORONA, 0.9F));
            FxDraw.beam(consumer, matrix, origin, end, 0.28F * beamPulse, FxDraw.argb(EclipseSentinel.CORONA, 0.55F), FxDraw.argb(EclipseSentinel.ECLIPSE_PURPLE, 0.5F));
            FxDraw.beam(consumer, matrix, origin, end, 0.6F * beamPulse, FxDraw.argb(EclipseSentinel.ECLIPSE_PURPLE, 0.22F), FxDraw.argb(EclipseSentinel.ECLIPSE_PURPLE, 0.15F));
            FxDraw.sphere(consumer, matrix, end, 0.45F * beamPulse, 8, 12, FxDraw.argb(EclipseSentinel.CORONA, 0.5F), time, false);
            FxDraw.rays(consumer, matrix, end, 8, 1.4F, 1.0F, FxDraw.argb(EclipseSentinel.CORONA, 0.6F), sentinel.getId() * 31L + sentinel.tickCount, 0.0F);
        }

        if (state == EclipseSentinel.SentinelPose.HYPER_BURST && ticks >= EclipseSentinel.HYPER_TARGET_TICK && sentinel.ticksSinceImpact(partialTick) > ticks) {
            Vec3 landing = sentinel.getAbilityPos().subtract(base).add(0.0D, 0.07D, 0.0D);
            float progress = Mth.clamp((ticks + partialTick - EclipseSentinel.HYPER_TARGET_TICK) / (EclipseSentinel.HYPER_LAND_TICK - EclipseSentinel.HYPER_TARGET_TICK), 0.0F, 1.0F);
            float shrinking = 7.0F * (1.0F - progress);
            FxDraw.flatRing(consumer, matrix, landing, 6.75F, 7.0F, 64, FxDraw.argb(EclipseSentinel.ECLIPSE_PURPLE, 0.85F), FxDraw.argb(EclipseSentinel.CORONA, 0.85F), time * 0.02F);
            FxDraw.flatRing(consumer, matrix, landing, Math.max(0.0F, shrinking - 0.35F), shrinking, 64, FxDraw.argb(EclipseSentinel.CORONA, 0.0F), FxDraw.argb(EclipseSentinel.CORONA, 0.7F), 0.0F);
            FxDraw.flatRing(consumer, matrix, landing, 0.0F, 7.0F, 64, FxDraw.argb(EclipseSentinel.ECLIPSE_PURPLE, 0.12F * progress), FxDraw.argb(EclipseSentinel.ECLIPSE_PURPLE, 0.04F), 0.0F);
        }

        float shockRadius = switch (state) {
            case HYPER_BURST -> 7.0F;
            case RISE -> 6.0F;
            case ECLIPSE_PULSE -> (float) EclipseSentinel.PULSE_RADIUS;
            default -> 2.5F;
        };
        renderShockwave(consumer, matrix, sentinel, base, partialTick, shockRadius, EclipseSentinel.ECLIPSE_PURPLE);
        renderDeathRays(consumer, matrix, sentinel, partialTick, time, EclipseSentinel.ECLIPSE_PURPLE, 0.8F);
    }

    private static void renderWarrior(VertexConsumer consumer, Matrix4f matrix, WarriorOfChimericDarkness warrior, Vec3 base, Vec3 camera, float partialTick, float time) {
        WarriorOfChimericDarkness.WarriorPose state = warrior.getWarriorPose();
        if (warrior.isShielding()) {
            int ticks = warrior.clientStateTicks;
            float duration = state.duration();
            float appear = Mth.clamp((ticks + partialTick) / 5.0F, 0.0F, 1.0F) * Mth.clamp((duration - ticks - partialTick) / 6.0F, 0.0F, 1.0F);
            float radius = (float) warrior.halfWidth() + 1.1F;
            Vec3 center = new Vec3(0.0D, 0.0D, 0.0D);
            Vec3 eye = camera.subtract(base);
            FxDraw.rimDome(consumer, matrix, center, radius * (0.9F + 0.1F * appear), 8, 24, eye, FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.55F * appear), time * 0.4F);
            FxDraw.flatRing(consumer, matrix, new Vec3(0.0D, 0.08D, 0.0D), radius - 0.15F, radius + 0.1F, 48, FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.8F * appear), FxDraw.argb(WarriorOfChimericDarkness.ABYSS_VIOLET, 0.0F), time * 0.05F);
        }

        if (state == WarriorOfChimericDarkness.WarriorPose.CHARGED_STRIKE) {
            float charge = warrior.getChargeProgress(partialTick);
            if (charge > 0.0F && warrior.ticksSinceImpact(partialTick) > warrior.clientStateTicks) {
                Vec3 ground = new Vec3(0.0D, 0.07D, 0.0D);
                float radius = (float) WarriorOfChimericDarkness.CHARGED_STRIKE_RADIUS;
                FxDraw.flatRing(consumer, matrix, ground, radius - 0.3F, radius, 64, FxDraw.argb(WarriorOfChimericDarkness.ABYSS_VIOLET, 0.8F * charge), FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.9F * charge), time * 0.04F);
                FxDraw.flatRing(consumer, matrix, ground, 0.0F, radius * charge, 64, FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.05F), FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.3F * charge), 0.0F);
                FxDraw.runes(consumer, matrix, ground, radius - 1.1F, 12, 0.35F, FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.75F * charge), -time * 0.04F);
                Vec3 hammer = new Vec3(0.0D, warrior.getBbHeight() + 0.7D, 0.0D);
                FxDraw.sphere(consumer, matrix, hammer, 0.25F + 0.55F * charge, 8, 12, FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.3F + 0.4F * charge), time * 0.9F, false);
                FxDraw.rays(consumer, matrix, hammer, 6 + (int) (charge * 10.0F), 0.8F + 1.6F * charge, 1.0F, FxDraw.argb(WarriorOfChimericDarkness.GRIP_GLOW, 0.6F * charge), warrior.getId() * 17L, time * 0.05F);
            }
        }

        float shockRadius = switch (state) {
            case CHARGED_STRIKE -> (float) WarriorOfChimericDarkness.CHARGED_STRIKE_RADIUS;
            case SHIELDED_KNOCKBACK -> 4.0F;
            default -> 3.0F;
        };
        renderShockwave(consumer, matrix, warrior, base, partialTick, shockRadius, WarriorOfChimericDarkness.GRIP_GLOW);
        renderDeathRays(consumer, matrix, warrior, partialTick, time, WarriorOfChimericDarkness.ABYSS_VIOLET, 0.9F);
    }

    private static void renderTether(VertexConsumer consumer, Matrix4f matrix, Entity minion, Vec3 base, float partialTick, float time) {
        Entity owner = minion instanceof ReaverEntity reaver ? reaver.getOwner() : minion instanceof AureateVexEntity vex ? vex.getOwner() : null;
        if (!(owner instanceof AuricPatriarch patriarch) || !patriarch.isAlive() || !(minion instanceof LivingEntity living) || !living.isAlive()) {
            return;
        }
        Vec3 from = new Vec3(0.0D, minion.getBbHeight() * 0.6D, 0.0D);
        Vec3 to = lerpedPosition(patriarch, partialTick).add(0.0D, patriarch.getBbHeight() * 0.6D, 0.0D).subtract(base);
        float flicker = pulse(time + minion.getId() * 7.0F, 0.45F, 0.3F, 0.65F);
        FxDraw.beam(consumer, matrix, from, to, 0.035F, FxDraw.argb(AuricPatriarch.PALE_GOLD, flicker), FxDraw.argb(AuricPatriarch.GOLD, flicker * 0.6F));
        FxDraw.beam(consumer, matrix, from, to, 0.12F, FxDraw.argb(AuricPatriarch.GOLD, flicker * 0.25F), FxDraw.argb(AuricPatriarch.AMBER, 0.05F));
    }
}