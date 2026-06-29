package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.ai.goals.EspyerIgniteGoal;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

public class EspyerEntity extends Monster implements PowerableMob {
    private static final EntityDataAccessor<Integer> FUSE_SPEED = SynchedEntityData.defineId(EspyerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CHARGED = SynchedEntityData.defineId(EspyerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(EspyerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(EspyerEntity.class, EntityDataSerializers.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    private int lastFuseTime;
    private int currentFuseTime;
    private int fuseTime = 30;
    private int explosionRadius = 3;

    public EspyerEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EspyerIgniteGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, SaberpardEntity.class, 8.0F, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 24.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 22)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        boolean bl = super.causeFallDamage(fallDistance, damageMultiplier, damageSource);
        this.currentFuseTime += (int) (fallDistance * 1.5F);
        if (this.currentFuseTime > this.fuseTime - 5) {
            this.currentFuseTime = this.fuseTime - 5;
        }

        return bl;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FUSE_SPEED, -1);
        this.entityData.define(CHARGED, false);
        this.entityData.define(IGNITED, false);
        this.entityData.define(CONVERTING, false);
    }

    public boolean isConverting() {
        return this.getEntityData().get(CONVERTING);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.entityData.get(CHARGED))
            nbt.putBoolean("powered", true);
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null)
            nbt.putUUID("ConversionPlayer", this.converter);
        nbt.putShort("Fuse", (short) this.fuseTime);
        nbt.putByte("ExplosionRadius", (byte) this.explosionRadius);
        nbt.putBoolean("ignited", this.isIgnited());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
        this.entityData.set(CHARGED, nbt.getBoolean("powered"));
        if (nbt.contains("Fuse", Tag.TAG_ANY_NUMERIC))
            this.fuseTime = nbt.getShort("Fuse");
        if (nbt.contains("ExplosionRadius", Tag.TAG_ANY_NUMERIC))
            this.explosionRadius = nbt.getByte("ExplosionRadius");
        if (nbt.getBoolean("ignited"))
            this.ignite();
    }

    private void setConverting(@Nullable UUID uuid, int delay) {
        this.converter = uuid;
        this.conversionTimer = delay;
        this.getEntityData().set(CONVERTING, true);
        this.removeEffect(MobEffects.GLOWING);
        this.addEffect(new MobEffectInstance(MobEffects.DARKNESS, delay, Math.min(this.level().getDifficulty().getId() - 1, 0)));
        this.level().broadcastEntityEvent(this, EntityEvent.VILLAGER_SWEAT);
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            this.lastFuseTime = this.currentFuseTime;
            if (this.isIgnited()) {
                this.setFuseSpeed(1);
            }
            int i = this.getFuseSpeed();
            if (i > 0 && this.currentFuseTime == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }
            this.currentFuseTime += i;
            if (this.currentFuseTime < 0) {
                this.currentFuseTime = 0;
            }
            if (this.currentFuseTime >= this.fuseTime) {
                this.currentFuseTime = this.fuseTime;
                this.explode();
            }
            if (this.getTarget() != null && !this.isEntityLookingAtMe(this.getTarget(), 0.5, false, true, LivingEntity::isPickable, this::getEyeY, this::getY, () -> (this.getEyeY() + this.getY()) / 2.0))
                this.getNavigation().moveTo(this.getTarget(), 1);
            else
                this.getNavigation().stop();
            if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
                this.conversionTimer--;
                if (this.conversionTimer <= 0)
                    this.finishConversion((ServerLevel) this.level());
            }
        }
        super.tick();
    }

    private void finishConversion(ServerLevel world) {
        Creeper creeper = this.convertTo(EntityType.CREEPER, false);
        ActionsUtils.initializeConverting(this, creeper, uuid);
        if (!this.isSilent())
            world.playSound(this, this.blockPosition(), SoundEvents.CREEPER_HURT, SoundSource.NEUTRAL, 2, 2);
    }

    public boolean isEntityLookingAtMe(LivingEntity entity, double d, boolean bl, boolean visualShape, Predicate<LivingEntity> predicate, DoubleSupplier... entityYChecks) {
        if (predicate.test(entity)) {
            Vec3 vec3d = entity.getViewVector(1.0F).normalize();
            for (DoubleSupplier doubleSupplier : entityYChecks) {
                Vec3 vec3d2 = new Vec3(this.getX() - entity.getX(), doubleSupplier.getAsDouble() - entity.getEyeY(), this.getZ() - entity.getZ());
                double e = vec3d2.length();
                vec3d2 = vec3d2.normalize();
                double f = vec3d.dot(vec3d2);
                if (f > 1.0 - d / (bl ? e : 1.0))
                    return ActionsUtils.canSee(this, visualShape ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, doubleSupplier);
            }
        }
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    @Override
    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return true;
    }

    @Override
    public boolean isPowered() {
        return this.entityData.get(CHARGED);
    }

    public float getClientFuseTime(float timeDelta) {
        return Mth.lerp(timeDelta, (float) this.lastFuseTime, (float) this.currentFuseTime) / (float) (this.fuseTime - 2);
    }

    public int getFuseSpeed() {
        return this.entityData.get(FUSE_SPEED);
    }

    public void setFuseSpeed(int fuseSpeed) {
        this.entityData.set(FUSE_SPEED, fuseSpeed);
    }

    @Override
    public void thunderHit(ServerLevel world, LightningBolt lightning) {
        super.thunderHit(world, lightning);
        this.entityData.set(CHARGED, true);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent soundEvent = itemStack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            this.level().playSound(player, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level().isClientSide) {
                this.ignite();
                if (!itemStack.isDamageableItem()) {
                    itemStack.shrink(1);
                } else {
                    itemStack.hurtAndBreak(1, player, playerx -> playerx.broadcastBreakEvent(hand));
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (itemStack.is(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().instabuild && player instanceof ServerPlayer player1)
                itemStack.hurt(1, player1.getRandom(), player1);
            if (!this.level().isClientSide)
                this.setConverting(player.getUUID(), this.random.nextInt(1201) + 600);
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    private void explode() {
        if (!this.level().isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, Level.ExplosionInteraction.MOB);
            this.discard();
            this.spawnEffectsCloud();
        }
    }

    public static boolean canEspyerSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        BlockPos blockPos = pos.below();
        return spawnReason == MobSpawnType.SPAWNER
                || (world.getBlockState(blockPos).isValidSpawn(world, blockPos, type)
                && world.getEntities(EntityTypeTest.forClass(EspyerEntity.class), new AABB(pos).inflate(64), LivingEntity::isAlive).size() < 3
                && blockPos.getY() < 32
                && !world.canSeeSky(pos)
                && world.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), 32));
    }

    private void spawnEffectsCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaEffectCloudEntity = getAreaEffectCloudEntity();
            for (MobEffectInstance statusEffectInstance : collection)
                areaEffectCloudEntity.addEffect(new MobEffectInstance(statusEffectInstance));
            this.level().addFreshEntity(areaEffectCloudEntity);
        }
    }

    private @NotNull AreaEffectCloud getAreaEffectCloudEntity() {
        AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        areaEffectCloudEntity.setRadius(3F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
        areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
        return areaEffectCloudEntity;
    }

    public boolean isIgnited() {
        return this.entityData.get(IGNITED);
    }

    public void ignite() {
        this.entityData.set(IGNITED, true);
    }
}