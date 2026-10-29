package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RimehornEntity extends Animal {
    public static final byte GRAZE_EVENT = 91;
    public static final byte CHARGE_EVENT = 92;
    public static final int GRAZE_TICKS = 40;
    public static final int CHARGE_TICKS = 25;
    private static final int PAW_TICK = 12;
    private static final int RAM_COOLDOWN = 200;
    private static final double HERD_RADIUS = 12.0D;
    private static final float VOICE_PITCH = 0.8F;

    public final AnimationState graze = new AnimationState();
    public final AnimationState charge = new AnimationState();
    private int grazeStartTick;
    private int chargeStartTick;
    @Nullable
    private LivingEntity ramTarget;
    private int ramCooldown;

    public RimehornEntity(EntityType<? extends RimehornEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RimehornPanicGoal(this));
        this.goalSelector.addGoal(2, new RamOnceGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.1D, Ingredient.of(ModTags.Items.RIMEHORN_TAMING_ITEMS), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new GrazeGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModTags.Items.RIMEHORN_TAMING_ITEMS);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.RIMEHORN.create(level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = spawnData == null ? new AgeableMob.AgeableMobGroupData(0.2F) : spawnData;
        return super.finalizeSpawn(level, difficulty, reason, data);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide() && source.getEntity() instanceof LivingEntity attacker && isValidRamTarget(attacker)) {
            this.alertHerd(attacker);
        }
        return result;
    }

    private static boolean isValidRamTarget(@Nullable LivingEntity entity) {
        if (entity == null || !entity.isAlive() || entity instanceof RimehornEntity) {
            return false;
        }
        return !(entity instanceof Player player) || (!player.isCreative() && !player.isSpectator());
    }

    private void alertHerd(LivingEntity attacker) {
        for (RimehornEntity member : this.level().getEntitiesOfClass(RimehornEntity.class, this.getBoundingBox().inflate(HERD_RADIUS, 4.0D, HERD_RADIUS))) {
            if (!member.isBaby() && member.ramCooldown <= 0 && member.ramTarget == null) {
                member.ramTarget = attacker;
            }
        }
    }

    public boolean isDefending() {
        return this.ramTarget != null;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide() && this.ramCooldown > 0) {
            this.ramCooldown--;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.graze.isStarted() && this.tickCount - this.grazeStartTick > GRAZE_TICKS) {
                this.graze.stop();
            }
            if (this.charge.isStarted() && this.tickCount - this.chargeStartTick > CHARGE_TICKS) {
                this.charge.stop();
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == GRAZE_EVENT) {
            this.grazeStartTick = this.tickCount;
            this.graze.start(this.tickCount);
        } else if (id == CHARGE_EVENT) {
            this.chargeStartTick = this.tickCount;
            this.graze.stop();
            this.charge.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public float getVoicePitch() {
        float base = this.isBaby() ? 1.25F : VOICE_PITCH;
        return base + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GOAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.GOAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GOAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.GOAT_STEP, 0.15F, VOICE_PITCH);
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return SoundEvents.GOAT_EAT;
    }

    private static boolean isGrazingGround(BlockState state) {
        return state.is(BlockTags.SNOW) || state.is(BlockTags.DIRT) || state.is(BlockTags.GOATS_SPAWNABLE_ON) || state.is(ModBlocks.GLACIEMITE);
    }

    private BlockState grazedState() {
        BlockState feet = this.level().getBlockState(this.blockPosition());
        return feet.is(BlockTags.SNOW) ? feet : this.level().getBlockState(this.blockPosition().below());
    }

    static class RimehornPanicGoal extends PanicGoal {
        private final RimehornEntity rimehorn;

        RimehornPanicGoal(RimehornEntity rimehorn) {
            super(rimehorn, 1.6D);
            this.rimehorn = rimehorn;
        }

        @Override
        protected boolean shouldPanic() {
            if (this.rimehorn.isFreezing() || this.rimehorn.isOnFire()) {
                return true;
            }
            if (this.rimehorn.getLastHurtByMob() == null) {
                return false;
            }
            return this.rimehorn.isBaby() || (!this.rimehorn.isDefending() && this.rimehorn.ramCooldown > 0);
        }
    }

    static class GrazeGoal extends Goal {
        private final RimehornEntity rimehorn;
        private int timer;

        GrazeGoal(RimehornEntity rimehorn) {
            this.rimehorn = rimehorn;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (this.rimehorn.getRandom().nextInt(reducedTickDelay(this.rimehorn.isBaby() ? 200 : 600)) != 0) {
                return false;
            }
            return this.rimehorn.onGround() && !this.rimehorn.isDefending() && isGrazingGround(this.rimehorn.level().getBlockState(this.rimehorn.blockPosition().below()));
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer > 0 && !this.rimehorn.isDefending();
        }

        @Override
        public void start() {
            this.timer = adjustedTickDelay(GRAZE_TICKS);
            this.rimehorn.getNavigation().stop();
            this.rimehorn.level().broadcastEntityEvent(this.rimehorn, GRAZE_EVENT);
        }

        @Override
        public void stop() {
            this.timer = 0;
        }

        @Override
        public void tick() {
            this.timer = Math.max(0, this.timer - 1);
            int elapsed = adjustedTickDelay(GRAZE_TICKS) - this.timer;
            if (elapsed == adjustedTickDelay(PAW_TICK) || elapsed == adjustedTickDelay(PAW_TICK + 10)) {
                this.paw();
            }
            if (this.timer == 0 && this.rimehorn.isBaby()) {
                this.rimehorn.ageUp(30);
            }
        }

        private void paw() {
            BlockState state = this.rimehorn.grazedState();
            if (state.isAir() || !(this.rimehorn.level() instanceof ServerLevel serverLevel)) {
                return;
            }
            float yaw = this.rimehorn.yBodyRot * ((float) Math.PI / 180.0F);
            double x = this.rimehorn.getX() - Math.sin(yaw) * 0.6D;
            double z = this.rimehorn.getZ() + Math.cos(yaw) * 0.6D;
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), x, this.rimehorn.getY() + 0.1D, z, 8, 0.15D, 0.05D, 0.15D, 0.1D);
            this.rimehorn.playSound(state.getSoundType().getHitSound(), 0.6F, 0.9F);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(BlockTags.GOATS_SPAWNABLE_ON) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
    }

    static class RamOnceGoal extends Goal {
        private static final int GIVE_UP_TICKS = 140;
        private static final int WINDUP_TICKS = 8;
        private static final int DASH_TICKS = 12;
        private static final double CHARGE_START_DISTANCE_SQR = 36.0D;
        private static final double DASH_SPEED = 0.55D;
        private static final float RAM_KNOCKBACK = 1.5F;

        private final RimehornEntity rimehorn;
        private Phase phase = Phase.APPROACH;
        private int timer;
        private int phaseTimer;
        private Vec3 dashDirection = Vec3.ZERO;

        RamOnceGoal(RimehornEntity rimehorn) {
            this.rimehorn = rimehorn;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !this.rimehorn.isBaby() && isValidRamTarget(this.rimehorn.ramTarget)
                    && this.rimehorn.distanceToSqr(this.rimehorn.ramTarget) < 256.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return this.phase != Phase.DONE && this.timer < GIVE_UP_TICKS && isValidRamTarget(this.rimehorn.ramTarget);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.phase = Phase.APPROACH;
            this.timer = 0;
            this.phaseTimer = 0;
        }

        @Override
        public void stop() {
            this.rimehorn.ramTarget = null;
            this.rimehorn.ramCooldown = RAM_COOLDOWN;
            this.rimehorn.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.rimehorn.ramTarget;
            if (target == null) {
                this.phase = Phase.DONE;
                return;
            }
            this.timer++;
            this.phaseTimer++;
            this.rimehorn.getLookControl().setLookAt(target, 30.0F, 30.0F);
            switch (this.phase) {
                case APPROACH -> {
                    if (this.phaseTimer % 10 == 1) {
                        this.rimehorn.getNavigation().moveTo(target, 1.4D);
                    }
                    if (this.rimehorn.distanceToSqr(target) <= CHARGE_START_DISTANCE_SQR && this.rimehorn.hasLineOfSight(target)) {
                        this.rimehorn.getNavigation().stop();
                        this.rimehorn.level().broadcastEntityEvent(this.rimehorn, CHARGE_EVENT);
                        this.rimehorn.playSound(SoundEvents.GOAT_PREPARE_RAM, 1.0F, VOICE_PITCH);
                        this.enter(Phase.WINDUP);
                    }
                }
                case WINDUP -> {
                    if (this.phaseTimer >= WINDUP_TICKS) {
                        Vec3 offset = new Vec3(target.getX() - this.rimehorn.getX(), 0.0D, target.getZ() - this.rimehorn.getZ());
                        this.dashDirection = offset.lengthSqr() < 1.0E-4D ? Vec3.directionFromRotation(0.0F, this.rimehorn.getYRot()) : offset.normalize();
                        this.enter(Phase.DASH);
                    }
                }
                case DASH -> {
                    Vec3 motion = this.rimehorn.getDeltaMovement();
                    this.rimehorn.setDeltaMovement(this.dashDirection.x * DASH_SPEED, motion.y, this.dashDirection.z * DASH_SPEED);
                    if (this.rimehorn.getBoundingBox().inflate(0.3D).intersects(target.getBoundingBox())) {
                        this.impact(target);
                        this.phase = Phase.DONE;
                    } else if (this.phaseTimer >= DASH_TICKS) {
                        this.phase = Phase.DONE;
                    }
                }
                default -> {
                }
            }
        }

        private void enter(Phase next) {
            this.phase = next;
            this.phaseTimer = 0;
        }

        private void impact(LivingEntity target) {
            float damage = (float) this.rimehorn.getAttributeValue(Attributes.ATTACK_DAMAGE);
            if (target.hurt(this.rimehorn.damageSources().mobAttack(this.rimehorn), damage)) {
                target.knockback(RAM_KNOCKBACK, -this.dashDirection.x, -this.dashDirection.z);
                if (this.rimehorn.level() instanceof ServerLevel server) {
                    EnchantmentHelper.doPostAttackEffects(server, target, this.rimehorn.damageSources().mobAttack(this.rimehorn));
                }
            }
            this.rimehorn.playSound(SoundEvents.GOAT_RAM_IMPACT, 1.0F, VOICE_PITCH);
            this.rimehorn.setDeltaMovement(this.rimehorn.getDeltaMovement().multiply(0.2D, 1.0D, 0.2D));
        }

        private enum Phase {
            APPROACH,
            WINDUP,
            DASH,
            DONE
        }
    }
}