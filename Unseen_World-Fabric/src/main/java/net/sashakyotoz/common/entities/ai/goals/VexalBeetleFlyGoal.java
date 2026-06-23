package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class VexalBeetleFlyGoal extends FlyGoal {
    public VexalBeetleFlyGoal(PathAwareEntity pathAwareEntity, double speed) {
        super(pathAwareEntity, speed);
    }
    @Override
    protected Vec3d getWanderTarget() {
        Vec3d vec3d = null;
        if (this.mob.isTouchingWater())
            vec3d = FuzzyTargeting.find(this.mob, 32, 12);

        if (this.mob.getRandom().nextFloat() >= this.probability)
            vec3d = this.locateSpotToFlyOn();

        return vec3d == null ? super.getWanderTarget() : vec3d;
    }

    @Nullable
    private Vec3d locateSpotToFlyOn() {
        BlockPos blockPos = this.mob.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutable2 = new BlockPos.Mutable();

        for (BlockPos blockPos2 : BlockPos.iterate(
                MathHelper.floor(this.mob.getX() - 24.0),
                MathHelper.floor(this.mob.getY() - 8.0),
                MathHelper.floor(this.mob.getZ() - 24.0),
                MathHelper.floor(this.mob.getX() + 24.0),
                MathHelper.floor(this.mob.getY() + 16.0),
                MathHelper.floor(this.mob.getZ() + 24.0)
        )) {
            if (!blockPos.equals(blockPos2)) {
                BlockState blockState = this.mob.getWorld().getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                if (blockState.isOpaque() && this.mob.getWorld().isAir(blockPos2) && this.mob.getWorld().isAir(mutable.set(blockPos2, Direction.UP)))
                    return Vec3d.ofBottomCenter(blockPos2);
            }
        }

        return null;
    }
}