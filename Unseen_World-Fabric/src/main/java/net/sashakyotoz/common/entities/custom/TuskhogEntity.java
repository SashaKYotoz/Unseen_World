package net.sashakyotoz.common.entities.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.brain.TuskhogBrain;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.IntFunction;

public class TuskhogEntity extends AnimalEntity implements VariantHolder<TuskhogEntity.Type> {
    private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(TuskhogEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(Items.COD, Items.SALMON);
    private static final TrackedData<Integer> TYPE = DataTracker.registerData(TuskhogEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final EntityDimensions LONG_JUMPING_DIMENSIONS = EntityDimensions.changing(0.9F, 1.3F).scaled(0.7F);
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
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATE_RECENTLY,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.LONG_JUMP_COOLING_DOWN,
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

    public TuskhogEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.getNavigation().setCanSwim(true);
    }

    @Override
    protected void onGrowUp() {
        if (this.isBaby())
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0);
        else
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2.0);
    }

    @Override
    protected void initGoals() {
        TemptGoal temptGoal = new TemptGoal(this, 0.6, TAMING_INGREDIENT, true);
        this.goalSelector.add(3, temptGoal);
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return super.computeFallDamage(fallDistance, damageMultiplier) - 10;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GOAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_GOAT_STEP, 0.15F, 1.0F);
    }

    @Nullable
    public TuskhogEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        TuskhogEntity tuskhog = ModEntities.TUSKHOG.create(serverWorld);
        if (tuskhog != null)
            TuskhogBrain.resetLongJumpCooldown(tuskhog, serverWorld.getRandom());
        return tuskhog;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected Brain.Profile<TuskhogEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return TuskhogBrain.create(this.createBrainProfile().deserialize(dynamic));
    }

    @Override
    protected void mobTick() {
        this.getWorld().getProfiler().push("hogBrain");
        if (this.getWorld() instanceof ServerWorld world)
            this.getBrain().tick(world, this);
        this.getWorld().getProfiler().pop();
        this.getWorld().getProfiler().push("hogActivityUpdate");
        TuskhogBrain.updateActivities(this);
        this.getWorld().getProfiler().pop();
        super.mobTick();
    }

    @Override
    public int getMaxHeadRotation() {
        return 15;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<TuskhogEntity> getBrain() {
        return (Brain<TuskhogEntity>) super.getBrain();
    }

    @Override
    public void setHeadYaw(float headYaw) {
        int i = this.getMaxHeadRotation();
        float f = MathHelper.subtractAngles(this.bodyYaw, headYaw);
        float g = MathHelper.clamp(f, (float) (-i), (float) i);
        super.setHeadYaw(this.bodyYaw + g);
    }

    @Override
    public SoundEvent getEatSound(ItemStack stack) {
        return SoundEvents.ENTITY_GOAT_EAT;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        ActionResult actionResult = super.interactMob(player, hand);
        if (actionResult.isAccepted() && this.isBreedingItem(itemStack)) {
            this.getWorld()
                    .playSoundFromEntity(null, this, this.getEatSound(itemStack), SoundCategory.NEUTRAL, 1.0F, MathHelper.nextBetween(this.getWorld().random, 0.8F, 1.2F));
        } else if (itemStack.isOf(ModItems.CRYSTIE_APPLE)) {
            if (this.hasStatusEffect(StatusEffects.GLOWING)) {
                if (!player.getAbilities().creativeMode)
                    itemStack.decrement(1);
                if (!this.getWorld().isClient)
                    this.setConverting(player.getUuid(), this.random.nextInt(2401) + 2400);
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
        return actionResult;
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return pose == EntityPose.LONG_JUMPING ? LONG_JUMPING_DIMENSIONS.scaled(this.getScaleFactor()) : super.getDimensions(pose);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PREPARE_RAM) {
            this.preparingRam = true;
        } else if (status == EntityStatuses.FINISH_RAM) {
            this.preparingRam = false;
        } else {
            super.handleStatus(status);
        }
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

    private void finishConversion(ServerWorld world) {
        PigEntity pig = this.convertTo(EntityType.PIG, false);
        pig.initialize(world, world.getLocalDifficulty(pig.getBlockPos()), SpawnReason.CONVERSION, null, null);
        if (this.converter != null) {
            PlayerEntity player = world.getPlayerByUuid(this.converter);
            if (player != null)
                player.dropItem(ModItems.GRIPCRYSTAL);
            if (player instanceof ServerPlayerEntity player1)
                ModRegistry.CURED_GRIPCRYSTAL_ENTITY_CRITERION.trigger(player1, this, pig);
        }
        pig.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
        if (!this.isSilent())
            world.playSound(this, this.getBlockPos(), SoundEvents.ENTITY_OCELOT_HURT, SoundCategory.NEUTRAL, 3, 2);
    }

    @Override
    public void tickMovement() {
        if (this.preparingRam)
            this.headPitch++;
        else
            this.headPitch -= 2;
        this.headPitch = MathHelper.clamp(this.headPitch, 0, 20);
        super.tickMovement();
    }

    public float getHeadPitch() {
        return (float) this.headPitch / 20.0F * 30.0F * (float) (Math.PI / 180.0);
    }

    private void setConverting(@Nullable UUID uuid, int delay) {
        this.converter = uuid;
        this.conversionTimer = delay;
        this.getDataTracker().set(CONVERTING, true);
        this.removeStatusEffect(StatusEffects.GLOWING);
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, delay, Math.min(this.getWorld().getDifficulty().getId() - 1, 0)));
        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_SPLASH_PARTICLES);
    }

    public boolean isConverting() {
        return this.getDataTracker().get(CONVERTING);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, 0);
        this.dataTracker.startTracking(CONVERTING, false);
    }

    @Override
    public void setVariant(Type variant) {
        this.dataTracker.set(TYPE, variant.getId());
    }

    @Override
    public Type getVariant() {
        return Type.fromId(this.dataTracker.get(TYPE));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Type", this.getVariant().asString());
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null)
            nbt.putUuid("ConversionPlayer", this.converter);
    }

    @Nullable
    @Override
    public EntityData initialize(
            ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt
    ) {
        RegistryEntry<Biome> registryEntry = world.getBiome(this.getBlockPos());
        Type type = Type.fromBiome(registryEntry);
        this.setVariant(type);
        if (this.random.nextBoolean())
            this.setBreedingAge(-24000);
        this.initEquipment(world.getRandom(), difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(Type.byName(nbt.getString("Type")));
        if (nbt.contains("ConversionTime", NbtElement.NUMBER_TYPE) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.containsUuid("ConversionPlayer") ? nbt.getUuid("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(BlockTags.DIRT)
                && world.getEntitiesByType(TypeFilter.instanceOf(TuskhogEntity.class), new Box(pos).expand(64), LivingEntity::isAlive).size() < 4
                && random.nextInt(4) == 1;
    }

    public enum Type implements StringIdentifiable {
        WARM(0, "warm"),
        TEMPERATE(1, "temperate");

        public static final StringIdentifiable.Codec<Type> CODEC = StringIdentifiable.createCodec(Type::values);
        private static final IntFunction<Type> BY_ID = ValueLists.createIdToValueFunction(
                Type::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
        );
        private final int id;
        private final String key;

        Type(int id, String key) {
            this.id = id;
            this.key = key;
        }

        @Override
        public String asString() {
            return this.key;
        }

        public int getId() {
            return this.id;
        }

        public static Type byName(String name) {
            return CODEC.byId(name, TEMPERATE);
        }

        public static Type fromId(int id) {
            return BY_ID.apply(id);
        }

        public static Type fromBiome(RegistryEntry<Biome> biome) {
            return biome.isIn(ModTags.Biomes.SPAWNS_WARM_TUSKHOG) ? WARM : TEMPERATE;
        }
    }
}