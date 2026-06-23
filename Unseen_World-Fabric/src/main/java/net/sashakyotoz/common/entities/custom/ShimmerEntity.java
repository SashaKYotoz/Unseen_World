package net.sashakyotoz.common.entities.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ShimmerEntity extends WaterCreatureEntity {
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

    public ShimmerEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        this.random.setSeed(this.getId());
        this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ShimmerEntity.EscapeAttackerGoal());
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 16);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.6F;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        float length = 2.5F;
        float thickness = 0.9F;
        float pitchInRadians = Math.abs(this.tiltAngle * (float)(Math.PI / 180.0));
        float horizontalFactor = Math.abs(MathHelper.sin(pitchInRadians));
        float verticalFactor = Math.abs(MathHelper.cos(pitchInRadians));
        float currentWidth = thickness + (length - thickness) * horizontalFactor;
        float currentHeight = thickness + (length - thickness) * verticalFactor;

        return EntityDimensions.fixed(currentWidth, currentHeight);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }

    protected SoundEvent getSquirtSound() {
        return SoundEvents.ENTITY_SQUID_SQUIRT;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return !this.isLeashed();
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.EVENTS;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.prevTiltAngle = this.tiltAngle;
        this.prevRollAngle = this.rollAngle;
        this.prevThrustTimer = this.thrustTimer;
        this.prevTentacleAngle = this.tentacleAngle;
        this.thrustTimer = this.thrustTimer + this.thrustTimerSpeed;
        if ((double)this.thrustTimer > Math.PI * 2) {
            if (this.getWorld().isClient) {
                this.thrustTimer = (float) (Math.PI * 2);
            } else {
                this.thrustTimer -= (float) (Math.PI * 2);
                if (this.random.nextInt(10) == 0) {
                    this.thrustTimerSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }

                this.getWorld().sendEntityStatus(this, EntityStatuses.RESET_SQUID_THRUST_TIMER);
            }
        }

        if (this.isInsideWaterOrBubbleColumn()) {
            if (this.thrustTimer < (float) Math.PI) {
                float f = this.thrustTimer / (float) Math.PI;
                this.tentacleAngle = MathHelper.sin(f * f * (float) Math.PI) * (float) Math.PI * 0.25F;
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

            if (!this.getWorld().isClient) {
                this.setVelocity(
                        this.swimX * this.swimVelocityScale, this.swimY * this.swimVelocityScale, this.swimZ * this.swimVelocityScale
                );
            }

            Vec3d vec3d = this.getVelocity();
            double d = vec3d.horizontalLength();
            this.bodyYaw = this.bodyYaw + (-((float)MathHelper.atan2(vec3d.x, vec3d.z)) * (180.0F / (float)Math.PI) - this.bodyYaw) * 0.1F;
            this.setYaw(this.bodyYaw);
            this.rollAngle = this.rollAngle + (float) Math.PI * this.turningSpeed * 1.5F;
            this.tiltAngle = this.tiltAngle + (-((float)MathHelper.atan2(d, vec3d.y)) * (180.0F / (float)Math.PI) - this.tiltAngle) * 0.1F;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.thrustTimer)) * (float) Math.PI * 0.25F;
            if (!this.getWorld().isClient) {
                double e = this.getVelocity().y;
                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    e = 0.05 * (double)(this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1);
                } else if (!this.hasNoGravity()) {
                    e -= 0.08;
                }

                this.setVelocity(0.0, e * 0.98F, 0.0);
            }

            this.tiltAngle = this.tiltAngle + (-90.0F - this.tiltAngle) * 0.02F;
        }

        this.calculateDimensions();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (super.damage(source, amount) && this.getAttacker() != null) {
            if (!this.getWorld().isClient)
                this.squirt();
            return true;
        } else {
            return false;
        }
    }

    private Vec3d applyBodyRotations(Vec3d shootVector) {
        Vec3d vec3d = shootVector.rotateX(this.prevTiltAngle * (float) (Math.PI / 180.0));
        return vec3d.rotateY(-this.prevBodyYaw * (float) (Math.PI / 180.0));
    }

    private void squirt() {
        this.playSound(this.getSquirtSound(), this.getSoundVolume(), this.getSoundPitch());
        Vec3d vec3d = this.applyBodyRotations(new Vec3d(0.0, -1.0, 0.0)).add(this.getX(), this.getY(), this.getZ());

        for (int i = 0; i < 30; i++) {
            Vec3d vec3d2 = this.applyBodyRotations(new Vec3d((double)this.random.nextFloat() * 0.6 - 0.3, -1.0, (double)this.random.nextFloat() * 0.6 - 0.3));
            Vec3d vec3d3 = vec3d2.multiply(0.3 + (double)(this.random.nextFloat() * 2.0F));
            ((ServerWorld)this.getWorld()).spawnParticles(this.getInkParticle(), vec3d.x, vec3d.y + 0.5, vec3d.z, 0, vec3d3.x, vec3d3.y, vec3d3.z, 0.1F);
        }
    }

    protected ParticleEffect getInkParticle() {
        return ParticleTypes.CRIMSON_SPORE;
    }

    @Override
    public void travel(Vec3d movementInput) {
        this.move(MovementType.SELF, this.getVelocity());
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.RESET_SQUID_THRUST_TIMER) {
            this.thrustTimer = 0.0F;
        } else {
            super.handleStatus(status);
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
        public boolean canStart() {
            LivingEntity livingEntity = ShimmerEntity.this.getAttacker();
            return ShimmerEntity.this.isTouchingWater() && livingEntity != null && ShimmerEntity.this.squaredDistanceTo(livingEntity) < 100.0;
        }

        @Override
        public void start() {
            this.timer = 0;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity livingEntity = ShimmerEntity.this.getAttacker();
            if (livingEntity != null) {
                Vec3d vec3d = new Vec3d(
                        ShimmerEntity.this.getX() - livingEntity.getX(), ShimmerEntity.this.getY() - livingEntity.getY(), ShimmerEntity.this.getZ() - livingEntity.getZ()
                );
                BlockState blockState = ShimmerEntity.this.getWorld()
                        .getBlockState(BlockPos.ofFloored(ShimmerEntity.this.getX() + vec3d.x, ShimmerEntity.this.getY() + vec3d.y, ShimmerEntity.this.getZ() + vec3d.z));
                FluidState fluidState = ShimmerEntity.this.getWorld()
                        .getFluidState(BlockPos.ofFloored(ShimmerEntity.this.getX() + vec3d.x, ShimmerEntity.this.getY() + vec3d.y, ShimmerEntity.this.getZ() + vec3d.z));
                if (fluidState.isIn(FluidTags.WATER) || blockState.isAir()) {
                    double d = vec3d.length();
                    if (d > 0.0) {
                        vec3d.normalize();
                        double e = 3.0;
                        if (d > 5.0) {
                            e -= (d - 5.0) / 5.0;
                        }

                        if (e > 0.0) {
                            vec3d = vec3d.multiply(e);
                        }
                    }

                    if (blockState.isAir()) {
                        vec3d = vec3d.subtract(0.0, vec3d.y, 0.0);
                    }

                    ShimmerEntity.this.setSwimmingVector((float)vec3d.x / 20.0F, (float)vec3d.y / 20.0F, (float)vec3d.z / 20.0F);
                }

                if (this.timer % 10 == 5) {
                    ShimmerEntity.this.getWorld().addParticle(ParticleTypes.ASH, ShimmerEntity.this.getX(), ShimmerEntity.this.getY(), ShimmerEntity.this.getZ(), 0.0, 0.0, 0.0);
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
        public boolean canStart() {
            return true;
        }

        @Override
        public void tick() {
            int i = this.shimmer.getDespawnCounter();
            if (i > 100) {
                this.shimmer.setSwimmingVector(0.0F, 0.0F, 0.0F);
            } else if (this.shimmer.getRandom().nextInt(toGoalTicks(50)) == 0 || !this.shimmer.touchingWater || !this.shimmer.hasSwimmingVector()) {
                float f = this.shimmer.getRandom().nextFloat() * (float) (Math.PI * 2);
                float g = MathHelper.cos(f) * 0.2F;
                float h = -0.1F + this.shimmer.getRandom().nextFloat() * 0.2F;
                float j = MathHelper.sin(f) * 0.2F;
                this.shimmer.setSwimmingVector(g, h, j);
            }
        }
    }
    public static boolean canSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        int i = world.getSeaLevel();
        int j = i - 6;
        return pos.getY() >= j && pos.getY() <= i && world.getFluidState(pos.down()).isIn(FluidTags.WATER) && world.getFluidState(pos.up()).isIn(FluidTags.WATER);
    }
}