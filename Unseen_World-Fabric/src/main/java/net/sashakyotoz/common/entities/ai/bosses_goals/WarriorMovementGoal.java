package net.sashakyotoz.common.entities.ai.bosses_goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorMovementGoal extends Goal {
    private final WarriorOfChimericDarkness warrior;

    public WarriorMovementGoal(WarriorOfChimericDarkness warrior) {
        this.warrior = warrior;
    }

    @Override
    public boolean canUse() {
        return this.warrior.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.warrior.getTarget();
        if (target != null) {
            if (this.warrior.hasLineOfSight(target)) {
                if (!this.warrior.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.DYING))
                    this.warrior.lookAt(target, 15, 15);
                switch (this.warrior.getWarriorPose()) {
                    case DASHING -> this.warrior.getMoveControl().strafe(-0.75f, 0.125f);
                    case HAMMER_ATTACKING -> this.warrior.getNavigation().moveTo(target, 1.25f);
                    case HEAVY_HAMMER_ATTACKING -> this.warrior.getNavigation().moveTo(target, 0.75f);
                    case SPINNING -> {
                        this.warrior.getNavigation().moveTo(target, 1.25f);
                        if (this.warrior.distanceToSqr(target) < 4)
                            this.warrior.getMoveControl().strafe(0, -0.5f);
                    }
                    case SHIELDED_WALK -> {
                        if (this.warrior.distanceToSqr(target) > 3)
                            this.warrior.getNavigation().moveTo(target, 1.25f);
                    }
                    case EROFLAMING -> {
                        if (target.distanceToSqr(this.warrior) > 12)
                            this.warrior.getNavigation().moveTo(target, 0.75f);
                        if (target.distanceToSqr(this.warrior) < 5)
                            this.warrior.getMoveControl().strafe(-0.75f, 0);
                    }
                    case DYING -> this.warrior.getMoveControl().strafe(-0.25f, 0);
                }
            } else
                this.warrior.getNavigation().moveTo(target, 1);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}