package net.sashakyotoz.common.entities.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

public final class EntityShapes {
    private EntityShapes() {
    }

    public static EntityDimensions oriented(float length, float thickness, float pitchDegrees) {
        float radians = Math.abs(pitchDegrees) * Mth.DEG_TO_RAD;
        float horizontal = Math.abs(Mth.sin(radians));
        float vertical = Math.abs(Mth.cos(radians));
        return EntityDimensions.fixed(thickness + (length - thickness) * horizontal, thickness + (length - thickness) * vertical);
    }

    public static int orientationBucket(float pitchDegrees, float bucketDegrees) {
        return Mth.floor(Math.abs(pitchDegrees) / bucketDegrees);
    }

    public static boolean isLookedAtBy(Mob self, LivingEntity viewer, double tolerance, boolean scaleByDistance, boolean visualShape, Predicate<LivingEntity> predicate, DoubleSupplier... heights) {
        if (!predicate.test(viewer)) {
            return false;
        }
        Vec3 view = viewer.getViewVector(1.0F).normalize();
        for (DoubleSupplier height : heights) {
            Vec3 toSelf = new Vec3(self.getX() - viewer.getX(), height.getAsDouble() - viewer.getEyeY(), self.getZ() - viewer.getZ());
            double distance = toSelf.length();
            double dot = view.dot(toSelf.normalize());
            if (dot > 1.0D - tolerance / (scaleByDistance ? distance : 1.0D)) {
                Vec3 target = new Vec3(self.getX(), height.getAsDouble(), self.getZ());
                ClipContext context = new ClipContext(viewer.getEyePosition(), target, visualShape ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, viewer);
                return self.level().clip(context).getType() == HitResult.Type.MISS;
            }
        }
        return false;
    }
}
