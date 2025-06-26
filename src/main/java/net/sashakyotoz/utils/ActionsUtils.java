package net.sashakyotoz.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class ActionsUtils {
    public static double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }

    public static double getYVector(double speed, double pitch) {
        return pitch * (-0.025) * speed;
    }

    public static double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }

    public static boolean isMoving(LivingEntity entity) {
        return entity.getVelocity().horizontalLengthSquared() > 1.0E-6D;
    }

    public static boolean isModLoaded(String name) {
        return FabricLoader.getInstance().isModLoaded(name);
    }

    public static void spawnParticle(ParticleEffect type, ServerWorld world, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.spawnParticles(type, x + 0.25, y, z + 0.25, 1, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier, 0);
        }
    }

    public static void spawnParticle(ParticleEffect type, World world, double x, double y, double z, float modifier) {
        if (world instanceof ServerWorld world1)
            spawnParticle(type, world1, x, y, z, modifier);
        else {
            for (int i = 0; i < 360; i++) {
                if (i % 30 == 0)
                    world.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
            }
        }
    }

    public static void hitNearbyMobs(LivingEntity livingEntity, float damage, int radius) {
        List<LivingEntity> entityList = livingEntity.getWorld().getEntitiesByClass(LivingEntity.class, new Box(livingEntity.getPos(), livingEntity.getPos()).expand(radius), e -> true)
                .stream().sorted(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(livingEntity.getPos()))).toList();
        for (LivingEntity entityIterator : entityList) {
            if (entityIterator != livingEntity) {
                entityIterator.damage(livingEntity.getDamageSources().magic(), damage);
            }
        }
    }

    public static void dropItem(LivingEntity entity, ItemStack... stack) {
        for (ItemStack itemStack : stack) {
            LookTargetUtil.give(entity, itemStack, entity.getPos().add(0.0, 1.0, 0.0));
        }
    }
}