package net.sashakyotoz.common.entities.custom.projectiles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ParticleProjectileEntity extends Projectile {
    private static final int MAX_TRAIL_POINTS = 20;

    private final ConcurrentLinkedDeque<Vec3> trailPositions = new ConcurrentLinkedDeque<>();
    private final List<PendingExplosion> pendingExplosions = new CopyOnWriteArrayList<>();

    private volatile int lifeTime = 0;
    private volatile int maxLifeTime = 150;
    private volatile boolean clusterExplosion = false;
    private volatile boolean isDetonating = false;

    public ParticleProjectileEntity(EntityType<? extends ParticleProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public ParticleProjectileEntity(EntityType<? extends ParticleProjectileEntity> entityType, Level level, LivingEntity owner) {
        this(entityType, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
    }

    public boolean isClusterExplosion() {
        return this.clusterExplosion;
    }

    public void setClusterExplosion(boolean clusterExplosion) {
        this.clusterExplosion = clusterExplosion;
    }

    @Override
    protected void defineSynchedData() {
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

        if (this.isDetonating) {
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
        }

        Vec3 motion = this.getDeltaMovement();
        double nextX = this.getX() + motion.x;
        double nextY = this.getY() + motion.y;
        double nextZ = this.getZ() + motion.z;

        this.setPos(nextX, nextY, nextZ);

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
        if (this.isDetonating) {
            return;
        }

        if (this.clusterExplosion) {
            this.isDetonating = true;
            this.setDeltaMovement(Vec3.ZERO);
            this.setInvisible(true);

            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 5; i++) {
                    double offsetX = (this.random.nextDouble() - 0.5D) * 6.0D;
                    double offsetY = (this.random.nextDouble() - 0.5D) * 2.0D;
                    double offsetZ = (this.random.nextDouble() - 0.5D) * 6.0D;

                    Vec3 targetPos = impactPos.add(offsetX, offsetY, offsetZ);
                    serverLevel.sendParticles(ModParticleTypes.EXPLOSION_CIRCLE, targetPos.x, targetPos.y + 0.05D, targetPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    this.pendingExplosions.add(new PendingExplosion(targetPos, 20));
                }
            }
        } else {
            this.level().explode(this, impactPos.x, impactPos.y, impactPos.z, 2.0F, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    private void tickPendingExplosions() {
        List<PendingExplosion> completed = new ArrayList<>();

        for (PendingExplosion explosion : this.pendingExplosions) {
            if (explosion.decrementAndCheck()) {
                Vec3 pos = explosion.getPos();
                this.level().explode(this, pos.x, pos.y, pos.z, 2.0F, Level.ExplosionInteraction.NONE);
                completed.add(explosion);
            }
        }

        if (!completed.isEmpty()) {
            this.pendingExplosions.removeAll(completed);
        }

        if (this.pendingExplosions.isEmpty()) {
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.lifeTime = tag.getInt("LifeTime");
        this.maxLifeTime = tag.getInt("MaxLifeTime");
        this.clusterExplosion = tag.getBoolean("ClusterExplosion");
        this.isDetonating = tag.getBoolean("IsDetonating");

        this.pendingExplosions.clear();
        if (tag.contains("PendingExplosions", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("PendingExplosions", Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag entry = listTag.getCompound(i);
                Vec3 pos = new Vec3(entry.getDouble("X"), entry.getDouble("Y"), entry.getDouble("Z"));
                int ticks = entry.getInt("Ticks");
                this.pendingExplosions.add(new PendingExplosion(pos, ticks));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LifeTime", this.lifeTime);
        tag.putInt("MaxLifeTime", this.maxLifeTime);
        tag.putBoolean("ClusterExplosion", this.clusterExplosion);
        tag.putBoolean("IsDetonating", this.isDetonating);

        ListTag listTag = new ListTag();
        for (PendingExplosion explosion : this.pendingExplosions) {
            CompoundTag entry = new CompoundTag();
            Vec3 pos = explosion.getPos();
            entry.putDouble("X", pos.x);
            entry.putDouble("Y", pos.y);
            entry.putDouble("Z", pos.z);
            entry.putInt("Ticks", explosion.getTicks());
            listTag.add(entry);
        }
        tag.put("PendingExplosions", listTag);
    }

    private static final class PendingExplosion {
        private final Vec3 pos;
        private final AtomicInteger ticks;

        private PendingExplosion(Vec3 pos, int ticks) {
            this.pos = pos;
            this.ticks = new AtomicInteger(ticks);
        }

        public Vec3 getPos() {
            return this.pos;
        }

        public int getTicks() {
            return this.ticks.get();
        }

        public boolean decrementAndCheck() {
            return this.ticks.decrementAndGet() <= 0;
        }
    }
}