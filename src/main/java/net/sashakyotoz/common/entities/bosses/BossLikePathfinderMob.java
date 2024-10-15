package net.sashakyotoz.common.entities.bosses;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BossLikePathfinderMob extends PathAwareEntity implements Monster {
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public BossLikePathfinderMob(EntityType<? extends BossLikePathfinderMob> entityType, World world) {
        super(entityType, world);
    }

    public void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @Override
    protected boolean shouldFollowLeash() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age % 2 == 0){
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }
    public void provokeEarthquake(int radius){
        World world = this.getWorld();
        for (int y = -2; y < 2; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos = this.getBlockPos().add(x,y,z);
                    if (world.getBlockState(pos) != null && world.getBlockState(pos).isOpaque() && world.getBlockState(pos).getHardness(this.getWorld(),pos) < 10 && world.getBlockState(pos.up()).isAir()){
                        FallingBlockEntity.spawnFromBlock(world,pos.up(3),world.getBlockState(pos));
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }
    public void spawnParticle(ParticleEffect type, World world, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }

    public void hitNearbyMobs(float damage, int radius) {
        List<LivingEntity> entityList = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(this.getPos(), this.getPos()).expand(radius), e -> true)
                .stream().sorted(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(this.getPos()))).toList();
        for (LivingEntity entityIterator : entityList) {
            if (entityIterator != this) {
                entityIterator.damage(this.getDamageSources().magic(), damage);
                this.spawnParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, entityIterator.getBlockStateAtPos()),
                        this.getWorld(), this.getX(), this.getY() + 0.5f, this.getZ(), 2);
            }
        }
    }

    public abstract ServerBossBar bossInfo();

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossInfo().addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossInfo().removePlayer(player);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.bossInfo().setPercent(this.getHealth() / this.getMaxHealth());
    }

    public double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }

    public double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }
}