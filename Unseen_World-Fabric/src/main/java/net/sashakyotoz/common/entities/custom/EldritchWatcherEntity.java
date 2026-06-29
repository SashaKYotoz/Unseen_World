package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ai.goals.EldritchWatcherPlaceBlockGoal;
import net.sashakyotoz.common.entities.ai.listeners.EldritchEventListener;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiConsumer;

public class EldritchWatcherEntity extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(EldritchWatcherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CHARGE_TICKS = SynchedEntityData.defineId(EldritchWatcherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRYING_BLOCK = SynchedEntityData.defineId(EldritchWatcherEntity.class, EntityDataSerializers.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    public final AnimationState attack = new AnimationState();
    public final AnimationState death = new AnimationState();
    public final DynamicGameEventListener<EldritchEventListener> listener = new DynamicGameEventListener<>(new EldritchEventListener(this));

    public EldritchWatcherEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.setMaxUpStep(1.5f);
        this.xpReward = 15;
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        if (this.level() instanceof ServerLevel serverWorld)
            callback.accept(this.listener, serverWorld);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CONVERTING, false);
        this.entityData.define(CHARGE_TICKS, 0);
        this.entityData.define(CARRYING_BLOCK, false);
    }

    public void setCarriedBlock(boolean flag) {
        this.entityData.set(CARRYING_BLOCK, flag);
    }

    public boolean isCarringBlock() {
        return this.entityData.get(CARRYING_BLOCK);
    }

    public static boolean canWatcherSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        BlockPos blockPos = pos.below();
        return spawnReason == MobSpawnType.SPAWNER
                || (world.getBlockState(blockPos).isValidSpawn(world, blockPos, type)
                && world.getEntities(EntityTypeTest.forClass(EldritchWatcherEntity.class), new AABB(pos).inflate(64), LivingEntity::isAlive).size() < 3
                && blockPos.getY() > -32
                && blockPos.getY() < 96
                && world.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), 32));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EldritchWatcherPlaceBlockGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.75, 0.1f));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 4.0F));
        this.targetSelector.addGoal(2, new MeleeAttackGoal(this, 1, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && (mob.getTarget() != null && mob.distanceToSqr(mob.getTarget()) < 20);
            }
        });
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (!(target instanceof LivingEntity)) {
            return false;
        } else {
            this.level().broadcastEntityEvent(this, EntityEvent.START_ATTACKING);
            this.playSound(SoundEvents.DEEPSLATE_BRICKS_FALL, 1.0F, this.getVoicePitch());
            return HoglinBase.hurtAndThrowTarget(this, (LivingEntity) target);
        }
    }

    @Override
    protected void blockedByShield(LivingEntity target) {
        HoglinBase.throwTarget(this, target);
    }

    @Override
    public void handleEntityEvent(byte status) {
        super.handleEntityEvent(status);
        if (status >= 4 && status <= 20) {
            this.attack.start(this.tickCount);
        }
    }

    @Override
    protected void createWitherRose(@Nullable LivingEntity adversary) {
        if (adversary instanceof EldritchWatcherEntity && this.level() instanceof ServerLevel world) {
            world.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap(registry ->
                    registry.getHolder(ModConfiguredFeatures.GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR)).ifPresent(reference ->
                    reference.value().place(world, world.getChunkSource().getGenerator(),
                            RandomSource.create(this.getOnPos().asLong()), this.getOnPos().above()));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().instabuild && player instanceof ServerPlayer player1)
                itemStack.hurt(1, player1.getRandom(), player1);
            if (!this.level().isClientSide)
                this.setConverting(player.getUUID(), this.random.nextInt(801) + 400);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null) {
            nbt.putUUID("ConversionPlayer", this.converter);
        }
        BlockState blockState = ModBlocks.GRIPCRYSTAL_WART.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP);
        if (blockState != null) {
            nbt.put("carriedBlockState", NbtUtils.writeBlockState(blockState));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
        BlockState blockState = null;
        if (nbt.contains("carriedBlockState", Tag.TAG_COMPOUND)) {
            blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), nbt.getCompound("carriedBlockState"));
            if (blockState.isAir()) {
                blockState = null;
            }
        }

        this.setCarriedBlock(blockState != null);
    }

    public boolean isConverting() {
        return this.getEntityData().get(CONVERTING);
    }

    public int getChargingTicks() {
        return this.getEntityData().get(CHARGE_TICKS);
    }

    public void setChargingTicks(int tick) {
        this.getEntityData().set(CHARGE_TICKS, tick);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
            this.conversionTimer--;
            if (this.conversionTimer <= 0)
                this.finishConversion((ServerLevel) this.level());
        }
        if (this.isAlive() && this.getTarget() == null && this.tickCount % 100 == 0) {
            if (this.getChargingTicks() < 2000 && !this.isCarringBlock()) {
                setChargingTicks(this.getChargingTicks() + 100);
                this.playSound(SoundEvents.BEACON_AMBIENT, 1.5f, 2);
                ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, this.level(), this.getX(), this.getY() + 1.5f, this.getZ(), 2);
            }
        }
        if (this.getChargingTicks() > 2000) {
            this.setCarriedBlock(true);
            this.playSound(SoundEvents.BEACON_AMBIENT, 2f, 2.5f);
            setChargingTicks(0);
            ActionsUtils.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, this.level(), this.getX(), this.getY() + 1.5f, this.getZ(), 2.5f);
        }
        super.tick();
    }

    @Override
    public void die(DamageSource damageSource) {
        this.deathTime = -30;
        super.die(damageSource);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (!this.death.isStarted())
            this.death.start(this.tickCount);
    }

    private void finishConversion(ServerLevel world) {
        if (!this.isSilent())
            world.playSound(this, this.blockPosition(), SoundEvents.DEEPSLATE_BRICKS_BREAK, SoundSource.HOSTILE, 3, 2);
        if (this.converter != null) {
            Player player = world.getPlayerByUUID(this.converter);
            if (player != null) {
                player.spawnAtLocation(ModItems.GRIPCRYSTAL);
                int random = this.getRandom().nextInt(4);
                for (int i = 0; i < random; i++) {
                    player.spawnAtLocation(ModBlocks.GLACIEMITE);
                }
                this.discard();
            }
        }
    }
}