package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.features.custom.NestDweller;
import org.jetbrains.annotations.Nullable;

public class GleamcarverEntity extends PathfinderMob implements NestDweller {
    private static final EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(GleamcarverEntity.class, EntityDataSerializers.INT);
    private static final String TYPE_KEY = "entityType";
    private static final String NEST_KEY = "NestPos";
    private static final int NO_TYPE = -1;
    private static final int REGROW_INTERVAL = 1000;
    private static final int NEST_RADIUS = 12;
    private static final int HARVEST_COOLDOWN = 40;

    public final AnimationState death = new AnimationState();
    public Type gleamcarverType;
    @Nullable
    private BlockPos nestPos;

    public GleamcarverEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE, NO_TYPE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_TYPE.equals(key)) {
            this.gleamcarverType = Type.byId(this.entityData.get(DATA_TYPE));
        }
    }

    public void setGleamcarverType(@Nullable Type type) {
        this.gleamcarverType = type;
        this.entityData.set(DATA_TYPE, type == null ? NO_TYPE : type.ordinal());
    }

    @Override
    public void bindToNest(BlockPos nest) {
        this.nestPos = nest.immutable();
        this.restrictTo(this.nestPos, NEST_RADIUS);
        this.setPersistenceRequired();
    }

    @Nullable
    public BlockPos getNestPos() {
        return this.nestPos;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setGleamcarverType(nbt.contains(TYPE_KEY) ? Type.byName(nbt.getString(TYPE_KEY)) : null);
        if (nbt.contains(NEST_KEY)) {
            this.bindToNest(NbtUtils.readBlockPos(nbt.getCompound(NEST_KEY)));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.gleamcarverType != null) {
            nbt.putString(TYPE_KEY, this.gleamcarverType.typeName);
        }
        if (this.nestPos != null) {
            nbt.put(NEST_KEY, NbtUtils.writeBlockPos(this.nestPos));
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof PickaxeItem) || this.gleamcarverType == null) {
            return super.mobInteract(player, hand);
        }
        ItemLike drop = harvestDrop(this.gleamcarverType);
        if (drop == null) {
            if (!this.level().isClientSide()) {
                this.level().playSound(null, this.blockPosition(), SoundEvents.DEEPSLATE_HIT, SoundSource.NEUTRAL, 2.0F, 2.0F);
            }
            return super.mobInteract(player, hand);
        }
        if (!this.level().isClientSide()) {
            this.spawnAtLocation(drop);
            this.setGleamcarverType(depleted(this.gleamcarverType));
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            player.getCooldowns().addCooldown(stack.getItem(), HARVEST_COOLDOWN);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(3) == 0 ? ModSoundEvents.GLEAMCARVER_AMBIENT_SOUND : null;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide()) {
            return;
        }
        if (this.gleamcarverType == null) {
            this.setGleamcarverType(typeFor(this.level().getBiome(this.blockPosition()), this.random));
        }
        if (this.tickCount % REGROW_INTERVAL == 0 && (this.isHome() || this.random.nextBoolean())) {
            Type regrown = regrow(this.gleamcarverType, this.random);
            if (regrown != this.gleamcarverType) {
                this.setGleamcarverType(regrown);
            }
        }
    }

    private boolean isHome() {
        return this.nestPos != null && this.isWithinRestriction();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return this.nestPos == null && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    public void die(DamageSource damageSource) {
        this.deathTime = -20;
        super.die(damageSource);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (!this.death.isStarted())
            this.death.start(this.tickCount);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.75D, 0.05F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    private static Type typeFor(Holder<Biome> biome, RandomSource random) {
        if (biome.is(ModBiomes.DEEP_GLACIEMITE_CAVES)) {
            return Type.ABYSSAL;
        }
        if (biome.is(ModBiomes.TANZANITE_CAVES)) {
            return Type.TANZANITE;
        }
        if (biome.is(ModBiomes.SHINY_CAVERNS)) {
            return Type.GLACIEMITE;
        }
        return random.nextBoolean() ? Type.DARK_CURRANTSLATE : Type.UNSEENIUM;
    }

    private static Type regrow(Type type, RandomSource random) {
        return switch (type) {
            case GLACIEMITE -> Type.ABYSSAL;
            case DARK_CURRANTSLATE -> random.nextBoolean() ? Type.TANZANITE : Type.UNSEENIUM;
            default -> type;
        };
    }

    @Nullable
    private static ItemLike harvestDrop(Type type) {
        return switch (type) {
            case ABYSSAL -> ModItems.RAW_ABYSSAL_ORE;
            case TANZANITE -> ModBlocks.TANZANITE_BLOCK;
            case UNSEENIUM -> ModItems.RAW_UNSEENIUM_ORE;
            default -> null;
        };
    }

    private static Type depleted(Type type) {
        return type == Type.ABYSSAL ? Type.GLACIEMITE : Type.DARK_CURRANTSLATE;
    }

    public enum Type {
        GLACIEMITE("glaciemite"),
        DARK_CURRANTSLATE("dark_currantslate"),
        ABYSSAL("abyssal"),
        UNSEENIUM("unseenium"),
        TANZANITE("tanzanite");
        public final String typeName;

        Type(String nameOfType) {
            typeName = nameOfType;
        }

        @Nullable
        public static Type byName(String name) {
            for (Type type : values()) {
                if (type.typeName.equals(name)) {
                    return type;
                }
            }
            return null;
        }

        @Nullable
        public static Type byId(int id) {
            return id >= 0 && id < values().length ? values()[id] : null;
        }
    }
}