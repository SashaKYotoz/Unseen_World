package net.sashakyotoz.common.entities.ai.bosses_goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;

public class SentinelMovementGoal extends Goal {
    private final EclipseSentinelEntity sentinel;

    public SentinelMovementGoal(EclipseSentinelEntity sentinel) {
        this.sentinel = sentinel;
    }

    @Override
    public boolean canStart() {
        return this.sentinel.getTarget() != null;
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.sentinel.getTarget();
        if (target != null) {
            if (this.sentinel.canSee(target)) {
                if (!this.sentinel.isInSentinelPose(EclipseSentinelEntity.SentinelPose.DYING))
                    this.sentinel.lookAtEntity(target, this.sentinel.isInSentinelPose(EclipseSentinelEntity.SentinelPose.BEAMING) ? 5 : 15, 15);
                switch (this.sentinel.getSentinelPose()) {
                    case SWORD_SWING -> this.sentinel.getNavigation().startMovingTo(target, 0.75f);
                    case BACKFLIP -> {
                        if (this.sentinel.squaredDistanceTo(target) < 5 && this.sentinel.isOnGround())
                            this.sentinel.getMoveControl().strafeTo(-0.5f, 0f);
                    }
                    case HARD_RUSH -> {
                        if (this.sentinel.squaredDistanceTo(target) > 3)
                            this.sentinel.getNavigation().startMovingTo(target, 1f);
                    }
                    case BLASTING_EROFLAME -> {
                        if (target.squaredDistanceTo(this.sentinel) > 4)
                            this.sentinel.getNavigation().startMovingTo(target, 1.25f);
                        else
                            this.sentinel.getMoveControl().strafeTo(-0.5f, 1f);
                    }
                    case DARKNESS, BEAMING, IDLING, DYING, RUSH_AND_SWING -> this.sentinel.getNavigation().stop();
                }
            } else
                this.sentinel.getNavigation().startMovingTo(target, 0.5f);
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}