package net.sashakyotoz.utils;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.environment.weather.ChimericWeatherState;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.custom.ModArmorItem;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.DoubleSupplier;

public class ActionsUtils {
    @FunctionalInterface
    public interface BlockPosAction {
        void apply(Level world, BlockPos pos);
    }

    public static void rayCastAlong(
            Level world,
            Entity entity,
            float maxDistance,
            BlockPosAction action) {
        float scaling = 0;
        for (int i = 0; i < maxDistance; i++) {
            BlockPos pos = world.clip(new ClipContext(entity.getEyePosition(), entity.getEyePosition().add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
            if (!world.getBlockState(pos).canOcclude() || world.getBlockState(pos).is(ConventionalBlockTags.GLASS_BLOCKS) || world.getBlockState(pos).getBlock().getDescriptionId().contains("glass"))
                ++scaling;
            action.apply(world, pos);
        }
    }

    public static double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }

    public static double getYVector(double speed, double pitch) {
        return pitch * (-0.025) * speed;
    }

    public static double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }

    public static boolean isMoving(Entity entity) {
        return entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    public static boolean isModLoaded(String name) {
        return FabricLoader.getInstance().isModLoaded(name);
    }

    public static void spawnParticle(ParticleOptions type, ServerLevel world, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.sendParticles(type, x + 0.25, y, z + 0.25, 1, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier, 0);
        }
    }

    public static void spawnParticle(ParticleOptions type, Level world, double x, double y, double z, float modifier) {
        if (world instanceof ServerLevel world1)
            spawnParticle(type, world1, x, y, z, modifier);
        else {
            for (int i = 0; i < 360; i++) {
                if (i % 30 == 0)
                    world.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
            }
        }
    }

    public static void hitNearbyMobs(LivingEntity livingEntity, float damage, int radius) {
        List<LivingEntity> entityList = livingEntity.level().getEntitiesOfClass(LivingEntity.class, new AABB(livingEntity.position(), livingEntity.position()).inflate(radius), e -> true)
                .stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(livingEntity.position()))).toList();
        for (LivingEntity entityIterator : entityList) {
            if (entityIterator != livingEntity) {
                entityIterator.hurt(livingEntity.damageSources().magic(), damage);
            }
        }
    }

    public static boolean isEntityInCover(LivingEntity livingEntity, ServerLevel world) {
        return !ChimericWeatherState.get(world).isGrippfallActive()
                || ModArmorItem.isAbyssalArmorSet(livingEntity)
                || (ChimericWeatherState.get(world).isGrippfallActive()
                && world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, livingEntity.blockPosition()).getY() > livingEntity.blockPosition().getY());
    }


    public static void dropItem(LivingEntity entity, ItemStack... stack) {
        for (ItemStack itemStack : stack) {
            BehaviorUtils.throwItem(entity, itemStack, entity.position().add(0.0, 1.0, 0.0));
        }
    }

    public static boolean canSee(Entity entity, ClipContext.Block shapeType, ClipContext.Fluid fluidHandling, DoubleSupplier entityY) {
        Vec3 vec3d = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
        Vec3 vec3d2 = new Vec3(entity.getX(), entityY.getAsDouble(), entity.getZ());
        if (vec3d2.distanceTo(vec3d) > 128.0) {
            return false;
        } else {
            return entity.level().clip(new ClipContext(vec3d, vec3d2, shapeType, fluidHandling, entity)).getType() == HitResult.Type.MISS;
        }
    }

    public static void initializeConverting(LivingEntity origin, Mob entity, UUID converter) {
        if (origin.level() instanceof ServerLevel world && entity != null) {
            entity.finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.CONVERSION, null, null);
            if (converter != null) {
                Player player = world.getPlayerByUUID(converter);
                if (player != null)
                    player.spawnAtLocation(ModItems.GRIPCRYSTAL);
                if (player instanceof ServerPlayer player1)
                    ModRegistry.CURED_GRIPCRYSTAL_ENTITY_CRITERION.trigger(player1, origin, entity);
            }
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
        }
    }
}