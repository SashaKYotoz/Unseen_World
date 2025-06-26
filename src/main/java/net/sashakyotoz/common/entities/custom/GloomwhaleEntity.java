package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.entities.ai.GloomwhaleJumpGoal;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GloomwhaleEntity extends WaterCreatureEntity {
    private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(GloomwhaleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;

    public final AnimationState death = new AnimationState();

    public GloomwhaleEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new AquaticMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new YawAdjustingLookControl(this, 10);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CONVERTING, false);
    }

    private void setConverting(@Nullable UUID uuid, int delay) {
        this.converter = uuid;
        this.conversionTimer = delay;
        this.getDataTracker().set(CONVERTING, true);
        this.removeStatusEffect(StatusEffects.GLOWING);
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, delay, Math.min(this.getWorld().getDifficulty().getId() - 1, 0)));
        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_SPLASH_PARTICLES);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ModItems.CRYSTIE_APPLE)) {
            if (this.hasStatusEffect(StatusEffects.GLOWING)) {
                if (!player.getAbilities().creativeMode)
                    itemStack.decrement(1);
                if (!this.getWorld().isClient)
                    this.setConverting(player.getUuid(), this.random.nextInt(2401) + 3600);
                return ActionResult.SUCCESS;
            } else
                return ActionResult.CONSUME;
        } else if (itemStack.isOf(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().creativeMode && player instanceof ServerPlayerEntity player1)
                itemStack.damage(1, player1.getRandom(), player1);
            if (!this.getWorld().isClient)
                this.setConverting(player.getUuid(), this.random.nextInt(1201) + 1200);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    public static boolean canWhaleSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getFluidState(pos.up()).isIn(FluidTags.WATER)
                && world.getEntitiesByType(TypeFilter.instanceOf(GloomwhaleEntity.class), type.getDimensions().getBoxAt(pos.toCenterPos()).expand(32), LivingEntity::isAlive).size() < 3
                && random.nextInt(6) == 2;
    }

    public boolean isConverting() {
        return this.getDataTracker().get(CONVERTING);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null) {
            nbt.putUuid("ConversionPlayer", this.converter);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ConversionTime", NbtElement.NUMBER_TYPE) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.containsUuid("ConversionPlayer") ? nbt.getUuid("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient && this.isAlive() && this.isConverting()) {
            this.conversionTimer--;
            if (this.conversionTimer <= 0)
                this.finishConversion((ServerWorld) this.getWorld());
        }
        super.tick();
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.deathTime = -30;
        super.onDeath(damageSource);
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (!this.death.isRunning())
            this.death.start(this.age);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new BreatheAirGoal(this));
        this.goalSelector.add(0, new MoveIntoWaterGoal(this));
        this.goalSelector.add(4, new SwimAroundGoal(this, 1.0, 10));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(5, new GloomwhaleJumpGoal(this, 10));
        this.goalSelector.add(6, new MeleeAttackGoal(this, 1.2F, true));
        this.goalSelector.add(8, new ChaseBoatGoal(this));
        this.goalSelector.add(9, new FleeEntityGoal(this, GuardianEntity.class, 8.0F, 1.0, 1.0));
        this.targetSelector.add(1, new RevengeGoal(this, GuardianEntity.class).setGroupRevenge());
    }

    private void finishConversion(ServerWorld world) {
        DolphinEntity dolphin = this.convertTo(EntityType.DOLPHIN, false);
        dolphin.initialize(world, world.getLocalDifficulty(dolphin.getBlockPos()), SpawnReason.CONVERSION, null, null);
        if (this.converter != null) {
            PlayerEntity player = world.getPlayerByUuid(this.converter);
            if (player != null)
                player.dropItem(ModItems.GRIPCRYSTAL);
            if (player instanceof ServerPlayerEntity player1)
                ModRegistry.CURED_GRIPCRYSTAL_ENTITY_CRITERION.trigger(player1, this, dolphin);
        }
        dolphin.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
        if (!this.isSilent())
            world.playSound(this, this.getBlockPos(), SoundEvents.ENTITY_DOLPHIN_HURT, SoundCategory.NEUTRAL, 3, 2);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new SwimNavigation(this, world);
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(this.getMovementSpeed(), movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.0025, 0.0));
            }
        } else
            super.travel(movementInput);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80)
                .add(EntityAttributes.GENERIC_ARMOR, 10)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5);
    }
}