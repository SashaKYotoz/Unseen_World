
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.sashakyotoz.unseenworld.UnseenWorldConfigs;
import net.sashakyotoz.unseenworld.managers.AdvancementManager;
import net.sashakyotoz.unseenworld.managers.EventManager;
import net.sashakyotoz.unseenworld.managers.TheBlazerOnEntityTickUpdateProcedure;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;

import java.util.EnumSet;
import java.util.Objects;

public class TheBlazerEntity extends Blaze implements RangedAttackMob {
    public AnimationState strikeAnimationState = new AnimationState();
    public AnimationState spawnAnimationState = new AnimationState();
    private final TheBlazerEntity blazer = this;
    private static final EntityDataAccessor<Boolean> DATA_IS_BLOCKED = SynchedEntityData.defineId(TheBlazerEntity.class, EntityDataSerializers.BOOLEAN);
    private final ServerBossEvent bossInfo = new ServerBossEvent(Component.translatable("entity.unseen_world.the_blazer").withStyle(ChatFormatting.GOLD), BossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.NOTCHED_6);

    public TheBlazerEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldEntities.THE_BLAZER.get(), world);
    }

    public TheBlazerEntity(EntityType<TheBlazerEntity> type, Level world) {
        super(type, world);
        xpReward = 30;
        setNoAi(false);
        setCustomName(Component.translatable("entity.unseen_world.the_blazer").withStyle(ChatFormatting.GOLD));
        setCustomNameVisible(true);
        setPersistenceRequired();
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        spawnAnimationState.start(this.tickCount);
        EventManager.addParticles(UnseenWorldParticleTypes.FIRE_PARTICLE.get(),this.level(),this.getX(),this.getY(),this.getZ(),2);
        if (!Objects.equals(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_BLAZER.get(), UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_BLAZER.getDefault())) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_BLAZER.get());
            this.setHealth(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_BLAZER.get().floatValue());
        }
    }

    public boolean isBlocked() {
        return this.entityData.get(DATA_IS_BLOCKED);
    }

    public void setBlocking(boolean blocking) {
        this.entityData.set(DATA_IS_BLOCKED, blocking);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_BLOCKED, false);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new FlyingPathNavigation(this, world);
    }

    static class TheBlazerisBlockingGoal extends Goal {
        private final TheBlazerEntity blazer;
        public int chargeTime;

        public TheBlazerisBlockingGoal(TheBlazerEntity entity) {
            this.blazer = entity;
        }

        public boolean canUse() {
            return this.blazer.getTarget() != null;
        }

        public void start() {
            this.chargeTime = 0;
        }

        public void stop() {
            this.blazer.setBlocking(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.blazer.getTarget();
            if (livingentity != null) {
                if (livingentity.distanceToSqr(this.blazer) < 2048.0D && this.blazer.hasLineOfSight(livingentity)) {
                    ++this.chargeTime;
                    if (this.chargeTime == 80 && !this.blazer.isSilent()) {
                        blazer.playSound(SoundEvents.SHIELD_BLOCK);
                        blazer.setBlocking(true);
                        blazer.strikeAnimationState.start(blazer.tickCount);
                    }
                    if (this.chargeTime == 160) {
                        blazer.setBlocking(true);
                        this.chargeTime = -160;
                    }
                } else if (this.chargeTime > 0) {
                    --this.chargeTime;
                }
                this.blazer.setBlocking(this.chargeTime > 80);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new Goal() {
            {
                this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            }

            public boolean canUse() {
                return TheBlazerEntity.this.getTarget() != null && !TheBlazerEntity.this.getMoveControl().hasWanted();
            }

            @Override
            public boolean canContinueToUse() {
                return TheBlazerEntity.this.getMoveControl().hasWanted() && TheBlazerEntity.this.getTarget() != null && TheBlazerEntity.this.getTarget().isAlive();
            }

            @Override
            public void start() {
                LivingEntity livingentity = TheBlazerEntity.this.getTarget();
                if (livingentity != null){
                    Vec3 vec3d = livingentity.getEyePosition(1);
                    TheBlazerEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.5);
                }
            }

            @Override
            public void tick() {
                LivingEntity livingentity = TheBlazerEntity.this.getTarget();
                if (livingentity != null){
                    if (TheBlazerEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                        TheBlazerEntity.this.doHurtTarget(livingentity);
                    } else {
                        double d0 = TheBlazerEntity.this.distanceToSqr(livingentity);
                        if (d0 < 32) {
                            Vec3 vec3d = livingentity.getEyePosition(1);
                            TheBlazerEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.5);
                        }
                    }
                }
            }
        });
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.25, 20) {
            @Override
            protected Vec3 getPosition() {
                RandomSource random = TheBlazerEntity.this.getRandom();
                double dir_x = TheBlazerEntity.this.getX() + ((random.nextFloat() * 2 - 1) * 16);
                double dir_y = TheBlazerEntity.this.getY() + ((random.nextFloat() * 2 - 1) * 16);
                double dir_z = TheBlazerEntity.this.getZ() + ((random.nextFloat() * 2 - 1) * 16);
                return new Vec3(dir_x, dir_y, dir_z);
            }
        });
        this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, -0.5f));
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 20, 60, 12f) {
            @Override
            public void stop() {
                blazer.strikeAnimationState.start(blazer.tickCount);
            }

            @Override
            public boolean canContinueToUse() {
                return this.canUse();
            }
        });
        this.goalSelector.addGoal(6, new TheBlazerEntity.TheBlazerisBlockingGoal(this));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.NETHERITE_BLOCK_STEP, 0.15f, 1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.BLAZE_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    public boolean causeFallDamage(float l, float d, DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isBlocked()) {
            if (source.getDirectEntity() instanceof Player)
                return false;
        }
        if (source.is(DamageTypes.LIGHTNING_BOLT))
            return false;
        if (source.is(DamageTypes.EXPLOSION))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.getMsgId().equals("witherSkull"))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if(source.getEntity() instanceof Player player) {
            AdvancementManager.addAdvancement(player, AdvancementManager.THE_BLAZER_ADV);
            if (!player.level().isClientSide())
                player.displayClientMessage(Component.translatable("entity.unseen_world.the_blazer.death_message"), true);
        }
        this.spawnAtLocation(new ItemStack(UnseenWorldItems.VOID_HAMMER.get()));
        int tmp = this.getRandom().nextInt(1,4)+1;
        this.spawnAtLocation(new ItemStack(UnseenWorldItems.RED_TITANIUM_INGOT.get(),tmp));
    }

    @Override
    public void tick() {
        super.tick();
        if (blazer.getTarget() != null && this.getRandom().nextBoolean())
            TheBlazerOnEntityTickUpdateProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
    }

    @Override
    public void onLeaveCombat() {
        this.setBlocking(false);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float flval) {
        NetheriumStaffEntity.shoot(this, target);
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

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    public void aiStep() {
        super.aiStep();
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
        builder = builder.add(Attributes.MAX_HEALTH, 450);
        builder = builder.add(Attributes.ARMOR, 8);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 4);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 0.5);
        builder = builder.add(Attributes.FLYING_SPEED, 0.25);
        return builder;
    }
}
