package net.sashakyotoz.common.entities.custom;

import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.entities.ai.EspyerIgniteGoal;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

public class EspyerEntity extends HostileEntity implements SkinOverlayOwner {
    private static final TrackedData<Integer> FUSE_SPEED = DataTracker.registerData(EspyerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CHARGED = DataTracker.registerData(EspyerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IGNITED = DataTracker.registerData(EspyerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(EspyerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    private int lastFuseTime;
    private int currentFuseTime;
    private int fuseTime = 30;
    private int explosionRadius = 3;

    public EspyerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new EspyerIgniteGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, SaberpardEntity.class, 8.0F, 1.0, 1.2));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 24.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new RevengeGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 22)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        boolean bl = super.handleFallDamage(fallDistance, damageMultiplier, damageSource);
        this.currentFuseTime += (int) (fallDistance * 1.5F);
        if (this.currentFuseTime > this.fuseTime - 5) {
            this.currentFuseTime = this.fuseTime - 5;
        }

        return bl;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FUSE_SPEED, -1);
        this.dataTracker.startTracking(CHARGED, false);
        this.dataTracker.startTracking(IGNITED, false);
        this.dataTracker.startTracking(CONVERTING, false);
    }

    public boolean isConverting() {
        return this.getDataTracker().get(CONVERTING);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.dataTracker.get(CHARGED))
            nbt.putBoolean("powered", true);
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null) {
            nbt.putUuid("ConversionPlayer", this.converter);
        }
        nbt.putShort("Fuse", (short) this.fuseTime);
        nbt.putByte("ExplosionRadius", (byte) this.explosionRadius);
        nbt.putBoolean("ignited", this.isIgnited());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ConversionTime", NbtElement.NUMBER_TYPE) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.containsUuid("ConversionPlayer") ? nbt.getUuid("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
        this.dataTracker.set(CHARGED, nbt.getBoolean("powered"));
        if (nbt.contains("Fuse", NbtElement.NUMBER_TYPE))
            this.fuseTime = nbt.getShort("Fuse");
        if (nbt.contains("ExplosionRadius", NbtElement.NUMBER_TYPE))
            this.explosionRadius = nbt.getByte("ExplosionRadius");
        if (nbt.getBoolean("ignited"))
            this.ignite();
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
    public void tick() {
        if (this.isAlive()) {
            this.lastFuseTime = this.currentFuseTime;
            if (this.isIgnited()) {
                this.setFuseSpeed(1);
            }
            int i = this.getFuseSpeed();
            if (i > 0 && this.currentFuseTime == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
                this.emitGameEvent(GameEvent.PRIME_FUSE);
            }
            this.currentFuseTime += i;
            if (this.currentFuseTime < 0) {
                this.currentFuseTime = 0;
            }
            if (this.currentFuseTime >= this.fuseTime) {
                this.currentFuseTime = this.fuseTime;
                this.explode();
            }
            if (this.getTarget() != null && !this.isEntityLookingAtMe(this.getTarget(), 0.5, false, true, LivingEntity::canHit, this::getEyeY, this::getY, () -> (this.getEyeY() + this.getY()) / 2.0))
                this.getNavigation().startMovingTo(this.getTarget(), 1);
            else
                this.getNavigation().stop();
            if (!this.getWorld().isClient && this.isAlive() && this.isConverting()) {
                this.conversionTimer--;
                if (this.conversionTimer <= 0)
                    this.finishConversion((ServerWorld) this.getWorld());
            }
        }
        super.tick();
    }
    private void finishConversion(ServerWorld world) {
        CreeperEntity creeper = this.convertTo(EntityType.CREEPER, false);
        creeper.initialize(world, world.getLocalDifficulty(creeper.getBlockPos()), SpawnReason.CONVERSION, null, null);
        if (this.converter != null) {
            PlayerEntity player = world.getPlayerByUuid(this.converter);
            if (player != null)
                player.dropItem(ModItems.GRIPCRYSTAL);
            if (player instanceof ServerPlayerEntity player1)
                ModRegistry.CURED_GRIPCRYSTAL_ENTITY_CRITERION.trigger(player1, this, creeper);
        }
        creeper.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
        if (!this.isSilent())
            world.playSound(this, this.getBlockPos(), SoundEvents.ENTITY_CREEPER_HURT, SoundCategory.NEUTRAL, 2, 2);
    }
    public boolean isEntityLookingAtMe(LivingEntity entity, double d, boolean bl, boolean visualShape, Predicate<LivingEntity> predicate, DoubleSupplier... entityYChecks) {
        if (predicate.test(entity)) {
            Vec3d vec3d = entity.getRotationVec(1.0F).normalize();
            for (DoubleSupplier doubleSupplier : entityYChecks) {
                Vec3d vec3d2 = new Vec3d(this.getX() - entity.getX(), doubleSupplier.getAsDouble() - entity.getEyeY(), this.getZ() - entity.getZ());
                double e = vec3d2.length();
                vec3d2 = vec3d2.normalize();
                double f = vec3d.dotProduct(vec3d2);
                if (f > 1.0 - d / (bl ? e : 1.0))
                    return canSee(this, visualShape ? RaycastContext.ShapeType.VISUAL : RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, doubleSupplier);
            }
        }
        return false;
    }

    public boolean canSee(Entity entity, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling, DoubleSupplier entityY) {
        if (entity.getWorld() != this.getWorld()) {
            return false;
        } else {
            Vec3d vec3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
            Vec3d vec3d2 = new Vec3d(entity.getX(), entityY.getAsDouble(), entity.getZ());
            if (vec3d2.distanceTo(vec3d) > 128.0) {
                return false;
            } else {
                return this.getWorld().raycast(new RaycastContext(vec3d, vec3d2, shapeType, fluidHandling, this)).getType() == HitResult.Type.MISS;
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    @Override
    public int getSafeFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return true;
    }

    @Override
    public boolean shouldRenderOverlay() {
        return this.dataTracker.get(CHARGED);
    }

    public float getClientFuseTime(float timeDelta) {
        return MathHelper.lerp(timeDelta, (float) this.lastFuseTime, (float) this.currentFuseTime) / (float) (this.fuseTime - 2);
    }

    public int getFuseSpeed() {
        return this.dataTracker.get(FUSE_SPEED);
    }

    public void setFuseSpeed(int fuseSpeed) {
        this.dataTracker.set(FUSE_SPEED, fuseSpeed);
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        super.onStruckByLightning(world, lightning);
        this.dataTracker.set(CHARGED, true);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isIn(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent soundEvent = itemStack.isOf(Items.FIRE_CHARGE) ? SoundEvents.ITEM_FIRECHARGE_USE : SoundEvents.ITEM_FLINTANDSTEEL_USE;
            this.getWorld().playSound(player, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundCategory(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.getWorld().isClient) {
                this.ignite();
                if (!itemStack.isDamageable()) {
                    itemStack.decrement(1);
                } else {
                    itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
                }
            }

            return ActionResult.success(this.getWorld().isClient);
        } else if (itemStack.isOf(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().creativeMode && player instanceof ServerPlayerEntity player1)
                itemStack.damage(1, player1.getRandom(), player1);
            if (!this.getWorld().isClient)
                this.setConverting(player.getUuid(), this.random.nextInt(1201) + 600);
            return ActionResult.SUCCESS;
        } else {
            return super.interactMob(player, hand);
        }
    }

    private void explode() {
        if (!this.getWorld().isClient) {
            float f = this.shouldRenderOverlay() ? 2.0F : 1.0F;
            this.dead = true;
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, World.ExplosionSourceType.MOB);
            this.discard();
            this.spawnEffectsCloud();
        }
    }

    public static boolean canEspyerSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos blockPos = pos.down();
        return spawnReason == SpawnReason.SPAWNER
                || (world.getBlockState(blockPos).allowsSpawning(world, blockPos, type)
                && world.getEntitiesByType(TypeFilter.instanceOf(EspyerEntity.class), new Box(pos).expand(64), LivingEntity::isAlive).size() < 3
                && blockPos.getY() < 32
                && !world.isSkyVisible(pos)
                && world.isPlayerInRange(pos.getX(), pos.getY(), pos.getZ(), 32));
    }

    private void spawnEffectsCloud() {
        Collection<StatusEffectInstance> collection = this.getStatusEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaEffectCloudEntity = getAreaEffectCloudEntity();
            for (StatusEffectInstance statusEffectInstance : collection)
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
            this.getWorld().spawnEntity(areaEffectCloudEntity);
        }
    }

    private @NotNull AreaEffectCloudEntity getAreaEffectCloudEntity() {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY(), this.getZ());
        areaEffectCloudEntity.setRadius(3F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
        areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
        return areaEffectCloudEntity;
    }

    public boolean isIgnited() {
        return this.dataTracker.get(IGNITED);
    }

    public void ignite() {
        this.dataTracker.set(IGNITED, true);
    }
}