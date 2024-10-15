package net.sashakyotoz.common.entities.ai.warrior_goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorMovementGoal extends Goal {
    private final WarriorOfChimericDarkness warrior;

    public WarriorMovementGoal(WarriorOfChimericDarkness warrior) {
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
                if (!this.warrior.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.DYING))
                    this.warrior.lookAtEntity(target, 15, 15);
                switch (this.warrior.getWarriorPose()) {
                    case DASHING -> this.warrior.getMoveControl().strafeTo(-0.75f, 0.125f);
                    case HAMMER_ATTACKING -> this.warrior.getNavigation().startMovingTo(target, 1.25f);
                    case HEAVY_HAMMER_ATTACKING -> this.warrior.getNavigation().startMovingTo(target, 0.75f);
                    case SPINNING -> {
                        this.warrior.getNavigation().startMovingTo(target, 1.25f);
                        if (this.warrior.squaredDistanceTo(target) < 4)
                            this.warrior.getMoveControl().strafeTo(0, -0.5f);
                    }
                    case SHIELDED_WALK -> {
                        if (this.warrior.squaredDistanceTo(target) > 3)
                            this.warrior.getNavigation().startMovingTo(target, 1.25f);
                    }
                    case EROFLAMING -> {
                        if (target.squaredDistanceTo(this.warrior) > 12)
                            this.warrior.getNavigation().startMovingTo(target, 0.75f);
                        if (target.squaredDistanceTo(this.warrior) < 5)
                            this.warrior.getMoveControl().strafeTo(-0.75f, 0);
                    }
                    case DYING -> this.warrior.getMoveControl().strafeTo(-0.25f, 0);
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