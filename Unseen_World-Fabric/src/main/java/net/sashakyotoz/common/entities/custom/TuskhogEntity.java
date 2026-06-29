package net.sashakyotoz.common.entities.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.brain.TuskhogBrain;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.IntFunction;

public class TuskhogEntity extends Animal implements VariantHolder<TuskhogEntity.Type> {
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(TuskhogEntity.class, EntityDataSerializers.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    private static final Ingredient TAMING_INGREDIENT = Ingredient.of(Items.COD, Items.SALMON);
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(TuskhogEntity.class, EntityDataSerializers.INT);
    public static final EntityDimensions LONG_JUMPING_DIMENSIONS = EntityDimensions.scalable(0.9F, 1.3F).scale(0.7F);
    protected static final ImmutableList<SensorType<? extends Sensor<? super TuskhogEntity>>> SENSORS = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ITEMS,
            SensorType.NEAREST_ADULT,
            SensorType.HURT_BY,
            SensorType.GOAT_TEMPTATIONS
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATE_RECENTLY,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS,
            MemoryModuleType.LONG_JUMP_MID_JUMP,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.RAM_COOLDOWN_TICKS,
            MemoryModuleType.RAM_TARGET,
            MemoryModuleType.IS_PANICKING
    );
    private boolean preparingRam;
    private int headPitch;

    public TuskhogEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
        this.getNavigation().setCanFloat(true);
    }

    @Override
    protected void ageBoundaryReached() {
        if (this.isBaby())
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.0);
        else
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0);
    }

    @Override
    protected void registerGoals() {
        TemptGoal temptGoal = new TemptGoal(this, 0.6, TAMING_INGREDIENT, true);
        this.goalSelector.addGoal(3, temptGoal);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 10;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GOAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.GOAT_STEP, 0.15F, 1.0F);
    }

    @Nullable
    public TuskhogEntity getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
        TuskhogEntity tuskhog = ModEntities.TUSKHOG.create(serverWorld);
        if (tuskhog != null)
            TuskhogBrain.resetLongJumpCooldown(tuskhog, serverWorld.getRandom());
        return tuskhog;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected Brain.Provider<TuskhogEntity> brainProvider() {
        return Brain.provider(MEMORY_MODULES, SENSORS);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return TuskhogBrain.create(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("hogBrain");
        if (this.level() instanceof ServerLevel world)
            this.getBrain().tick(world, this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("hogActivityUpdate");
        TuskhogBrain.updateActivities(this);
        this.level().getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    public int getMaxHeadYRot() {
        return 15;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<TuskhogEntity> getBrain() {
        return (Brain<TuskhogEntity>) super.getBrain();
    }

    @Override
    public void setYHeadRot(float headYaw) {
        int i = this.getMaxHeadYRot();
        float f = Mth.degreesDifference(this.yBodyRot, headYaw);
        float g = Mth.clamp(f, (float) (-i), (float) i);
        super.setYHeadRot(this.yBodyRot + g);
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return SoundEvents.GOAT_EAT;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        InteractionResult actionResult = super.mobInteract(player, hand);
        if (actionResult.consumesAction() && this.isFood(itemStack)) {
            this.level()
                    .playSound(null, this, this.getEatingSound(itemStack), SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(this.level().random, 0.8F, 1.2F));
        } else if (itemStack.is(ModItems.CRYSTIE_APPLE)) {
            if (this.hasEffect(MobEffects.GLOWING)) {
                if (!player.getAbilities().instabuild)
                    itemStack.shrink(1);
                if (!this.level().isClientSide)
                    this.setConverting(player.getUUID(), this.random.nextInt(2401) + 2400);
                return InteractionResult.SUCCESS;
            } else
                return InteractionResult.CONSUME;
        } else if (itemStack.is(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().instabuild && player instanceof ServerPlayer player1)
                itemStack.hurt(1, player1.getRandom(), player1);
            if (!this.level().isClientSide)
                this.setConverting(player.getUUID(), this.random.nextInt(1201) + 1200);
            return InteractionResult.SUCCESS;
        }
        return actionResult;
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return pose == Pose.LONG_JUMPING ? LONG_JUMPING_DIMENSIONS.scale(this.getScale()) : super.getDimensions(pose);
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.START_RAM) {
            this.preparingRam = true;
        } else if (status == EntityEvent.END_RAM) {
            this.preparingRam = false;
        } else {
            super.handleEntityEvent(status);
        }
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
            this.conversionTimer--;
            if (this.conversionTimer <= 0)
                this.finishConversion((ServerLevel) this.level());
        }
        super.tick();
    }

    private void finishConversion(ServerLevel world) {
        Pig pig = this.convertTo(EntityType.PIG, false);
        ActionsUtils.initializeConverting(this, pig, uuid);
        if (!this.isSilent())
            world.playSound(this, this.blockPosition(), SoundEvents.OCELOT_HURT, SoundSource.NEUTRAL, 3, 2);
    }

    @Override
    public void aiStep() {
        if (this.preparingRam)
            this.headPitch++;
        else
            this.headPitch -= 2;
        this.headPitch = Mth.clamp(this.headPitch, 0, 20);
        super.aiStep();
    }

    public float getHeadPitch() {
        return (float) this.headPitch / 20.0F * 30.0F * (float) (Math.PI / 180.0);
    }

    private void setConverting(@Nullable UUID uuid, int delay) {
        this.converter = uuid;
        this.conversionTimer = delay;
        this.getEntityData().set(CONVERTING, true);
        this.removeEffect(MobEffects.GLOWING);
        this.addEffect(new MobEffectInstance(MobEffects.DARKNESS, delay, Math.min(this.level().getDifficulty().getId() - 1, 0)));
        this.level().broadcastEntityEvent(this, EntityEvent.VILLAGER_SWEAT);
    }

    public boolean isConverting() {
        return this.getEntityData().get(CONVERTING);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, 0);
        this.entityData.define(CONVERTING, false);
    }

    @Override
    public void setVariant(Type variant) {
        this.entityData.set(TYPE, variant.getId());
    }

    @Override
    public Type getVariant() {
        return Type.fromId(this.entityData.get(TYPE));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Type", this.getVariant().getSerializedName());
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null)
            nbt.putUUID("ConversionPlayer", this.converter);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityNbt
    ) {
        Holder<Biome> registryEntry = world.getBiome(this.blockPosition());
        Type type = Type.fromBiome(registryEntry);
        this.setVariant(type);
        if (this.random.nextBoolean())
            this.setAge(-24000);
        this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(Type.byName(nbt.getString("Type")));
        if (nbt.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(BlockTags.DIRT)
                && world.getEntities(EntityTypeTest.forClass(TuskhogEntity.class), new AABB(pos).inflate(64), LivingEntity::isAlive).size() < 4
                && random.nextInt(4) == 1;
    }

    public enum Type implements StringRepresentable {
        WARM(0, "warm"),
        TEMPERATE(1, "temperate");

        public static final StringRepresentable.EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private static final IntFunction<Type> BY_ID = ByIdMap.continuous(
                Type::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO
        );
        private final int id;
        private final String key;

        Type(int id, String key) {
            this.id = id;
            this.key = key;
        }

        @Override
        public String getSerializedName() {
            return this.key;
        }

        public int getId() {
            return this.id;
        }

        public static Type byName(String name) {
            return CODEC.byName(name, TEMPERATE);
        }

        public static Type fromId(int id) {
            return BY_ID.apply(id);
        }

        public static Type fromBiome(Holder<Biome> biome) {
            return biome.is(ModTags.Biomes.SPAWNS_WARM_TUSKHOG) ? WARM : TEMPERATE;
        }
    }
}