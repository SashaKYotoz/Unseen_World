package net.sashakyotoz.common.entities.ai.brains;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.schedule.Activity;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;

public class AuricPatriarchBrain {

    public static Brain<?> create(Brain<AuricPatriarch> brain) {
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);

        addCoreActivities(brain);
        addIdleActivities(brain);
        addFightActivities(brain);

        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<AuricPatriarch> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }

    private static void addIdleActivities(Brain<AuricPatriarch> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of()
        );
    }

    private static void addFightActivities(Brain<AuricPatriarch> brain) {
        brain.addActivity(
                Activity.FIGHT,
                10,
                ImmutableList.of()
        );
    }

    public static void updateActivities(AuricPatriarch patriarch) {
        Brain<AuricPatriarch> brain = patriarch.getBrain();
        if (patriarch.getTarget() != null && patriarch.getTarget().isAlive()) {
            brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        } else {
            brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
        }
    }
}