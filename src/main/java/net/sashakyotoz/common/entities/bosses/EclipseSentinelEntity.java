package net.sashakyotoz.common.entities.bosses;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
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
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.BlockPositionSource;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ai.bosses_goals.SentinelMovementGoal;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.ChimericDarknessData;
import net.sashakyotoz.utils.IEntityDataSaver;
import net.sashakyotoz.utils.JsonWorldController;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class EclipseSentinelEntity extends BossLikePathfinderMob {
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

    private final ServerBossBar bossBar = new ServerBossBar(Text.translatable("entity.unseen_world.eclipse_sentinel"), BossBar.Color.BLUE, BossBar.Style.NOTCHED_6);
    public static final TrackedDataHandler<EclipseSentinelEntity.SentinelPose> SENTINEL_POSE = TrackedDataHandler.ofEnum(EclipseSentinelEntity.SentinelPose.class);
    public static final TrackedData<EclipseSentinelEntity.SentinelPose> PHASE = DataTracker.registerData(EclipseSentinelEntity.class, SENTINEL_POSE);
    public static final TrackedData<Boolean> IS_EXALTED = DataTracker.registerData(EclipseSentinelEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private BlockPos pos;
    private int timeOfAbility = 0;

    public EclipseSentinelEntity(EntityType<? extends BossLikePathfinderMob> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = WITHER_XP;
        this.pos = this.getBlockPos().add(this.random.nextInt(8) - 4, 0, this.random.nextInt(8) - 4);
        this.setStepHeight(1.5f);
        this.setPhase();
        MobNavigation mobNavigation = (MobNavigation) this.getNavigation();
        mobNavigation.setCanSwim(true);
        mobNavigation.setCanWalkOverFences(true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PHASE, SentinelPose.IDLING);
        this.dataTracker.startTracking(IS_EXALTED, false);
    }

    @Override
    public ServerBossBar bossInfo() {
        return bossBar;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
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
                case DYING -> this.death.start(this.age);
                case RUSH_AND_SWING -> {
                    this.exalting.stop();
                    this.rush_and_swing.start(this.age);
                    this.queueServerWork(15, () -> this.setVelocity(
                            this.getXVector(1, this.getYaw()),
                            0.65,
                            this.getZVector(1, this.getYaw())));
                    this.queueServerWork(18, () -> {
                        this.playSound(SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.5f, 1);
                        if (this.getTarget() != null && this.squaredDistanceTo(this.getTarget()) < 7f) {
                            this.tryAttack(this.getTarget());
                            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 2, World.ExplosionSourceType.NONE);
                        }
                    });
                }
                case IDLING -> {
                    this.idling.startIfNotRunning(this.age);
                    this.spawnParticle(ParticleTypes.DRAGON_BREATH, this.getWorld(), this.getX(), this.getY() + 1, this.getZ(), 2);
                }
                case SWORD_SWING -> {
                    this.sword_swing.start(this.age);
                    this.queueServerWork(25, () -> {
                        if (this.getTarget() != null && this.getTarget().squaredDistanceTo(this) < 5)
                            this.tryAttack(this.getTarget());
                        float scaling = 0;
                        World world = this.getWorld();
                        BlockPos particlePos = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(10)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                        if (world instanceof ServerWorld serverWorld)
                            serverWorld.spawnParticles(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 10), this.getX(), this.getY() + 1, this.getZ(), 3, 0, 0, 0, 1);
                        for (int i1 = 0; i1 < 10; i1++) {
                            BlockPos pos = world.raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            if (!this.getWorld().getBlockState(pos).isOpaque())
                                scaling = scaling + 1;
                            BlockPos pos1 = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(1.25), LivingEntity::canHit);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.damage(this.getDamageSources().magic(), 10);
                            }
                        }
                    });
                    this.queueServerWork(35, () -> {
                        if (this.getTarget() != null && this.getTarget().squaredDistanceTo(this) < 5)
                            this.tryAttack(this.getTarget());
                        float scaling = 0;
                        World world = this.getWorld();
                        BlockPos particlePos = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(11)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                        if (world instanceof ServerWorld serverWorld)
                            serverWorld.spawnParticles(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 10), this.getX(), this.getY() + 1, this.getZ(), 3, 0, 0, 0, 1);
                        for (int i1 = 0; i1 < 11; i1++) {
                            BlockPos pos = world.raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            if (!this.getWorld().getBlockState(pos).isOpaque())
                                scaling = scaling + 1;
                            BlockPos pos1 = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(1.25), LivingEntity::canHit);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.damage(this.getDamageSources().magic(), 10);
                            }
                        }
                    });
                    this.queueServerWork(45, () -> {
                        if (this.getTarget() != null && this.getTarget().squaredDistanceTo(this) < 5)
                            this.tryAttack(this.getTarget());
                        float scaling = 0;
                        World world = this.getWorld();
                        BlockPos particlePos = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(12)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                        if (world instanceof ServerWorld serverWorld)
                            serverWorld.spawnParticles(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 10), this.getX(), this.getY() + 1, this.getZ(), 3, 0, 0, 0, 1);
                        for (int i1 = 0; i1 < 12; i1++) {
                            BlockPos pos = world.raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            if (!this.getWorld().getBlockState(pos).isOpaque())
                                scaling = scaling + 1;
                            BlockPos pos1 = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(1.25), LivingEntity::canHit);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.damage(this.getDamageSources().magic(), 10);
                            }
                        }
                    });
                }
                case HEAVY_SWING -> {
                    this.heavy_swing.start(this.age);
                    if (this.getTarget() instanceof ServerPlayerEntity player)
                        GrippingData.addGrippingSeconds((IEntityDataSaver) player, 5);
                    this.navigation.stop();
                    this.queueServerWork(50, () -> {
                        float scaling = 0;
                        World world = this.getWorld();
                        BlockPos particlePos = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(11)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                        if (world instanceof ServerWorld serverWorld)
                            serverWorld.spawnParticles(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 10), this.getX(), this.getY() + 1, this.getZ(), 3, 0, 0, 0, 1);
                        for (int i1 = 0; i1 < 12; i1++) {
                            BlockPos pos = world.raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            if (!this.getWorld().getBlockState(pos).isOpaque())
                                scaling = scaling + 1;
                            BlockPos pos1 = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                            List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(1.25), LivingEntity::canHit);
                            for (LivingEntity entity : entities) {
                                if (entity != this)
                                    entity.damage(this.getDamageSources().magic(), 10);
                            }
                        }
                    });
                    this.queueServerWork(60, () -> {
                        this.hitNearbyMobs(10, 6);
                        this.spawnParticle(ParticleTypes.END_ROD, this.getWorld(), this.getX(), this.getY() + 0.5f, this.getZ(), 2);
                        this.playSound(SoundEvents.ENTITY_PHANTOM_SWOOP, 2, 2.5f);
                    });
                }
                case BACKFLIP -> {
                    this.backflip.start(this.age);
                    this.queueServerWork(10, () -> {
                        this.setVelocity(this.getXVector(-1.5, this.getYaw()),
                                0.75,
                                this.getZVector(-1.5, this.getYaw()));
                        this.setPhase();
                    });
                }
                case DARKNESS -> {
                    this.darkness.startIfNotRunning(this.age);
                    pos = this.getBlockPos().add(this.random.nextInt(7) - 3, 0, this.random.nextInt(7) - 3);
                    List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(4.0, 2.0, 4.0));
                    AreaEffectCloudEntity areaEffectCloudEntity = getAreaEffectCloudEntity();
                    areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 70, 1));
                    if (!list.isEmpty()) {
                        for (LivingEntity livingEntity : list) {
                            double d = this.squaredDistanceTo(livingEntity);
                            if (d < 16.0) {
                                areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                                break;
                            }
                        }
                    }
                    this.getWorld().syncWorldEvent(WorldEvents.DRAGON_BREATH_CLOUD_SPAWNS, this.getBlockPos(), this.isSilent() ? -1 : 1);
                    this.getWorld().spawnEntity(areaEffectCloudEntity);
                }
                case BEAMING -> {
                    this.beaming.startIfNotRunning(this.age);
                    this.getNavigation().stop();
                }
                case BLASTING_EROFLAME -> {
                    this.blasting_eroflame.start(this.age);
                    this.spawnParticle(ParticleTypes.SQUID_INK, this.getWorld(), this.getX(), this.getEyeY(), this.getZ(), 1.5f);
                    this.queueServerWork(15, () -> {
                        ItemStack stack = getFirework();
                        ProjectileEntity projectile = new FireworkRocketEntity(this.getWorld(), stack, this, this.getX(), this.getEyeY() - 0.15F, this.getZ(), true);
                        Vec3d vec31 = this.getOppositeRotationVector(1.0F);
                        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, vec31.x, vec31.y, vec31.z);
                        Vec3d vec3 = this.getRotationVec(1.0F);
                        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                        projectile.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), 3.15F, 1);
                        this.getWorld().spawnEntity(projectile);
                    });
                }
                case EXALTATION -> {
                    this.exaltation.start(this.age);
                    if (!this.isExalted())
                        this.queueServerWork(80, () -> {
                            this.exaltation.stop();
                            this.playSound(SoundEvents.ENTITY_WARDEN_ROAR, 2, 2f);
                            this.setExaltation(true);
                            this.setSentinelPose(SentinelPose.EXALTING);
                        });
                }
                case EXALTING -> this.exalting.startIfNotRunning(this.age);
                case SKY_JUMPING -> {
                    this.sky_jumping.start(this.age);
                    this.navigation.stop();
                    this.spawnParticle(ParticleTypes.SQUID_INK, this.getWorld(), this.getX(), this.getY() + 1, this.getZ(), 1.5f);
                    this.setVelocity(0, 1, 0);

                    this.queueServerWork(15, () -> {
                        if (this.getTarget() != null) {
                            Vec3d vec3d = new Vec3d(
                                    (this.getX() - this.getTarget().getX()) * 0.1,
                                    0.5,
                                    (this.getZ() - this.getTarget().getZ()) * 0.1
                            );
                            vec3d.normalize();
                            this.setVelocity(vec3d);
                        }
                    });
                    this.queueServerWork(25, () -> {
                        if (this.getTarget() != null) {
                            this.teleport(this.getTarget().getX(), this.getTarget().getY() + 2, this.getTarget().getZ());
                            this.getTarget().damage(this.getDamageSources().magic(), 6);
                            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 2, World.ExplosionSourceType.NONE);
                            setPhase();
                        }
                    });
                }
            }
        }
        super.onTrackedDataSet(data);
    }

    private @NotNull AreaEffectCloudEntity getAreaEffectCloudEntity() {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY() + 1f, this.getZ());
        areaEffectCloudEntity.setOwner(this);
        areaEffectCloudEntity.setParticleType(ParticleTypes.FALLING_OBSIDIAN_TEAR);
        areaEffectCloudEntity.setRadius(4.0F);
        areaEffectCloudEntity.setDuration(120);
        areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float) areaEffectCloudEntity.getDuration());
        return areaEffectCloudEntity;
    }

    private ItemStack getFirework() {
        ItemStack stack = Items.FIREWORK_ROCKET.getDefaultStack();
        NbtCompound tag = stack.getOrCreateSubNbt("Fireworks");
        NbtList list = new NbtList();
        NbtCompound explosion = new NbtCompound();
        explosion.putByte("Type", (byte) 1);
        explosion.putByte("Trail", (byte) 1);
        NbtIntArray colors = new NbtIntArray(List.of(
                2651799,
                11250603,
                6719955,
                15790320
        ));
        explosion.put("Colors", colors);
        NbtIntArray fadecolors = new NbtIntArray(List.of(
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
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new SentinelMovementGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 32, 1));
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
    public void tick() {
        super.tick();
        if (this.isDead() && !this.isInSentinelPose(SentinelPose.DYING))
            this.setSentinelPose(SentinelPose.DYING);
        if (this.isInSentinelPose(SentinelPose.IDLING) && !this.idling.isRunning())
            this.idling.start(this.age);
        if (this.timeOfAbility > 0)
            this.timeOfAbility--;
        if (this.getTarget() != null) {
            if (this.age % ((this.getHealth() < this.getMaxHealth() / 2f) ? 20 : 40) == 0)
                this.pos = this.random.nextBoolean() ? this.getBlockPos().add(this.random.nextInt(8) - 4, 0, this.random.nextInt(8) - 4)
                        : this.getTarget().getBlockPos().up();
            if (this.timeOfAbility <= 0 && !this.isInSentinelPose(SentinelPose.DYING))
                this.setPhase();
            if (this.isInSentinelPose(SentinelPose.IDLING))
                this.navigation.stop();
            if (this.isInSentinelPose(SentinelPose.DARKNESS)) {
                this.navigation.stop();
                if (this.age % 20 == 0) {
                    this.playSound(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 2, 1);
                    for (int i = -3; i < 3; i++) {
                        if (i % 2 == 0) {
                            if (this.getWorld() instanceof ServerWorld world)
                                world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.OBSIDIAN.getDefaultState()), pos.getX() + i, pos.getY() + 0.25, pos.getZ() + i, 9, 0, 1, 0, 1);
                            if (this.age % 5 == 0) {
                                List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos, pos).expand(1), LivingEntity::canHit);
                                for (LivingEntity entity : entities) {
                                    if (entity != this)
                                        entity.damage(this.getDamageSources().magic(), 8);
                                }
                            }
                        }
                    }
                    this.heal(6);
                }
            }
            if (this.isInSentinelPose(SentinelPose.HARD_RUSH) && this.age % 10 == 0) {
                if (this.getWorld() instanceof ServerWorld world)
                    world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.OBSIDIAN.getDefaultState()),
                            this.getX() + this.getXVector(1, this.getYaw()), this.getY() + 1, this.getZ() + this.getZVector(1, this.getYaw()),
                            9, 0, 1, 0, 1);
                this.hitNearbyMobs(4, 2);
                this.playSound(SoundEvents.BLOCK_DEEPSLATE_FALL, 3, 2.5f);
            }
            if (this.isInSentinelPose(SentinelPose.BEAMING) && this.age % 5 == 0) {
                float scaling = 0;
                World world = this.getWorld();
                for (int i1 = 0; i1 < 16; i1++) {
                    BlockPos pos = world.raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                    if (!this.getWorld().getBlockState(pos).isOpaque() || this.getWorld().getBlockState(pos).getBlock().getTranslationKey().contains("glass"))
                        scaling = scaling + 1;
                    BlockPos pos1 = this.getWorld().raycast(new RaycastContext(this.getEyePos(), this.getEyePos().add(this.getRotationVec(1f).multiply(scaling)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this)).getBlockPos();
                    List<LivingEntity> entities = this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos1.toCenterPos(), pos1.toCenterPos()).expand(0.675), LivingEntity::canHit);
                    for (LivingEntity entity : entities) {
                        if (entity != this)
                            entity.damage(this.getDamageSources().magic(), 6);
                    }
                }
            }
            if (this.isInSentinelPose(SentinelPose.EXALTING)) {
                if (this.age % 4 == 0 && isSolidBlockBelow()) {
                    this.addVelocity(0, 0.55f, 0);
                    scheduleVelocityUpdate();
                }
                if (this.age % 30 == 0) {
                    Vec3d vec3d3 = this.getRotationVec(1.0F);
                    double l = this.getCameraPosVec(1).getX() - vec3d3.x;
                    double m = this.getBodyY(0.75) + 0.5;
                    double n = this.getCameraPosVec(1).getZ() - vec3d3.z;
                    double o = this.getTarget().getX() - l;
                    double p = this.getTarget().getBodyY(0.5) - m;
                    double q = this.getTarget().getZ() - n;
                    DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(this.getWorld(), this, o, p, q);
                    dragonFireballEntity.refreshPositionAndAngles(l, m, n, 0.0F, 0.0F);
                    this.getWorld().spawnEntity(dragonFireballEntity);
                }
            }
        }
    }

    private boolean isSolidBlockBelow() {
        for (int i = 0; i < 4; i++) {
            if (this.getWorld().getBlockState(this.getBlockPos().down(i)).isOpaque())
                return true;
        }
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (!(this.isInSentinelPose(SentinelPose.IDLING)
                || this.isInSentinelPose(SentinelPose.EXALTATION)
                || this.isInSentinelPose(SentinelPose.EXALTING)
                || this.isInSentinelPose(SentinelPose.DYING)))
            super.pushAwayFrom(entity);
    }

    @Override
    protected void pushAway(Entity entity) {
        if (!(this.isInSentinelPose(SentinelPose.IDLING)
                || this.isInSentinelPose(SentinelPose.EXALTATION)
                || this.isInSentinelPose(SentinelPose.EXALTING)
                || this.isInSentinelPose(SentinelPose.DYING)))
            super.pushAway(entity);
    }

    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {
        if (!(this.isInSentinelPose(SentinelPose.IDLING)
                || this.isInSentinelPose(SentinelPose.EXALTATION)
                || this.isInSentinelPose(SentinelPose.EXALTING)
                || this.isInSentinelPose(SentinelPose.DYING)))
            super.pushOutOfBlocks(x, y, z);
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return this.isExalted() ? 0 : Math.round(super.computeFallDamage(fallDistance, damageMultiplier) / 2f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("exaltingStatus", this.isExalted());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setExaltation(nbt.getBoolean("exaltingStatus"));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.EXPLOSION))
            return false;
        if (source.isOf(DamageTypes.ARROW))
            return false;
        if (source.isOf(DamageTypes.CACTUS))
            return false;
        if (source.isOf(DamageTypes.INDIRECT_MAGIC))
            return false;
        if (source.isOf(DamageTypes.DRAGON_BREATH))
            return false;
        if (!this.isExalted() && this.getHealth() < this.getMaxHealth() / 2f && this.isInSentinelPose(SentinelPose.IDLING))
            this.setSentinelPose(SentinelPose.EXALTATION);
        if (!this.isExalted() && this.getHealth() < this.getMaxHealth() / 2f && !this.isInSentinelPose(SentinelPose.IDLING))
            return false;
        if (isExalted())
            return super.damage(source, amount / 2f);
        return super.damage(source, amount);
    }

    @Override
    public boolean hasNoGravity() {
        return this.isInSentinelPose(SentinelPose.SKY_JUMPING);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 400)
                .add(EntityAttributes.GENERIC_ARMOR, 10)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 4)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.deathTime = -30;
        if (this.getWorld() instanceof ServerWorld world) {
            ChimericDarknessData data = JsonWorldController.data.get(0);
            JsonWorldController.saveController(world, data.starsUnlock(), true, data.galacticUnlock());
            JsonWorldController.updateSave(world);
        }
        updateTranslocatone(this.getWorld(), this.getBlockPos());
        super.onDeath(damageSource);
    }

    private void updateTranslocatone(World world, BlockPos pos) {
        int radius = 21;
        int height = 6;
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
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (this.deathTime == -15)
            this.setSentinelPose(SentinelPose.DYING);
    }

    public void setSentinelPose(EclipseSentinelEntity.SentinelPose pose) {
        this.dataTracker.set(PHASE, pose);
        this.timeOfAbility = pose.getAbilityTime;
    }

    public EclipseSentinelEntity.SentinelPose getSentinelPose() {
        return this.dataTracker.get(PHASE);
    }

    public void setExaltation(boolean status) {
        this.dataTracker.set(IS_EXALTED, status);
    }

    public boolean isExalted() {
        return this.dataTracker.get(IS_EXALTED);
    }

    public boolean isInSentinelPose(EclipseSentinelEntity.SentinelPose pose) {
        return this.getSentinelPose() == pose;
    }

    public enum SentinelPose {
        IDLING(100),
        DARKNESS(120),
        BACKFLIP(60),
        SWORD_SWING(60),
        HEAVY_SWING(80),
        HARD_RUSH(80),
        BLASTING_EROFLAME(50),
        RUSH_AND_SWING(50),
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