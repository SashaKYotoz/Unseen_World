package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class VexalBeetleFlyGoal extends WaterAvoidingRandomFlyingGoal {
    private static final int PERCH_ATTEMPTS = 16;
    private static final int HORIZONTAL_RANGE = 24;
    private static final int VERTICAL_RANGE = 8;
    private static final float PERCH_CHANCE = 0.35F;

    public VexalBeetleFlyGoal(PathfinderMob mob, double speed) {
        super(mob, speed);
    }

    @Override
    public boolean canUse() {
        return !this.mob.isVehicle() && super.canUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.mob.isInWater()) {
            Vec3 land = LandRandomPos.getPos(this.mob, 32, 12);
            if (land != null) {
                return land;
            }
        }
        if (this.mob.getRandom().nextFloat() < PERCH_CHANCE) {
            Vec3 perch = this.findPerch();
            if (perch != null) {
                return perch;
            }
        }
        return super.getPosition();
    }

    @Nullable
    private Vec3 findPerch() {
        Level level = this.mob.level();
        BlockPos origin = this.mob.blockPosition();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int attempt = 0; attempt < PERCH_ATTEMPTS; attempt++) {
            int dx = this.mob.getRandom().nextInt(HORIZONTAL_RANGE * 2 + 1) - HORIZONTAL_RANGE;
            int dy = this.mob.getRandom().nextInt(VERTICAL_RANGE * 2 + 1) - VERTICAL_RANGE;
            int dz = this.mob.getRandom().nextInt(HORIZONTAL_RANGE * 2 + 1) - HORIZONTAL_RANGE;
            cursor.setWithOffset(origin, dx, dy, dz);
            if (!level.hasChunkAt(cursor)) {
                continue;
            }
            BlockPos below = cursor.below();
            if (level.getBlockState(below).canOcclude() && level.isEmptyBlock(cursor) && level.isEmptyBlock(cursor.above())) {
                return Vec3.atBottomCenterOf(cursor);
            }
        }
        return null;
    }
}