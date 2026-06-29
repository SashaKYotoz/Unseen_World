package net.sashakyotoz.common.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.custom.HarmonyWatcherEntity;

public class WatcherAttackGoal extends Goal {
    private final HarmonyWatcherEntity watcher;
    private int timerTick = 0;

    public WatcherAttackGoal(HarmonyWatcherEntity watcher) {
        this.watcher = watcher;
    }

    @Override
    public boolean canUse() {
        return this.watcher.isAngry && this.watcher.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.watcher.getTarget();
        if (target != null && target.distanceToSqr(this.watcher) > 16) {
            if (this.watcher.hasLineOfSight(target)){
                if (timerTick == 50) {
                    timerTick = 0;
                } else if (timerTick < 50) {
                    timerTick++;
                    this.watcher.lookAt(target,30,30);
                    this.watcher.getNavigation().moveTo(target, 0.5);
                }
            }
            this.watcher.getNavigation().stop();
        } else {
            if (target != null) {
                this.watcher.getMoveControl().strafe(-2, 0);
                if (target.distanceToSqr(this.watcher) < 4)
                    this.watcher.doHurtTarget(target);
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}