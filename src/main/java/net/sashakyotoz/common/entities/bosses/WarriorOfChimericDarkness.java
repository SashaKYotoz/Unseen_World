package net.sashakyotoz.common.entities.bosses;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.ai.bosses_goals.WarriorMovementGoal;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.ActionsManager;
import net.sashakyotoz.utils.ChimericDarknessData;
import net.sashakyotoz.utils.IEntityDataSaver;
import net.sashakyotoz.utils.JsonWorldController;

import java.util.List;

public class WarriorOfChimericDarkness extends BossLikePathfinderMob {
    public final AnimationState spawn = new AnimationState();
    public final AnimationState death = new AnimationState();
    public final AnimationState dash = new AnimationState();
    public final AnimationState hammerAttack = new AnimationState();
    public final AnimationState shieldStriking = new AnimationState();
    public final AnimationState heavyHammerAttack = new AnimationState();

    public static final TrackedDataHandler<WarriorPose> WARRIOR_POSE = TrackedDataHandler.ofEnum(WarriorPose.class);
    protected static final TrackedData<WarriorPose> PHASE = DataTracker.registerData(WarriorOfChimericDarkness.class, WARRIOR_POSE);

    private final ServerBossBar bossBar = new ServerBossBar(Text.translatable("entity.unseen_world.warrior_of_chimeric_darkness"), BossBar.Color.PURPLE, BossBar.Style.NOTCHED_6);
    private BlockPos pos;
    private int timeOfAbility = 0;

    public WarriorOfChimericDarkness(EntityType<? extends BossLikePathfinderMob> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = WITHER_XP;
        this.pos = this.getBlockPos().add(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3);
        this.setStepHeight(1.5f);
        this.setRandomPhase();
        MobNavigation mobNavigation = (MobNavigation) this.getNavigation();
        mobNavigation.setCanSwim(true);
        mobNavigation.setCanWalkOverFences(true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PHASE, WarriorPose.SPAWNING);
    }

    @Override
    public ServerBossBar bossInfo() {
        return bossBar;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.deathTime = -20;
        if (this.getWorld() instanceof ServerWorld world) {
            ChimericDarknessData data = JsonWorldController.data.get(0);
            JsonWorldController.saveController(world, true, data.sunUnlock(), data.galacticUnlock());
            JsonWorldController.updateSave(world);
        }
        updateTranslocatone(this.getWorld(), this.getBlockPos());
        super.onDeath(damageSource);
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (this.deathTime == -2)
            this.setWarriorPose(WarriorPose.DYING);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 350)
                .add(EntityAttributes.GENERIC_ARMOR, 12)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 4)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (PHASE.equals(data)) {
            switch (this.getWarriorPose()) {
                case SPAWNING -> {
                    spawn.start(this.age);
                    this.setWarriorPose(WarriorPose.IDLING);
                }
                case DYING -> this.death.start(this.age);
                case DASHING -> {
                    this.dash.start(this.age);
                    this.queueServerWork(5, () -> {
                        this.setVelocity(
                                this.getXVector(2, this.getYaw()),
                                0.5,
                                this.getZVector(2, this.getYaw()));
                        this.queueServerWork(10, () -> {
                            if (this.getTarget() != null && this.getTarget().squaredDistanceTo(this) < 7) {
                                this.playSound(SoundEvents.BLOCK_ANVIL_HIT, 2, 2);
                                this.getTarget().setVelocity(
                                        this.getXVector(-2, this.getYaw()),
                                        0.375,
                                        this.getZVector(-2, this.getYaw()));
                            }
                        });
                    });
                }
                case HAMMER_ATTACKING -> {
                    this.hammerAttack.start(this.age);
                    this.queueServerWork(12, () -> {
                        if (this.getTarget() != null && this.getTarget().squaredDistanceTo(this) < 5)
                            this.tryAttack(this.getTarget());
                        if (this.getWorld() instanceof ServerWorld world)
                            world.spawnParticles(ParticleTypes.ASH,
                                    this.getX(), this.getY() + 1.5f, this.getZ(),
                                    12, this.getXVector(1, this.getYaw()), 0.25, this.getZVector(1, this.getYaw()), 1);
                    });
                }
                case HEAVY_HAMMER_ATTACKING -> {
                    this.heavyHammerAttack.start(this.age);
                    this.getJumpControl().setActive();
                    this.queueServerWork(18, () -> {
                        this.hitNearbyMobs(10, 7);
                        if (!ActionsManager.isModLoaded("sodium"))
                            this.provokeEarthquake(3);
                        this.playSound(SoundEvents.BLOCK_ANVIL_HIT, 2.5f, 2.5f);
                    });
                }
                case SHIELDED_STRIKING -> {
                    this.shieldStriking.start(this.age);
                    this.getNavigation().stop();
                    this.queueServerWork(10, () -> {
                        this.playSound(SoundEvents.BLOCK_ANVIL_FALL, 2.5f, 2.5f);
                        if (this.getTarget() != null) {
                            this.lookAtEntity(this.getTarget(), 45, 45);
                            if (this.getWorld() instanceof ServerWorld world) {
                                for (int i = 0; i < 3; i++)
                                    world.spawnParticles(new WindVibrationParticleEffect(new EntityPositionSource(this.getTarget(), 0.1F + (i / 5f)), 20), this.getX(), this.getY() + (i / 5f), this.getZ(), 3, 0, 0, 0, 0);
                            }
                            float scaling = 0;
                            for (int i1 = 0; i1 < 7; i1++) {
                                BlockPos pos = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                                if (!this.getWorld().getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).isOpaque())
                                    scaling = scaling + 1;
                                BlockPos pos1 = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                                List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(1.5), LivingEntity::canHit);
                                for (LivingEntity entity : entities) {
                                    if (entity != this) {
                                        entity.damage(this.getDamageSources().generic(), 10);
                                        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 160, 1));
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }

        super.onTrackedDataSet(data);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new WarriorMovementGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 32));
    }

    @Override
    public boolean canBeHitByProjectile() {
        return super.canBeHitByProjectile() && !this.isInWarriorPose(WarriorPose.SPINNING);
    }

    public void setRandomPhase() {
        int random = this.getRandom().nextInt(4);
        switch (this.getWarriorPose()) {
            case DASHING -> {
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.EROFLAMING);
                    case 2 -> this.setWarriorPose(WarriorPose.SPINNING);
                    case 3 -> this.setWarriorPose(WarriorPose.BLASTING);
                    default -> this.setWarriorPose(WarriorPose.HAMMER_ATTACKING);
                }
            }
            case HAMMER_ATTACKING -> {
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.HEAVY_HAMMER_ATTACKING);
                    case 2 -> this.setWarriorPose(WarriorPose.SPINNING);
                    case 3 -> this.setWarriorPose(WarriorPose.HAMMER_ATTACKING);
                    default -> this.setWarriorPose(WarriorPose.DASHING);
                }
            }
            case HEAVY_HAMMER_ATTACKING -> {
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.DASHING);
                    case 2 -> this.setWarriorPose(WarriorPose.EROFLAMING);
                    case 3 -> this.setWarriorPose(WarriorPose.BLASTING);
                    default -> this.setWarriorPose(WarriorPose.SHIELDED_STRIKING);
                }
            }
            case SPINNING -> {
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.HEAVY_HAMMER_ATTACKING);
                    case 2 -> this.setWarriorPose(WarriorPose.SHIELDED_STRIKING);
                    case 3 -> this.setWarriorPose(WarriorPose.SHIELDED_WALK);
                    default -> this.setWarriorPose(WarriorPose.HAMMER_ATTACKING);
                }
            }
            case SHIELDED_WALK -> {
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.EROFLAMING);
                    case 2 -> this.setWarriorPose(WarriorPose.DASHING);
                    case 3 -> this.setWarriorPose(WarriorPose.BLASTING);
                    default -> this.setWarriorPose(WarriorPose.HAMMER_ATTACKING);
                }
            }
            case IDLING -> {
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.SPINNING);
                    case 2 -> this.setWarriorPose(WarriorPose.DASHING);
                    case 3 -> this.setWarriorPose(WarriorPose.BLASTING);
                    default -> this.setWarriorPose(WarriorPose.HAMMER_ATTACKING);
                }
            }
            case BLASTING -> {
                pos = this.getBlockPos().add(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3);
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.SHIELDED_WALK);
                    case 2 -> this.setWarriorPose(WarriorPose.DASHING);
                    case 3 -> this.setWarriorPose(WarriorPose.HEAVY_HAMMER_ATTACKING);
                    default -> this.setWarriorPose(WarriorPose.SHIELDED_STRIKING);
                }
            }
            default -> {
                switch (random) {
                    case 1 -> this.setWarriorPose(WarriorPose.SHIELDED_WALK);
                    case 2 -> this.setWarriorPose(WarriorPose.EROFLAMING);
                    case 3 -> this.setWarriorPose(WarriorPose.SPINNING);
                    default -> this.setWarriorPose(WarriorPose.HAMMER_ATTACKING);
                }
            }
        }
    }

    private void updateTranslocatone(World world, BlockPos pos) {
        int radius = 9;
        int height = 7;
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (world.getBlockState(pos1).isOf(ModBlocks.GLACIEMITE_TRANSLOCATONE)) {
                        world.setBlockState(pos1, world.getBlockState(pos1).with(Properties.TRIGGERED, false));
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {
        if (!this.isInWarriorPose(WarriorPose.BLASTING))
            super.pushOutOfBlocks(x, y, z);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isInWarriorPose(WarriorPose.BLASTING) && !this.isInWarriorPose(WarriorPose.SHIELDED_STRIKING);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.timeOfAbility > 0)
            this.timeOfAbility--;
        if (this.getTarget() != null) {
            if (this.squaredDistanceTo(this.getTarget()) < 4) {
                if (!this.isInWarriorPose(WarriorPose.HAMMER_ATTACKING))
                    this.getMoveControl().strafeTo(-1, 0);
            }
            if (this.timeOfAbility <= 0 && !this.isInWarriorPose(WarriorPose.DYING))
                this.setRandomPhase();
            if (this.isInWarriorPose(WarriorPose.BLASTING)) {
                this.navigation.stop();
                if (this.getWorld() instanceof ServerWorld world)
                    world.spawnParticles(ParticleTypes.ASH,
                            this.getX() + this.getXVector(1, this.getYaw()),
                            this.getY(),
                            this.getZ() + this.getZVector(1, this.getYaw()),
                            3, 0, 0, 0, 0.5f);
                for (int i = -3; i < 3; i++) {
                    if (i % 2 == 0) {
                        if (this.getWorld() instanceof ServerWorld world)
                            world.spawnParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.getX() + i, pos.getY() + 0.25, pos.getZ() + i, 9, 0, 1, 0, 1);
                        if (this.age % 5 == 0) {
                            List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos, pos).expand(2), LivingEntity::canHit);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.damage(this.getDamageSources().dryOut(), 8);
                            }
                        }
                    }
                }
                if (this.age % 60 == 0)
                    this.pos = this.random.nextBoolean() ? this.getBlockPos().add(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3)
                            : this.getTarget().getBlockPos().up();
            }
            if (this.squaredDistanceTo(this.getTarget()) < 9 && this.isInWarriorPose(WarriorPose.SPINNING) && this.age % 15 == 0)
                this.tryAttack(this.getTarget());
            if (this.squaredDistanceTo(this.getTarget()) < 5 && this.isInWarriorPose(WarriorPose.SHIELDED_WALK) && this.age % 5 == 0)
                this.getTarget().setVelocity(
                        this.getXVector(-0.75f, this.getTarget().getYaw()),
                        0.45f,
                        this.getZVector(-0.75f, this.getTarget().getYaw())
                );
            if (this.age % 4 == 0 && this.isInWarriorPose(WarriorPose.EROFLAMING)) {
                World world = this.getWorld();
                if (world instanceof ServerWorld serverWorld) {
                    Vec3d eyePos = this.getEyePos();
                    Vec3d lookVec = this.getRotationVec(1.0F);
                    Vec3d endPos = eyePos.add(lookVec.multiply(5));
                    int particleCount = 20;
                    Vec3d step = endPos.subtract(eyePos).multiply(1.0 / particleCount);
                    double radius = 0.5;
                    for (int j = 0; j < particleCount; j++) {
                        Vec3d basePos = eyePos.add(step.multiply(j));
                        double angle = this.age % 360 + (j * Math.PI / 10);
                        double offsetX = radius * Math.cos(angle);
                        double offsetY = radius * Math.sin(angle);

                        Vec3d perpendicular = lookVec.crossProduct(new Vec3d(0, 1, 0)).normalize();
                        Vec3d particlePos = basePos.add(perpendicular.multiply(offsetX)).add(0, offsetY, 0);
                        serverWorld.spawnParticles(
                                ParticleTypes.SOUL_FIRE_FLAME,
                                particlePos.x,
                                particlePos.y,
                                particlePos.z,
                                1, 0, 0, 0, 1
                        );
                    }
                }
                BlockPos center = world.raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(this.age % 2 == 0 ? 6 : 2.5f)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, new Box(center.toCenterPos(), center.toCenterPos()).expand(3), LivingEntity::canHit);
                for (LivingEntity entity : entities) {
                    if (entity != this && !entity.isFireImmune()) {
                        if (entity instanceof ServerPlayerEntity player)
                            GrippingData.addGrippingSeconds((IEntityDataSaver) player, 2);
                        entity.damage(this.getDamageSources().dryOut(), 4);
                    }
                }
            }
        }
    }

    @Override
    public boolean haveToDropLoot(DamageSource source) {
        return source.getAttacker() instanceof ServerPlayerEntity player &&
                !(player.getStatHandler().getStat(Stats.KILLED.getOrCreateStat(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS)) > 1);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.random.nextFloat() > 0.875 && !this.isInWarriorPose(WarriorPose.SHIELDED_WALK)
                && !this.getWorld().isClient() && source.isOf(DamageTypes.PLAYER_ATTACK))
            this.setWarriorPose(WarriorPose.SHIELDED_WALK);
        if (this.isInWarriorPose(WarriorPose.SHIELDED_WALK)
                || this.isInWarriorPose(WarriorPose.EROFLAMING)
                || this.isInWarriorPose(WarriorPose.BLASTING))
            return false;
        if (this.getTarget() != null && this.isInWarriorPose(WarriorPose.SHIELDED_WALK))
            this.getTarget().damage(source, amount / 2f);
        return super.damage(source, amount);
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return Math.round(super.computeFallDamage(fallDistance, damageMultiplier) / 2f);
    }

    public void setWarriorPose(WarriorPose pose) {
        this.dataTracker.set(PHASE, pose);
        this.timeOfAbility = pose.getAbilityTime;
    }

    public WarriorPose getWarriorPose() {
        return this.dataTracker.get(PHASE);
    }

    public boolean isInWarriorPose(WarriorPose pose) {
        return this.getWarriorPose() == pose;
    }

    public enum WarriorPose {
        IDLING(300),
        SPINNING(200),
        HAMMER_ATTACKING(50),
        HEAVY_HAMMER_ATTACKING(60),
        DASHING(40),
        EROFLAMING(120),
        BLASTING(280),
        SHIELDED_WALK(160),
        SHIELDED_STRIKING(60),
        DYING(500),
        SPAWNING(20);
        public final int getAbilityTime;

        WarriorPose(int getAbilityTime) {
            this.getAbilityTime = getAbilityTime;
        }
    }
}