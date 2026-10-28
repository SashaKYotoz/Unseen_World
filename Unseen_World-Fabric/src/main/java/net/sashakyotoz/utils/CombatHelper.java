package net.sashakyotoz.utils;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public final class CombatHelper {
    private CombatHelper() {
    }

    public static boolean canHit(LivingEntity attacker, Entity target) {
        if (!(target instanceof LivingEntity living) || living == attacker || !living.isAlive() || living.isSpectator() || !living.attackable()) {
            return false;
        }
        if (living instanceof ArmorStand stand && stand.isMarker()) {
            return false;
        }
        if (attacker.isAlliedTo(living) || attacker.isPassengerOfSameVehicle(living)) {
            return false;
        }
        if (living instanceof OwnableEntity ownable && attacker.getUUID().equals(ownable.getOwnerUUID())) {
            return false;
        }
        return !(attacker instanceof Player attackingPlayer && living instanceof Player targetPlayer && !attackingPlayer.canHarmPlayer(targetPlayer));
    }

    public static boolean isBoss(Entity entity) {
        return entity.getType().is(ConventionalEntityTypeTags.BOSSES);
    }

    public static DamageSource attackSource(LivingEntity attacker) {
        return attacker instanceof Player player
                ? attacker.damageSources().playerAttack(player)
                : attacker.damageSources().mobAttack(attacker);
    }

    public static List<LivingEntity> enemiesInRadius(LivingEntity attacker, Vec3 center, double radius) {
        double radiusSqr = radius * radius;
        return attacker.level().getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(radius),
                entity -> canHit(attacker, entity) && distanceSqrToBox(entity.getBoundingBox(), center) <= radiusSqr);
    }

    public static List<LivingEntity> enemiesInArc(LivingEntity attacker, double reach, double halfAngleDegrees) {
        Vec3 origin = attacker.position();
        Vec3 facing = Vec3.directionFromRotation(0.0F, attacker.getYRot());
        double minDot = Math.cos(Math.toRadians(halfAngleDegrees));
        AABB area = attacker.getBoundingBox().inflate(reach + 1.0D, 1.0D, reach + 1.0D);
        return attacker.level().getEntitiesOfClass(LivingEntity.class, area, entity -> {
            if (!canHit(attacker, entity)) {
                return false;
            }
            Vec3 offset = new Vec3(entity.getX() - origin.x, 0.0D, entity.getZ() - origin.z);
            if (offset.length() - entity.getBbWidth() * 0.5D > reach) {
                return false;
            }
            return offset.lengthSqr() < 1.0E-4D || offset.normalize().dot(facing) >= minDot;
        });
    }

    public static List<LivingEntity> enemiesInLine(LivingEntity attacker, Vec3 start, Vec3 direction, double length, double halfWidth) {
        Vec3 flat = new Vec3(direction.x, 0.0D, direction.z);
        if (flat.lengthSqr() < 1.0E-4D) {
            return List.of();
        }
        Vec3 dir = flat.normalize();
        Vec3 end = start.add(dir.scale(length));
        AABB area = new AABB(start, end).inflate(halfWidth + 1.0D, 2.5D, halfWidth + 1.0D);
        return attacker.level().getEntitiesOfClass(LivingEntity.class, area, entity -> {
            if (!canHit(attacker, entity)) {
                return false;
            }
            double halfBody = entity.getBbWidth() * 0.5D;
            Vec3 relative = new Vec3(entity.getX() - start.x, 0.0D, entity.getZ() - start.z);
            double along = relative.dot(dir);
            if (along < -halfBody || along > length + halfBody) {
                return false;
            }
            if (relative.subtract(dir.scale(along)).length() > halfWidth + halfBody) {
                return false;
            }
            AABB box = entity.getBoundingBox();
            return box.maxY >= start.y - 1.0D && box.minY <= start.y + 2.5D;
        });
    }

    public static void launch(LivingEntity target, Vec3 from, double horizontal, double vertical, double minFactor) {
        double factor = knockbackFactor(target, minFactor);
        if (factor <= 0.0D) {
            return;
        }
        Vec3 away = new Vec3(target.getX() - from.x, 0.0D, target.getZ() - from.z);
        Vec3 push = away.lengthSqr() < 1.0E-4D ? Vec3.ZERO : away.normalize().scale(horizontal * factor);
        Vec3 current = target.getDeltaMovement();
        double horizontalX = horizontal > 0.0D ? push.x : current.x;
        double horizontalZ = horizontal > 0.0D ? push.z : current.z;
        target.setDeltaMovement(horizontalX, Math.max(current.y, vertical * factor), horizontalZ);
        markMoved(target);
    }

    public static void pop(LivingEntity target, double vertical) {
        launch(target, target.position(), 0.0D, vertical, 0.0D);
    }

    public static void dragDown(LivingEntity target, double downwardSpeed) {
        double factor = knockbackFactor(target, 0.0D);
        if (factor <= 0.0D) {
            return;
        }
        Vec3 current = target.getDeltaMovement();
        target.setDeltaMovement(current.x * 0.5D, Math.min(current.y, -Math.abs(downwardSpeed) * factor), current.z * 0.5D);
        markMoved(target);
    }

    public static void grip(LivingEntity target, int seconds) {
        if (seconds > 0 && target instanceof IGrippingEntity gripping && gripping.getGrippingData() <= 0) {
            GrippingData.addGrippingSeconds(gripping, seconds);
        }
    }

    public static BeamHit traceBeam(LivingEntity shooter, double range, Predicate<Entity> filter) {
        Level level = shooter.level();
        Vec3 start = shooter.getEyePosition();
        Vec3 end = start.add(shooter.getViewVector(1.0F).scale(range));
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, shooter));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }
        AABB area = shooter.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(shooter, start, end, area,
                entity -> entity.isPickable() && filter.test(entity), start.distanceToSqr(end));
        return entityHit == null
                ? new BeamHit(start, end, null)
                : new BeamHit(start, entityHit.getLocation(), entityHit.getEntity());
    }

    private static double knockbackFactor(LivingEntity target, double minFactor) {
        return Math.max(minFactor, 1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }

    private static void markMoved(LivingEntity target) {
        target.hasImpulse = true;
        target.hurtMarked = true;
    }

    private static double distanceSqrToBox(AABB box, Vec3 point) {
        double x = Mth.clamp(point.x, box.minX, box.maxX) - point.x;
        double y = Mth.clamp(point.y, box.minY, box.maxY) - point.y;
        double z = Mth.clamp(point.z, box.minZ, box.maxZ) - point.z;
        return x * x + y * y + z * z;
    }

    public record BeamHit(Vec3 start, Vec3 end, @Nullable Entity entity) {
    }
}
