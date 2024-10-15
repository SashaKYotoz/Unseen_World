package net.sashakyotoz.utils;

import net.minecraft.entity.LivingEntity;

public class ActionsManager {
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
}
