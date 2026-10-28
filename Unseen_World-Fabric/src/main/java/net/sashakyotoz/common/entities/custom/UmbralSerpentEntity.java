package net.sashakyotoz.common.entities.custom;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class UmbralSerpentEntity extends TamableAnimal implements Saddleable {
    public static final TagKey<Item> TAMING_ITEMS = ModTags.Items.SERPENT_TAMING_ITEMS;
    public static final byte ATTACK_EVENT = 93;
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(UmbralSerpentEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int ATTACK_ANIMATION_TICKS = 10;
    private static final double RIDER_SEAT_HEIGHT = 0.62D;
    private static final double PLAYER_SEAT_OFFSET = 0.25D;
    private static final float RIDDEN_LAND_SPEED_FACTOR = 0.375F;
    private static final float RIDDEN_WATER_SPEED_FACTOR = 0.28F;
    private static final double WATER_DRAG = 0.9D;
    private static final double FLOAT_SUBMERSION = 0.45D;
    private static final double FLOAT_LIFT_STRENGTH = 0.15D;
    private static final double FLOAT_MAX_SINK = -0.04D;
    private static final double FLOAT_MAX_RISE = 0.08D;
    private static final double FLOAT_VERTICAL_DAMPING = 0.6D;
    private static final double SHORE_CLIMB_BOOST = 0.3D;
    private static final float VOICE_PITCH = 0.6F;
    private static final int PASSENGER_RESYNC_TICKS = 20;
    private static final int PASSENGER_RESYNC_INTERVAL = 5;

    public final AnimationState landIdle = new AnimationState();
    public final AnimationState waterIdle = new AnimationState();
    public final AnimationState sit = new AnimationState();
    public final AnimationState attack = new AnimationState();
    private final MoveControl landMoveControl;
    private final MoveControl waterMoveControl;
    private int attackAnimationTicks;
    private int passengerResyncTicks;

    public UmbralSerpentEntity(EntityType<? extends UmbralSerpentEntity> entityType, Level level) {
        super(entityType, level);
        this.landMoveControl = new MoveControl(this);
        this.waterMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 1.0F, 1.0F, false);
        this.moveControl = this.landMoveControl;
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.16D);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SerpentNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SADDLED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, true));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(TAMING_ITEMS), false));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.2D, 10.0F, 3.0F));
        this.goalSelector.addGoal(5, new SeekWaterGoal(this));
        this.goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 160));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, Player.class, false, this::isHostileTowards));
    }

    private boolean isHostileTowards(LivingEntity entity) {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL || !(entity instanceof Player player)) {
            return false;
        }
        return !player.getMainHandItem().is(TAMING_ITEMS) && !player.getOffhandItem().is(TAMING_ITEMS);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
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
                if (this.isSaddled() && !this.isVehicle() && !player.isPassenger() && !stack.is(TAMING_ITEMS) && !stack.is(Items.SADDLE)) {
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
        if (stack.is(TAMING_ITEMS)) {
            this.usePlayerItem(player, hand, stack);
            if (!client) {
                if (this.random.nextFloat() < 0.3F) {
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

    @Override
    public boolean doHurtTarget(Entity target) {
        this.level().broadcastEntityEvent(this, ATTACK_EVENT);
        boolean result = super.doHurtTarget(target);
        if (result) {
            this.playSound(SoundEvents.AXOLOTL_ATTACK, 1.0F, VOICE_PITCH);
        }
        return result;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == ATTACK_EVENT) {
            this.attackAnimationTicks = ATTACK_ANIMATION_TICKS;
            this.attack.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.isTame() && this.isSaddled() && this.getFirstPassenger() instanceof Player player && player.getUUID().equals(this.getOwnerUUID())) {
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
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0.0D, (RIDER_SEAT_HEIGHT + PLAYER_SEAT_OFFSET) * scale, 0.0D);
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && this.isTame() && !this.isBaby();
    }

    @Override
    public void equipSaddle(ItemStack stack, @Nullable SoundSource source) {
        this.entityData.set(SADDLED, true);
        if (source != null) {
            this.level().playSound(null, this, SoundEvents.STRIDER_SADDLE, source, 0.5F, 1.0F);
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
        float base = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
        return base * (this.isInWater() ? RIDDEN_WATER_SPEED_FACTOR : RIDDEN_LAND_SPEED_FACTOR);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            if (this.getControllingPassenger() instanceof Player) {
                this.travelFloating(travelVector);
            } else {
                this.moveRelative(this.getSpeed(), travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(WATER_DRAG));
            }
            this.calculateEntityAnimation(true);
            return;
        }
        super.travel(travelVector);
    }

    private void travelFloating(Vec3 input) {
        this.moveRelative(this.getSpeed(), input);
        Vec3 motion = this.getDeltaMovement();
        double submerged = this.getFluidHeight(FluidTags.WATER);
        double lift = Mth.clamp((submerged - this.getBbHeight() * FLOAT_SUBMERSION) * FLOAT_LIFT_STRENGTH, FLOAT_MAX_SINK, FLOAT_MAX_RISE);
        double vertical = motion.y * FLOAT_VERTICAL_DAMPING + lift;
        if (this.horizontalCollision && (input.z != 0.0D || input.x != 0.0D)) {
            vertical = Math.max(vertical, SHORE_CLIMB_BOOST);
        }
        motion = new Vec3(motion.x, vertical, motion.z);
        this.move(MoverType.SELF, motion);
        this.setDeltaMovement(motion.multiply(WATER_DRAG, 1.0D, WATER_DRAG));
        this.resetFallDistance();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        MoveControl wanted = this.isInWater() ? this.waterMoveControl : this.landMoveControl;
        if (this.moveControl != wanted) {
            MoveControl previous = this.moveControl;
            this.moveControl = wanted;
            if (previous.hasWanted()) {
                wanted.setWantedPosition(previous.getWantedX(), previous.getWantedY(), previous.getWantedZ(), previous.getSpeedModifier());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            boolean sitting = this.isInSittingPose();
            boolean inWater = this.isInWaterOrBubble();
            this.sit.animateWhen(sitting, this.tickCount);
            this.waterIdle.animateWhen(inWater && !sitting, this.tickCount);
            this.landIdle.animateWhen(!inWater && !sitting, this.tickCount);
            if (this.attackAnimationTicks > 0 && --this.attackAnimationTicks == 0) {
                this.attack.stop();
            }
        } else {
            this.purgeInvalidPassengers();
            if (this.isVehicle() && (this.isOrderedToSit() || this.isInSittingPose())) {
                this.setOrderedToSit(false);
                this.setInSittingPose(false);
            }
            if (this.passengerResyncTicks > 0 && --this.passengerResyncTicks % PASSENGER_RESYNC_INTERVAL == 0) {
                this.resyncPassengers();
            }
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public int getMaxHeadYRot() {
        return 35;
    }

    @Override
    public float getVoicePitch() {
        return VOICE_PITCH + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.AXOLOTL_IDLE_WATER : SoundEvents.AXOLOTL_IDLE_AIR;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.AXOLOTL_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.AXOLOTL_DEATH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.FROG_STEP, 0.3F, VOICE_PITCH);
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
        return ModEntities.UMBRAL_SERPENT.create(level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Saddle", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(SADDLED, compound.getBoolean("Saddle"));
    }

    static class SerpentNavigation extends GroundPathNavigation {
        SerpentNavigation(Mob mob, Level level) {
            super(mob, level);
        }

        @Override
        protected PathFinder createPathFinder(int maxVisitedNodes) {
            this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
        }

        @Override
        protected boolean canUpdatePath() {
            return true;
        }

        @Override
        protected Vec3 getTempMobPos() {
            return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
        }

        @Override
        protected double getGroundY(Vec3 pos) {
            return pos.y;
        }

        @Override
        protected boolean canMoveDirectly(Vec3 from, Vec3 to) {
            return this.mob.isInWater() && isClearForMovementBetween(this.mob, from, to, false);
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            return !this.level.getBlockState(pos.below()).isAir();
        }
    }

    static class SeekWaterGoal extends Goal {
        private static final int SEARCH_ATTEMPTS = 12;
        private static final int SEARCH_RADIUS = 10;
        private static final int SEARCH_HEIGHT = 3;
        private static final int CHANCE = 80;

        private final UmbralSerpentEntity serpent;
        @Nullable
        private BlockPos water;

        SeekWaterGoal(UmbralSerpentEntity serpent) {
            this.serpent = serpent;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.serpent.isInWater() || this.serpent.isVehicle() || this.serpent.isOrderedToSit() || this.serpent.getTarget() != null) {
                return false;
            }
            if (this.serpent.isTame() && this.serpent.getOwner() != null) {
                return false;
            }
            if (this.serpent.getRandom().nextInt(reducedTickDelay(CHANCE)) != 0) {
                return false;
            }
            this.water = this.findWater();
            return this.water != null;
        }

        @Nullable
        private BlockPos findWater() {
            BlockPos origin = this.serpent.blockPosition();
            for (int i = 0; i < SEARCH_ATTEMPTS; i++) {
                BlockPos candidate = origin.offset(
                        this.serpent.getRandom().nextInt(SEARCH_RADIUS * 2 + 1) - SEARCH_RADIUS,
                        this.serpent.getRandom().nextInt(SEARCH_HEIGHT * 2 + 1) - SEARCH_HEIGHT,
                        this.serpent.getRandom().nextInt(SEARCH_RADIUS * 2 + 1) - SEARCH_RADIUS);
                if (this.serpent.level().getFluidState(candidate).is(FluidTags.WATER)) {
                    return candidate;
                }
            }
            return null;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.serpent.isInWater() && !this.serpent.getNavigation().isDone();
        }

        @Override
        public void start() {
            if (this.water != null) {
                this.serpent.getNavigation().moveTo(this.water.getX() + 0.5D, this.water.getY(), this.water.getZ() + 0.5D, 1.0D);
            }
        }

        @Override
        public void stop() {
            this.water = null;
        }
    }
}
