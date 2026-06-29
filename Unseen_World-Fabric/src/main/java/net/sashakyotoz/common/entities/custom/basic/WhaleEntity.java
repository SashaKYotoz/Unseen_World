package net.sashakyotoz.common.entities.custom.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.ai.goals.GloomwhaleJumpGoal;
import net.sashakyotoz.common.entities.ai.goals.SwimAroundFarGoal;

public class WhaleEntity extends WaterAnimal {
    public WhaleEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new SmoothSwimmingMoveControl(this, 70, 10, 0.2F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public static boolean canWhaleSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return world.getFluidState(pos.above()).is(FluidTags.WATER)
                && world.getEntities(EntityTypeTest.forClass(WhaleEntity.class), type.getDimensions().makeBoundingBox(pos.getCenter()).inflate(32), LivingEntity::isAlive).size() < 4
                && random.nextInt(7) == 3;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isInWaterOrBubble() && this.tickCount % 20 == 0) {
            this.setDeltaMovement(this.getRandom().nextIntBetweenInclusive(-1, 2) / 10f, 0.15f, this.getRandom().nextIntBetweenInclusive(-1, 2) / 10f);
            this.hurtMarked = true;
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float length = 3.5F;
        float thickness = 2F;
        float pitchInRadians = Math.abs(this.yHeadRot / (float) (Math.PI / 180.0));
        float horizontalFactor = Math.abs(Mth.sin(pitchInRadians));
        float verticalFactor = Math.abs(Mth.cos(pitchInRadians));
        float currentWidth = thickness + (length - thickness) * horizontalFactor;
        float currentHeight = thickness + (length - thickness) * verticalFactor;

        return EntityDimensions.fixed(currentWidth, currentHeight);
    }

    @Override
    public void handleAirSupply(int air) {
    }

    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new SwimAroundFarGoal(this, 0.8f, 0.65f));
        this.goalSelector.addGoal(5, new GloomwhaleJumpGoal(this, 10));
    }

    @Override
    public int getMaxAirSupply() {
        return 6000;
    }

    @Override
    protected int increaseAirSupply(int air) {
        return this.getMaxAirSupply();
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null && this.getDeltaMovement().lengthSqr() < 0.005)
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.00125, 0.0));
        } else
            super.travel(movementInput);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new WhaleSwimNavigation(this, world);
    }

    public static class WhaleSwimNavigation extends WaterBoundPathNavigation {
        private boolean canJumpOutOfWater;

        public WhaleSwimNavigation(Mob mobEntity, Level world) {
            super(mobEntity, world);
        }

        @Override
        protected PathFinder createPathFinder(int range) {
            this.canJumpOutOfWater = this.mob instanceof WhaleEntity;
            this.nodeEvaluator = new SwimNodeEvaluator(this.canJumpOutOfWater);
            return new PathFinder(this.nodeEvaluator, range);
        }

        @Override
        protected boolean canUpdatePath() {
            return this.canJumpOutOfWater || this.isInLiquid();
        }
    }
}