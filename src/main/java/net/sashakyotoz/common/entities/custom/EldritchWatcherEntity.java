package net.sashakyotoz.common.entities.custom;

import net.minecraft.block.BlockState;
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
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ai.EldritchWatcherPlaceBlockGoal;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsManager;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EldritchWatcherEntity extends PathAwareEntity {
    private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(EldritchWatcherEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> CHARGE_TICKS = DataTracker.registerData(EldritchWatcherEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CARRING_BLOCK = DataTracker.registerData(EldritchWatcherEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    public final AnimationState attack = new AnimationState();
    public final AnimationState death = new AnimationState();

    public EldritchWatcherEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setStepHeight(1.5f);
        this.experiencePoints = 15;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CONVERTING, false);
        this.dataTracker.startTracking(CHARGE_TICKS, 0);
        this.dataTracker.startTracking(CARRING_BLOCK, false);
    }

    public void setCarriedBlock(boolean flag) {
        UnseenWorld.log(flag);
        this.dataTracker.set(CARRING_BLOCK, flag);
    }

    public boolean isCarringBlock() {
        return this.dataTracker.get(CARRING_BLOCK);
    }
    public static boolean canWatcherSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos blockPos = pos.down();
        return spawnReason == SpawnReason.SPAWNER
                || (world.getBlockState(blockPos).allowsSpawning(world, blockPos, type)
                && world.getEntitiesByType(TypeFilter.instanceOf(EspyerEntity.class), new Box(pos).expand(64), LivingEntity::isAlive).size() < 2
                && blockPos.getY() > -32
                && world.isPlayerInRange(pos.getX(), pos.getY(), pos.getZ(), 32));
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EldritchWatcherPlaceBlockGoal(this));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 0.75, 0.1f));
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 24.0F));
        this.targetSelector.add(2, new MeleeAttackGoal(this, 1, true));
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!(target instanceof LivingEntity)) {
            return false;
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
            this.playSound(SoundEvents.BLOCK_DEEPSLATE_BRICKS_FALL, 1.0F, this.getSoundPitch());
            return Hoglin.tryAttack(this, (LivingEntity) target);
        }
    }

    @Override
    protected void knockback(LivingEntity target) {
        Hoglin.knockback(this, target);
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status >= 4 && status <= 20){
            this.attack.start(this.age);
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 70)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 2)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2);
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
        if (itemStack.isOf(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().creativeMode && player instanceof ServerPlayerEntity player1)
                itemStack.damage(1, player1.getRandom(), player1);
            if (!this.getWorld().isClient)
                this.setConverting(player.getUuid(), this.random.nextInt(801) + 400);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null) {
            nbt.putUuid("ConversionPlayer", this.converter);
        }
        BlockState blockState = ModBlocks.GRIPCRYSTAL_WART.getDefaultState().with(Properties.FACING, Direction.UP);
        if (blockState != null) {
            nbt.put("carriedBlockState", NbtHelper.fromBlockState(blockState));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ConversionTime", NbtElement.NUMBER_TYPE) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.containsUuid("ConversionPlayer") ? nbt.getUuid("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
        BlockState blockState = null;
        if (nbt.contains("carriedBlockState", NbtElement.COMPOUND_TYPE)) {
            blockState = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("carriedBlockState"));
            if (blockState.isAir()) {
                blockState = null;
            }
        }

        this.setCarriedBlock(blockState != null);
    }

    public boolean isConverting() {
        return this.getDataTracker().get(CONVERTING);
    }

    public int getChargingTicks() {
        return this.getDataTracker().get(CHARGE_TICKS);
    }

    public void setChargingTicks(int tick) {
        this.getDataTracker().set(CHARGE_TICKS, tick);
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient && this.isAlive() && this.isConverting()) {
            this.conversionTimer--;
            if (this.conversionTimer <= 0)
                this.finishConversion((ServerWorld) this.getWorld());
        }
        if (this.isAlive() && this.getTarget() == null && this.age % 100 == 0) {
            if (this.getChargingTicks() < 2000 && !this.isCarringBlock()) {
                setChargingTicks(this.getChargingTicks() + 100);
                this.playSound(SoundEvents.BLOCK_BEACON_AMBIENT, 1.5f, 2);
                if (this.getWorld() instanceof ServerWorld world)
                    ActionsManager.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, world, this.getX(), this.getY() + 1.5f, this.getZ(), 2);
            }
        }
        if (this.getChargingTicks() > 2000) {
            this.setCarriedBlock(true);
            this.playSound(SoundEvents.BLOCK_BEACON_AMBIENT, 2f, 2.5f);
            setChargingTicks(0);
            if (this.getWorld() instanceof ServerWorld world)
                ActionsManager.spawnParticle(ModParticleTypes.GRIPPING_CRYSTAL, world, this.getX(), this.getY() + 1.5f, this.getZ(), 2.5f);
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

    private void finishConversion(ServerWorld world) {
        if (!this.isSilent())
            world.playSound(this, this.getBlockPos(), SoundEvents.BLOCK_DEEPSLATE_BRICKS_BREAK, SoundCategory.HOSTILE, 3, 2);
        if (this.converter != null) {
            PlayerEntity player = world.getPlayerByUuid(this.converter);
            if (player != null) {
                player.dropItem(ModItems.GRIPCRYSTAL);
                int random = this.getRandom().nextInt(4);
                for (int i = 0; i < random; i++) {
                    player.dropItem(ModBlocks.GLACIEMITE);
                }
                this.discard();
            }
        }
    }
}