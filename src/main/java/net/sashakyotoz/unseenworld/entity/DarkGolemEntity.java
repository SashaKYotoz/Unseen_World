
package net.sashakyotoz.unseenworld.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.UnseenWorldConfigs;
import net.sashakyotoz.unseenworld.entity.ai_goals.DarkGolemAttackGoal;
import net.sashakyotoz.unseenworld.managers.AdvancementManager;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DarkGolemEntity extends Monster implements Enemy {
    public AnimationState spawnAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState throwingHammerState = new AnimationState();
    public AnimationState attackWithHammerAnimationState = new AnimationState();
    public AnimationState attackBlockingAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;
    public int throwHammerTimer = 200;
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(DarkGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> THROWING_HAMMER = SynchedEntityData.defineId(DarkGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_CURRENT_ATTACK = SynchedEntityData.defineId(DarkGolemEntity.class, EntityDataSerializers.STRING);
    private final ServerBossEvent bossInfo = new ServerBossEvent(Component.translatable("entity.unseen_world.dark_golem").withStyle(ChatFormatting.DARK_AQUA), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);

    public DarkGolemEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldModEntities.DARK_GOLEM.get(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(THROWING_HAMMER, false);
        this.entityData.define(DATA_CURRENT_ATTACK, "unarmed");
    }

    protected void tickDeath() {
        if (!deathAnimationState.isStarted())
            this.deathAnimationState.start(this.tickCount);
        super.tickDeath();
    }

    public DarkGolemEntity(EntityType<DarkGolemEntity> type, Level world) {
        super(type, world);
        xpReward = XP_REWARD_BOSS;
        this.setMaxUpStep(1.5f);
        setNoAi(false);
        setPersistenceRequired();
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(UnseenWorldModItems.VOID_HAMMER.get()));
    }

    static boolean hurtAndThrowTarget(LivingEntity darkGolem, LivingEntity entity) {
        float f = (float) darkGolem.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = entity.hurt(darkGolem.damageSources().mobAttack(darkGolem), f);
        if (flag) {
            darkGolem.doEnchantDamageEffects(darkGolem, entity);
            throwTarget(darkGolem, entity);
        }
        return flag;
    }

    static void throwTarget(LivingEntity livingEntity, LivingEntity target) {
        double d0 = livingEntity.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double d1 = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double d2 = 0.5D + d0 - d1;
        if (!(d2 <= 0.0D)) {
            double d3 = target.getX() - livingEntity.getX();
            double d4 = target.getZ() - livingEntity.getZ();
            float f = (float) (livingEntity.level().random.nextInt(21) - 8);
            double d5 = d2 * (double) (livingEntity.level().random.nextFloat() * 0.75F + 0.25F);
            Vec3 vec3 = (new Vec3(d3, 0.0D, d4)).normalize().scale(d5).yRot(f);
            double d6 = d2 * (double) livingEntity.level().random.nextFloat() * 0.5D;
            target.push(vec3.x, d6, vec3.z);
            target.hurtMarked = true;
        }
    }

    private boolean isMovingOnLand() {
        return this.onGround() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    public void tick() {
        if (this.level().isClientSide()) {
            if (this.isMovingOnLand())
                this.walkAnimationState.startIfStopped(this.tickCount);
            else
                this.walkAnimationState.stop();
        }
        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 30;
            if (getAttackType().equals("unarmed"))
                this.attackAnimationState.start(this.tickCount);
            else
                this.attackWithHammerAnimationState.start(this.tickCount);
        } else
            attackAnimationTimeout--;
        if (!this.isAttacking()) {
            this.attackAnimationState.stop();
            this.attackWithHammerAnimationState.stop();
        }
        if (this.isThrowingHammer()) {
            this.navigation.stop();
            this.walkAnimationState.stop();
            this.attackAnimationState.stop();
            this.throwingHammerState.start(this.tickCount);
        }
        if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) >= 16) {
            if (this.throwHammerTimer > 0)
                this.throwHammerTimer--;
            else {
                throwHammer();
                this.throwHammerTimer += 200;
            }
        }
        setThrowingHammer();
        super.tick();
    }

    private void throwHammer() {
        UnseenWorldMod.queueServerWork(20, () -> {
            VoidHammerEntity thrownHammer = new VoidHammerEntity(this.level(), this);
            thrownHammer.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 4F, 1.0F);
            this.level().addFreshEntity(thrownHammer);
            this.level().playSound(null, thrownHammer, SoundEvents.ANVIL_BREAK, SoundSource.VOICE, 1.0F, 1.0F);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        });
        UnseenWorldMod.queueServerWork(100, () -> this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(UnseenWorldModItems.VOID_HAMMER.get())));
    }

    @Override
    public void animateHurt(float amount) {
        super.animateHurt(amount);
        if (this.getRandom().nextFloat() < 0.35f)
            attackBlockingAnimationState.start(this.tickCount);
    }

    public boolean doHurtTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        } else {
            if (random.nextBoolean())
                setAttackType("unarmed");
            else {
                setAttackType("armed");
                spawnParticle(this.level(), this.getX() - 0.25, this.getY(), this.getZ() - 0.25);
            }
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, this.getVoicePitch());
            return DarkGolemEntity.hurtAndThrowTarget(this, (LivingEntity) entity);
        }
    }

    private static void spawnParticle(Level world, double x, double y, double z) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.addParticle(ParticleTypes.ASH, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * (float) 2, 0.2d, Math.sin(i) * 0.25d * (float) 2);
        }
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setThrowingHammer() {
        this.entityData.set(THROWING_HAMMER, throwHammerTimer <= 0);
    }

    public boolean isThrowingHammer() {
        return this.entityData.get(THROWING_HAMMER);
    }

    public void setAttackType(String attackType) {
        this.entityData.set(DATA_CURRENT_ATTACK, attackType);
    }

    public String getAttackType() {
        return this.entityData.get(DATA_CURRENT_ATTACK);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new DarkGolemAttackGoal(this, 1.5, true, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, false, true));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ELDER_GUARDIAN_AMBIENT;
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15f, 1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DAMAGE;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float hurtBack = (float) Math.random();
        if (source.is(DamageTypes.CACTUS))
            return false;
        if (source.is(DamageTypes.DROWN))
            return false;
        if (source.is(DamageTypes.EXPLOSION))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.getMsgId().equals("witherSkull"))
            return false;
        if (source.getEntity() instanceof Player && this.getHealth() < this.getMaxHealth() / 1.5f) {
            if (hurtBack < 0.35 && amount > 6f) {
                attackBlockingAnimationState.start(this.tickCount);
                source.getEntity().hurt(this.damageSources().magic(), amount - 6f);
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        this.deathTime = -50;
        this.tickDeath();
        if (source.getEntity() instanceof Player player)
            AdvancementManager.addAdvancement(player, AdvancementManager.DARK_WARRIOR_ADV);
        this.spawnAtLocation(new ItemStack(UnseenWorldModItems.VOID_HAMMER.get()));
        int tmp = this.getRandom().nextInt(1, 4) + 1;
        this.spawnAtLocation(new ItemStack(UnseenWorldModItems.VOID_INGOT.get(), tmp));
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public static void init() {
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.spawnAnimationState.start(this.tickCount);
        if (!Objects.equals(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_DARK_WARRIOR.get(), UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_DARK_WARRIOR.getDefault()))
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_DARK_WARRIOR.get());
        this.setHealth(UnseenWorldConfigs.HEALTH_ATTRIBUTE_OF_DARK_WARRIOR.get().floatValue());
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
        builder = builder.add(Attributes.MAX_HEALTH, 300);
        builder = builder.add(Attributes.ARMOR, 6);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 10);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 2);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.5);
        return builder;
    }
}
