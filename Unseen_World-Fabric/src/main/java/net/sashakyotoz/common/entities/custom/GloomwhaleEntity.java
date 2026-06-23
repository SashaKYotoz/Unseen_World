package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

public class GloomwhaleEntity extends WhaleEntity {

    public GloomwhaleEntity(EntityType<? extends WhaleEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2F, true));
        this.targetSelector.add(1, new RevengeGoal(this, PlayerEntity.class).setGroupRevenge());
        this.targetSelector.add(2, new RevengeGoal(this, GuardianEntity.class).setGroupRevenge());
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ShimmerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80)
                .add(EntityAttributes.GENERIC_ARMOR, 10)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5);
    }
}