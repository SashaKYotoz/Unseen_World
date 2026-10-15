package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.ai.goals.VexalBeetleFlyGoal;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VexalBeetleEntity extends TamableAnimal implements FlyingAnimal, Saddleable, PlayerRideableJumping {
    public static final TagKey<Item> TAMING_ITEMS = ModTags.Items.VEXAL_BETTLE_TAMING_ITEMS;
    private static final EntityDataAccessor<Integer> FLIGHT_STAMINA = SynchedEntityData.defineId(VexalBeetleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> EXHAUSTED = SynchedEntityData.defineId(VexalBeetleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(VexalBeetleEntity.class, EntityDataSerializers.BOOLEAN);

    public static final int MAX_FLIGHT_TICKS = 60;
    private static final double RIDER_SEAT_HEIGHT = 0.3D;
    private static final float GROUND_SPEED_FACTOR = 0.5F;
    private static final float GLIDE_SPEED_FACTOR = 0.6F;
    private static final int RIDDEN_SYNC_INTERVAL = 2;
    private static final int RECOVERY_THRESHOLD = MAX_FLIGHT_TICKS / 3;
    private static final int GROUND_REGEN_PER_TICK = 2;
    private static final int STAMINA_SYNC_INTERVAL = 5;
    private static final double GLIDE_FALL_FACTOR = 0.6D;
    private static final double GRAVITY = 0.08D;
    private static final float TAME_CHANCE = 0.25F;
    private static final int PASSENGER_RESYNC_TICKS = 20;
    private static final int PASSENGER_RESYNC_INTERVAL = 5;

    public final AnimationState flying = new AnimationState();
    private int flightStamina = MAX_FLIGHT_TICKS;
    private int passengerResyncTicks;

    public VexalBeetleEntity(EntityType<? extends VexalBeetleEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FLYING_SPEED, 0.3D);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLIGHT_STAMINA, MAX_FLIGHT_TICKS);
        this.entityData.define(EXHAUSTED, false);
        this.entityData.define(SADDLED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(TAMING_ITEMS), false));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.1D, 8.0F, 2.5F, true));
        this.goalSelector.addGoal(5, new VexalBeetleFlyGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, Player.class, false, this::isHostileTowards));
    }

    private boolean isHostileTowards(LivingEntity entity) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL || !(entity instanceof Player player)) {
            return false;
        }
        if (this.getTags().contains(OrdealSpawnerBlockEntity.ORDEAL_TAG)) {
            return true;
        }
        return !player.getMainHandItem().is(TAMING_ITEMS) && !player.getOffhandItem().is(TAMING_ITEMS);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean client = this.level().isClientSide();
        if (!client) {
            this.purgeInvalidPassengers();
            if (player.getVehicle() == this) {
                this.resyncPassengers();
                return InteractionResult.SUCCESS;
            }
        }
        if (this.isTame()) {
            if (stack.is(TAMING_ITEMS) && this.getHealth() < this.getMaxHealth()) {
                this.usePlayerItem(player, hand, stack);
                this.heal(4.0F);
                this.gameEvent(GameEvent.EAT, this);
                return InteractionResult.sidedSuccess(client);
            }
            if (this.isOwnedBy(player)) {
                if (player.isSecondaryUseActive() && !this.isVehicle()) {
                    if (!client) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.getNavigation().stop();
                        this.setTarget(null);
                    }
                    return InteractionResult.sidedSuccess(client);
                }
                if (this.isSaddled() && !this.isVehicle() && !this.isBaby() && !player.isPassenger() && !stack.is(TAMING_ITEMS) && !stack.is(Items.SADDLE)) {
                    if (!client) {
                        this.setTarget(null);
                        this.getNavigation().stop();
                        player.startRiding(this);
                    }
                    return InteractionResult.sidedSuccess(client);
                }
            }
            return super.mobInteract(player, hand);
        }
        if (stack.is(TAMING_ITEMS) && !this.getTags().contains(OrdealSpawnerBlockEntity.ORDEAL_TAG)) {
            this.usePlayerItem(player, hand, stack);
            if (!client) {
                if (this.random.nextFloat() < TAME_CHANCE) {
                    this.tame(player);
                    this.getNavigation().stop();
                    this.setTarget(null);
                    this.setLastHurtByMob(null);
                    this.setOrderedToSit(false);
                    this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
                } else {
                    this.level().broadcastEntityEvent(this, EntityEvent.TAMING_FAILED);
                }
            }
            return InteractionResult.sidedSuccess(client);
        }
        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.isTame() && this.getFirstPassenger() instanceof Player player && player.getUUID().equals(this.getOwnerUUID())) {
            return player;
        }
        return null;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty() && passenger instanceof Player;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (!this.level().isClientSide()) {
            this.setOrderedToSit(false);
            this.setInSittingPose(false);
            this.passengerResyncTicks = PASSENGER_RESYNC_TICKS;
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide()) {
            this.passengerResyncTicks = 0;
            this.resyncPassengers();
        }
    }

    private boolean isValidPassenger(Entity passenger) {
        if (passenger.isRemoved() || !passenger.isAlive() || passenger.level() != this.level() || passenger.getVehicle() != this) {
            return false;
        }
        return !(passenger instanceof ServerPlayer serverPlayer) || !serverPlayer.hasDisconnected();
    }

    private void purgeInvalidPassengers() {
        if (!this.isVehicle()) {
            return;
        }
        List<Entity> invalid = this.getPassengers().stream().filter(passenger -> !this.isValidPassenger(passenger)).toList();
        if (invalid.isEmpty()) {
            return;
        }
        for (Entity passenger : invalid) {
            if (passenger.getVehicle() == this) {
                passenger.stopRiding();
            } else {
                this.removePassenger(passenger);
            }
        }
    }

    private void resyncPassengers() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().broadcast(this, new ClientboundSetPassengersPacket(this));
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return RIDER_SEAT_HEIGHT;
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && this.isTame() && !this.isBaby();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource source) {
        this.entityData.set(SADDLED, true);
        if (source != null) {
            this.level().playSound(null, this, SoundEvents.HORSE_SADDLE, source, 0.5F, 1.4F);
        }
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public int getJumpCooldown() {
        return this.canPowerFly() ? 0 : 1;
    }

    @Override
    public void onPlayerJump(int jumpPower) {
    }

    @Override
    public void handleStartJump(int jumpPower) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float forward = player.zza <= 0.0F ? player.zza * 0.25F : player.zza;
        return new Vec3(player.xxa * 0.5F, 0.0D, forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        float speed = (float) this.getAttributeValue(Attributes.FLYING_SPEED) * 0.35F;
        if (this.onGround()) {
            speed *= GROUND_SPEED_FACTOR;
        }
        return this.canPowerFly() ? speed : speed * GLIDE_SPEED_FACTOR;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isAlive() && this.isVehicle() && this.getControllingPassenger() instanceof Player rider && !this.isInWater() && !this.isInLava()) {
            this.travelMounted(rider, travelVector);
            return;
        }
        super.travel(travelVector);
    }

    private void travelMounted(Player rider, Vec3 input) {
        float speed = this.getSpeed();
        Vec3 look = rider.getLookAngle();
        Vec3 flatLook = new Vec3(look.x, 0.0D, look.z);
        flatLook = flatLook.lengthSqr() < 1.0E-6D ? Vec3.directionFromRotation(0.0F, this.getYRot()) : flatLook.normalize();
        Vec3 left = new Vec3(flatLook.z, 0.0D, -flatLook.x);
        Vec3 motion = this.getDeltaMovement();

        if (this.canPowerFly()) {
            motion = motion.add(look.scale(input.z * speed)).add(left.scale(input.x * speed));
            if (input.z == 0.0D && input.x == 0.0D) {
                motion = motion.multiply(1.0D, 0.6D, 1.0D);
            }
            this.move(MoverType.SELF, motion);
            this.setDeltaMovement(motion.scale(0.91D));
        } else {
            motion = motion.add(flatLook.scale(input.z * speed)).add(left.scale(input.x * speed));
            motion = motion.add(0.0D, -GRAVITY, 0.0D);
            if (!this.onGround() && motion.y < 0.0D) {
                motion = motion.multiply(1.0D, GLIDE_FALL_FACTOR, 1.0D);
            }
            this.move(MoverType.SELF, motion);
            double friction = this.onGround() ? 0.546D : 0.91D;
            this.setDeltaMovement(motion.multiply(friction, 0.98D, friction));
        }
        this.calculateEntityAnimation(false);
    }

    public boolean canPowerFly() {
        return !this.entityData.get(EXHAUSTED);
    }

    public float getFlightStaminaRatio() {
        return this.entityData.get(FLIGHT_STAMINA) / (float) MAX_FLIGHT_TICKS;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.tickStamina();
        } else if (this.isVehicle() && !this.onGround()) {
            this.spawnFlightParticles();
        }
    }

    private void tickStamina() {
        boolean exhausted = this.entityData.get(EXHAUSTED);
        boolean airborne = !this.onGround() && !this.isInWater();
        if (this.isVehicle() && airborne) {
            if (!exhausted && --this.flightStamina <= 0) {
                this.flightStamina = 0;
                exhausted = true;
                this.playSound(SoundEvents.PHANTOM_FLAP, 1.0F, 0.5F);
            }
        } else if (!airborne || !this.isVehicle()) {
            this.flightStamina = Math.min(MAX_FLIGHT_TICKS, this.flightStamina + (this.onGround() ? GROUND_REGEN_PER_TICK : 1));
        }
        if (exhausted && this.flightStamina >= RECOVERY_THRESHOLD && this.onGround()) {
            exhausted = false;
        }
        if (exhausted != this.entityData.get(EXHAUSTED)) {
            this.entityData.set(EXHAUSTED, exhausted);
        }
        int interval = this.isVehicle() ? RIDDEN_SYNC_INTERVAL : STAMINA_SYNC_INTERVAL;
        if (this.tickCount % interval == 0 || this.flightStamina == 0 || this.flightStamina == MAX_FLIGHT_TICKS) {
            this.entityData.set(FLIGHT_STAMINA, this.flightStamina);
        }
    }

    private void spawnFlightParticles() {
        float ratio = this.getFlightStaminaRatio();
        if (!this.canPowerFly()) {
            if (this.tickCount % 3 == 0) {
                this.level().addParticle(ParticleTypes.CLOUD, this.getRandomX(0.6D), this.getY() + 0.2D, this.getRandomZ(0.6D), 0.0D, 0.02D, 0.0D);
            }
        } else if (ratio < 0.25F && this.tickCount % 4 == 0) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getY() + 0.6D, this.getRandomZ(0.5D), 0.0D, 0.01D, 0.0D);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.purgeInvalidPassengers();
            if (this.isVehicle() && (this.isOrderedToSit() || this.isInSittingPose())) {
                this.setOrderedToSit(false);
                this.setInSittingPose(false);
            }
            if (this.passengerResyncTicks > 0 && --this.passengerResyncTicks % PASSENGER_RESYNC_INTERVAL == 0) {
                this.resyncPassengers();
            }
        }
        this.flying.animateWhen(!this.onGround(), this.tickCount);
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isTame();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.VEXAL_BEETLE.create(level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("FlightStamina", this.flightStamina);
        compound.putBoolean("Exhausted", this.entityData.get(EXHAUSTED));
        compound.putBoolean("Saddle", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("FlightStamina")) {
            this.flightStamina = Mth.clamp(compound.getInt("FlightStamina"), 0, MAX_FLIGHT_TICKS);
            this.entityData.set(FLIGHT_STAMINA, this.flightStamina);
        }
        this.entityData.set(EXHAUSTED, compound.getBoolean("Exhausted"));
        this.entityData.set(SADDLED, compound.getBoolean("Saddle"));
    }
}