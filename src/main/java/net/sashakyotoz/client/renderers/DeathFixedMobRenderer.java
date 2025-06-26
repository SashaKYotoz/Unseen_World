package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.common.config.ConfigEntries;

public abstract class DeathFixedMobRenderer<T extends MobEntity, M extends EntityModel<T>> extends MobEntityRenderer<T, M> {
    public DeathFixedMobRenderer(EntityRendererFactory.Context context, M entityModel, float f) {
        super(context, entityModel, f);
    }

    protected void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        if (entity.deathTime > 0 && !ConfigEntries.doAdvancedDeathForMobs) {
            float f = ((float) entity.deathTime + tickDelta - 1.0F) / 20.0F * 1.6F;
            f = Math.min(1, MathHelper.sqrt(f));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * this.getLyingAngle(entity)));
        }
        if (this.isShaking(entity))
            bodyYaw += (float) (Math.cos((double) entity.age * 3.25) * Math.PI * 0.4F);
        if (!entity.isInPose(EntityPose.SLEEPING))
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - bodyYaw));
        if (entity.isUsingRiptide()) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F - entity.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) entity.age + tickDelta) * -75.0F));
        } else if (entity.isInPose(EntityPose.SLEEPING)) {
            Direction direction = entity.getSleepingDirection();
            float g = direction != null ? getYaw(direction) : bodyYaw;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.getLyingAngle(entity)));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0F));
        } else if (shouldFlipUpsideDown(entity)) {
            matrices.translate(0.0F, entity.getHeight() + 0.1F, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        }
    }

    private float getYaw(Direction direction) {
        return switch (direction) {
            case SOUTH -> 90.0F;
            case NORTH -> 270.0F;
            case EAST -> 180.0F;
            default -> 0.0F;
        };
    }
}