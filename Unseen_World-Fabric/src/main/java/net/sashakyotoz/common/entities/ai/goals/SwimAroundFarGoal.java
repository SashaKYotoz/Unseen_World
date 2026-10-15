package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SwimAroundFarGoal extends RandomStrollGoal {
    private static final int RANGE = 24;
    private static final int VERTICAL_RANGE = 6;
    private static final int ATTEMPTS = 6;
    private static final int FORWARD_ATTEMPTS = 4;
    private static final int MAX_DIVE = 6;

    public final float probability;

    public SwimAroundFarGoal(PathfinderMob mob, double speed) {
        this(mob, speed, 0.25F);
    }

    public SwimAroundFarGoal(PathfinderMob mob, double speed, float probability) {
        super(mob, speed, 40);
        this.probability = probability;
    }

    @Override
    public boolean canUse() {
        return this.mob.isInWater() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isInWater() && super.canContinueToUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        Vec3 heading = Vec3.directionFromRotation(0.0F, this.mob.getYRot());
        Vec3 ahead = this.mob.position().add(heading.scale(RANGE));
        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            Vec3 candidate = attempt < FORWARD_ATTEMPTS
                    ? DefaultRandomPos.getPosTowards(this.mob, RANGE, VERTICAL_RANGE, ahead, Math.PI / 2.0D)
                    : DefaultRandomPos.getPos(this.mob, RANGE, VERTICAL_RANGE);
            if (candidate != null && this.hasClearance(BlockPos.containing(candidate))) {
                return this.adjustDepth(candidate);
            }
        }
        return null;
    }

    private boolean hasClearance(BlockPos center) {
        Level level = this.mob.level();
        int radius = Math.max(1, (int) Math.ceil(this.mob.getBbWidth() * 0.5D) - 1);
        int height = Math.max(1, (int) Math.ceil(this.mob.getBbHeight()) - 1);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = -radius; x <= radius; x += radius) {
            for (int z = -radius; z <= radius; z += radius) {
                for (int y = 0; y <= height; y++) {
                    cursor.setWithOffset(center, x, y, z);
                    if (!level.getFluidState(cursor).is(FluidTags.WATER)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Vec3 adjustDepth(Vec3 candidate) {
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return candidate;
        }
        Level level = this.mob.level();
        BlockPos.MutableBlockPos cursor = BlockPos.containing(candidate).mutable();
        int dive = this.mob.getRandom().nextInt(MAX_DIVE) + 1;
        for (int i = 0; i < dive; i++) {
            cursor.move(0, -1, 0);
            if (!level.getFluidState(cursor).is(FluidTags.WATER) || !level.getFluidState(cursor.below()).is(FluidTags.WATER)) {
                cursor.move(0, 1, 0);
                break;
            }
        }
        return Vec3.atCenterOf(cursor);
    }
}
