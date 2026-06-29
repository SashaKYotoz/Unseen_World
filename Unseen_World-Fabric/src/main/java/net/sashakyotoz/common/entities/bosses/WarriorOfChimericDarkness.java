package net.sashakyotoz.common.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.config.ChimericDarknessData;
import net.sashakyotoz.common.config.WorldConfigController;
import net.sashakyotoz.common.entities.ai.bosses_goals.WarriorMovementGoal;
import net.sashakyotoz.common.entities.bosses.parts.WarriorPartEntity;

import java.util.List;

public class WarriorOfChimericDarkness extends BossLikePathfinderMob implements MultipartEntity {
    public final AnimationState spawn = new AnimationState();
    public final AnimationState death = new AnimationState();
    public final AnimationState dash = new AnimationState();
    public final AnimationState hammerAttack = new AnimationState();
    public final AnimationState shieldStriking = new AnimationState();
    public final AnimationState heavyHammerAttack = new AnimationState();

    public static final EntityDataSerializer<WarriorPose> WARRIOR_POSE = EntityDataSerializer.simpleEnum(WarriorPose.class);
    protected static final EntityDataAccessor<WarriorPose> PHASE = SynchedEntityData.defineId(WarriorOfChimericDarkness.class, WARRIOR_POSE);

    private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("entity.unseen_world.warrior_of_chimeric_darkness"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_6);

    private final WarriorPartEntity[] parts;
    public final WarriorPartEntity body;
    public final WarriorPartEntity backCrack;

    private BlockPos pos;
    private int timeOfAbility = 0;

    public WarriorOfChimericDarkness(EntityType<? extends BossLikePathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.xpReward = XP_REWARD_BOSS;
        this.pos = this.blockPosition().offset(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3);
        this.setMaxUpStep(1.5f);
        this.setPhase();
        GroundPathNavigation mobNavigation = (GroundPathNavigation) this.getNavigation();
        mobNavigation.setCanFloat(true);
        mobNavigation.setCanWalkOverFences(true);
        this.body = new WarriorPartEntity(this, "body", 0.6F, 0.8F);
        this.backCrack = new WarriorPartEntity(this, "backCrack", 0.6F, 0.8F);
        this.parts = new WarriorPartEntity[]{this.body, this.backCrack};
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, WarriorPose.SPAWNING);
    }

    @Override
    public ServerBossEvent bossInfo() {
        return bossBar;
    }

    @Override
    public void die(DamageSource damageSource) {
        this.deathTime = -20;
        if (this.level() instanceof ServerLevel world) {
            ChimericDarknessData data = WorldConfigController.data.get(0);
            WorldConfigController.saveController(world, true, data.sunUnlock(), data.galacticUnlock());
            WorldConfigController.updateSave(world);
        }
        updateTranslocatone(this.level(), this.blockPosition());
        super.die(damageSource);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime == -2)
            this.setWarriorPose(WarriorPose.DYING);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 350)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 4)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (PHASE.equals(data)) {
            switch (this.getWarriorPose()) {
                case SPAWNING -> {
                    spawn.start(this.tickCount);
                    this.setWarriorPose(WarriorPose.IDLING);
                }
                case DYING -> this.death.start(this.tickCount);
                case DASHING -> {
                    this.dash.start(this.tickCount);
                    this.queueServerWork(5, () -> {
                        this.setDeltaMovement(
                                this.getXVector(2, this.getYRot()),
                                0.5,
                                this.getZVector(2, this.getYRot()));
                        this.queueServerWork(10, () -> {
                            if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 7) {
                                this.playSound(SoundEvents.ANVIL_HIT, 2, 2);
                                this.getTarget().setDeltaMovement(
                                        this.getXVector(-2, this.getYRot()),
                                        0.375,
                                        this.getZVector(-2, this.getYRot()));
                            }
                        });
                    });
                }
                case HAMMER_ATTACKING -> {
                    this.hammerAttack.start(this.tickCount);
                    this.queueServerWork(12, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 5)
                            this.doHurtTarget(this.getTarget());
                        if (this.level() instanceof ServerLevel world)
                            world.sendParticles(ParticleTypes.ASH,
                                    this.getX(), this.getY() + 1.5f, this.getZ(),
                                    12, this.getXVector(1, this.getYRot()), 0.25, this.getZVector(1, this.getYRot()), 1);
                    });
                }
                case HEAVY_HAMMER_ATTACKING -> {
                    this.heavyHammerAttack.start(this.tickCount);
                    this.getJumpControl().jump();
                    this.queueServerWork(18, () -> {
                        this.hitNearbyMobs(10, 7);
                        this.provokeEarthquake(3);
                        this.playSound(SoundEvents.ANVIL_HIT, 2.5f, 2.5f);
                    });
                }
                case SHIELDED_STRIKING -> {
                    this.shieldStriking.start(this.tickCount);
                    this.getNavigation().stop();
                    this.queueServerWork(10, () -> {
                        this.playSound(SoundEvents.ANVIL_FALL, 2.5f, 2.5f);
                        if (this.getTarget() != null) {
                            this.lookAt(this.getTarget(), 45, 45);
                            if (this.level() instanceof ServerLevel world) {
                                for (int i = 0; i < 3; i++)
                                    world.sendParticles(new WindVibrationParticleEffect(new EntityPositionSource(this.getTarget(), 0.1F + (i / 5f)), 20), this.getX(), this.getY() + (i / 5f), this.getZ(), 3, 0, 0, 0, 0);
                            }
                            float scaling = 0;
                            for (int i1 = 0; i1 < 7; i1++) {
                                BlockPos pos = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                                if (!this.level().getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                                    scaling = scaling + 1;
                                BlockPos pos1 = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos1.getCenter(), pos1.getCenter()).inflate(1.5), LivingEntity::isPickable);
                                for (LivingEntity entity : entities) {
                                    if (entity != this) {
                                        entity.hurt(this.damageSources().generic(), 10);
                                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 1));
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }

        super.onSyncedDataUpdated(data);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        float f14 = this.getYHeadRot() * ((float) Math.PI / 180F);
        float f1 = Mth.sin(f14);
        float f15 = Mth.cos(f14);
        Vec3[] vec3ds = new Vec3[this.parts.length];
        for (int s = 0; s < this.parts.length; s++)
            vec3ds[s] = new Vec3(this.parts[s].getX(), this.parts[s].getY(), this.parts[s].getZ());
        this.movePart(this.body, f1 * -1.25F, 1.5D, -f15 * -1.25F);
        this.movePart(this.backCrack, f1 * 1.1F, 1.5D, -f15 * 1.1F);
        for (int ac = 0; ac < this.parts.length; ac++) {
            this.parts[ac].xo = vec3ds[ac].x;
            this.parts[ac].yo = vec3ds[ac].y;
            this.parts[ac].zo = vec3ds[ac].z;
            this.parts[ac].xOld = vec3ds[ac].x;
            this.parts[ac].yOld = vec3ds[ac].y;
            this.parts[ac].zOld = vec3ds[ac].z;
        }
    }

    public WarriorPartEntity[] getBodyParts() {
        return this.parts;
    }

    private void movePart(WarriorPartEntity partEntity, double dx, double dy, double dz) {
        partEntity.setPos(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
    }

    @Override
    public EntityPart[] getParts() {
        return this.parts;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        WarriorPartEntity[] bodyParts = this.getBodyParts();
        for (int i = 0; i < bodyParts.length; i++) {
            bodyParts[i].setId(i + packet.getId());
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new WarriorMovementGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 32));
    }

    @Override
    public boolean canBeHitByProjectile() {
        return super.canBeHitByProjectile() && !this.isInWarriorPose(WarriorPose.SPINNING);
    }

    public void setPhase() {
        switch (this.getWarriorPose()) {
            case IDLING -> this.setWarriorPose(WarriorPose.HAMMER_ATTACKING);
            case HAMMER_ATTACKING -> this.setWarriorPose(WarriorPose.SHIELDED_WALK);
            case SHIELDED_WALK -> this.setWarriorPose(WarriorPose.DASHING);
            case DASHING -> this.setWarriorPose(WarriorPose.BLASTING);
            case BLASTING -> this.setWarriorPose(WarriorPose.HEAVY_HAMMER_ATTACKING);
            case HEAVY_HAMMER_ATTACKING -> this.setWarriorPose(WarriorPose.SPINNING);
            case SPINNING -> this.setWarriorPose(WarriorPose.EROFLAMING);
            case EROFLAMING -> this.setWarriorPose(WarriorPose.SHIELDED_STRIKING);
            case SHIELDED_STRIKING, SPAWNING -> this.setWarriorPose(WarriorPose.IDLING);
        }
        pos = this.blockPosition().offset(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3);
    }

    private void updateTranslocatone(Level world, BlockPos pos) {
        int radius = 9;
        int height = 7;
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos1 = pos.offset(x, y, z);
                    if (world.getBlockState(pos1).is(ModBlocks.GLACIEMITE_TRANSLOCATONE)) {
                        world.setBlockAndUpdate(pos1, world.getBlockState(pos1).setValue(BlockStateProperties.TRIGGERED, false));
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void moveTowardsClosestSpace(double x, double y, double z) {
        if (!this.isInWarriorPose(WarriorPose.BLASTING))
            super.moveTowardsClosestSpace(x, y, z);
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
            if (this.distanceToSqr(this.getTarget()) < 4) {
                if (!this.isInWarriorPose(WarriorPose.HAMMER_ATTACKING))
                    this.getMoveControl().strafe(-1, 0);
            }
            if (this.timeOfAbility <= 0 && !this.isInWarriorPose(WarriorPose.DYING))
                this.setPhase();
            if (this.isInWarriorPose(WarriorPose.BLASTING)) {
                this.navigation.stop();
                if (this.level() instanceof ServerLevel world)
                    world.sendParticles(ParticleTypes.ASH,
                            this.getX() + this.getXVector(1, this.getYRot()),
                            this.getY(),
                            this.getZ() + this.getZVector(1, this.getYRot()),
                            3, 0, 0, 0, 0.5f);
                for (int i = -3; i < 3; i++) {
                    if (i % 2 == 0) {
                        if (this.level() instanceof ServerLevel world)
                            world.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.getX() + i, pos.getY() + 0.25, pos.getZ() + i, 9, 0, 1, 0, 1);
                        if (this.tickCount % 5 == 0) {
                            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos).inflate(2), LivingEntity::isPickable);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.hurt(this.damageSources().dryOut(), 8);
                            }
                        }
                    }
                }
                if (this.tickCount % 60 == 0)
                    this.pos = this.random.nextBoolean() ? this.blockPosition().offset(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3)
                            : this.getTarget().blockPosition().above();
            }
            if (this.distanceToSqr(this.getTarget()) < 9 && this.isInWarriorPose(WarriorPose.SPINNING) && this.tickCount % 15 == 0)
                this.doHurtTarget(this.getTarget());
            if (this.distanceToSqr(this.getTarget()) < 5 && this.isInWarriorPose(WarriorPose.SHIELDED_WALK) && this.tickCount % 5 == 0)
                this.getTarget().setDeltaMovement(
                        this.getXVector(-0.75f, this.getTarget().getYRot()),
                        0.45f,
                        this.getZVector(-0.75f, this.getTarget().getYRot())
                );
            if (this.tickCount % 4 == 0 && this.isInWarriorPose(WarriorPose.EROFLAMING)) {
                Level world = this.level();
                if (world instanceof ServerLevel serverWorld) {
                    Vec3 eyePos = this.getEyePosition();
                    Vec3 lookVec = this.getViewVector(1.0F);
                    Vec3 endPos = eyePos.add(lookVec.scale(5));
                    int particleCount = 20;
                    Vec3 step = endPos.subtract(eyePos).scale(1.0 / particleCount);
                    double radius = 0.5;
                    for (int j = 0; j < particleCount; j++) {
                        Vec3 basePos = eyePos.add(step.scale(j));
                        double angle = this.tickCount % 360 + (j * Math.PI / 10);
                        double offsetX = radius * Math.cos(angle);
                        double offsetY = radius * Math.sin(angle);

                        Vec3 perpendicular = lookVec.cross(new Vec3(0, 1, 0)).normalize();
                        Vec3 particlePos = basePos.add(perpendicular.scale(offsetX)).add(0, offsetY, 0);
                        serverWorld.sendParticles(
                                ParticleTypes.SOUL_FIRE_FLAME,
                                particlePos.x,
                                particlePos.y,
                                particlePos.z,
                                1, 0, 0, 0, 1
                        );
                    }
                }
                BlockPos center = world.clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(this.tickCount % 2 == 0 ? 6 : 2.5f)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(center.getCenter(), center.getCenter()).inflate(3), LivingEntity::isPickable);
                for (LivingEntity entity : entities) {
                    if (entity != this && !entity.fireImmune()) {
                        if (entity instanceof IGrippingEntity entity1)
                            GrippingData.addGrippingSeconds(entity1, 2);
                        entity.hurt(this.damageSources().dryOut(), 4);
                    }
                }
            }
        }
    }

    @Override
    public boolean haveToDropLoot(DamageSource source) {
        return source.getEntity() instanceof Player;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.random.nextFloat() > 0.875 && !this.isInWarriorPose(WarriorPose.SHIELDED_WALK)
                && !this.level().isClientSide() && source.is(DamageTypes.PLAYER_ATTACK))
            this.setWarriorPose(WarriorPose.SHIELDED_WALK);
        if (this.isInWarriorPose(WarriorPose.SHIELDED_WALK)
                || this.isInWarriorPose(WarriorPose.EROFLAMING)
                || this.isInWarriorPose(WarriorPose.BLASTING)) {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.1f, 0.9f);
            return false;
        }
        if (this.getTarget() != null && this.isInWarriorPose(WarriorPose.SHIELDED_WALK))
            this.getTarget().hurt(source, amount / 2f);
        return super.hurt(source, amount);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return Math.round(super.calculateFallDamage(fallDistance, damageMultiplier) / 2f);
    }

    public void setWarriorPose(WarriorPose pose) {
        this.entityData.set(PHASE, pose);
        this.timeOfAbility = pose.getAbilityTime;
    }

    public WarriorPose getWarriorPose() {
        return this.entityData.get(PHASE);
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