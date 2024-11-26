package net.sashakyotoz.common.entities.ai.bosses_goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;

public class SentinelMovementGoal extends Goal {
    private final EclipseSentinelEntity warrior;

    public SentinelMovementGoal(EclipseSentinelEntity warrior) {
        this.warrior = warrior;
    }

    @Override
    public boolean canStart() {
        return this.warrior.getTarget() != null;
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.warrior.getTarget();
        if (target != null) {
            if (this.warrior.canSee(target)) {
                if (!this.warrior.isInSentinelPose(EclipseSentinelEntity.SentinelPose.DYING))
                    this.warrior.lookAtEntity(target, 15, 15);
                switch (this.warrior.getSentinelPose()) {
                    case SWORD_SWING -> this.warrior.getNavigation().startMovingTo(target, 0.75f);
                    case BACKFLIP -> {
                        if (this.warrior.squaredDistanceTo(target) < 5)
                            this.warrior.getMoveControl().strafeTo(-0.5f, 0f);
                    }
                    case HARD_RUSH, RUSH_AND_SWING -> {
                        if (this.warrior.squaredDistanceTo(target) > 3)
                            this.warrior.getNavigation().startMovingTo(target, 1f);
                    }
                    case BLASTING_EROFLAME -> {
                        if (target.squaredDistanceTo(this.warrior) > 4)
                            this.warrior.getNavigation().startMovingTo(target, 1.25f);
                        else
                            this.warrior.getMoveControl().strafeTo(-0.5f, 1f);
                    }
                    case DARKNESS, BEAMING, IDLING, DYING -> this.warrior.getNavigation().stop();
                }
            } else
                this.warrior.getNavigation().startMovingTo(target, 1);
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}