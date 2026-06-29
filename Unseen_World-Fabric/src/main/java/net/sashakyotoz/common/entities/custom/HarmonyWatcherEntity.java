package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.sashakyotoz.common.entities.ai.goals.FertilizeGoal;
import net.sashakyotoz.common.entities.ai.goals.WatcherAttackGoal;
import net.sashakyotoz.common.tags.ModTags;

public class HarmonyWatcherEntity extends PathfinderMob {
    public final AnimationState death = new AnimationState();
    public final AnimationState fertilize = new AnimationState();

    public HarmonyWatcherEntity.Type watcherType;

    public boolean isAngry;

    public HarmonyWatcherEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.getString("entityType") != null) {
            for (Type type : Type.values()) {
                if (type.typeName.equals(nbt.getString("entityType"))) {
                    this.watcherType = type;
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.watcherType != null)
            nbt.putString("entityType", this.watcherType.typeName);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.watcherType == null) {
            if (this.level().getBiome(this.blockPosition()).is(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON))
                this.watcherType = Type.DARK_CURRANTSLATE;
            else if (this.level().getBiome(this.blockPosition()).is(ModTags.Biomes.HAS_AMETHYST_TREE))
                this.watcherType = HarmonyWatcherEntity.Type.TANZANITE;
            else
                this.watcherType = Type.GLACIEMITE;
        }
        if (this.tickCount % 300 == 0 && this.isAngry)
            this.isAngry = false;
        if (this.goalSelector.getRunningGoals().anyMatch(goal -> goal.getGoal() instanceof FertilizeGoal) && !this.fertilize.isStarted())
            this.fertilize.start(this.tickCount);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.75, 0.15f));
        this.goalSelector.addGoal(2, new FertilizeGoal(this));
        this.goalSelector.addGoal(2, new HurtByTargetGoal(this, HarmonyWatcherEntity.class));
        this.goalSelector.addGoal(3, new WatcherAttackGoal(this));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!isAngry)
            this.isAngry = true;
        return super.hurt(source, amount);
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24)
                .add(Attributes.ARMOR, 6)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static boolean canWatcherSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        BlockPos blockPos = pos.below();
        return spawnReason == MobSpawnType.SPAWNER || world.getBlockState(blockPos).isValidSpawn(world, blockPos, type) && random.nextInt(6) == 1;
    }

    public enum Type {
        GLACIEMITE("glaciemite"),
        DARK_CURRANTSLATE("dark_currantslate"),
        TANZANITE("tanzanite");
        public final String typeName;

        Type(String nameOfType) {
            typeName = nameOfType;
        }
    }
}