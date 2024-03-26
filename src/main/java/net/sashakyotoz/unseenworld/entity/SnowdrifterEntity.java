package net.sashakyotoz.unseenworld.entity;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.PlayMessages;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SnowdrifterEntity extends Animal implements NeutralMob {
    private static final EntityDimensions UNDER_SNOW_DIMENSION = EntityDimensions.scalable(0.25f, 0.25f);
    public final AnimationState digging = new AnimationState();
    public final AnimationState attack = new AnimationState();
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 100);
    private static final UniformInt UNDER_SNOW_COOLDOWN = TimeUtil.rangeOfSeconds(15, 100);
    private int underSnowTimer;
    @Nullable
    private UUID persistentAngerTarget;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(SnowdrifterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> UNDER_SNOW = SynchedEntityData.defineId(SnowdrifterEntity.class, EntityDataSerializers.BOOLEAN);
    public SnowdrifterEntity(EntityType<? extends SnowdrifterEntity> type, Level level) {
        super(type, level);
        setMaxUpStep(1f);
        this.xpReward = 5;
        this.underSnowTimer = UNDER_SNOW_COOLDOWN.sample(this.getRandom());
    }
    public SnowdrifterEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(UnseenWorldModEntities.SNOWDRIFTER.get(), level);
    }
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        this.entityData.define(UNDER_SNOW, false);
    }
    public static void init() {
        SpawnPlacements.register(UnseenWorldModEntities.SNOWDRIFTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return UnseenWorldModEntities.SNOWDRIFTER.get().create(level);
    }
    public boolean isUnderSnow(){
        return this.entityData.get(UNDER_SNOW);
    }
    public EntityDimensions getDimensions(Pose pose) {
        return this.isUnderSnow() ? UNDER_SNOW_DIMENSION.scale(this.getScale()) : super.getDimensions(pose);
    }
    private void setUnderSnow(boolean b){
        if (!b){
            this.setDeltaMovement(0,0.5f,0);
            this.entityData.set(UNDER_SNOW,false);
        }
        else {
            if (this.level().getBlockState(this.getOnPos().below()).is(Blocks.SNOW_BLOCK)){
                this.digging.start(this.tickCount);
                UnseenWorldMod.queueServerWork(20,()-> this.entityData.set(UNDER_SNOW,true));
            }
        }
    }

    @Override
    public int calculateFallDamage(float p_21237_, float p_21238_) {
        return super.calculateFallDamage(p_21237_, p_21238_) / 2;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getHealth() < 6 && !this.hasEffect(MobEffects.REGENERATION)){
            this.underSnowTimer = 0;
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100,2,false,false));
        }
        return !this.isUnderSnow() && super.hurt(source, amount);
    }

    private void clientDiggingParticles() {
        if (this.isUnderSnow()) {
            RandomSource randomsource = this.getRandom();
            BlockState blockstate = this.getBlockStateOn();
            if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                for (int i = 0; i < 30; ++i) {
                    double d0 = this.getX() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    double d1 = this.getY() + 0.5f;
                    double d2 = this.getZ() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
    @Override
    public void baseTick() {
        if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) > 8 && !this.attack.isStarted())
            this.attack.start(this.tickCount);
        if (this.isUnderSnow() && this.tickCount % 5 == 0){
            clientDiggingParticles();
        }
        if (this.underSnowTimer > 0)
            this.underSnowTimer--;
        else {
            this.underSnowTimer = UNDER_SNOW_COOLDOWN.sample(this.getRandom());
            this.setUnderSnow(!this.isUnderSnow());
        }
        super.baseTick();
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(1,new FollowParentGoal(this,1.0));
        this.goalSelector.addGoal(2,new RandomStrollGoal(this,1.5){
            @Override
            public void setInterval(int i) {
                super.setInterval(SnowdrifterEntity.this.isUnderSnow() ? 30 : i);
            }
        });
        this.targetSelector.addGoal(1, new ResetUniversalAngerTargetGoal<>(this, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new MeleeAttackGoal(this,1,true));
        super.registerGoals();
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int i) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, i);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.getRandom()));
    }
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }
    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 16);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 6);
        builder = builder.add(Attributes.FOLLOW_RANGE, 24);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2);
        return builder;
    }
}
