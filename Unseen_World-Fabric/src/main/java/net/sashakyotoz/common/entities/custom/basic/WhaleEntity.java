package net.sashakyotoz.common.entities.custom.basic;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.BreatheAirGoal;
import net.minecraft.entity.ai.goal.MoveIntoWaterGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.ai.pathing.WaterPathNodeMaker;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.sashakyotoz.common.entities.ai.goals.GloomwhaleJumpGoal;
import net.sashakyotoz.common.entities.ai.goals.SwimAroundFarGoal;

public class WhaleEntity extends WaterCreatureEntity {
    public WhaleEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new AquaticMoveControl(this, 70, 10, 0.2F, 0.1F, true);
        this.lookControl = new YawAdjustingLookControl(this, 10);
    }

    public static boolean canWhaleSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getFluidState(pos.up()).isIn(FluidTags.WATER)
                && world.getEntitiesByType(TypeFilter.instanceOf(WhaleEntity.class), type.getDimensions().getBoxAt(pos.toCenterPos()).expand(32), LivingEntity::isAlive).size() < 4
                && random.nextInt(7) == 3;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isInsideWaterOrBubbleColumn() && this.age % 20 == 0) {
            this.setVelocity(this.getRandom().nextBetween(-1, 2) / 10f, 0.15f, this.getRandom().nextBetween(-1, 2) / 10f);
            this.velocityModified = true;
        }
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        float length = 3.5F;
        float thickness = 2F;
        float pitchInRadians = Math.abs(this.headYaw / (float) (Math.PI / 180.0));
        float horizontalFactor = Math.abs(MathHelper.sin(pitchInRadians));
        float verticalFactor = Math.abs(MathHelper.cos(pitchInRadians));
        float currentWidth = thickness + (length - thickness) * horizontalFactor;
        float currentHeight = thickness + (length - thickness) * verticalFactor;

        return EntityDimensions.fixed(currentWidth, currentHeight);
    }

    @Override
    public void tickWaterBreathingAir(int air) {
    }

    @Override
    public boolean canBreatheInWater() {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new BreatheAirGoal(this));
        this.goalSelector.add(1, new MoveIntoWaterGoal(this));
        this.goalSelector.add(4, new SwimAroundFarGoal(this, 0.8f, 0.65f));
        this.goalSelector.add(5, new GloomwhaleJumpGoal(this, 10));
    }

    @Override
    public int getMaxAir() {
        return 6000;
    }

    @Override
    protected int getNextAirOnLand(int air) {
        return this.getMaxAir();
    }

    @Override
    public int getMaxLookPitchChange() {
        return 1;
    }

    @Override
    public int getMaxHeadRotation() {
        return 1;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(this.getMovementSpeed(), movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
            if (this.getTarget() == null && this.getVelocity().lengthSquared() < 0.005)
                this.setVelocity(this.getVelocity().add(0.0, -0.00125, 0.0));
        } else
            super.travel(movementInput);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new WhaleSwimNavigation(this, world);
    }

    public static class WhaleSwimNavigation extends SwimNavigation {
        private boolean canJumpOutOfWater;

        public WhaleSwimNavigation(MobEntity mobEntity, World world) {
            super(mobEntity, world);
        }

        @Override
        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.canJumpOutOfWater = this.entity instanceof WhaleEntity;
            this.nodeMaker = new WaterPathNodeMaker(this.canJumpOutOfWater);
            return new PathNodeNavigator(this.nodeMaker, range);
        }

        @Override
        protected boolean isAtValidPosition() {
            return this.canJumpOutOfWater || this.isInLiquid();
        }
    }
}