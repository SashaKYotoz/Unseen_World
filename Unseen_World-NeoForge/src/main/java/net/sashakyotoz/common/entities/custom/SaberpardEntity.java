package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.util.GripConversion;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class SaberpardEntity extends Animal implements VariantHolder<SaberpardEntity.Type> {
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SaberpardEntity.class, EntityDataSerializers.BOOLEAN);
    private final GripConversion conversion = new GripConversion(this, CONVERTING, this::finishConversion);
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
        }
        InteractionResult result = this.conversion.tryCrystieApple(player, hand, 2400, 2400);
        if (result == InteractionResult.PASS) {
            result = this.conversion.tryGriptonite(player, hand, 1200, 1200);
        }
        return result != InteractionResult.PASS ? result : super.mobInteract(player, hand);
    }

    public boolean isConverting() {
        return this.conversion.isConverting();
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TYPE, 0);
        GripConversion.define(builder, CONVERTING);
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
        this.conversion.save(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(Type.byName(nbt.getString("Type")));
        this.conversion.load(nbt);
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
        this.conversion.tick();
        super.tick();
    }

    private void finishConversion(ServerLevel world, GripConversion state) {
        Ocelot ocelot = this.convertTo(EntityType.OCELOT, false);
        if (ocelot != null) {
            ActionsUtils.initializeConverting(this, ocelot, state.converter());
        }
        if (!this.isSilent())
            world.playSound(null, this.blockPosition(), SoundEvents.OCELOT_HURT, SoundSource.NEUTRAL, 3, 2);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(
            ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData
    ) {
        Holder<Biome> registryEntry = world.getBiome(this.blockPosition());
        Type type = Type.fromBiome(registryEntry);
        this.setVariant(type);
        if (this.random.nextBoolean())
            this.setAge(-24000);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
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