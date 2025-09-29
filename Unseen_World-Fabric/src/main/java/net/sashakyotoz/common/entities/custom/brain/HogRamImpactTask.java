package net.sashakyotoz.common.entities.custom.brain;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class HogRamImpactTask extends MultiTickTask<TuskhogEntity> {
    private final Function<TuskhogEntity, UniformIntProvider> cooldownRangeFactory;
    private final TargetPredicate targetPredicate;
    private final float speed;
    private final ToDoubleFunction<TuskhogEntity> strengthMultiplierFactory;
    private Vec3d direction;
    private final Function<TuskhogEntity, SoundEvent> impactSoundFactory;
    private final Function<TuskhogEntity, SoundEvent> hornBreakSoundFactory;

    public HogRamImpactTask(
            Function<TuskhogEntity, UniformIntProvider> cooldownRangeFactory,
            TargetPredicate targetPredicate,
            float speed,
            ToDoubleFunction<TuskhogEntity> strengthMultiplierFactory,
            Function<TuskhogEntity, SoundEvent> impactSoundFactory,
            Function<TuskhogEntity, SoundEvent> hornBreakSoundFactory
    ) {
        super(ImmutableMap.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.RAM_TARGET, MemoryModuleState.VALUE_PRESENT), 200);
        this.cooldownRangeFactory = cooldownRangeFactory;
        this.targetPredicate = targetPredicate;
        this.speed = speed;
        this.strengthMultiplierFactory = strengthMultiplierFactory;
        this.impactSoundFactory = impactSoundFactory;
        this.hornBreakSoundFactory = hornBreakSoundFactory;
        this.direction = Vec3d.ZERO;
    }

    protected boolean shouldRun(ServerWorld serverWorld, TuskhogEntity tuskhog) {
        return tuskhog.getBrain().hasMemoryModule(MemoryModuleType.RAM_TARGET);
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, TuskhogEntity tuskhog, long l) {
        return tuskhog.getBrain().hasMemoryModule(MemoryModuleType.RAM_TARGET);
    }

    protected void run(ServerWorld serverWorld, TuskhogEntity tuskhog, long l) {
        BlockPos blockPos = tuskhog.getBlockPos();
        Brain<?> brain = tuskhog.getBrain();
        if (brain.getOptionalRegisteredMemory(MemoryModuleType.RAM_TARGET).isPresent()) {
            Vec3d vec3d = brain.getOptionalRegisteredMemory(MemoryModuleType.RAM_TARGET).get();
            this.direction = new Vec3d((double) blockPos.getX() - vec3d.getX(), 0.0, (double) blockPos.getZ() - vec3d.getZ()).normalize();
            brain.remember(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3d, this.speed, 0));
        }
    }

    protected void keepRunning(ServerWorld serverWorld, TuskhogEntity tuskhog, long l) {
        List<LivingEntity> list = serverWorld.getTargets(LivingEntity.class, this.targetPredicate, tuskhog, tuskhog.getBoundingBox());
        Brain<?> brain = tuskhog.getBrain();
        if (!list.isEmpty()) {
            LivingEntity livingEntity = list.get(0);
            livingEntity.damage(serverWorld.getDamageSources().mobAttackNoAggro(tuskhog), (float) tuskhog.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
            int i = tuskhog.hasStatusEffect(StatusEffects.SPEED) ? tuskhog.getStatusEffect(StatusEffects.SPEED).getAmplifier() + 1 : 0;
            int j = tuskhog.hasStatusEffect(StatusEffects.SLOWNESS) ? tuskhog.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier() + 1 : 0;
            float f = 0.25F * (float) (i - j);
            float g = MathHelper.clamp(tuskhog.getMovementSpeed() * 1.65F, 0.2F, 3.0F) + f;
            float h = livingEntity.blockedByShield(serverWorld.getDamageSources().mobAttack(tuskhog)) ? 0.5F : 1.0F;
            livingEntity.takeKnockback((double) (h * g) * this.strengthMultiplierFactory.applyAsDouble(tuskhog), this.direction.getX(), this.direction.getZ());
            this.finishRam(serverWorld, tuskhog);
            serverWorld.playSoundFromEntity(null, tuskhog, this.impactSoundFactory.apply(tuskhog), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        } else if (this.shouldSnapHorn(serverWorld, tuskhog)) {
            serverWorld.playSoundFromEntity(null, tuskhog, this.impactSoundFactory.apply(tuskhog), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            serverWorld.playSoundFromEntity(null, tuskhog, this.hornBreakSoundFactory.apply(tuskhog), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            this.finishRam(serverWorld, tuskhog);
        } else {
            Optional<WalkTarget> optional = brain.getOptionalRegisteredMemory(MemoryModuleType.WALK_TARGET);
            Optional<Vec3d> optional2 = brain.getOptionalRegisteredMemory(MemoryModuleType.RAM_TARGET);
            boolean bl2 = optional.isEmpty() || optional2.isEmpty() || optional.get().getLookTarget().getPos().isInRange(optional2.get(), 0.25);
            if (bl2) {
                this.finishRam(serverWorld, tuskhog);
            }
        }
    }

    private boolean shouldSnapHorn(ServerWorld world, TuskhogEntity tuskhog) {
        Vec3d vec3d = tuskhog.getVelocity().multiply(1.0, 0.0, 1.0).normalize();
        BlockPos blockPos = BlockPos.ofFloored(tuskhog.getPos().add(vec3d));
        return world.getBlockState(blockPos).isIn(BlockTags.SNAPS_GOAT_HORN) || world.getBlockState(blockPos.up()).isIn(BlockTags.SNAPS_GOAT_HORN);
    }

    protected void finishRam(ServerWorld world, TuskhogEntity tuskhog) {
        world.sendEntityStatus(tuskhog, EntityStatuses.FINISH_RAM);
        tuskhog.getBrain().remember(MemoryModuleType.RAM_COOLDOWN_TICKS, this.cooldownRangeFactory.apply(tuskhog).get(world.random));
        tuskhog.getBrain().forget(MemoryModuleType.RAM_TARGET);
    }
}