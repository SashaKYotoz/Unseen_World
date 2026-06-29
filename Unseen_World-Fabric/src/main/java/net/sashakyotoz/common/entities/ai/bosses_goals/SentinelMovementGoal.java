package net.sashakyotoz.common.entities.ai.bosses_goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;

public class SentinelMovementGoal extends Goal {
    private final EclipseSentinel sentinel;

    public SentinelMovementGoal(EclipseSentinel sentinel) {
        this.sentinel = sentinel;
    }

    @Override
    public boolean canUse() {
        return this.sentinel.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.sentinel.getTarget();
        if (target != null) {
            if (this.sentinel.hasLineOfSight(target)) {
                if (!this.sentinel.isInSentinelPose(EclipseSentinel.SentinelPose.DYING))
                    this.sentinel.lookAt(target, this.sentinel.isInSentinelPose(EclipseSentinel.SentinelPose.BEAMING) ? 5 : 15, 15);
                switch (this.sentinel.getSentinelPose()) {
                    case SWORD_SWING -> this.sentinel.getNavigation().moveTo(target, 0.75f);
                    case BACKFLIP -> {
                        if (this.sentinel.distanceToSqr(target) < 5 && this.sentinel.onGround())
                            this.sentinel.getMoveControl().strafe(-0.5f, 0f);
                    }
                    case HARD_RUSH -> {
                        if (this.sentinel.distanceToSqr(target) > 3)
                            this.sentinel.getNavigation().moveTo(target, 1f);
                    }
                    case BLASTING_EROFLAME -> {
                        if (target.distanceToSqr(this.sentinel) > 4)
                            this.sentinel.getNavigation().moveTo(target, 1.25f);
                        else
                            this.sentinel.getMoveControl().strafe(-0.5f, 1f);
                    }
                    case DARKNESS, BEAMING, IDLING, DYING, RUSH_AND_SWING -> this.sentinel.getNavigation().stop();
                }
            } else
                this.sentinel.getNavigation().moveTo(target, 0.5f);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}