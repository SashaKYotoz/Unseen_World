package net.sashakyotoz.common.entities.ai;

import net.minecraft.entity.ai.goal.DiveJumpingGoal;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.sashakyotoz.common.entities.custom.GloomwhaleEntity;

public class GloomwhaleJumpGoal extends DiveJumpingGoal {
    private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 1, 4, 5, 6, 7};
    private final GloomwhaleEntity gloomwhale;
    private final int chance;
    private boolean inWater;

    public GloomwhaleJumpGoal(GloomwhaleEntity gloomwhale, int chance) {
        this.gloomwhale = gloomwhale;
        this.chance = toGoalTicks(chance);
    }

    @Override
    public boolean canStart() {
        if (this.gloomwhale.getRandom().nextInt(this.chance) != 0) {
            return false;
        } else {
            Direction direction = this.gloomwhale.getMovementDirection();
            int i = direction.getOffsetX();
            int j = direction.getOffsetZ();
            BlockPos blockPos = this.gloomwhale.getBlockPos();

            for (int k : OFFSET_MULTIPLIERS) {
                if (!this.isWater(blockPos, i, j, k) || !this.isAirAbove(blockPos, i, j, k)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean isWater(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        BlockPos blockPos = pos.add(offsetX * multiplier, 0, offsetZ * multiplier);
        return this.gloomwhale.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER) && !this.gloomwhale.getWorld().getBlockState(blockPos).blocksMovement();
    }

    private boolean isAirAbove(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
        return this.gloomwhale.getWorld().getBlockState(pos.add(offsetX * multiplier, 1, offsetZ * multiplier)).isAir()
                && this.gloomwhale.getWorld().getBlockState(pos.add(offsetX * multiplier, 2, offsetZ * multiplier)).isAir();
    }

    @Override
    public boolean shouldContinue() {
        double d = this.gloomwhale.getVelocity().y;
        return (!(d * d < 0.03F) || this.gloomwhale.getPitch() == 0.0F || !(Math.abs(this.gloomwhale.getPitch()) < 10.0F) || !this.gloomwhale.isTouchingWater())
                && !this.gloomwhale.isOnGround();
    }

    @Override
    public boolean canStop() {
        return false;
    }

    @Override
    public void start() {
        Direction direction = this.gloomwhale.getMovementDirection();
        this.gloomwhale.setVelocity(this.gloomwhale.getVelocity().add((double)direction.getOffsetX() * 0.6, 0.7, (double)direction.getOffsetZ() * 0.6));
        this.gloomwhale.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.gloomwhale.setPitch(0.0F);
    }

    @Override
    public void tick() {
        boolean bl = this.inWater;
        if (!bl) {
            FluidState fluidState = this.gloomwhale.getWorld().getFluidState(this.gloomwhale.getBlockPos());
            this.inWater = fluidState.isIn(FluidTags.WATER);
        }

        if (this.inWater && !bl) {
            this.gloomwhale.playSound(SoundEvents.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vec3d vec3d = this.gloomwhale.getVelocity();
        if (vec3d.y * vec3d.y < 0.03F && this.gloomwhale.getPitch() != 0.0F) {
            this.gloomwhale.setPitch(MathHelper.lerpAngleDegrees(0.2F, this.gloomwhale.getPitch(), 0.0F));
        } else if (vec3d.length() > 1.0E-5F) {
            double d = vec3d.horizontalLength();
            double e = Math.atan2(-vec3d.y, d) * 180.0F / (float)Math.PI;
            this.gloomwhale.setPitch((float)e);
        }
    }
}