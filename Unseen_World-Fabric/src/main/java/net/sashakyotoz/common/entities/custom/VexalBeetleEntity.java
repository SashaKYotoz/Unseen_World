package net.sashakyotoz.common.entities.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.common.entities.ai.goals.VexalBeetleFlyGoal;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class VexalBeetleEntity extends HostileEntity implements Angerable {
    public final AnimationState flying = new AnimationState();

    public VexalBeetleEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public int getAngerTime() {
        return 0;
    }

    @Override
    public void setAngerTime(int angerTime) {

    }

    @Override
    public @Nullable UUID getAngryAt() {
        return null;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {

    }

    @Override
    public void chooseRandomAngerTime() {

    }

    @Override
    public void tick() {
        super.tick();

        flying.setRunning(ActionsUtils.isMoving(this) && !this.isOnGround(), this.age);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(2, new VexalBeetleFlyGoal(this, 1.0F));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 2.0F));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.35);
    }
}