package net.sashakyotoz.common.entities.custom.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;

public class TuskhogBrain {
    private static final UniformInt WALKING_SPEED = UniformInt.of(5, 16);
    private static final UniformInt LONG_JUMP_COOLDOWN_RANGE = UniformInt.of(800, 1600);
    private static final UniformInt RAM_COOLDOWN_RANGE = UniformInt.of(500, 5000);
    private static final TargetingConditions RAM_TARGET_PREDICATE = TargetingConditions.forCombat()
            .selector(entity -> !entity.getType().equals(EntityType.GOAT) && entity.level().getWorldBorder().isWithinBounds(entity.getBoundingBox()));

    public static void resetLongJumpCooldown(TuskhogEntity goat, RandomSource random) {
        goat.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, LONG_JUMP_COOLDOWN_RANGE.sample(random));
        goat.getBrain().setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, RAM_COOLDOWN_RANGE.sample(random));
    }

    public static Brain<?> create(Brain<TuskhogEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        addLongJumpActivities(brain);
        addRamActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivities(Brain<TuskhogEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new AnimalPanic(2.0F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(MemoryModuleType.RAM_COOLDOWN_TICKS)
                )
        );
    }

    private static void addIdleActivities(Brain<TuskhogEntity> brain) {
        brain.addActivityWithConditions(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
                        Pair.of(0, new AnimalMakeLove(EntityType.GOAT, 1.0F)),
                        Pair.of(1, new FollowTemptation(goat -> 1.25F)),
                        Pair.of(2, BabyFollowAdult.create(WALKING_SPEED, 1.25F)),
                        Pair.of(
                                3,
                                new RunOne<>(
                                        ImmutableList.of(Pair.of(RandomStroll.stroll(1.0F), 2), Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 2), Pair.of(new DoNothing(30, 60), 1))
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    private static void addLongJumpActivities(Brain<TuskhogEntity> brain) {
        brain.addActivityWithConditions(
                Activity.LONG_JUMP,
                ImmutableList.of(
                        Pair.of(0, new LongJumpMidJump(LONG_JUMP_COOLDOWN_RANGE, SoundEvents.GOAT_STEP)),
                        Pair.of(
                                1,
                                new LongJumpToRandomPos<>(
                                        LONG_JUMP_COOLDOWN_RANGE, 3, 4, 1.5F, hog -> SoundEvents.GOAT_LONG_JUMP
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    private static void addRamActivities(Brain<TuskhogEntity> brain) {
        brain.addActivityWithConditions(
                Activity.RAM,
                ImmutableList.of(
                        Pair.of(
                                0,
                                new HogRamImpactTask(
                                        hog -> RAM_COOLDOWN_RANGE,
                                        RAM_TARGET_PREDICATE,
                                        3.0F,
                                        tuskhog -> tuskhog.isBaby() ? 1.0 : 2.5,
                                        tuskhog -> SoundEvents.GOAT_RAM_IMPACT,
                                        tuskhog -> SoundEvents.GOAT_HORN_BREAK
                                )
                        ),
                        Pair.of(
                                1,
                                new PrepareRamNearestTarget<>(
                                        hog -> RAM_COOLDOWN_RANGE.getMinValue(),
                                        4,
                                        7,
                                        1.25F,
                                        RAM_TARGET_PREDICATE,
                                        20,
                                        hog -> SoundEvents.PIG_AMBIENT
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    public static void updateActivities(TuskhogEntity tuskhog) {
        tuskhog.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.RAM, Activity.LONG_JUMP, Activity.IDLE));
    }

    public static Ingredient getTemptItems() {
        return Ingredient.of(Items.WHEAT);
    }
}