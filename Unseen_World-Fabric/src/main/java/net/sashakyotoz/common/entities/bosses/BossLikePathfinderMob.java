package net.sashakyotoz.common.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BossLikePathfinderMob extends PathfinderMob implements Enemy {
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public BossLikePathfinderMob(EntityType<? extends BossLikePathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    public void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        List<AbstractMap.SimpleEntry<Runnable, Integer>> toExecute = new ArrayList<>();
        workQueue.forEach(entry -> {
            entry.setValue(entry.getValue() - 1);
            if (entry.getValue() <= 0)
                toExecute.add(entry);
        });
        if (!toExecute.isEmpty())
            workQueue.removeAll(toExecute);
        toExecute.forEach(entry -> entry.getKey().run());
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    public abstract boolean haveToDropLoot(DamageSource source);

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        Entity entity = source.getEntity();
        int i;
        if (entity instanceof Player)
            i = EnchantmentHelper.getMobLooting((LivingEntity) entity);
        else
            i = 0;
        boolean bl = this.lastHurtByPlayerTime > 0;
        if (this.shouldDropLoot() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            if (haveToDropLoot(source))
                this.dropFromLootTable(source, bl);
            this.dropCustomDeathLoot(source, i, bl);
        }
        this.dropEquipment();
        this.dropExperience();
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return false;
    }

    public void provokeEarthquake(int radius) {
        if (!ActionsUtils.isModLoaded("sodium")) {
            Level world = this.level();
            if (!world.isClientSide())
                for (int y = -2; y < 2; y++) {
                    for (int x = -radius; x < radius; x++) {
                        for (int z = -radius; z < radius; z++) {
                            BlockPos pos = this.blockPosition().offset(x, y, z);
                            if (world.getBlockState(pos) != null && world.getBlockState(pos).canOcclude() && world.getBlockState(pos).getDestroySpeed(this.level(), pos) < 10 && world.getBlockState(pos.above()).isAir()) {
                                FallingBlockEntity.fall(world, pos.above(3), world.getBlockState(pos));
                                world.destroyBlock(pos, false);
                            }
                        }
                    }
                }
        }
    }

    public void spawnParticle(ParticleOptions type, Level world, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }

    public void hitNearbyMobs(float damage, int radius) {
        List<LivingEntity> entityList = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.position(), this.position()).inflate(radius), e -> true)
                .stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(this.position()))).toList();
        for (LivingEntity entityIterator : entityList) {
            if (entityIterator != this) {
                entityIterator.hurt(this.damageSources().magic(), damage);
                this.spawnParticle(new BlockParticleOption(ParticleTypes.BLOCK, entityIterator.getFeetBlockState()),
                        this.level(), this.getX(), this.getY() + 0.5f, this.getZ(), 2);
            }
        }
    }

    public abstract ServerBossEvent bossInfo();

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo().addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo().removePlayer(player);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo().setProgress(this.getHealth() / this.getMaxHealth());
    }

    public double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }

    public double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }
}