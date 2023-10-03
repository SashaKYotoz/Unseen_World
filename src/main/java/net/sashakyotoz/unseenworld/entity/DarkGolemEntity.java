
package net.sashakyotoz.unseenworld.entity;

import net.sashakyotoz.unseenworld.UnseenWorldModCommonConfigs;
import net.sashakyotoz.unseenworld.init.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.init.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.procedures.DarkGolemEntityDiesProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumSet;
import java.util.Objects;

public class DarkGolemEntity extends Monster {
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState attackBlockingAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);

    public DarkGolemEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(UnseenWorldModEntities.DARK_GOLEM.get(), world);
    }

    public DarkGolemEntity(EntityType<DarkGolemEntity> type, Level world) {
        super(type, world);
        xpReward = 25;
        setNoAi(false);
        setCustomName(Component.literal("Warrior of the Chimeric Darkness"));
        setCustomNameVisible(true);
        setPersistenceRequired();
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND,new ItemStack(UnseenWorldModItems.VOID_HAMMER.get()));
    }

    static boolean hurtAndThrowTarget(LivingEntity p_34643_, LivingEntity p_34644_) {
        float f = (float) p_34643_.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = p_34644_.hurt(p_34643_.damageSources().mobAttack(p_34643_), f);
        if (flag) {
            p_34643_.doEnchantDamageEffects(p_34643_, p_34644_);
                throwTarget(p_34643_, p_34644_);
        }
        return flag;
    }
    static void throwTarget(LivingEntity p_34646_, LivingEntity p_34647_) {
        double d0 = p_34646_.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double d1 = p_34647_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double d2 = 0.5D + d0 - d1;
        if (!(d2 <= 0.0D)) {
            double d3 = p_34647_.getX() - p_34646_.getX();
            double d4 = p_34647_.getZ() - p_34646_.getZ();
            float f = (float) (p_34646_.level().random.nextInt(21) - 8);
            double d5 = d2 * (double) (p_34646_.level().random.nextFloat() * 0.75F + 0.25F);
            Vec3 vec3 = (new Vec3(d3, 0.0D, d4)).normalize().scale(d5).yRot(f);
            double d6 = d2 * (double) p_34646_.level().random.nextFloat() * 0.5D;
            p_34647_.push(vec3.x, d6, vec3.z);
            p_34647_.hurtMarked = true;
        }
    }

    private boolean isMovingOnLand() {
        return this.onGround() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    public void tick() {
        if (this.level().isClientSide()) {
            if (this.isMovingOnLand()) {
                this.walkAnimationState.startIfStopped(this.tickCount);
            } else {
                this.walkAnimationState.stop();
            }
        }
        super.tick();
    }

    public boolean doHurtTarget(Entity p_34491_) {
        if (!(p_34491_ instanceof LivingEntity)) {
            return false;
        } else {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, this.getVoicePitch());
            return DarkGolemEntity.hurtAndThrowTarget(this, (LivingEntity) p_34491_);
        }
    }

    public void handleEntityEvent(byte p_219360_) {
        if (p_219360_ >= 4 && p_219360_ <= 20) {
            this.attackAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(p_219360_);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth() + 1.0D;
            }
        });
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new GolemLeapAtTargetGoal(this, (float) 0.5));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Player.class, false, true));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    static class GolemLeapAtTargetGoal extends Goal {
        private final Mob mob;
        private LivingEntity target;
        private final float yd;

        public GolemLeapAtTargetGoal(Mob p_25492_, float p_25493_) {
            this.mob = p_25492_;
            this.yd = p_25493_;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.isVehicle()) {
                return false;
            } else {
                this.target = this.mob.getTarget();
                if (this.target == null) {
                    return false;
                } else {
                    double d0 = this.mob.distanceToSqr(this.target);
                    if (!(d0 < 4.0D) && !(d0 > 16.0D)) {
                        if (!this.mob.onGround()) {
                            return false;
                        } else {
                            return this.mob.getRandom().nextInt(reducedTickDelay(8)) == 0;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            return !this.mob.onGround();
        }

        public void start() {
            Vec3 vec3 = this.mob.getDeltaMovement();
            Vec3 vec31 = new Vec3(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
            if (vec31.lengthSqr() > 1.0E-7D) {
                vec31 = vec31.normalize().scale(0.4D).add(vec3.scale(0.2D));
            }
            this.mob.setDeltaMovement(vec31.x, (double) this.yd, vec31.z);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.elder_guardian.ambient"));
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.step"))), 0.15f, 1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.death"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float hurtBack = Math.round((Math.random()));
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
        if(source.getEntity() instanceof Player)
        {
            if(hurtBack < 0.15 && amount > 6f)
            {
                attackBlockingAnimationState.start(this.tickCount);
                source.getEntity().hurt(new DamageSource(source.getEntity().level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), amount - 6f);
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        DarkGolemEntityDiesProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), source.getEntity());
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

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, UnseenWorldModCommonConfigs.DARK_GOLEM_SPEED.getDefault());
        builder = builder.add(Attributes.MAX_HEALTH,UnseenWorldModCommonConfigs.DARK_GOLEM_HEALTH_POINTS.getDefault());
        builder = builder.add(Attributes.ARMOR, 5);
        builder = builder.add(Attributes.ATTACK_DAMAGE, UnseenWorldModCommonConfigs.DARK_GOLEM_DAMAGE.getDefault());
        builder = builder.add(Attributes.FOLLOW_RANGE, 24);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 2);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.5);
        return builder;
    }
}
