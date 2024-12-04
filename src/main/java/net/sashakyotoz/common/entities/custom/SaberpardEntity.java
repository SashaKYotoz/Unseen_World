package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.IntFunction;

public class SaberpardEntity extends AnimalEntity implements VariantHolder<SaberpardEntity.Type> {
    private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(SaberpardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    private TemptGoal temptGoal;
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(Items.COD, Items.SALMON);
    private static final TrackedData<Integer> TYPE = DataTracker.registerData(SaberpardEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public SaberpardEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    public SaberpardEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        SaberpardEntity saberpard = ModEntities.SABERPARD.create(serverWorld);
        if (saberpard != null)
            saberpard.setVariant(this.random.nextBoolean() ? this.getVariant() : ((SaberpardEntity) passiveEntity).getVariant());
        return saberpard;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SaberpardSwimGoal());
        this.goalSelector.add(0, new PowderSnowJumpGoal(this, this.getWorld()));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8, 0.9f));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.add(3, new AttackGoal(this));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.6F));
        this.goalSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
        this.temptGoal = new TemptGoal(this, 0.6, TAMING_INGREDIENT, true);
        this.goalSelector.add(3, this.temptGoal);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if ((this.temptGoal == null || this.temptGoal.isActive()) && this.isBreedingItem(itemStack) && player.squaredDistanceTo(this) < 9.0) {
            this.eat(player, hand, itemStack);
            if (!this.getWorld().isClient) {
                if (this.random.nextInt(3) == 0) {
                    this.showEmoteParticle(true);
                    this.getWorld().sendEntityStatus(this, EntityStatuses.TAME_OCELOT_SUCCESS);
                } else {
                    this.showEmoteParticle(false);
                    this.getWorld().sendEntityStatus(this, EntityStatuses.TAME_OCELOT_FAILED);
                }
            }
            return ActionResult.success(this.getWorld().isClient);
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
        } else
            return super.interactMob(player, hand);
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

    private void showEmoteParticle(boolean positive) {
        ParticleEffect particleEffect = ParticleTypes.HEART;
        if (!positive)
            particleEffect = ParticleTypes.SMOKE;
        for (int i = 0; i < 7; i++) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.getWorld().addParticle(particleEffect, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, 0);
        this.dataTracker.startTracking(CONVERTING, false);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return spawnReason == SpawnReason.SPAWNER
                || (world.getBlockState(pos.down()).isIn(BlockTags.DIRT)
                || world.getBlockState(pos).isIn(BlockTags.DIRT));
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

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(Type.byName(nbt.getString("Type")));
        if (nbt.contains("ConversionTime", NbtElement.NUMBER_TYPE) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.containsUuid("ConversionPlayer") ? nbt.getUuid("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0);
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
        OcelotEntity ocelot = this.convertTo(EntityType.OCELOT, false);
        ocelot.initialize(world, world.getLocalDifficulty(ocelot.getBlockPos()), SpawnReason.CONVERSION, null, null);
        if (this.converter != null) {
            PlayerEntity player = world.getPlayerByUuid(this.converter);
            if (player != null)
                player.dropItem(ModItems.GRIPCRYSTAL);
            if (player instanceof ServerPlayerEntity player1)
                ModRegistry.CURED_GRIPCRYSTAL_ENTITY_CRITERION.trigger(player1, this, ocelot);
        }
        ocelot.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));
        if (!this.isSilent())
            world.playSound(this, this.getBlockPos(), SoundEvents.ENTITY_OCELOT_HURT, SoundCategory.NEUTRAL, 3, 2);
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

    public enum Type implements StringIdentifiable {
        JUNGLE(0, "jungle"),
        STEPPE(1, "steppe");

        public static final StringIdentifiable.Codec<SaberpardEntity.Type> CODEC = StringIdentifiable.createCodec(SaberpardEntity.Type::values);
        private static final IntFunction<SaberpardEntity.Type> BY_ID = ValueLists.createIdToValueFunction(
                SaberpardEntity.Type::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
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

        public static SaberpardEntity.Type byName(String name) {
            return CODEC.byId(name, JUNGLE);
        }

        public static SaberpardEntity.Type fromId(int id) {
            return BY_ID.apply(id);
        }

        public static SaberpardEntity.Type fromBiome(RegistryEntry<Biome> biome) {
            return biome.isIn(ModTags.Biomes.SPAWNS_STEPPE_SABERPARD) ? STEPPE : JUNGLE;
        }
    }

    class SaberpardSwimGoal extends SwimGoal {
        public SaberpardSwimGoal() {
            super(SaberpardEntity.this);
        }

        @Override
        public boolean canStart() {
            return SaberpardEntity.this.isTouchingWater() && SaberpardEntity.this.getFluidHeight(FluidTags.WATER) > 0.25 || SaberpardEntity.this.isInLava();
        }
    }
}