package net.sashakyotoz.common.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.config.ChimericDarknessData;
import net.sashakyotoz.common.config.WorldConfigController;
import net.sashakyotoz.common.entities.ai.bosses_goals.SentinelMovementGoal;
import net.sashakyotoz.common.entities.bosses.parts.EclipseSentinelPartEntity;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class EclipseSentinel extends BossLikePathfinderMob implements MultipartEntity {
    public final AnimationState death = new AnimationState();
    public final AnimationState backflip = new AnimationState();
    public final AnimationState sword_swing = new AnimationState();
    public final AnimationState sky_jumping = new AnimationState();
    public final AnimationState darkness = new AnimationState();
    public final AnimationState idling = new AnimationState();
    public final AnimationState beaming = new AnimationState();
    public final AnimationState rush_and_swing = new AnimationState();
    public final AnimationState blasting_eroflame = new AnimationState();
    public final AnimationState exaltation = new AnimationState();
    public final AnimationState exalting = new AnimationState();
    public final AnimationState heavy_swing = new AnimationState();

    private final EclipseSentinelPartEntity[] parts;
    public final EclipseSentinelPartEntity body;
    public final EclipseSentinelPartEntity crack;

    private final ServerBossEvent bossBar = new ServerBossEvent(Component.translatable("entity.unseen_world.eclipse_sentinel"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6);
    public static final EntityDataSerializer<EclipseSentinel.SentinelPose> SENTINEL_POSE = EntityDataSerializer.simpleEnum(EclipseSentinel.SentinelPose.class);
    public static final EntityDataAccessor<EclipseSentinel.SentinelPose> PHASE = SynchedEntityData.defineId(EclipseSentinel.class, SENTINEL_POSE);
    public static final EntityDataAccessor<Boolean> IS_EXALTED = SynchedEntityData.defineId(EclipseSentinel.class, EntityDataSerializers.BOOLEAN);
    private BlockPos pos;
    private int timeOfAbility = 0;

    public EclipseSentinel(EntityType<? extends BossLikePathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.xpReward = XP_REWARD_BOSS;
        this.pos = this.blockPosition().offset(this.random.nextInt(8) - 4, 0, this.random.nextInt(8) - 4);
        this.setMaxUpStep(1.5f);
        GroundPathNavigation mobNavigation = (GroundPathNavigation) this.getNavigation();
        mobNavigation.setCanFloat(true);
        mobNavigation.setCanWalkOverFences(true);
        this.body = new EclipseSentinelPartEntity(this, "body", 0.45F, 0.35F);
        this.crack = new EclipseSentinelPartEntity(this, "cape", 0.45F, 0.35F);
        this.parts = new EclipseSentinelPartEntity[]{this.body, this.crack};
    }

    @Override
    public int getMaxHeadYRot() {
        return this.isInSentinelPose(SentinelPose.BEAMING) ? 10 : super.getMaxHeadYRot();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, SentinelPose.IDLING);
        this.entityData.define(IS_EXALTED, false);
    }

    @Override
    public ServerBossEvent bossInfo() {
        return bossBar;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (PHASE.equals(data)) {
            if (!this.isInSentinelPose(SentinelPose.DARKNESS))
                this.darkness.stop();
            if (!this.isInSentinelPose(SentinelPose.IDLING))
                this.idling.stop();
            if (!this.isInSentinelPose(SentinelPose.BEAMING))
                this.beaming.stop();
            if (!this.isInSentinelPose(SentinelPose.EXALTING))
                this.exalting.stop();
            switch (this.getSentinelPose()) {
                case DYING -> this.death.start(this.tickCount);
                case RUSH_AND_SWING -> {
                    this.exalting.stop();
                    this.navigation.stop();
                    this.rush_and_swing.start(this.tickCount);
                    this.queueServerWork(35, () -> this.setDeltaMovement(
                            this.getXVector(1, this.getYRot()),
                            0.65,
                            this.getZVector(1, this.getYRot())));
                    this.queueServerWork(60, () -> {
                        this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 1.5f, 1);
                        spawnWorldParticle(ModParticleTypes.GRIPPING_CRYSTAL,
                                this.getX() + getXVector(1, this.getYRot()),
                                this.getY() + 1.5f,
                                this.getZ() + getZVector(1, this.getYRot()),
                                7, 0, 0, 0, 1
                        );
                        if (this.getTarget() != null) {
                            if (this.getTarget() instanceof IGrippingEntity entity1)
                                GrippingData.addGrippingSeconds(entity1, 8);
                            if (this.distanceToSqr(this.getTarget()) < 7f) {
                                this.doHurtTarget(this.getTarget());
                                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2, Level.ExplosionInteraction.NONE);
                            }
                        }
                    });
                }
                case IDLING -> {
                    this.idling.startIfStopped(this.tickCount);
                    this.spawnParticle(ParticleTypes.DRAGON_BREATH, this.level(), this.getX(), this.getY() + 1, this.getZ(), 2);
                }
                case SWORD_SWING -> {
                    this.sword_swing.start(this.tickCount);
                    this.queueServerWork(25, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 5)
                            this.doHurtTarget(this.getTarget());
                        this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 1, 1.5f);
                        float scaling = 0;
                        Level world = this.level();
                        BlockPos particlePos = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(12)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                        spawnWorldParticle(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 10), this.getX(), this.getY() + 1, this.getZ(), 3, 0, 0, 0, 1);
                        for (int i1 = 0; i1 < 12; i1++) {
                            BlockPos pos = world.clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                            if (!this.level().getBlockState(pos).canOcclude())
                                scaling = scaling + 1;
                            BlockPos pos1 = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos1.getCenter(), pos1.getCenter()).inflate(1.25), LivingEntity::isPickable);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.hurt(this.damageSources().magic(), 12);
                            }
                        }
                    });
                    this.queueServerWork(45, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 5)
                            this.doHurtTarget(this.getTarget());
                        float scaling = 0;
                        this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 1, 1.5f);
                        Level world = this.level();
                        BlockPos particlePos = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(13)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                        spawnWorldParticle(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 10), this.getX(), this.getY() + 1, this.getZ(), 3, 0, 0, 0, 1);
                        for (int i1 = 0; i1 < 13; i1++) {
                            BlockPos pos = world.clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                            if (!this.level().getBlockState(pos).canOcclude())
                                scaling = scaling + 1;
                            BlockPos pos1 = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos1.getCenter(), pos1.getCenter()).inflate(1.25), LivingEntity::isPickable);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.hurt(this.damageSources().magic(), 12);
                            }
                        }
                    });
                }
                case HEAVY_SWING -> {
                    this.heavy_swing.start(this.tickCount);
                    if (this.getTarget() instanceof IGrippingEntity entity1)
                        GrippingData.addGrippingSeconds(entity1, 4);
                    this.navigation.stop();
                    this.queueServerWork(50, () -> {
                        float scaling = 0;
                        Level world = this.level();
                        BlockPos particlePos = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(11)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                        spawnWorldParticle(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 10), this.getX(), this.getY() + 1, this.getZ(), 3, 0, 0, 0, 1);
                        for (int i1 = 0; i1 < 12; i1++) {
                            BlockPos pos = world.clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                            if (!this.level().getBlockState(pos).canOcclude())
                                scaling = scaling + 1;
                            BlockPos pos1 = this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
                            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos1.getCenter(), pos1.getCenter()).inflate(1.25), LivingEntity::isPickable);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.hurt(this.damageSources().magic(), 10);
                            }
                        }
                    });
                    this.queueServerWork(60, () -> {
                        this.hitNearbyMobs(10, 6);
                        this.spawnParticle(ParticleTypes.END_ROD, this.level(), this.getX(), this.getY() + 0.5f, this.getZ(), 2);
                        this.playSound(SoundEvents.PHANTOM_SWOOP, 2, 2.5f);
                    });
                }
                case BACKFLIP -> {
                    this.backflip.start(this.tickCount);
                    this.queueServerWork(10, () -> {
                        this.setDeltaMovement(this.getXVector(-1.5, this.getYRot()),
                                0.75,
                                this.getZVector(-1.5, this.getYRot()));
                        this.setPhase();
                    });
                }
                case DARKNESS -> {
                    this.darkness.startIfStopped(this.tickCount);
                    pos = this.blockPosition().offset(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3);
                    AreaEffectCloud areaEffectCloudEntity = getAreaEffectCloudEntity();
                    areaEffectCloudEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 70, 1));
                    areaEffectCloudEntity.setPos(this.getX(), this.getY() + 0.5f, this.getZ());
                    this.level().levelEvent(LevelEvent.PARTICLES_DRAGON_FIREBALL_SPLASH, this.blockPosition(), this.isSilent() ? -1 : 1);
                    this.level().addFreshEntity(areaEffectCloudEntity);
                }
                case BEAMING -> {
                    this.beaming.startIfStopped(this.tickCount);
                    this.getNavigation().stop();
                }
                case BLASTING_EROFLAME -> {
                    this.blasting_eroflame.start(this.tickCount);
                    this.spawnParticle(ParticleTypes.SQUID_INK, this.level(), this.getX(), this.getEyeY(), this.getZ(), 1.5f);
                    this.queueServerWork(15, () -> {
                        ItemStack stack = getFirework();
                        Projectile projectile = new FireworkRocketEntity(this.level(), stack, this, this.getX(), this.getEyeY() - 0.15F, this.getZ(), true);
                        Vec3 vec31 = this.getUpVector(1.0F);
                        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, vec31.x, vec31.y, vec31.z);
                        Vec3 vec3 = this.getViewVector(1.0F);
                        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 3.15F, 1);
                        this.level().addFreshEntity(projectile);
                    });
                }
                case EXALTATION -> {
                    this.exaltation.start(this.tickCount);
                    if (!this.isExalted())
                        this.queueServerWork(80, () -> {
                            this.exaltation.stop();
                            this.playSound(SoundEvents.WARDEN_ROAR, 2, 2f);
                            this.setExaltation(true);
                            this.setSentinelPose(SentinelPose.EXALTING);
                        });
                }
                case EXALTING -> this.exalting.startIfStopped(this.tickCount);
                case SKY_JUMPING -> {
                    this.sky_jumping.start(this.tickCount);
                    this.navigation.stop();
                    this.spawnParticle(ParticleTypes.SQUID_INK, this.level(), this.getX(), this.getY() + 1, this.getZ(), 1.5f);
                    this.setDeltaMovement(0, 1, 0);
                    this.queueServerWork(15, () -> {
                        if (this.getTarget() != null) {
                            Vec3 vec3d = new Vec3(
                                    (this.getX() - this.getTarget().getX()) * 0.1,
                                    0.5,
                                    (this.getZ() - this.getTarget().getZ()) * 0.1
                            );
                            vec3d.normalize();
                            this.setDeltaMovement(vec3d);
                        }
                    });
                    this.queueServerWork(30, () -> {
                        if (this.getTarget() != null) {
                            this.teleportToWithTicket(this.getTarget().getX(), this.getTarget().getY() + 2, this.getTarget().getZ());
                            this.getTarget().hurt(this.damageSources().magic(), 6);
                            this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 1, 2);
                            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2, Level.ExplosionInteraction.NONE);
                            setPhase();
                        }
                    });
                }
            }
        }
        super.onSyncedDataUpdated(data);
    }

    private @NotNull AreaEffectCloud getAreaEffectCloudEntity() {
        AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(this.level(), this.getX(), this.getY() + 1f, this.getZ());
        areaEffectCloudEntity.setOwner(this);
        areaEffectCloudEntity.setParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR);
        areaEffectCloudEntity.setRadius(4.0F);
        areaEffectCloudEntity.setDuration(120);
        areaEffectCloudEntity.setRadiusPerTick((7.0F - areaEffectCloudEntity.getRadius()) / (float) areaEffectCloudEntity.getDuration());
        return areaEffectCloudEntity;
    }

    private ItemStack getFirework() {
        ItemStack stack = Items.FIREWORK_ROCKET.getDefaultInstance();
        CompoundTag tag = stack.getOrCreateTagElement("Fireworks");
        ListTag list = new ListTag();
        CompoundTag explosion = new CompoundTag();
        explosion.putByte("Type", (byte) 1);
        explosion.putByte("Trail", (byte) 1);
        IntArrayTag colors = new IntArrayTag(List.of(
                2651799,
                11250603,
                6719955,
                15790320
        ));
        explosion.put("Colors", colors);
        IntArrayTag fadecolors = new IntArrayTag(List.of(
                8073150,
                11250603,
                12801229
        ));
        explosion.put("FadeColors", fadecolors);
        list.add(explosion);
        tag.put("Explosions", list);
        tag.putByte("Flight", (byte) 4);
        return stack;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new SentinelMovementGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 32, 1));
    }

    public void setPhase() {
        if (!this.isExalted()) {
            switch (this.getSentinelPose()) {
                case IDLING -> this.setSentinelPose(SentinelPose.RUSH_AND_SWING);
                case RUSH_AND_SWING -> this.setSentinelPose(SentinelPose.BACKFLIP);
                case BACKFLIP -> this.setSentinelPose(SentinelPose.BEAMING);
                case BEAMING -> this.setSentinelPose(SentinelPose.DARKNESS);
                case DARKNESS -> this.setSentinelPose(SentinelPose.SKY_JUMPING);
                case SKY_JUMPING -> this.setSentinelPose(SentinelPose.SWORD_SWING);
                case SWORD_SWING -> this.setSentinelPose(SentinelPose.BLASTING_EROFLAME);
                case BLASTING_EROFLAME -> this.setSentinelPose(SentinelPose.HARD_RUSH);
                default -> this.setSentinelPose(SentinelPose.IDLING);
            }
        } else {
            switch (this.getSentinelPose()) {
                case EXALTING -> this.setSentinelPose(SentinelPose.RUSH_AND_SWING);
                case RUSH_AND_SWING -> this.setSentinelPose(SentinelPose.BACKFLIP);
                case BACKFLIP -> this.setSentinelPose(SentinelPose.BEAMING);
                case BEAMING -> this.setSentinelPose(SentinelPose.DARKNESS);
                case DARKNESS -> this.setSentinelPose(SentinelPose.SKY_JUMPING);
                case SKY_JUMPING -> this.setSentinelPose(SentinelPose.HEAVY_SWING);
                case HEAVY_SWING -> this.setSentinelPose(SentinelPose.BLASTING_EROFLAME);
                case BLASTING_EROFLAME -> this.setSentinelPose(SentinelPose.HARD_RUSH);
                default -> this.setSentinelPose(SentinelPose.EXALTING);
            }
        }
    }

    @Override
    public boolean haveToDropLoot(DamageSource source) {
        return source.getEntity() instanceof Player;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isDeadOrDying() && !this.isInSentinelPose(SentinelPose.DYING))
            this.setSentinelPose(SentinelPose.DYING);
        if (this.isInSentinelPose(SentinelPose.IDLING) && !this.idling.isStarted())
            this.idling.start(this.tickCount);
        if (this.timeOfAbility > 0)
            this.timeOfAbility--;
        if (this.getTarget() != null) {
            if (this.getTarget().distanceToSqr(this) > 784 && this.isInSentinelPose(SentinelPose.BEAMING))
                this.setSentinelPose(SentinelPose.HARD_RUSH);
            if (this.tickCount % ((this.getHealth() < this.getMaxHealth() / 2f) ? 20 : 40) == 0)
                this.pos = this.random.nextBoolean() ? this.blockPosition().offset(this.random.nextInt(8) - 4, 0, this.random.nextInt(8) - 4)
                        : this.getTarget().blockPosition().above();
            if (this.timeOfAbility <= 0 && !this.isInSentinelPose(SentinelPose.DYING))
                this.setPhase();
            if (this.isInSentinelPose(SentinelPose.IDLING))
                this.navigation.stop();
            if (this.isInSentinelPose(SentinelPose.DARKNESS)) {
                this.navigation.stop();
                if (this.tickCount % 20 == 0) {
                    this.playSound(SoundEvents.ILLUSIONER_CAST_SPELL, 2, 1);
                    for (int i = -3; i < 3; i++) {
                        if (i % 2 == 0) {
                            spawnWorldParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OBSIDIAN.defaultBlockState()), pos.getX() + i, pos.getY() + 0.25, pos.getZ() + i, 9, 0, 1, 0, 1);
                            if (this.tickCount % 5 == 0) {
                                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos).inflate(1), LivingEntity::isPickable);
                                for (LivingEntity entity : entities) {
                                    if (entity != this)
                                        entity.hurt(this.damageSources().magic(), 8);
                                }
                            }
                        }
                    }
                    this.heal(6);
                }
            }
            if (this.isInSentinelPose(SentinelPose.HARD_RUSH) && this.tickCount % 10 == 0) {
                spawnWorldParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OBSIDIAN.defaultBlockState()),
                        this.getX() + this.getXVector(1, this.getYRot()), this.getY() + 1, this.getZ() + this.getZVector(1, this.getYRot()),
                        9, 0, 1, 0, 1);
                this.hitNearbyMobs(4, 2);
                this.playSound(SoundEvents.DEEPSLATE_FALL, 3, 2.5f);
            }
            if (this.isInSentinelPose(SentinelPose.BEAMING) && this.tickCount % 5 == 0) {
                ActionsUtils.rayCastAlong(this.level(), this, 16, (world, pos1) ->
                        world.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getCenter(), pos.getCenter()).inflate(0.75),
                                LivingEntity::isPickable).forEach(entity -> {
                            if (entity != this)
                                entity.hurt(this.damageSources().magic(), 6);
                        }));
            }
            if (this.isInSentinelPose(SentinelPose.EXALTING)) {
                if (this.tickCount % 4 == 0 && isSolidBlockBelow()) {
                    this.push(0, 0.5f, 0);
                    markHurt();
                }
                if (this.tickCount % 30 == 0) {
                    Vec3 vec3d3 = this.getViewVector(1.0F);
                    double l = this.getEyePosition(1).x() - vec3d3.x;
                    double m = this.getY(0.75) + 0.4;
                    double n = this.getEyePosition(1).z() - vec3d3.z;
                    double o = this.getTarget().getX() - l;
                    double p = this.getTarget().getY(0.5) - m;
                    double q = this.getTarget().getZ() - n;
                    DragonFireball dragonFireballEntity = new DragonFireball(this.level(), this, o, p, q);
                    dragonFireballEntity.moveTo(l, m, n, 0.0F, 0.0F);
                    this.level().addFreshEntity(dragonFireballEntity);
                }
            }
        }
    }

    private void spawnWorldParticle(ParticleOptions type, double x, double y, double z, int count, double xV, double yV, double zV, float speed) {
        if (this.level() instanceof ServerLevel world)
            world.sendParticles(type, x, y, z, count, xV, yV, zV, speed);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        float f14 = this.getYHeadRot() * ((float) Math.PI / 180F);
        float f1 = Mth.sin(f14);
        float f15 = Mth.cos(f14);
        Vec3[] vec3ds = new Vec3[this.parts.length];
        for (int s = 0; s < this.parts.length; s++) {
            vec3ds[s] = new Vec3(this.parts[s].getX(), this.parts[s].getY(), this.parts[s].getZ());
        }

        this.movePart(this.body, f1 * -0.75F, 1.65D, -f15 * -0.75F);
        this.movePart(this.crack, f1 * 0.75F, 1.5D, -f15 * 0.75F);
        for (int ac = 0; ac < this.parts.length; ac++) {
            this.parts[ac].xo = vec3ds[ac].x;
            this.parts[ac].yo = vec3ds[ac].y;
            this.parts[ac].zo = vec3ds[ac].z;
            this.parts[ac].xOld = vec3ds[ac].x;
            this.parts[ac].yOld = vec3ds[ac].y;
            this.parts[ac].zOld = vec3ds[ac].z;
        }
    }

    public EclipseSentinelPartEntity[] getBodyParts() {
        return this.parts;
    }

    private void movePart(EclipseSentinelPartEntity partEntity, double dx, double dy, double dz) {
        partEntity.setPos(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
    }

    private boolean isSolidBlockBelow() {
        for (int i = 0; i < 5; i++) {
            if (this.level().getBlockState(this.blockPosition().below(i)).canOcclude())
                return true;
        }
        return false;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        EclipseSentinelPartEntity[] bodyParts = this.getBodyParts();
        for (int i = 0; i < bodyParts.length; i++) {
            bodyParts[i].setId(i + packet.getId());
        }
    }

    @Override
    public void push(Entity entity) {
        if (!(this.isInSentinelPose(SentinelPose.IDLING)
                || this.isInSentinelPose(SentinelPose.EXALTATION)
                || this.isInSentinelPose(SentinelPose.EXALTING)
                || this.isInSentinelPose(SentinelPose.DYING)))
            super.push(entity);
    }

    @Override
    protected void doPush(Entity entity) {
        if (!(this.isInSentinelPose(SentinelPose.IDLING)
                || this.isInSentinelPose(SentinelPose.EXALTATION)
                || this.isInSentinelPose(SentinelPose.EXALTING)
                || this.isInSentinelPose(SentinelPose.DYING)))
            super.doPush(entity);
    }

    @Override
    protected void moveTowardsClosestSpace(double x, double y, double z) {
        if (!(this.isInSentinelPose(SentinelPose.IDLING)
                || this.isInSentinelPose(SentinelPose.EXALTATION)
                || this.isInSentinelPose(SentinelPose.EXALTING)
                || this.isInSentinelPose(SentinelPose.DYING)))
            super.moveTowardsClosestSpace(x, y, z);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return this.isExalted() ? 0 : Math.round(super.calculateFallDamage(fallDistance, damageMultiplier) / 2f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("exaltingStatus", this.isExalted());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setExaltation(nbt.getBoolean("exaltingStatus"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.EXPLOSION))
            return false;
        if (source.is(DamageTypes.ARROW))
            return false;
        if (source.is(DamageTypes.CACTUS))
            return false;
        if (source.is(DamageTypes.INDIRECT_MAGIC))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (!this.isExalted() && this.getHealth() < this.getMaxHealth() / 2f && this.isInSentinelPose(SentinelPose.IDLING))
            this.setSentinelPose(SentinelPose.EXALTATION);
        if (!this.isExalted() && this.getHealth() < this.getMaxHealth() / 2f && !this.isInSentinelPose(SentinelPose.IDLING))
            return false;
        if (isExalted())
            return super.hurt(source, amount / 2f);
        return super.hurt(source, amount);
    }

    @Override
    public boolean isNoGravity() {
        return this.isInSentinelPose(SentinelPose.SKY_JUMPING);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 400)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 4)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public void die(DamageSource damageSource) {
        this.deathTime = -30;
        if (this.level() instanceof ServerLevel world) {
            ChimericDarknessData data = WorldConfigController.data.get(0);
            WorldConfigController.saveController(world, data.starsUnlock(), true, data.galacticUnlock());
            WorldConfigController.updateSave(world);
        }
        updateTranslocatone(this.level(), this.blockPosition());
        super.die(damageSource);
    }

    private void updateTranslocatone(Level world, BlockPos pos) {
        int radius = 21;
        int height = 6;
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
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime == -15)
            this.setSentinelPose(SentinelPose.DYING);
    }

    public void setSentinelPose(EclipseSentinel.SentinelPose pose) {
        this.entityData.set(PHASE, pose);
        this.timeOfAbility = pose.getAbilityTime;
    }

    public EclipseSentinel.SentinelPose getSentinelPose() {
        return this.entityData.get(PHASE);
    }

    public void setExaltation(boolean status) {
        this.entityData.set(IS_EXALTED, status);
    }

    public boolean isExalted() {
        return this.entityData.get(IS_EXALTED);
    }

    public boolean isInSentinelPose(EclipseSentinel.SentinelPose pose) {
        return this.getSentinelPose() == pose;
    }

    @Override
    public EntityPart[] getParts() {
        return this.parts;
    }

    public enum SentinelPose {
        IDLING(100),
        DARKNESS(120),
        BACKFLIP(60),
        SWORD_SWING(80),
        HEAVY_SWING(70),
        HARD_RUSH(80),
        BLASTING_EROFLAME(50),
        RUSH_AND_SWING(60),
        SKY_JUMPING(80),
        BEAMING(160),
        EXALTATION(80),
        EXALTING(200),
        DYING(300);
        public final int getAbilityTime;

        SentinelPose(int abilityTime) {
            this.getAbilityTime = abilityTime;
        }
    }
}