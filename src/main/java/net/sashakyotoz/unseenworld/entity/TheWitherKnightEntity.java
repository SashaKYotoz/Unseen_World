
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.sashakyotoz.unseenworld.UnseenWorldConfigs;
import net.sashakyotoz.unseenworld.managers.AdvancementManager;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;

import java.util.EnumSet;
import java.util.Objects;

public class TheWitherKnightEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_ADVANCED = SynchedEntityData.defineId(TheWitherKnightEntity.class, EntityDataSerializers.BOOLEAN);
    private boolean gravityFlag = false;
    private int timer = 0;
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState attack1AnimationState = new AnimationState();
    public AnimationState flyingAttackAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idle1AnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    private final ServerBossEvent bossInfo = new ServerBossEvent(Component.translatable("entity.unseen_world.the_wither_knight").withStyle(ChatFormatting.YELLOW), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.NOTCHED_6);

    public TheWitherKnightEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldEntities.THE_WITHER_KNIGHT.get(), world);
    }
    public TheWitherKnightEntity(EntityType<TheWitherKnightEntity> type, Level world) {
        super(type, world);
        xpReward = 20;
        setMaxUpStep(1.5f);
        setPersistenceRequired();
        this.moveControl = new FlyingMoveControl(this, 16, true);
        setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND,new ItemStack(UnseenWorldItems.HEAVY_CLAYMORE.get()));
    }
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(!Objects.equals(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_WITHER_KNIGHT.get(), UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_WITHER_KNIGHT.getDefault())){
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_WITHER_KNIGHT.get());
            this.setHealth(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_WITHER_KNIGHT.get().floatValue());
        }
    }

    public boolean isAdvanced() {
        return this.entityData.get(DATA_IS_ADVANCED);
    }

    public void setAdvanced(boolean advanced) {
        this.entityData.set(DATA_IS_ADVANCED, advanced);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ADVANCED, false);
    }

    public void handleEntityEvent(byte bytes) {
        if (bytes >= 4 && bytes <= 20) {
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
            super.handleEntityEvent(bytes);
        }
    }
    public boolean doHurtTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        } else {
            this.level().broadcastEntityEvent(this, (byte) 4);
            return TheWitherKnightEntity.hurtAndThrowTarget(this, (LivingEntity) entity);
        }
    }
    static boolean hurtAndThrowTarget(LivingEntity livingEntity, LivingEntity target) {
        float f = (float) livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = target.hurt(livingEntity.damageSources().mobAttack(livingEntity), f);
        if (flag) {
            livingEntity.doEnchantDamageEffects(livingEntity, target);
            throwTarget(livingEntity, target);
        }
        return flag;
    }
    static void throwTarget(LivingEntity livingEntity, LivingEntity livingEntity1) {
        double d0 = livingEntity.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double d1 = livingEntity1.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double d2 = 0.5D + d0 - d1;
        if (!(d2 <= 0.0D)) {
            double d3 = livingEntity1.getX() - livingEntity.getX();
            double d4 = livingEntity1.getZ() - livingEntity.getZ();
            float f = (float) (livingEntity.level().random.nextInt(21) - 8);
            double d5 = d2 * (double) (livingEntity.level().random.nextFloat() * 0.75F + 0.25F);
            Vec3 vec3 = (new Vec3(d3, 0.0D, d4)).normalize().scale(d5).yRot(f);
            double d6 = d2 * (double) livingEntity.level().random.nextFloat() * 0.5D;
            livingEntity1.push(vec3.x, d6, vec3.z);
            livingEntity1.hurtMarked = true;
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
        if (this.getTarget() != null && this.getTarget().getY() >= this.getY() + 2 && !this.isAdvanced()){
            timer = 20;
            this.setDeltaMovement(0,0.25f,0);
            gravityFlag = true;
        }
        if (timer > -20)
            timer--;
        if (gravityFlag && timer <=0){
            this.setShiftKeyDown(timer > -20);
            gravityFlag = timer > -5;
            this.setDeltaMovement(0,0.15f,0);
        }
        this.setAdvanced(this.getHealth() < (this.getMaxHealth() / 2));
        super.tick();
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
                    return TheWitherKnightEntity.this.getTarget() != null && !TheWitherKnightEntity.this.getMoveControl().hasWanted();
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
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false, true));
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
        return SoundEvents.WITHER_AMBIENT;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.NETHER_GOLD_ORE_STEP, 0.15f, 1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
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
        if(chance < 0.25 && isAdvanced()) {
            this.setHealth(getHealth() + 5f);
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if(source.getEntity() instanceof Player player)
            AdvancementManager.addAdvancement(player,AdvancementManager.THE_WITHER_KNIGHT_ADV);
        this.spawnAtLocation(new ItemStack(UnseenWorldBlocks.GOLDEN_CHEST.get()));
        if(this.getRandom().nextBoolean()){
            this.spawnAtLocation(new ItemStack(UnseenWorldItems.KNIGHT_ARMOR_HELMET.get()));
            this.spawnAtLocation(new ItemStack(UnseenWorldItems.KNIGHT_ARMOR_CHESTPLATE.get()));
        }else{
            this.spawnAtLocation(new ItemStack(UnseenWorldItems.KNIGHT_ARMOR_LEGGINGS.get()));
            this.spawnAtLocation(new ItemStack(UnseenWorldItems.KNIGHT_ARMOR_BOOTS.get()));
        }
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

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
        builder = builder.add(Attributes.MAX_HEALTH, 350);
        builder = builder.add(Attributes.ARMOR, 15);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 15);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 5);
        builder = builder.add(Attributes.FLYING_SPEED, 1.2);
        return builder;
    }
}
