package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.entities.bosses.BossLikePathfinderMob;
import net.sashakyotoz.common.entities.util.MinionOwnership;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ReaverEntity extends Monster implements NeutralMob, TraceableEntity {
    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(ReaverEntity.class, EntityDataSerializers.INT);

    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.05, AttributeModifier.Operation.ADDITION);
    private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
    private int playFirstAngerSoundIn;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
    private int ticksUntilNextAlert;

    public final AnimationState attack = new AnimationState();

    private final MinionOwnership ownership = new MinionOwnership(this, OWNER_ID);

    public ReaverEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        MinionOwnership.define(this.entityData, OWNER_ID);
    }

    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || entity instanceof BossLikePathfinderMob;
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt) {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.playSound(ModSoundEvents.REAVER_ATTACK, 1.0F, this.getVoicePitch());
        }
        return hurt;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attack.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return this.isBaby() ? 0.97F : 1.79F;
    }

    protected void customServerAiStep() {
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance != null) {
            if (this.isAngry()) {
                if (!this.isBaby() && !attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                    attributeInstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
                }
                this.maybePlayFirstAngerSound();
            } else if (attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                attributeInstance.removeModifier(SPEED_MODIFIER_ATTACKING);
            }
        }

        this.updatePersistentAnger((ServerLevel) this.level(), true);
        if (this.getTarget() != null) {
            this.maybeAlertOthers();
        }

        if (this.isAngry()) {
            this.lastHurtByPlayerTime = this.tickCount;
        }

        super.customServerAiStep();
    }

    private void maybePlayFirstAngerSound() {
        if (this.playFirstAngerSoundIn > 0) {
            --this.playFirstAngerSoundIn;
            if (this.playFirstAngerSoundIn == 0) {
                this.playAngerSound();
            }
        }
    }

    private void maybeAlertOthers() {
        if (this.ticksUntilNextAlert > 0) {
            --this.ticksUntilNextAlert;
        } else {
            if (this.getSensing().hasLineOfSight(this.getTarget())) {
                this.alertOthers();
            }
            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }
    }

    private void alertOthers() {
        double d = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aABB = AABB.unitCubeFromLowerCorner(this.position()).inflate(d, 10.0F, d);
        this.level().getEntitiesOfClass(ReaverEntity.class, aABB, EntitySelector.NO_SPECTATORS).stream().filter(
                (entity) -> entity != this).filter(
                (entity) -> entity.getTarget() == null).filter(
                (entity) -> !entity.isAlliedTo(this.getTarget())).forEach(
                (entity) -> entity.setTarget(this.getTarget()));
    }

    private void playAngerSound() {
        this.playSound(ModSoundEvents.REAVER_SCREAM, this.getSoundVolume(), this.getVoicePitch());
        this.playSound(ModSoundEvents.REAVER_GRIND, 0.6F, this.getVoicePitch());
    }

    @Override
    public void playAmbientSound() {
        super.playAmbientSound();
        if (!this.isSilent()) {
            this.playSound(ModSoundEvents.REAVER_GRIND, 0.4F, this.getVoicePitch());
        }
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        super.playHurtSound(source);
        this.playSound(ModSoundEvents.REAVER_CRACK, 0.7F, this.getVoicePitch());
    }

    @Override
    public void die(DamageSource source) {
        boolean firstDeath = !this.isRemoved() && !this.dead;
        super.die(source);
        if (firstDeath && !this.level().isClientSide()) {
            this.playSound(ModSoundEvents.REAVER_CRUMBLE, 0.8F, this.getVoicePitch());
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSoundEvents.REAVER_STEP, 1.0F, 1.0F);
    }

    public void setTarget(@Nullable LivingEntity target) {
        if (this.getTarget() == null && target != null) {
            this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.sample(this.random);
            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }

        if (target instanceof Player player) {
            this.setLastHurtByPlayer(player);
        }
        super.setTarget(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            boolean bl = source.getDirectEntity() instanceof ThrownPotion;
            if (!source.is(DamageTypeTags.IS_PROJECTILE) && !bl) {
                boolean bl2 = super.hurt(source, amount);
                if (!this.level().isClientSide() && !(source.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
                    this.teleport();
                }

                return bl2;
            } else {
                boolean bl2 = bl && this.hurtWithCleanWater(source, (ThrownPotion) source.getDirectEntity(), amount);

                for (int i = 0; i < 32; ++i) {
                    if (this.teleport()) {
                        return true;
                    }
                }

                return bl2;
            }
        }
    }

    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, y, z);

        while (mutableBlockPos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(mutableBlockPos).blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }

        BlockState blockState = this.level().getBlockState(mutableBlockPos);
        boolean bl = blockState.blocksMotion();
        boolean bl2 = blockState.getFluidState().is(FluidTags.WATER);
        if (bl && !bl2) {
            Vec3 vec3 = this.position();
            boolean bl3 = this.randomTeleport(x, y, z, true);
            if (bl3) {
                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level().playSound(null, this.xo, this.yo, this.zo, ModSoundEvents.REAVER_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(ModSoundEvents.REAVER_TELEPORT, 1.0F, 1.0F);
                    this.playSound(ModSoundEvents.REAVER_RESONATE, 0.6F, this.getVoicePitch());
                }
            }

            return bl3;
        } else {
            return false;
        }
    }

    private boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d = this.getX() + (this.random.nextDouble() - 0.5F) * 32.0F;
            double e = this.getY() + (this.random.nextInt(32) - 16);
            double f = this.getZ() + (this.random.nextDouble() - 0.5F) * 32.0F;
            return this.teleport(d, e, f);
        } else {
            return false;
        }
    }

    private boolean hurtWithCleanWater(DamageSource source, ThrownPotion potion, float amount) {
        ItemStack itemStack = potion.getItem();
        Potion potion2 = PotionUtils.getPotion(itemStack);
        List<MobEffectInstance> list = PotionUtils.getMobEffects(itemStack);
        return potion2 == Potions.WATER && list.isEmpty() && super.hurt(source, amount);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this) && !level.containsAnyLiquid(this.getBoundingBox());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
        this.ownership.save(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
        this.ownership.load(compound);
    }

    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? ModSoundEvents.REAVER_SCREAM : ModSoundEvents.REAVER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.REAVER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return ModSoundEvents.REAVER_DEATH;
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public boolean isPreventingPlayerRest(Player player) {
        return this.isAngryAt(player);
    }

    public void setOwner(@Nullable Entity owner) {
        this.ownership.set(owner);
    }

    @Override
    public @Nullable Entity getOwner() {
        return this.ownership.get();
    }
}