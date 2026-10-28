package net.sashakyotoz.common.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public abstract class BossLikePathfinderMob extends PathfinderMob implements Enemy {
    private static final int RING_PARTICLES = 18;
    private static final float UNBREAKABLE_HARDNESS = 50.0F;

    public int stateTicks = 0;

    public BossLikePathfinderMob(EntityType<? extends BossLikePathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    public abstract boolean haveToDropLoot(DamageSource source);

    public abstract ServerBossEvent bossInfo();

    @Override
    protected void dropAllDeathLoot(ServerLevel level, DamageSource source) {
        boolean hitByPlayer = this.lastHurtByPlayerTime > 0;
        if (this.shouldDropLoot() && level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            if (this.haveToDropLoot(source)) {
                this.dropFromLootTable(source, hitByPlayer);
            }
            this.dropCustomDeathLoot(level, source, hitByPlayer);
        }
        this.dropEquipment();
        this.dropExperience(source.getEntity());
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return false;
    }

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

    public double getXVector(double distance, float yRot) {
        return -Mth.sin(yRot * Mth.DEG_TO_RAD) * distance;
    }

    public double getZVector(double distance, float yRot) {
        return Mth.cos(yRot * Mth.DEG_TO_RAD) * distance;
    }

    public Vec3 horizontalForward() {
        return new Vec3(this.getXVector(1.0D, this.getYRot()), 0.0D, this.getZVector(1.0D, this.getYRot()));
    }

    public Vec3 horizontalLeft() {
        Vec3 forward = this.horizontalForward();
        return new Vec3(forward.z, 0.0D, -forward.x);
    }

    public double halfWidth() {
        return this.getBbWidth() * 0.5D;
    }

    public void spawnParticle(ParticleOptions type, double x, double y, double z, float modifier) {
        for (int i = 0; i < RING_PARTICLES; i++) {
            double angle = i * (Math.PI * 2.0D / RING_PARTICLES);
            double vx = Math.cos(angle) * 0.25D * modifier;
            double vz = Math.sin(angle) * 0.25D * modifier;
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(type, x, y, z, 0, vx, 0.2D, vz, 1.0D);
            } else {
                this.level().addParticle(type, x, y, z, vx, 0.2D, vz);
            }
        }
    }

    public void serverParticles(ParticleOptions type, double x, double y, double z, int count, double spreadX, double spreadY, double spreadZ, double speed) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(type, x, y, z, count, spreadX, spreadY, spreadZ, speed);
        }
    }

    public void serverRing(ParticleOptions type, Vec3 center, double radius, int points) {
        for (int i = 0; i < points; i++) {
            double angle = i * Math.PI * 2.0D / points;
            this.serverParticles(type, center.x + Math.cos(angle) * radius, center.y, center.z + Math.sin(angle) * radius, 0, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    public void serverLine(ParticleOptions type, Vec3 from, Vec3 to, double step) {
        Vec3 delta = to.subtract(from);
        double length = delta.length();
        if (length < 1.0E-4D) {
            return;
        }
        Vec3 direction = delta.scale(1.0D / length);
        for (double d = 0.0D; d <= length; d += step) {
            Vec3 point = from.add(direction.scale(d));
            this.serverParticles(type, point.x, point.y, point.z, 0, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    public void groundBurst(Vec3 pos, int count, double spread) {
        BlockPos ground = BlockPos.containing(pos.x, pos.y - 0.2D, pos.z);
        BlockState state = this.level().getBlockState(ground);
        if (!state.isAir()) {
            this.serverParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.x, pos.y + 0.1D, pos.z, count, spread, 0.1D, spread, 0.15D);
        }
    }

    public Vec3 snapToGround(Vec3 pos, int maxDepth) {
        BlockPos.MutableBlockPos cursor = BlockPos.containing(pos.x, pos.y + 1.0D, pos.z).mutable();
        for (int i = 0; i < maxDepth; i++) {
            BlockPos below = cursor.below();
            if (!this.level().getBlockState(below).getCollisionShape(this.level(), below).isEmpty()) {
                return new Vec3(pos.x, cursor.getY(), pos.z);
            }
            cursor.move(Direction.DOWN);
        }
        return pos;
    }

    @Nullable
    public Vec3 findStandableGround(double x, double z, double referenceY, int searchDepth) {
        BlockPos.MutableBlockPos cursor = BlockPos.containing(x, referenceY + 3.0D, z).mutable();
        for (int i = 0; i < searchDepth; i++) {
            BlockPos below = cursor.below();
            boolean solidBelow = !this.level().getBlockState(below).getCollisionShape(this.level(), below).isEmpty();
            boolean openHere = this.level().getBlockState(cursor).getCollisionShape(this.level(), cursor).isEmpty();
            if (solidBelow && openHere) {
                return new Vec3(x, cursor.getY(), z);
            }
            cursor.move(Direction.DOWN);
        }
        return null;
    }

    public List<LivingEntity> enemiesInArcAt(Vec3 origin, Vec3 facing, double radius, double halfAngleDegrees, double height) {
        double cos = Math.cos(Math.toRadians(halfAngleDegrees));
        AABB box = new AABB(origin.x - radius, origin.y - 0.5D, origin.z - radius, origin.x + radius, origin.y + height, origin.z + radius);
        return this.enemiesIn(box).stream().filter(entity -> {
            Vec3 offset = new Vec3(entity.getX() - origin.x, 0.0D, entity.getZ() - origin.z);
            double length = offset.length();
            return length <= radius + entity.getBbWidth() * 0.5D && (length < 0.5D || facing.dot(offset.scale(1.0D / length)) >= cos);
        }).toList();
    }

    public boolean isEnemy(LivingEntity entity) {
        if (entity == this || !entity.isAlive() || entity.isAlliedTo(this) || this.isAlliedTo(entity)) {
            return false;
        }
        if (entity instanceof Player player) {
            return !player.isCreative() && !player.isSpectator();
        }
        return !(entity instanceof BossLikePathfinderMob);
    }

    public List<LivingEntity> enemiesIn(AABB box) {
        return this.level().getEntitiesOfClass(LivingEntity.class, box, this::isEnemy);
    }

    public List<LivingEntity> enemiesInRadius(double radius, double height) {
        Vec3 origin = this.position();
        AABB box = new AABB(origin.x - radius, origin.y - 1.0D, origin.z - radius, origin.x + radius, origin.y + height, origin.z + radius);
        return this.enemiesIn(box).stream()
                .filter(entity -> horizontalDistanceSqr(entity.position(), origin) <= (radius + entity.getBbWidth() * 0.5D) * (radius + entity.getBbWidth() * 0.5D))
                .sorted(Comparator.comparingDouble(entity -> horizontalDistanceSqr(entity.position(), origin)))
                .toList();
    }

    public List<LivingEntity> enemiesInLine(double from, double to, double sideReach, double height) {
        Vec3 origin = this.position();
        Vec3 forward = this.horizontalForward();
        Vec3 left = this.horizontalLeft();
        Vec3 end = origin.add(forward.scale(to));
        AABB box = new AABB(origin, end).inflate(sideReach + 1.0D, 0.0D, sideReach + 1.0D).expandTowards(0.0D, height, 0.0D).expandTowards(0.0D, -1.0D, 0.0D);
        return this.enemiesIn(box).stream().filter(entity -> {
            Vec3 offset = entity.position().subtract(origin);
            double along = offset.x * forward.x + offset.z * forward.z;
            double side = Math.abs(offset.x * left.x + offset.z * left.z);
            double reach = entity.getBbWidth() * 0.5D;
            return along >= from - reach && along <= to + reach && side <= sideReach + reach;
        }).toList();
    }

    public List<LivingEntity> enemiesInArc(double radius, double halfAngleDegrees, double height) {
        Vec3 origin = this.position();
        Vec3 forward = this.horizontalForward();
        double minDot = Math.cos(Math.toRadians(halfAngleDegrees));
        return this.enemiesInRadius(radius, height).stream().filter(entity -> {
            Vec3 offset = entity.position().subtract(origin);
            double length = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
            if (length < this.halfWidth()) {
                return true;
            }
            return (offset.x * forward.x + offset.z * forward.z) / length >= minDot;
        }).toList();
    }

    public float attackDamage(float multiplier) {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * multiplier;
    }

    public boolean strike(LivingEntity entity, float multiplier) {
        return entity.hurt(this.damageSources().mobAttack(this), this.attackDamage(multiplier));
    }

    public boolean strikeMagic(LivingEntity entity, float multiplier) {
        return entity.hurt(this.damageSources().indirectMagic(this, this), this.attackDamage(multiplier));
    }

    public void impulse(Entity entity, Vec3 velocity) {
        double factor = 1.0D;
        if (entity instanceof LivingEntity living) {
            factor = Math.max(0.4D, 1.0D - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        }
        entity.setDeltaMovement(entity.getDeltaMovement().add(velocity.scale(factor)));
        entity.hasImpulse = true;
        entity.hurtMarked = true;
    }

    public void grip(LivingEntity entity, int seconds) {
        if (entity instanceof IGrippingEntity gripping) {
            GrippingData.addGrippingSeconds(gripping, seconds);
        }
    }

    public boolean canGrief() {
        return this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    public boolean breakBlock(BlockPos pos, boolean drop) {
        BlockState state = this.level().getBlockState(pos);
        if (state.isAir() || state.getBlock() instanceof LiquidBlock || state.is(BlockTags.WITHER_IMMUNE)) {
            return false;
        }
        float hardness = state.getDestroySpeed(this.level(), pos);
        if (hardness < 0.0F || hardness >= UNBREAKABLE_HARDNESS) {
            return false;
        }
        return this.level().destroyBlock(pos, drop, this);
    }

    public void hitNearbyMobs(float damage, int radius) {
        for (LivingEntity entity : this.enemiesInRadius(radius, radius)) {
            entity.hurt(this.damageSources().indirectMagic(this, this), damage);
            this.groundBurst(entity.position(), 8, 0.4D);
        }
    }

    public void faceTowards(Entity target, float maxStep) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        float desired = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
        this.setFacing(Mth.approachDegrees(this.getYRot(), desired, maxStep));
    }

    public void setFacing(float yaw) {
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
    }

    protected static double horizontalDistanceSqr(Vec3 a, Vec3 b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return dx * dx + dz * dz;
    }
}