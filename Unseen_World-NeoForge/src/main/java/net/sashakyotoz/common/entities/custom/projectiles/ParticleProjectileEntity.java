package net.sashakyotoz.common.entities.custom.projectiles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.particles.ModParticleTypes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleProjectileEntity extends Projectile {
    private static final EntityDataAccessor<Boolean> DATA_CLUSTER = SynchedEntityData.defineId(ParticleProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_DETONATING = SynchedEntityData.defineId(ParticleProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private static final int MAX_TRAIL_POINTS = 20;
    private static final int DEFAULT_MAX_LIFE_TIME = 150;

    private final ArrayDeque<Vec3> trailPositions = new ArrayDeque<>();
    private final List<PendingExplosion> pendingExplosions = new ArrayList<>();

    private int lifeTime = 0;
    private int maxLifeTime = DEFAULT_MAX_LIFE_TIME;

    public ParticleProjectileEntity(EntityType<? extends ParticleProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public ParticleProjectileEntity(EntityType<? extends ParticleProjectileEntity> entityType, Level level, LivingEntity owner) {
        this(entityType, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_CLUSTER, false);
        builder.define(DATA_DETONATING, false);
    }

    public boolean isClusterExplosion() {
        return this.entityData.get(DATA_CLUSTER);
    }

    public void setClusterExplosion(boolean clusterExplosion) {
        this.entityData.set(DATA_CLUSTER, clusterExplosion);
    }

    public boolean isDetonating() {
        return this.entityData.get(DATA_DETONATING);
    }

    private void setDetonating(boolean detonating) {
        this.entityData.set(DATA_DETONATING, detonating);
    }

    @Override
    public void shoot(double x, double y, double z, float speed, float divergence) {
        Vec3 direction = new Vec3(x, y, z)
                .normalize()
                .add(
                        this.random.triangle(0.0D, 0.0172275D * (double) divergence),
                        this.random.triangle(0.0D, 0.0172275D * (double) divergence),
                        this.random.triangle(0.0D, 0.0172275D * (double) divergence)
                )
                .scale(speed);

        this.setDeltaMovement(direction);
        this.updateRotationFromVelocity();
    }

    public void updateRotationFromVelocity() {
        Vec3 motion = this.getDeltaMovement();
        if (motion.lengthSqr() > 1.0E-7D) {
            double horizontalDistance = motion.horizontalDistance();
            this.setYRot((float) (Mth.atan2(motion.x, motion.z) * (180.0D / Math.PI)));
            this.setXRot((float) (Mth.atan2(motion.y, horizontalDistance) * (180.0D / Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isDetonating()) {
            if (!this.level().isClientSide()) {
                this.tickPendingExplosions();
            }
            return;
        }

        if (this.level().isClientSide()) {
            this.updateTrail();
        }

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
            if (this.isRemoved() || this.isDetonating()) {
                return;
            }
        }

        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.setDeltaMovement(motion.x, motion.y - 0.04D, motion.z);

        this.updateRotationFromVelocity();
        this.checkInsideBlocks();

        this.lifeTime++;
        if (!this.level().isClientSide() && this.lifeTime >= this.maxLifeTime) {
            this.discard();
        }
    }

    private void updateTrail() {
        this.trailPositions.addFirst(this.position());
        while (this.trailPositions.size() > MAX_TRAIL_POINTS) {
            this.trailPositions.pollLast();
        }
    }

    public List<Vec3> getTrailPositions() {
        return List.copyOf(this.trailPositions);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!this.level().isClientSide()) {
            this.triggerImpact(entityHitResult.getLocation());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level().isClientSide()) {
            this.triggerImpact(blockHitResult.getLocation());
        }
    }

    private void triggerImpact(Vec3 impactPos) {
        if (this.isDetonating()) {
            return;
        }

        if (this.isClusterExplosion() && this.level() instanceof ServerLevel serverLevel) {
            this.setDetonating(true);
            this.setDeltaMovement(Vec3.ZERO);
            this.setInvisible(true);

            for (int i = 0; i < 5; i++) {
                double offsetX = (this.random.nextDouble() - 0.5D) * 6.0D;
                double offsetY = (this.random.nextDouble() - 0.5D) * 2.0D;
                double offsetZ = (this.random.nextDouble() - 0.5D) * 6.0D;

                Vec3 targetPos = impactPos.add(offsetX, offsetY, offsetZ);
                serverLevel.sendParticles(ModParticleTypes.EXPLOSION_CIRCLE, targetPos.x, targetPos.y + 0.05D, targetPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                this.pendingExplosions.add(new PendingExplosion(targetPos, 20));
            }
        } else {
            this.level().explode(this, impactPos.x, impactPos.y, impactPos.z, 2.0F, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    private void tickPendingExplosions() {
        Iterator<PendingExplosion> iterator = this.pendingExplosions.iterator();
        while (iterator.hasNext()) {
            PendingExplosion explosion = iterator.next();
            if (--explosion.ticks <= 0) {
                this.level().explode(this, explosion.pos.x, explosion.pos.y, explosion.pos.z, 2.0F, Level.ExplosionInteraction.NONE);
                iterator.remove();
            }
        }

        if (this.pendingExplosions.isEmpty()) {
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.lifeTime = tag.getInt("LifeTime");
        this.maxLifeTime = tag.contains("MaxLifeTime", Tag.TAG_INT) ? tag.getInt("MaxLifeTime") : DEFAULT_MAX_LIFE_TIME;
        this.setClusterExplosion(tag.getBoolean("ClusterExplosion"));
        this.setDetonating(tag.getBoolean("IsDetonating"));

        this.pendingExplosions.clear();
        if (tag.contains("PendingExplosions", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("PendingExplosions", Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag entry = listTag.getCompound(i);
                Vec3 pos = new Vec3(entry.getDouble("X"), entry.getDouble("Y"), entry.getDouble("Z"));
                this.pendingExplosions.add(new PendingExplosion(pos, entry.getInt("Ticks")));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LifeTime", this.lifeTime);
        tag.putInt("MaxLifeTime", this.maxLifeTime);
        tag.putBoolean("ClusterExplosion", this.isClusterExplosion());
        tag.putBoolean("IsDetonating", this.isDetonating());

        ListTag listTag = new ListTag();
        for (PendingExplosion explosion : this.pendingExplosions) {
            CompoundTag entry = new CompoundTag();
            entry.putDouble("X", explosion.pos.x);
            entry.putDouble("Y", explosion.pos.y);
            entry.putDouble("Z", explosion.pos.z);
            entry.putInt("Ticks", explosion.ticks);
            listTag.add(entry);
        }
        tag.put("PendingExplosions", listTag);
    }

    private static final class PendingExplosion {
        private final Vec3 pos;
        private int ticks;

        private PendingExplosion(Vec3 pos, int ticks) {
            this.pos = pos;
            this.ticks = ticks;
        }
    }
}