package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.entities.ai.goals.FertilizeGoal;
import net.sashakyotoz.common.entities.ai.goals.WatcherAttackGoal;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

public class HarmonyWatcherEntity extends PathfinderMob {
    private static final EntityDataAccessor<Byte> WATCHER_TYPE = SynchedEntityData.defineId(HarmonyWatcherEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(HarmonyWatcherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final String TYPE_KEY = "WatcherType";
    private static final String LEGACY_TYPE_KEY = "entityType";
    private static final String ANGER_KEY = "AngerTicks";
    private static final int ANGER_DURATION = 300;
    private static final byte UNASSIGNED = -1;
    public static final byte FERTILIZE_EVENT = 96;

    public final AnimationState fertilize = new AnimationState();

    private int angerTicks;
    private boolean wasFertilizing;

    public HarmonyWatcherEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WATCHER_TYPE, UNASSIGNED);
        this.entityData.define(ANGRY, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WatcherAttackGoal(this));
        this.goalSelector.addGoal(2, new FertilizeGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.75D, 0.15F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, HarmonyWatcherEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false, player -> this.isOrdealSummoned()));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
        if (!this.hasWatcherType()) {
            this.setWatcherType(typeForBiome(level.getBiome(this.blockPosition())));
        }
        return data;
    }

    public static Type typeForBiome(Holder<Biome> biome) {
        if (biome.is(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON)) {
            return Type.DARK_CURRANTSLATE;
        }
        if (biome.is(ModTags.Biomes.HAS_AMETHYST_TREE)) {
            return Type.TANZANITE;
        }
        return Type.GLACIEMITE;
    }

    public boolean hasWatcherType() {
        return this.entityData.get(WATCHER_TYPE) != UNASSIGNED;
    }

    public Type getWatcherType() {
        return Type.byId(this.entityData.get(WATCHER_TYPE));
    }

    public void setWatcherType(Type type) {
        this.entityData.set(WATCHER_TYPE, (byte) type.ordinal());
    }

    public boolean isOrdealSummoned() {
        return this.getTags().contains(OrdealSpawnerBlockEntity.ORDEAL_TAG);
    }

    public boolean isAngry() {
        return this.entityData.get(ANGRY);
    }

    private void setAngry(boolean angry) {
        if (this.entityData.get(ANGRY) != angry) {
            this.entityData.set(ANGRY, angry);
        }
    }

    public void provoke() {
        this.angerTicks = ANGER_DURATION;
        this.setAngry(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            return;
        }
        if (!this.hasWatcherType()) {
            this.setWatcherType(typeForBiome(this.level().getBiome(this.blockPosition())));
        }
        if (this.isOrdealSummoned()) {
            this.setAngry(true);
            return;
        }
        if (this.angerTicks > 0) {
            this.angerTicks--;
            if (this.getTarget() != null && this.getTarget().isAlive()) {
                this.angerTicks = Math.max(this.angerTicks, 1);
            }
        }
        if (this.angerTicks == 0 && this.isAngry()) {
            this.setAngry(false);
            this.setTarget(null);
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null && !this.level().isClientSide()) {
            this.provoke();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && source.getEntity() instanceof LivingEntity) {
            this.provoke();
        }
        return result;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isOrdealSummoned() && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }
        boolean fertilizing = this.goalSelector.getRunningGoals().anyMatch(goal -> goal.getGoal() instanceof FertilizeGoal);
        if (fertilizing && !this.wasFertilizing) {
            this.level().broadcastEntityEvent(this, FERTILIZE_EVENT);
        }
        this.wasFertilizing = fertilizing;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == FERTILIZE_EVENT) {
            this.fertilize.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.hasWatcherType()) {
            compound.putString(TYPE_KEY, this.getWatcherType().getSerializedName());
        }
        compound.putInt(ANGER_KEY, this.angerTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        String key = compound.contains(TYPE_KEY) ? TYPE_KEY : LEGACY_TYPE_KEY;
        Type type = Type.byName(compound.getString(key));
        if (type != null) {
            this.setWatcherType(type);
        }
        this.angerTicks = Math.max(0, compound.getInt(ANGER_KEY));
        this.setAngry(this.angerTicks > 0);
    }

    public enum Type implements StringRepresentable {
        GLACIEMITE("glaciemite"),
        DARK_CURRANTSLATE("dark_currantslate"),
        TANZANITE("tanzanite");

        private static final Type[] VALUES = values();
        public final String typeName;

        Type(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public String getSerializedName() {
            return this.typeName;
        }

        public static Type byId(int id) {
            return id >= 0 && id < VALUES.length ? VALUES[id] : GLACIEMITE;
        }

        @Nullable
        public static Type byName(String name) {
            for (Type type : VALUES) {
                if (type.typeName.equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}