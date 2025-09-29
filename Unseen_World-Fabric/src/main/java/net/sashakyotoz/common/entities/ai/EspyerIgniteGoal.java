package net.sashakyotoz.common.entities.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.sashakyotoz.common.entities.custom.EspyerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class EspyerIgniteGoal extends Goal {
    private final EspyerEntity espyer;
    @Nullable
    private LivingEntity target;

    public EspyerIgniteGoal(EspyerEntity espyer) {
        this.espyer = espyer;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.espyer.getTarget();
        return this.espyer.getFuseSpeed() > 0 || livingEntity != null && this.espyer.squaredDistanceTo(livingEntity) < 9.0;
    }

    @Override
    public void start() {
        this.espyer.getNavigation().stop();
        this.target = this.espyer.getTarget();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null)
            this.espyer.setFuseSpeed(-1);
        else if (this.espyer.squaredDistanceTo(this.target) > 49.0)
            this.espyer.setFuseSpeed(-1);
        else if (!this.espyer.getVisibilityCache().canSee(this.target))
            this.espyer.setFuseSpeed(-1);
        else
            this.espyer.setFuseSpeed(1);
    }
}