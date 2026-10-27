package net.sashakyotoz.common.entities.custom.basic;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.ai.goals.GloomwhaleJumpGoal;
import net.sashakyotoz.common.entities.ai.goals.SwimAroundFarGoal;
import net.sashakyotoz.common.entities.ai.navigation.WhaleSwimNavigation;
import org.jetbrains.annotations.Nullable;

public class WhaleEntity extends WaterAnimal {
    private static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(WhaleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> TARGET_DISTANCE_SQR = SynchedEntityData.defineId(WhaleEntity.class, EntityDataSerializers.FLOAT);
    private static final float DISTANCE_SYNC_THRESHOLD = 1.0F;
    private static final double SWIM_ACCELERATION = 0.06D;

    public WhaleEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new WhaleMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        this.entityData.define(HAS_TARGET, false);
        this.entityData.define(TARGET_DISTANCE_SQR, 0.0F);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (!this.level().isClientSide()) {
            this.syncTarget(target);
        }
    }

    private void syncTarget(@Nullable LivingEntity target) {
        boolean valid = target != null && target.isAlive();
        if (this.entityData.get(HAS_TARGET) != valid) {
            this.entityData.set(HAS_TARGET, valid);
        }
        float distance = valid ? (float) this.distanceToSqr(target) : 0.0F;
        if (Math.abs(this.entityData.get(TARGET_DISTANCE_SQR) - distance) > DISTANCE_SYNC_THRESHOLD || (!valid && distance == 0.0F)) {
            this.entityData.set(TARGET_DISTANCE_SQR, distance);
        }
    }

    public boolean hasSyncedTarget() {
        return this.entityData.get(HAS_TARGET);
    }

    public float getSyncedTargetDistanceSqr() {
        return this.entityData.get(TARGET_DISTANCE_SQR);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.tickCount % 2 == 0) {
                this.syncTarget(this.getTarget());
            }
            if (!this.isInWaterOrBubble() && this.onGround() && this.tickCount % 20 == 0) {
                RandomSource random = this.getRandom();
                this.setDeltaMovement((random.nextInt(3) - 1) * 0.1F, 0.15F, (random.nextInt(3) - 1) * 0.1F);
                this.hurtMarked = true;
            }
        }
    }

    @Override
    public void handleAirSupply(int air) {
    }

    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new SwimAroundFarGoal(this, 0.8D, 0.25F));
        this.goalSelector.addGoal(5, new GloomwhaleJumpGoal(this, 10));
    }

    @Override
    public int getMaxAirSupply() {
        return 6000;
    }

    @Override
    protected int increaseAirSupply(int air) {
        return this.getMaxAirSupply();
    }

    @Override
    public int getMaxHeadXRot() {
        return 30;
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new WhaleSwimNavigation(this, world);
    }

    static class WhaleMoveControl extends MoveControl {
        private static final float MAX_YAW_STEP = 8.0F;
        private static final float MAX_PITCH_STEP = 3.0F;
        private static final float MAX_PITCH = 40.0F;
        private static final double SLOWDOWN_DISTANCE = 4.0D;
        private final WhaleEntity whale;

        WhaleMoveControl(WhaleEntity whale) {
            super(whale);
            this.whale = whale;
        }

        @Override
        public void tick() {
            if (this.operation != Operation.MOVE_TO || this.whale.getNavigation().isDone()) {
                this.idle();
                return;
            }
            double dx = this.wantedX - this.whale.getX();
            double dy = this.wantedY - this.whale.getY();
            double dz = this.wantedZ - this.whale.getZ();
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            double distance = Math.sqrt(horizontal * horizontal + dy * dy);
            if (distance < 1.0E-3D) {
                this.idle();
                return;
            }

            float targetYaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            float yawError = Mth.wrapDegrees(targetYaw - this.whale.getYRot());
            float yaw = this.rotlerp(this.whale.getYRot(), targetYaw, MAX_YAW_STEP);
            this.whale.setYRot(yaw);
            this.whale.yBodyRot = yaw;
            this.whale.yHeadRot = yaw;

            float baseSpeed = (float) (this.speedModifier * this.whale.getAttributeValue(Attributes.MOVEMENT_SPEED));
            if (!this.whale.isInWater()) {
                this.whale.setSpeed(baseSpeed * 0.1F);
                this.whale.setZza(1.0F);
                return;
            }

            float alignment = Mth.cos(yawError * Mth.DEG_TO_RAD);
            float throttle = Mth.clamp((alignment + 0.2F) / 1.2F, 0.05F, 1.0F);
            throttle *= throttle;
            throttle *= (float) Mth.clamp(distance / SLOWDOWN_DISTANCE, 0.35D, 1.0D);

            float targetPitch = Mth.clamp((float) (-Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG), -MAX_PITCH, MAX_PITCH);
            this.whale.setXRot(this.rotlerp(this.whale.getXRot(), targetPitch, MAX_PITCH_STEP));
            float pitchRad = this.whale.getXRot() * Mth.DEG_TO_RAD;

            this.whale.setSpeed((float) (baseSpeed * SWIM_ACCELERATION));
            this.whale.setXxa(0.0F);
            this.whale.setZza(Mth.cos(pitchRad) * throttle);
            this.whale.setYya(-Mth.sin(pitchRad) * throttle + (float) Mth.clamp(dy, -1.0D, 1.0D) * 0.35F);
        }

        private void idle() {
            this.whale.setSpeed(0.0F);
            this.whale.setXxa(0.0F);
            this.whale.setYya(0.0F);
            this.whale.setZza(0.0F);
            this.whale.setXRot(this.rotlerp(this.whale.getXRot(), 0.0F, MAX_PITCH_STEP));
        }
    }
}
