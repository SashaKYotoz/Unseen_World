package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class SwimAroundFarGoal extends WanderAroundGoal {
    public final float probability;

    public SwimAroundFarGoal(PathAwareEntity pathAwareEntity, double speed) {
        this(pathAwareEntity, speed, 0.025F);
    }

    public SwimAroundFarGoal(PathAwareEntity mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
    }

    @Nullable
    @Override
    protected Vec3d getWanderTarget() {
        if (this.mob.isInsideWaterOrBubbleColumn()) {
            Vec3d vec3d = LookTargetUtil.find(this.mob, 72, 9);
            return vec3d == null ? FuzzyTargeting.find(this.mob, 48, 7) : vec3d;
        } else {
            return this.mob.getRandom().nextFloat() >= this.probability ? FuzzyTargeting.find(this.mob, 48, 7) : LookTargetUtil.find(this.mob, 32, 9);
        }
    }
}