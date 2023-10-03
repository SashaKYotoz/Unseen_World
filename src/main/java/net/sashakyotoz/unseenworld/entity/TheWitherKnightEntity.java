
package net.sashakyotoz.unseenworld.entity;

import net.sashakyotoz.unseenworld.UnseenWorldModCommonConfigs;
import net.sashakyotoz.unseenworld.init.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.procedures.TheWitherKnightEntityDiesProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumSet;

public class TheWitherKnightEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_ADVANCED = SynchedEntityData.defineId(TheWitherKnightEntity.class, EntityDataSerializers.BOOLEAN);
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState attack1AnimationState = new AnimationState();
    public AnimationState flyingAttackAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idle1AnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.NOTCHED_6);

    public TheWitherKnightEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldModEntities.THE_WITHER_KNIGHT.get(), world);
    }

    public boolean isAdvanced() {
        return this.entityData.get(DATA_IS_ADVANCED);
    }

    public void setAdvanced(boolean p_32759_) {
        this.entityData.set(DATA_IS_ADVANCED, p_32759_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ADVANCED, false);
    }

    public void handleEntityEvent(byte p_219360_) {
        if (p_219360_ >= 4 && p_219360_ <= 20) {
            if (!isAdvanced()) {
                int randomAttack = (int) Math.round(Math.random());
                if (randomAttack == 0)
                    this.attackAnimationState.start(this.tickCount);
                else
                    this.attack1AnimationState.start(this.tickCount);
            } else {
                this.flyingAttackAnimationState.start(this.tickCount);
            }
        } else {
            super.handleEntityEvent(p_219360_);
        }
    }
    public boolean doHurtTarget(Entity p_34491_) {
        if (!(p_34491_ instanceof LivingEntity)) {
            return false;
        } else {
            this.level().broadcastEntityEvent(this, (byte) 4);
            return TheWitherKnightEntity.hurtAndThrowTarget(this, (LivingEntity) p_34491_);
        }
    }
    static boolean hurtAndThrowTarget(LivingEntity p_34643_, LivingEntity p_34644_) {
        float f = (float) p_34643_.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = p_34644_.hurt(p_34643_.damageSources().mobAttack(p_34643_), f);
        if (flag) {
            p_34643_.doEnchantDamageEffects(p_34643_, p_34644_);
            throwTarget(p_34643_, p_34644_);
        }
        return flag;
    }
    static void throwTarget(LivingEntity p_34646_, LivingEntity p_34647_) {
        double d0 = p_34646_.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double d1 = p_34647_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double d2 = 0.5D + d0 - d1;
        if (!(d2 <= 0.0D)) {
            double d3 = p_34647_.getX() - p_34646_.getX();
            double d4 = p_34647_.getZ() - p_34646_.getZ();
            float f = (float) (p_34646_.level().random.nextInt(21) - 8);
            double d5 = d2 * (double) (p_34646_.level().random.nextFloat() * 0.75F + 0.25F);
            Vec3 vec3 = (new Vec3(d3, 0.0D, d4)).normalize().scale(d5).yRot(f);
            double d6 = d2 * (double) p_34646_.level().random.nextFloat() * 0.5D;
            p_34647_.push(vec3.x, d6, vec3.z);
            p_34647_.hurtMarked = true;
        }
    }
    private boolean isMovingInAir() {
        return !this.onGround() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }
    private boolean isMovingOnGround() {
        return this.onGround() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }
    public void tick() {
        if (this.level().isClientSide()) {
            if (!isAdvanced()) {
                if (isMovingOnGround()) {
                    walkAnimationState.startIfStopped(this.tickCount);
                    idleAnimationState.stop();
                } else {
                    idleAnimationState.startIfStopped(this.tickCount);
                    walkAnimationState.stop();
                }
            } else {
                if (isMovingInAir()) {
                    flyAnimationState.startIfStopped(this.tickCount);
                    idle1AnimationState.stop();
                } else {
                    idle1AnimationState.startIfStopped(this.tickCount);
                    flyAnimationState.stop();
                }
            }
        }
        this.setAdvanced(this.getHealth() < (this.getMaxHealth() / 2));
        super.tick();
    }
    public TheWitherKnightEntity(EntityType<TheWitherKnightEntity> type, Level world) {
        super(type, world);
        xpReward = 20;
        setNoAi(false);
        setCustomName(Component.literal("§6The Wither Knight"));
        setCustomNameVisible(true);
        setPersistenceRequired();
        this.moveControl = new FlyingMoveControl(this, 16, true);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new FlyingPathNavigation(this, world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        if (isAdvanced()) {
            this.goalSelector.addGoal(1, new Goal() {
                {
                    this.setFlags(EnumSet.of(Goal.Flag.MOVE));
                }

                public boolean canUse() {
                    if (TheWitherKnightEntity.this.getTarget() != null && !TheWitherKnightEntity.this.getMoveControl().hasWanted()) {
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public boolean canContinueToUse() {
                    return TheWitherKnightEntity.this.getMoveControl().hasWanted() && TheWitherKnightEntity.this.getTarget() != null && TheWitherKnightEntity.this.getTarget().isAlive();
                }

                @Override
                public void start() {
                    LivingEntity livingentity = TheWitherKnightEntity.this.getTarget();
                    Vec3 vec3d = livingentity.getEyePosition(1);
                    TheWitherKnightEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 2.5);
                }

                @Override
                public void tick() {
                    LivingEntity livingentity = TheWitherKnightEntity.this.getTarget();
                    if (TheWitherKnightEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                        TheWitherKnightEntity.this.doHurtTarget(livingentity);
                    } else {
                        double d0 = TheWitherKnightEntity.this.distanceToSqr(livingentity);
                        if (d0 < 32) {
                            Vec3 vec3d = livingentity.getEyePosition(1);
                            TheWitherKnightEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 2.5);
                        }
                    }
                }
            });
            this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.2, 20) {
                @Override
                protected Vec3 getPosition() {
                    RandomSource random = TheWitherKnightEntity.this.getRandom();
                    double dir_x = TheWitherKnightEntity.this.getX() + ((random.nextFloat() * 2 - 1) * 32);
                    double dir_y = TheWitherKnightEntity.this.getY() + ((random.nextFloat() * 2 - 1) * 32);
                    double dir_z = TheWitherKnightEntity.this.getZ() + ((random.nextFloat() * 2 - 1) * 32);
                    return new Vec3(dir_x, dir_y, dir_z);
                }
            });
        } else {
            this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.2));
        }
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth() + 2.0D;
            }
        });
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, false, true));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    @Override
    public boolean causeFallDamage(float l, float d, DamageSource source) {
        return false;
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(isAdvanced());
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.wither.ambient"));
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.wither_skeleton.step")), 0.15f, 1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.wither_skeleton.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.wither.death"));
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float chance = (float) Math.random();
        if (source.is(DamageTypes.IN_FIRE))
            return false;
        if (source.is(DamageTypes.EXPLOSION))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.is(DamageTypes.WITHER_SKULL))
            return false;
        if(chance < 0.25 && isAdvanced())
        {
            this.setHealth(getHealth() + 5f);
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        TheWitherKnightEntityDiesProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), source.getEntity());
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, UnseenWorldModCommonConfigs.WITHER_KNIGHT_SPEED.getDefault());
        builder = builder.add(Attributes.MAX_HEALTH, UnseenWorldModCommonConfigs.WITHER_KNIGHT_HEALTH_POINTS.getDefault());
        builder = builder.add(Attributes.ARMOR, 15);
        builder = builder.add(Attributes.ATTACK_DAMAGE, UnseenWorldModCommonConfigs.WITHER_KNIGHT_DAMAGE.getDefault());
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 5);
        builder = builder.add(Attributes.FLYING_SPEED, 1.2);
        return builder;
    }
}
