package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

public class GloomwhaleJumpGoal extends JumpGoal {
    private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 2, 5, 8, 10};
    private final WhaleEntity gloomwhale;
    private final int chance;
    private boolean inWater;

    public GloomwhaleJumpGoal(WhaleEntity gloomwhale, int chance) {
        this.gloomwhale = gloomwhale;
        this.chance = reducedTickDelay(chance);
    }

    @Override
    public boolean canUse() {
        if (!this.gloomwhale.isInWater() || this.gloomwhale.getNavigation().isDone()) {
            return false;
        }

        if (this.gloomwhale.getRandom().nextInt(this.chance) != 0) {
            return false;
        }

        Vec3 delta = this.gloomwhale.getDeltaMovement();
        if (delta.x * delta.x + delta.z * delta.z < 0.01D) {
            return false;
        }

        Direction direction = this.gloomwhale.getMotionDirection();
        int stepX = direction.getStepX();
        int stepZ = direction.getStepZ();
        BlockPos blockPos = this.gloomwhale.blockPosition();

        for (int multiplier : OFFSET_MULTIPLIERS) {
            if (!this.isWater(blockPos, stepX, stepZ, multiplier) || !this.isAirAbove(blockPos, stepX, stepZ, multiplier)) {
                return false;
            }
        }

        return true;
    }

    private boolean isWater(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        BlockPos checkPos = pos.offset(offsetX * multiplier, 0, offsetZ * multiplier);
        return this.gloomwhale.level().getFluidState(checkPos).is(FluidTags.WATER)
                && !this.gloomwhale.level().getBlockState(checkPos).blocksMotion();
    }

    private boolean isAirAbove(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        BlockPos checkPos = pos.offset(offsetX * multiplier, 0, offsetZ * multiplier);
        return this.gloomwhale.level().getBlockState(checkPos.above(1)).isAir()
                && this.gloomwhale.level().getBlockState(checkPos.above(2)).isAir()
                && this.gloomwhale.level().getBlockState(checkPos.above(3)).isAir();
    }

    @Override
    public boolean canContinueToUse() {
        double ySpeed = this.gloomwhale.getDeltaMovement().y;
        boolean horizontalVelocityPresent = this.gloomwhale.getDeltaMovement().horizontalDistanceSqr() > 0.005D;

        return (ySpeed > -0.5D || !this.gloomwhale.isInWater())
                && horizontalVelocityPresent
                && !this.gloomwhale.onGround();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        this.inWater = true;

        Vec3 forwardVector = this.gloomwhale.getForward();
        double currentSpeed = Math.max(this.gloomwhale.getDeltaMovement().horizontalDistance(), 0.35D);

        double xImpulse = forwardVector.x * currentSpeed * 1.8D;
        double yImpulse = 0.55D;
        double zImpulse = forwardVector.z * currentSpeed * 1.8D;

        this.gloomwhale.setDeltaMovement(xImpulse, yImpulse, zImpulse);
    }

    @Override
    public void stop() {
        this.gloomwhale.setXRot(0.0F);
    }

    @Override
    public void tick() {
        boolean wasInWater = this.inWater;
        FluidState fluidState = this.gloomwhale.level().getFluidState(this.gloomwhale.blockPosition());
        this.inWater = fluidState.is(FluidTags.WATER);

        if (this.inWater && !wasInWater) {
            this.gloomwhale.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 0.7F);
        }

        Vec3 velocity = this.gloomwhale.getDeltaMovement();
        if (velocity.y * velocity.y < 0.03D && this.gloomwhale.getXRot() != 0.0F) {
            this.gloomwhale.setXRot(Mth.rotLerp(0.1F, this.gloomwhale.getXRot(), 0.0F));
        } else if (velocity.lengthSqr() > 1.0E-5D) {
            double horizontalDist = velocity.horizontalDistance();
            double pitchAngle = Math.atan2(-velocity.y, horizontalDist) * 180.0D / Math.PI;
            this.gloomwhale.setXRot((float) pitchAngle);
        }
    }
}