package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SwimAroundFarGoal extends RandomStrollGoal {
    public final float probability;

    public SwimAroundFarGoal(PathfinderMob pathAwareEntity, double speed) {
        this(pathAwareEntity, speed, 0.025F);
    }

    public SwimAroundFarGoal(PathfinderMob mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            Vec3 vec3d = BehaviorUtils.getRandomSwimmablePos(this.mob, 72, 9);
            return vec3d == null ? LandRandomPos.getPos(this.mob, 48, 7) : vec3d;
        } else {
            return this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, 48, 7) : BehaviorUtils.getRandomSwimmablePos(this.mob, 32, 9);
        }
    }
}