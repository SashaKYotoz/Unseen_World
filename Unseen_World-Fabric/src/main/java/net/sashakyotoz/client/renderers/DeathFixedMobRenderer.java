package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.sashakyotoz.common.config.ModMainConfig;

public abstract class DeathFixedMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {
    public DeathFixedMobRenderer(EntityRendererProvider.Context context, M entityModel, float f) {
        super(context, entityModel, f);
    }

    protected void setupRotations(T entity, PoseStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        if (entity.deathTime > 0 && !ModMainConfig.doAdvancedDeathForMobs) {
            float f = ((float) entity.deathTime + tickDelta - 1.0F) / 20.0F * 1.6F;
            f = Math.min(1, Mth.sqrt(f));
            matrices.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(entity)));
        }
        if (this.isShaking(entity))
            bodyYaw += (float) (Math.cos((double) entity.tickCount * 3.25) * Math.PI * 0.4F);
        if (!entity.hasPose(Pose.SLEEPING))
            matrices.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
        if (entity.isAutoSpinAttack()) {
            matrices.mulPose(Axis.XP.rotationDegrees(-90.0F - entity.getXRot()));
            matrices.mulPose(Axis.YP.rotationDegrees(((float) entity.tickCount + tickDelta) * -75.0F));
        } else if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            float g = direction != null ? sleepDirectionToRotation(direction) : bodyYaw;
            matrices.mulPose(Axis.YP.rotationDegrees(g));
            matrices.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(entity)));
            matrices.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (isEntityUpsideDown(entity)) {
            matrices.translate(0.0F, entity.getBbHeight() + 0.1F, 0.0F);
            matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    private float sleepDirectionToRotation(Direction direction) {
        return switch (direction) {
            case SOUTH -> 90.0F;
            case NORTH -> 270.0F;
            case EAST -> 180.0F;
            default -> 0.0F;
        };
    }
}