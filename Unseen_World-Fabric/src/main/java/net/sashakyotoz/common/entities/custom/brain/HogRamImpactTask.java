package net.sashakyotoz.common.entities.custom.brain;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class HogRamImpactTask extends Behavior<TuskhogEntity> {
    private final Function<TuskhogEntity, UniformInt> cooldownRangeFactory;
    private final TargetingConditions targetPredicate;
    private final float speed;
    private final ToDoubleFunction<TuskhogEntity> strengthMultiplierFactory;
    private Vec3 direction;
    private final Function<TuskhogEntity, SoundEvent> impactSoundFactory;
    private final Function<TuskhogEntity, SoundEvent> hornBreakSoundFactory;

    public HogRamImpactTask(
            Function<TuskhogEntity, UniformInt> cooldownRangeFactory,
            TargetingConditions targetPredicate,
            float speed,
            ToDoubleFunction<TuskhogEntity> strengthMultiplierFactory,
            Function<TuskhogEntity, SoundEvent> impactSoundFactory,
            Function<TuskhogEntity, SoundEvent> hornBreakSoundFactory
    ) {
        super(ImmutableMap.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_PRESENT), 200);
        this.cooldownRangeFactory = cooldownRangeFactory;
        this.targetPredicate = targetPredicate;
        this.speed = speed;
        this.strengthMultiplierFactory = strengthMultiplierFactory;
        this.impactSoundFactory = impactSoundFactory;
        this.hornBreakSoundFactory = hornBreakSoundFactory;
        this.direction = Vec3.ZERO;
    }

    protected boolean shouldRun(ServerLevel serverWorld, TuskhogEntity tuskhog) {
        return tuskhog.getBrain().hasMemoryValue(MemoryModuleType.RAM_TARGET);
    }

    protected boolean shouldKeepRunning(ServerLevel serverWorld, TuskhogEntity tuskhog, long l) {
        return tuskhog.getBrain().hasMemoryValue(MemoryModuleType.RAM_TARGET);
    }

    protected void run(ServerLevel serverWorld, TuskhogEntity tuskhog, long l) {
        BlockPos blockPos = tuskhog.blockPosition();
        Brain<?> brain = tuskhog.getBrain();
        if (brain.getMemory(MemoryModuleType.RAM_TARGET).isPresent()) {
            Vec3 vec3d = brain.getMemory(MemoryModuleType.RAM_TARGET).get();
            this.direction = new Vec3((double) blockPos.getX() - vec3d.x(), 0.0, (double) blockPos.getZ() - vec3d.z()).normalize();
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3d, this.speed, 0));
        }
    }

    protected void keepRunning(ServerLevel serverWorld, TuskhogEntity tuskhog, long l) {
        List<LivingEntity> list = serverWorld.getNearbyEntities(LivingEntity.class, this.targetPredicate, tuskhog, tuskhog.getBoundingBox());
        Brain<?> brain = tuskhog.getBrain();
        if (!list.isEmpty()) {
            LivingEntity livingEntity = list.get(0);
            livingEntity.hurt(serverWorld.damageSources().noAggroMobAttack(tuskhog), (float) tuskhog.getAttributeValue(Attributes.ATTACK_DAMAGE));
            int i = tuskhog.hasEffect(MobEffects.MOVEMENT_SPEED) ? tuskhog.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() + 1 : 0;
            int j = tuskhog.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) ? tuskhog.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1 : 0;
            float f = 0.25F * (float) (i - j);
            float g = Mth.clamp(tuskhog.getSpeed() * 1.65F, 0.2F, 3.0F) + f;
            float h = livingEntity.isDamageSourceBlocked(serverWorld.damageSources().mobAttack(tuskhog)) ? 0.5F : 1.0F;
            livingEntity.knockback((double) (h * g) * this.strengthMultiplierFactory.applyAsDouble(tuskhog), this.direction.x(), this.direction.z());
            this.finishRam(serverWorld, tuskhog);
            serverWorld.playSound(null, tuskhog, this.impactSoundFactory.apply(tuskhog), SoundSource.NEUTRAL, 1.0F, 1.0F);
        } else if (this.shouldSnapHorn(serverWorld, tuskhog)) {
            serverWorld.playSound(null, tuskhog, this.impactSoundFactory.apply(tuskhog), SoundSource.NEUTRAL, 1.0F, 1.0F);
            serverWorld.playSound(null, tuskhog, this.hornBreakSoundFactory.apply(tuskhog), SoundSource.NEUTRAL, 1.0F, 1.0F);
            this.finishRam(serverWorld, tuskhog);
        } else {
            Optional<WalkTarget> optional = brain.getMemory(MemoryModuleType.WALK_TARGET);
            Optional<Vec3> optional2 = brain.getMemory(MemoryModuleType.RAM_TARGET);
            boolean bl2 = optional.isEmpty() || optional2.isEmpty() || optional.get().getTarget().currentPosition().closerThan(optional2.get(), 0.25);
            if (bl2) {
                this.finishRam(serverWorld, tuskhog);
            }
        }
    }

    private boolean shouldSnapHorn(ServerLevel world, TuskhogEntity tuskhog) {
        Vec3 vec3d = tuskhog.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize();
        BlockPos blockPos = BlockPos.containing(tuskhog.position().add(vec3d));
        return world.getBlockState(blockPos).is(BlockTags.SNAPS_GOAT_HORN) || world.getBlockState(blockPos.above()).is(BlockTags.SNAPS_GOAT_HORN);
    }

    protected void finishRam(ServerLevel world, TuskhogEntity tuskhog) {
        world.broadcastEntityEvent(tuskhog, EntityEvent.END_RAM);
        tuskhog.getBrain().setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, this.cooldownRangeFactory.apply(tuskhog).sample(world.random));
        tuskhog.getBrain().eraseMemory(MemoryModuleType.RAM_TARGET);
    }
}