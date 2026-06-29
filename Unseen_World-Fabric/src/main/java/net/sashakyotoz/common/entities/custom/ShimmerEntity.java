package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class ShimmerEntity extends WaterAnimal {
    public float tiltAngle;
    public float prevTiltAngle;
    public float rollAngle;
    public float prevRollAngle;

    public float thrustTimer;
    public float prevThrustTimer;

    public float tentacleAngle;
    public float prevTentacleAngle;

    private float swimVelocityScale;
    private float thrustTimerSpeed;
    private float turningSpeed;
    private float swimX;
    private float swimY;
    private float swimZ;

    public ShimmerEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
        this.random.setSeed(this.getId());
        this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new ShimmerEntity.EscapeAttackerGoal());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.6F;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float length = 2.5F;
        float thickness = 0.9F;
        float pitchInRadians = Math.abs(this.tiltAngle * (float)(Math.PI / 180.0));
        float horizontalFactor = Math.abs(Mth.sin(pitchInRadians));
        float verticalFactor = Math.abs(Mth.cos(pitchInRadians));
        float currentWidth = thickness + (length - thickness) * horizontalFactor;
        float currentHeight = thickness + (length - thickness) * verticalFactor;

        return EntityDimensions.fixed(currentWidth, currentHeight);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    protected SoundEvent getSquirtSound() {
        return SoundEvents.SQUID_SQUIRT;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return !this.isLeashed();
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.prevTiltAngle = this.tiltAngle;
        this.prevRollAngle = this.rollAngle;
        this.prevThrustTimer = this.thrustTimer;
        this.prevTentacleAngle = this.tentacleAngle;
        this.thrustTimer = this.thrustTimer + this.thrustTimerSpeed;
        if ((double)this.thrustTimer > Math.PI * 2) {
            if (this.level().isClientSide) {
                this.thrustTimer = (float) (Math.PI * 2);
            } else {
                this.thrustTimer -= (float) (Math.PI * 2);
                if (this.random.nextInt(10) == 0) {
                    this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }

                this.level().broadcastEntityEvent(this, EntityEvent.SQUID_ANIM_SYNCH);
            }
        }

        if (this.isInWaterOrBubble()) {
            if (this.thrustTimer < (float) Math.PI) {
                float f = this.thrustTimer / (float) Math.PI;
                this.tentacleAngle = Mth.sin(f * f * (float) Math.PI) * (float) Math.PI * 0.25F;
                if ((double)f > 0.75) {
                    this.swimVelocityScale = 1.0F;
                    this.turningSpeed = 1.0F;
                } else {
                    this.turningSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.swimVelocityScale *= 0.9F;
                this.turningSpeed *= 0.99F;
            }

            if (!this.level().isClientSide) {
                this.setDeltaMovement(
                        this.swimX * this.swimVelocityScale, this.swimY * this.swimVelocityScale, this.swimZ * this.swimVelocityScale
                );
            }

            Vec3 vec3d = this.getDeltaMovement();
            double d = vec3d.horizontalDistance();
            this.yBodyRot = this.yBodyRot + (-((float)Mth.atan2(vec3d.x, vec3d.z)) * (180.0F / (float)Math.PI) - this.yBodyRot) * 0.1F;
            this.setYRot(this.yBodyRot);
            this.rollAngle = this.rollAngle + (float) Math.PI * this.turningSpeed * 1.5F;
            this.tiltAngle = this.tiltAngle + (-((float)Mth.atan2(d, vec3d.y)) * (180.0F / (float)Math.PI) - this.tiltAngle) * 0.1F;
        } else {
            this.tentacleAngle = Mth.abs(Mth.sin(this.thrustTimer)) * (float) Math.PI * 0.25F;
            if (!this.level().isClientSide) {
                double e = this.getDeltaMovement().y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    e = 0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
                } else if (!this.isNoGravity()) {
                    e -= 0.08;
                }

                this.setDeltaMovement(0.0, e * 0.98F, 0.0);
            }

            this.tiltAngle = this.tiltAngle + (-90.0F - this.tiltAngle) * 0.02F;
        }

        this.refreshDimensions();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount) && this.getLastHurtByMob() != null) {
            if (!this.level().isClientSide)
                this.squirt();
            return true;
        } else {
            return false;
        }
    }

    private Vec3 applyBodyRotations(Vec3 shootVector) {
        Vec3 vec3d = shootVector.xRot(this.prevTiltAngle * (float) (Math.PI / 180.0));
        return vec3d.yRot(-this.yBodyRotO * (float) (Math.PI / 180.0));
    }

    private void squirt() {
        this.playSound(this.getSquirtSound(), this.getSoundVolume(), this.getVoicePitch());
        Vec3 vec3d = this.applyBodyRotations(new Vec3(0.0, -1.0, 0.0)).add(this.getX(), this.getY(), this.getZ());

        for (int i = 0; i < 30; i++) {
            Vec3 vec3d2 = this.applyBodyRotations(new Vec3((double)this.random.nextFloat() * 0.6 - 0.3, -1.0, (double)this.random.nextFloat() * 0.6 - 0.3));
            Vec3 vec3d3 = vec3d2.scale(0.3 + (double)(this.random.nextFloat() * 2.0F));
            ((ServerLevel)this.level()).sendParticles(this.getInkParticle(), vec3d.x, vec3d.y + 0.5, vec3d.z, 0, vec3d3.x, vec3d3.y, vec3d3.z, 0.1F);
        }
    }

    protected ParticleOptions getInkParticle() {
        return ParticleTypes.CRIMSON_SPORE;
    }

    @Override
    public void travel(Vec3 movementInput) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == EntityEvent.SQUID_ANIM_SYNCH) {
            this.thrustTimer = 0.0F;
        } else {
            super.handleEntityEvent(status);
        }
    }

    public void setSwimmingVector(float x, float y, float z) {
        this.swimX = x;
        this.swimY = y;
        this.swimZ = z;
    }

    public boolean hasSwimmingVector() {
        return this.swimX != 0.0F || this.swimY != 0.0F || this.swimZ != 0.0F;
    }

    class EscapeAttackerGoal extends Goal {
        private int timer;

        @Override
        public boolean canUse() {
            LivingEntity livingEntity = ShimmerEntity.this.getLastHurtByMob();
            return ShimmerEntity.this.isInWater() && livingEntity != null && ShimmerEntity.this.distanceToSqr(livingEntity) < 100.0;
        }

        @Override
        public void start() {
            this.timer = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity livingEntity = ShimmerEntity.this.getLastHurtByMob();
            if (livingEntity != null) {
                Vec3 vec3d = new Vec3(
                        ShimmerEntity.this.getX() - livingEntity.getX(), ShimmerEntity.this.getY() - livingEntity.getY(), ShimmerEntity.this.getZ() - livingEntity.getZ()
                );
                BlockState blockState = ShimmerEntity.this.level()
                        .getBlockState(BlockPos.containing(ShimmerEntity.this.getX() + vec3d.x, ShimmerEntity.this.getY() + vec3d.y, ShimmerEntity.this.getZ() + vec3d.z));
                FluidState fluidState = ShimmerEntity.this.level()
                        .getFluidState(BlockPos.containing(ShimmerEntity.this.getX() + vec3d.x, ShimmerEntity.this.getY() + vec3d.y, ShimmerEntity.this.getZ() + vec3d.z));
                if (fluidState.is(FluidTags.WATER) || blockState.isAir()) {
                    double d = vec3d.length();
                    if (d > 0.0) {
                        vec3d.normalize();
                        double e = 3.0;
                        if (d > 5.0) {
                            e -= (d - 5.0) / 5.0;
                        }

                        if (e > 0.0) {
                            vec3d = vec3d.scale(e);
                        }
                    }

                    if (blockState.isAir()) {
                        vec3d = vec3d.subtract(0.0, vec3d.y, 0.0);
                    }

                    ShimmerEntity.this.setSwimmingVector((float)vec3d.x / 20.0F, (float)vec3d.y / 20.0F, (float)vec3d.z / 20.0F);
                }

                if (this.timer % 10 == 5) {
                    ShimmerEntity.this.level().addParticle(ParticleTypes.ASH, ShimmerEntity.this.getX(), ShimmerEntity.this.getY(), ShimmerEntity.this.getZ(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    static class SwimGoal extends Goal {
        private final ShimmerEntity shimmer;

        public SwimGoal(ShimmerEntity squid) {
            this.shimmer = squid;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int i = this.shimmer.getNoActionTime();
            if (i > 100) {
                this.shimmer.setSwimmingVector(0.0F, 0.0F, 0.0F);
            } else if (this.shimmer.getRandom().nextInt(reducedTickDelay(50)) == 0 || !this.shimmer.wasTouchingWater || !this.shimmer.hasSwimmingVector()) {
                float f = this.shimmer.getRandom().nextFloat() * (float) (Math.PI * 2);
                float g = Mth.cos(f) * 0.2F;
                float h = -0.1F + this.shimmer.getRandom().nextFloat() * 0.2F;
                float j = Mth.sin(f) * 0.2F;
                this.shimmer.setSwimmingVector(g, h, j);
            }
        }
    }
    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        int i = world.getSeaLevel();
        int j = i - 6;
        return pos.getY() >= j && pos.getY() <= i && world.getFluidState(pos.below()).is(FluidTags.WATER) && world.getFluidState(pos.above()).is(FluidTags.WATER);
    }
}