package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class VexalBeetleFlyGoal extends WaterAvoidingRandomFlyingGoal {
    public VexalBeetleFlyGoal(PathfinderMob pathAwareEntity, double speed) {
        super(pathAwareEntity, speed);
    }
    @Override
    protected Vec3 getPosition() {
        Vec3 vec3d = null;
        if (this.mob.isInWater())
            vec3d = LandRandomPos.getPos(this.mob, 32, 12);

        if (this.mob.getRandom().nextFloat() >= this.probability)
            vec3d = this.locateSpotToFlyOn();

        return vec3d == null ? super.getPosition() : vec3d;
    }

    @Nullable
    private Vec3 locateSpotToFlyOn() {
        BlockPos blockPos = this.mob.blockPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutable2 = new BlockPos.MutableBlockPos();

        for (BlockPos blockPos2 : BlockPos.betweenClosed(
                Mth.floor(this.mob.getX() - 24.0),
                Mth.floor(this.mob.getY() - 8.0),
                Mth.floor(this.mob.getZ() - 24.0),
                Mth.floor(this.mob.getX() + 24.0),
                Mth.floor(this.mob.getY() + 16.0),
                Mth.floor(this.mob.getZ() + 24.0)
        )) {
            if (!blockPos.equals(blockPos2)) {
                BlockState blockState = this.mob.level().getBlockState(mutable2.setWithOffset(blockPos2, Direction.DOWN));
                if (blockState.canOcclude() && this.mob.level().isEmptyBlock(blockPos2) && this.mob.level().isEmptyBlock(mutable.setWithOffset(blockPos2, Direction.UP)))
                    return Vec3.atBottomCenterOf(blockPos2);
            }
        }

        return null;
    }
}