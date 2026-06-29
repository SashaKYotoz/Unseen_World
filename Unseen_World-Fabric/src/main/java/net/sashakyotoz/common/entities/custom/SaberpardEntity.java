package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.IntFunction;

public class SaberpardEntity extends Animal implements VariantHolder<SaberpardEntity.Type> {
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SaberpardEntity.class, EntityDataSerializers.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;
    private TemptGoal temptGoal;
    private static final Ingredient TAMING_INGREDIENT = Ingredient.of(Items.COD, Items.SALMON);
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(SaberpardEntity.class, EntityDataSerializers.INT);

    public SaberpardEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Nullable
    public SaberpardEntity getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
        SaberpardEntity saberpard = ModEntities.SABERPARD.create(serverWorld);
        if (saberpard != null)
            saberpard.setVariant(this.random.nextBoolean() ? this.getVariant() : ((SaberpardEntity) passiveEntity).getVariant());
        return saberpard;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SaberpardSwimGoal());
        this.goalSelector.addGoal(0, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8, 0.9f));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.6F));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.temptGoal = new TemptGoal(this, 0.6, TAMING_INGREDIENT, true);
        this.goalSelector.addGoal(3, this.temptGoal);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if ((this.temptGoal == null || this.temptGoal.isRunning()) && this.isFood(itemStack) && player.distanceToSqr(this) < 9.0) {
            this.usePlayerItem(player, hand, itemStack);
            if (!this.level().isClientSide) {
                if (this.random.nextInt(3) == 0) {
                    this.showEmoteParticle(true);
                    this.level().broadcastEntityEvent(this, EntityEvent.TRUSTING_SUCCEEDED);
                } else {
                    this.showEmoteParticle(false);
                    this.level().broadcastEntityEvent(this, EntityEvent.TRUSTING_FAILED);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
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
        } else
            return super.mobInteract(player, hand);
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

    private void showEmoteParticle(boolean positive) {
        ParticleOptions particleEffect = ParticleTypes.HEART;
        if (!positive)
            particleEffect = ParticleTypes.SMOKE;
        for (int i = 0; i < 7; i++) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particleEffect, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d, e, f);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, 0);
        this.entityData.define(CONVERTING, false);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(BlockTags.DIRT);
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

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(Type.byName(nbt.getString("Type")));
        if (nbt.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
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
        Ocelot ocelot = this.convertTo(EntityType.OCELOT, false);
        ActionsUtils.initializeConverting(this, ocelot, uuid);
        if (!this.isSilent())
            world.playSound(this, this.blockPosition(), SoundEvents.OCELOT_HURT, SoundSource.NEUTRAL, 3, 2);
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

    public enum Type implements StringRepresentable {
        JUNGLE(0, "jungle"),
        STEPPE(1, "steppe");

        public static final StringRepresentable.EnumCodec<SaberpardEntity.Type> CODEC = StringRepresentable.fromEnum(SaberpardEntity.Type::values);
        private static final IntFunction<SaberpardEntity.Type> BY_ID = ByIdMap.continuous(
                SaberpardEntity.Type::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO
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

        public static SaberpardEntity.Type byName(String name) {
            return CODEC.byName(name, JUNGLE);
        }

        public static SaberpardEntity.Type fromId(int id) {
            return BY_ID.apply(id);
        }

        public static SaberpardEntity.Type fromBiome(Holder<Biome> biome) {
            return biome.is(ModTags.Biomes.SPAWNS_STEPPE_SABERPARD) ? STEPPE : JUNGLE;
        }
    }

    class SaberpardSwimGoal extends FloatGoal {
        public SaberpardSwimGoal() {
            super(SaberpardEntity.this);
        }

        @Override
        public boolean canUse() {
            return SaberpardEntity.this.isInWater() && SaberpardEntity.this.getFluidHeight(FluidTags.WATER) > 0.25 || SaberpardEntity.this.isInLava();
        }
    }
}