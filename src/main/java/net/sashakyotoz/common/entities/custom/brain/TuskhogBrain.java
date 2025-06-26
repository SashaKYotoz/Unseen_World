package net.sashakyotoz.common.entities.custom.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;

public class TuskhogBrain {
    private static final UniformIntProvider WALKING_SPEED = UniformIntProvider.create(5, 16);
    private static final UniformIntProvider LONG_JUMP_COOLDOWN_RANGE = UniformIntProvider.create(800, 1600);
    private static final UniformIntProvider RAM_COOLDOWN_RANGE = UniformIntProvider.create(500, 5000);
    private static final TargetPredicate RAM_TARGET_PREDICATE = TargetPredicate.createAttackable()
            .setPredicate(entity -> !entity.getType().equals(EntityType.GOAT) && entity.getWorld().getWorldBorder().contains(entity.getBoundingBox()));

    public static void resetLongJumpCooldown(TuskhogEntity goat, Random random) {
        goat.getBrain().remember(MemoryModuleType.LONG_JUMP_COOLING_DOWN, LONG_JUMP_COOLDOWN_RANGE.get(random));
        goat.getBrain().remember(MemoryModuleType.RAM_COOLDOWN_TICKS, RAM_COOLDOWN_RANGE.get(random));
    }

    public static Brain<?> create(Brain<TuskhogEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addLongJumpActivities(brain);
        addRamActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<TuskhogEntity> brain) {
        brain.setTaskList(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new StayAboveWaterTask(0.8F),
                        new FleeTask(2.0F),
                        new LookAroundTask(45, 90),
                        new WanderAroundTask(),
                        new TemptationCooldownTask(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        new TemptationCooldownTask(MemoryModuleType.LONG_JUMP_COOLING_DOWN),
                        new TemptationCooldownTask(MemoryModuleType.RAM_COOLDOWN_TICKS)
                )
        );
    }

    private static void addIdleActivities(Brain<TuskhogEntity> brain) {
        brain.setTaskList(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, LookAtMobWithIntervalTask.follow(EntityType.PLAYER, 6.0F, UniformIntProvider.create(30, 60))),
                        Pair.of(0, new BreedTask(EntityType.GOAT, 1.0F)),
                        Pair.of(1, new TemptTask(goat -> 1.25F)),
                        Pair.of(2, WalkTowardClosestAdultTask.create(WALKING_SPEED, 1.25F)),
                        Pair.of(
                                3,
                                new RandomTask<>(
                                        ImmutableList.of(Pair.of(StrollTask.create(1.0F), 2), Pair.of(GoTowardsLookTargetTask.create(1.0F, 3), 2), Pair.of(new WaitTask(30, 60), 1))
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.RAM_TARGET, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleState.VALUE_ABSENT)
                )
        );
    }

    private static void addLongJumpActivities(Brain<TuskhogEntity> brain) {
        brain.setTaskList(
                Activity.LONG_JUMP,
                ImmutableList.of(
                        Pair.of(0, new LeapingChargeTask(LONG_JUMP_COOLDOWN_RANGE, SoundEvents.ENTITY_GOAT_STEP)),
                        Pair.of(
                                1,
                                new LongJumpTask<>(
                                        LONG_JUMP_COOLDOWN_RANGE, 3, 4, 1.5F, hog -> SoundEvents.ENTITY_GOAT_LONG_JUMP
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryModuleState.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.BREED_TARGET, MemoryModuleState.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.LONG_JUMP_COOLING_DOWN, MemoryModuleState.VALUE_ABSENT)
                )
        );
    }

    private static void addRamActivities(Brain<TuskhogEntity> brain) {
        brain.setTaskList(
                Activity.RAM,
                ImmutableList.of(
                        Pair.of(
                                0,
                                new HogRamImpactTask(
                                        hog -> RAM_COOLDOWN_RANGE,
                                        RAM_TARGET_PREDICATE,
                                        3.0F,
                                        tuskhog -> tuskhog.isBaby() ? 1.0 : 2.5,
                                        tuskhog -> SoundEvents.ENTITY_GOAT_RAM_IMPACT,
                                        tuskhog -> SoundEvents.ENTITY_GOAT_HORN_BREAK
                                )
                        ),
                        Pair.of(
                                1,
                                new PrepareRamTask<>(
                                        hog -> RAM_COOLDOWN_RANGE.getMin(),
                                        4,
                                        7,
                                        1.25F,
                                        RAM_TARGET_PREDICATE,
                                        20,
                                        hog -> SoundEvents.ENTITY_PIG_AMBIENT
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryModuleState.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.BREED_TARGET, MemoryModuleState.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryModuleState.VALUE_ABSENT)
                )
        );
    }

    public static void updateActivities(TuskhogEntity tuskhog) {
        tuskhog.getBrain().resetPossibleActivities(ImmutableList.of(Activity.RAM, Activity.LONG_JUMP, Activity.IDLE));
    }

    public static Ingredient getTemptItems() {
        return Ingredient.ofItems(Items.WHEAT);
    }
}