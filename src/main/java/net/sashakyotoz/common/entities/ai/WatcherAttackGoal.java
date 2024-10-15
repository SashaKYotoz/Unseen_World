package net.sashakyotoz.common.entities.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.HarmonyWatcherEntity;

public class WatcherAttackGoal extends Goal {
    private final HarmonyWatcherEntity watcher;
    private int timerTick = 0;

    public WatcherAttackGoal(HarmonyWatcherEntity watcher) {
        this.watcher = watcher;
    }

    @Override
    public boolean canStart() {
        return this.watcher.isAngry && this.watcher.getTarget() != null;
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.watcher.getTarget();
        if (target != null && target.squaredDistanceTo(this.watcher) > 16) {
            if (this.watcher.canSee(target)){
                if (timerTick == 50) {
                    timerTick = 0;
                } else if (timerTick < 50) {
                    timerTick++;
                    this.watcher.lookAtEntity(target,30,30);
                    this.watcher.getNavigation().startMovingTo(target, 0.5);
                }
            }
            this.watcher.getNavigation().stop();
        } else {
            if (target != null) {
                this.watcher.getMoveControl().strafeTo(-2, 0);
                if (target.squaredDistanceTo(this.watcher) < 4)
                    this.watcher.tryAttack(target);
            }
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}