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
    private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 1, 4, 5, 6, 7};
    private final WhaleEntity gloomwhale;
    private final int chance;
    private boolean inWater;

    public GloomwhaleJumpGoal(WhaleEntity gloomwhale, int chance) {
        this.gloomwhale = gloomwhale;
        this.chance = reducedTickDelay(chance);
    }

    @Override
    public boolean canUse() {
        if (this.gloomwhale.getRandom().nextInt(this.chance) != 0) {
            return false;
        } else {
            Direction direction = this.gloomwhale.getMotionDirection();
            int i = direction.getStepX();
            int j = direction.getStepZ();
            BlockPos blockPos = this.gloomwhale.blockPosition();

            for (int k : OFFSET_MULTIPLIERS) {
                if (!this.isWater(blockPos, i, j, k) || !this.isAirAbove(blockPos, i, j, k)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean isWater(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        BlockPos blockPos = pos.offset(offsetX * multiplier, 0, offsetZ * multiplier);
        return this.gloomwhale.level().getFluidState(blockPos).is(FluidTags.WATER) && !this.gloomwhale.level().getBlockState(blockPos).blocksMotion();
    }

    private boolean isAirAbove(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        return this.gloomwhale.level().getBlockState(pos.offset(offsetX * multiplier, 1, offsetZ * multiplier)).isAir()
                && this.gloomwhale.level().getBlockState(pos.offset(offsetX * multiplier, 2, offsetZ * multiplier)).isAir();
    }

    @Override
    public boolean canContinueToUse() {
        double d = this.gloomwhale.getDeltaMovement().y;
        return (!(d * d < 0.03F) || this.gloomwhale.getXRot() == 0.0F || !(Math.abs(this.gloomwhale.getXRot()) < 10.0F) || !this.gloomwhale.isInWater())
                && !this.gloomwhale.onGround();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        Direction direction = this.gloomwhale.getMotionDirection();
        this.gloomwhale.setDeltaMovement(this.gloomwhale.getDeltaMovement().add((double)direction.getStepX() * 0.6, 0.7, (double)direction.getStepZ() * 0.6));
        this.gloomwhale.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.gloomwhale.setXRot(0.0F);
    }

    @Override
    public void tick() {
        boolean bl = this.inWater;
        if (!bl) {
            FluidState fluidState = this.gloomwhale.level().getFluidState(this.gloomwhale.blockPosition());
            this.inWater = fluidState.is(FluidTags.WATER);
        }

        if (this.inWater && !bl) {
            this.gloomwhale.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vec3 vec3d = this.gloomwhale.getDeltaMovement();
        if (vec3d.y * vec3d.y < 0.03F && this.gloomwhale.getXRot() != 0.0F) {
            this.gloomwhale.setXRot(Mth.rotLerp(0.2F, this.gloomwhale.getXRot(), 0.0F));
        } else if (vec3d.length() > 1.0E-5F) {
            double d = vec3d.horizontalDistance();
            double e = Math.atan2(-vec3d.y, d) * 180.0F / (float)Math.PI;
            this.gloomwhale.setXRot((float)e);
        }
    }
}