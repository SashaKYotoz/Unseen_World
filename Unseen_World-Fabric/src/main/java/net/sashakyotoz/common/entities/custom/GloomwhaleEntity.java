package net.sashakyotoz.common.entities.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sashakyotoz.common.entities.ai.goals.WhaleHuntGoal;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

public class GloomwhaleEntity extends WhaleEntity {

    public GloomwhaleEntity(EntityType<? extends WhaleEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new GloomwhalePanicGoal(this));
        this.goalSelector.addGoal(2, new WhaleHuntGoal(this, 1.2D, false, 0));
        this.goalSelector.addGoal(3, new FollowBoatGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Player.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ShimmerEntity.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    static class GloomwhalePanicGoal extends PanicGoal {
        GloomwhalePanicGoal(GloomwhaleEntity whale) {
            super(whale, 1.6D);
        }

        @Override
        protected boolean shouldPanic() {
            return this.mob.isInWater() && this.mob.getLastHurtByMob() instanceof Player;
        }
    }
}
