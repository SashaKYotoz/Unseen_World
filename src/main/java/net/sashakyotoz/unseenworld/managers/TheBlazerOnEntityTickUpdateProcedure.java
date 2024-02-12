package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.sashakyotoz.unseenworld.entity.TheBlazerEntity;
import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.entity.RedBlazeEntity;
import net.sashakyotoz.unseenworld.entity.NetheriumStaffEntity;

public class TheBlazerOnEntityTickUpdateProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, TheBlazerEntity entity) {
        if (entity == null)
            return;
        double speed = 0.8;
        double Yaw;
        if (Math.random() < 0.005) {
                entity.strikeAnimationState.start(entity.tickCount);
            for (int index0 = 0; index0 < 4; index0++) {
                entity.setYRot(entity.getYRot() + 90);
                entity.setXRot(0);
                entity.setYBodyRot(entity.getYRot());
                entity.setYHeadRot(entity.getYRot());
                entity.yRotO = entity.getYRot();
                entity.xRotO = entity.getXRot();
                entity.yBodyRotO = entity.getYRot();
                entity.yHeadRotO = entity.getYRot();
                Level projectileLevel = entity.level();
                if (!projectileLevel.isClientSide()) {
                    Projectile entityToSpawn = new Object() {
                        public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                            AbstractArrow entityToSpawn = new NetheriumStaffEntity(UnseenWorldModEntities.NETHERIUM_STAFF.get(), level);
                            entityToSpawn.setOwner(shooter);
                            entityToSpawn.setBaseDamage(damage);
                            entityToSpawn.setKnockback(knockback);
                            entityToSpawn.setSilent(true);
                            return entityToSpawn;
                        }
                    }.getArrow(projectileLevel, entity, (float) 7, 5);
                    entityToSpawn.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
                    entityToSpawn.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, 2, 0);
                    projectileLevel.addFreshEntity(entityToSpawn);
                }
            }
        } else if (Math.random() < 0.01) {
            if (world.getEntitiesOfClass(RedBlazeEntity.class, AABB.ofSize(new Vec3(x, y, z), 16, 16, 16), e -> true).isEmpty()) {
                if (world instanceof ServerLevel _level) {
                    Entity entityToSpawn = UnseenWorldModEntities.RED_BLAZE.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                    }
                }
            }
        } else if (Math.random() < 0.015) {
            Yaw = entity.getYRot();
            entity.setDeltaMovement(new Vec3((speed * Math.cos((Yaw + 90) * (Math.PI / 180))), (Mth.nextDouble(RandomSource.create(), -0.3, -0.15)), (speed * Math.sin((Yaw + 90) * (Math.PI / 180)))));
        }
    }
}
