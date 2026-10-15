package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

import java.util.EnumSet;

public class WhaleHuntGoal extends Goal {
    private static final int BITE_COOLDOWN = 30;
    private static final int GIVE_UP_TICKS = 100;
    private static final int WATER_SEARCH_DEPTH = 3;
    private static final double LUNGE_DISTANCE_SQR = 64.0D;
    private static final double LUNGE_MULTIPLIER = 1.4D;
    private static final float BOAT_BREAK_DAMAGE = 5.0F;

    private final WhaleEntity whale;
    private final double speed;
    private final boolean breaksBoats;
    private final int grippingSeconds;
    private int repathCooldown;
    private int biteCooldown;
    private int outOfReachTicks;

    public WhaleHuntGoal(WhaleEntity whale, double speed, boolean breaksBoats, int grippingSeconds) {
        this.whale = whale;
        this.speed = speed;
        this.breaksBoats = breaksBoats;
        this.grippingSeconds = grippingSeconds;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public static boolean isHuntablePlayer(LivingEntity entity) {
        return entity.level().getDifficulty() != Difficulty.PEACEFUL && isReachableByWhale(entity);
    }

    public static boolean isReachableByWhale(LivingEntity entity) {
        if (entity.isInWater() || entity.getVehicle() instanceof Boat) {
            return true;
        }
        BlockPos.MutableBlockPos cursor = entity.blockPosition().mutable();
        for (int i = 0; i < WATER_SEARCH_DEPTH; i++) {
            if (entity.level().getFluidState(cursor).is(FluidTags.WATER)) {
                return true;
            }
            cursor.move(0, -1, 0);
        }
        return false;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.whale.getTarget();
        return this.whale.isInWater() && this.isValid(target) && isReachableByWhale(target);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.whale.getTarget();
        if (!this.isValid(target) || this.outOfReachTicks > GIVE_UP_TICKS) {
            return false;
        }
        double range = this.whale.getAttributeValue(Attributes.FOLLOW_RANGE);
        return this.whale.distanceToSqr(target) <= range * range;
    }

    private boolean isValid(LivingEntity target) {
        if (target == null || !target.isAlive()) {
            return false;
        }
        return !(target instanceof Player player) || (!player.isCreative() && !player.isSpectator());
    }

    @Override
    public void start() {
        this.repathCooldown = 0;
        this.outOfReachTicks = 0;
        this.whale.setAggressive(true);
    }

    @Override
    public void stop() {
        if (this.outOfReachTicks > GIVE_UP_TICKS) {
            this.whale.setTarget(null);
        }
        this.whale.setAggressive(false);
        this.whale.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.whale.getTarget();
        if (target == null) {
            return;
        }
        Entity prey = this.breaksBoats && target.getVehicle() instanceof Boat boat ? boat : target;
        this.outOfReachTicks = isReachableByWhale(target) ? 0 : this.outOfReachTicks + 1;
        this.whale.getLookControl().setLookAt(prey, 10.0F, this.whale.getMaxHeadXRot());

        if (--this.repathCooldown <= 0) {
            this.repathCooldown = 8 + this.whale.getRandom().nextInt(5);
            Vec3 aim = prey instanceof Boat ? prey.position().subtract(0.0D, this.whale.getBbHeight() * 0.5D + 0.5D, 0.0D) : prey.position();
            double moveSpeed = this.whale.distanceToSqr(prey) < LUNGE_DISTANCE_SQR ? this.speed * LUNGE_MULTIPLIER : this.speed;
            this.whale.getNavigation().moveTo(aim.x, aim.y, aim.z, moveSpeed);
        }

        if (this.biteCooldown > 0) {
            this.biteCooldown--;
            return;
        }
        if (this.isInBiteRange(prey)) {
            this.biteCooldown = BITE_COOLDOWN;
            if (prey instanceof Boat boat) {
                this.breakBoat(boat);
            } else {
                this.bite(target);
            }
        }
    }

    private Vec3 mouthPosition() {
        float yaw = this.whale.getYRot() * ((float) Math.PI / 180.0F);
        double reach = this.whale.getBbWidth() * 0.5D;
        return this.whale.position().add(-Math.sin(yaw) * reach, this.whale.getBbHeight() * 0.5D, Math.cos(yaw) * reach);
    }

    private boolean isInBiteRange(Entity prey) {
        AABB bounds = prey.getBoundingBox().inflate(1.5D);
        return bounds.contains(this.mouthPosition());
    }

    private void bite(LivingEntity target) {
        if (this.whale.doHurtTarget(target)) {
            if (this.grippingSeconds > 0 && target instanceof IGrippingEntity gripping) {
                GrippingData.addGrippingSeconds(gripping, this.grippingSeconds);
            }
            this.whale.playSound(SoundEvents.PHANTOM_BITE, 1.5F, 0.5F);
        }
    }

    private void breakBoat(Boat boat) {
        Vec3 pos = boat.position();
        this.whale.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 1.5F, 0.7F);
        this.whale.playSound(SoundEvents.GENERIC_SPLASH, 2.0F, 0.6F);
        if (this.whale.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SPLASH, pos.x, pos.y + 0.5D, pos.z, 40, 1.0D, 0.3D, 1.0D, 0.2D);
            serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, pos.x, pos.y - 0.5D, pos.z, 20, 0.8D, 0.4D, 0.8D, 0.1D);
        }
        for (Entity passenger : boat.getPassengers()) {
            passenger.setDeltaMovement(passenger.getDeltaMovement().add(0.0D, 0.6D, 0.0D));
            passenger.hurtMarked = true;
        }
        boat.hurt(this.whale.damageSources().mobAttack(this.whale), BOAT_BREAK_DAMAGE);
    }
}