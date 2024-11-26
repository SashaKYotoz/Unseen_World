package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.sashakyotoz.common.entities.ai.FertilizeGoal;
import net.sashakyotoz.common.entities.ai.WatcherAttackGoal;
import net.sashakyotoz.common.tags.ModTags;

public class HarmonyWatcherEntity extends PathAwareEntity {
    public final AnimationState death = new AnimationState();
    public final AnimationState fertilize = new AnimationState();

    public HarmonyWatcherEntity.Type watcherType;

    public boolean isAngry;

    public HarmonyWatcherEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.getString("entityType") != null) {
            for (Type type : Type.values()) {
                if (type.typeName.equals(nbt.getString("entityType"))) {
                    this.watcherType = type;
                }
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.watcherType != null)
            nbt.putString("entityType", this.watcherType.typeName);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.watcherType == null) {
            if (this.getWorld().getBiome(this.getBlockPos()).isIn(ModTags.Biomes.CURRANTSLATE_BOULDER_SPAWNS_ON))
                this.watcherType = Type.DARK_CURRANTSLATE;
            else if (this.getWorld().getBiome(this.getBlockPos()).isIn(ModTags.Biomes.HAS_AMETHYST_TREE))
                this.watcherType = HarmonyWatcherEntity.Type.TANZANITE;
            else
                this.watcherType = Type.GLACIEMITE;
        }
        if (this.age % 300 == 0 && this.isAngry)
            this.isAngry = false;
        if (this.goalSelector.getRunningGoals().anyMatch(goal -> goal.getGoal() instanceof FertilizeGoal) && !this.fertilize.isRunning())
            this.fertilize.start(this.age);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.75, 0.15f));
        this.goalSelector.add(2, new FertilizeGoal(this));
        this.goalSelector.add(2, new RevengeGoal(this, HarmonyWatcherEntity.class));
        this.goalSelector.add(3, new WatcherAttackGoal(this));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!isAngry)
            this.isAngry = true;
        return super.damage(source, amount);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.deathTime = -20;
        super.onDeath(damageSource);
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (!this.death.isRunning())
            this.death.start(this.age);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24)
                .add(EntityAttributes.GENERIC_ARMOR, 6)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    public static boolean canWatcherSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos blockPos = pos.down();
        return spawnReason == SpawnReason.SPAWNER || world.getBlockState(blockPos).allowsSpawning(world, blockPos, type) && random.nextInt(6) == 1;
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