package net.sashakyotoz.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.items.ModItems;

public class DarkGuardianEntity extends HostileEntity implements RangedAttackMob {
    private final BowAttackGoal<DarkGuardianEntity> bowGoal = new BowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false);
    public DarkGuardianEntity.Type guardianType;

    public DarkGuardianEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        int random = this.getRandom().nextInt(3);
        this.experiencePoints = 5;
        if (!this.getWorld().isClient()) {
            if (this.getRandom().nextBoolean()) {
                this.equipStack(EquipmentSlot.CHEST, new ItemStack(
                        ModRegistry.ITEMS.stream()
                                .filter(item -> item instanceof ArmorItem item1 && item1.getType() == ArmorItem.Type.CHESTPLATE)
                                .toList().get(random)
                ));
                this.equipStack(EquipmentSlot.LEGS, new ItemStack(
                        ModRegistry.ITEMS.stream()
                                .filter(item -> item instanceof ArmorItem item1 && item1.getType() == ArmorItem.Type.LEGGINGS)
                                .toList().get(random)
                ));
                this.equipStack(EquipmentSlot.FEET, new ItemStack(
                        ModRegistry.ITEMS.stream()
                                .filter(item -> item instanceof ArmorItem item1 && item1.getType() == ArmorItem.Type.BOOTS)
                                .toList().get(random)
                ));
            } else
                this.setStackInHand(Hand.OFF_HAND, new ItemStack(Items.SHIELD));
        }
        switch (random) {
            default -> guardianType = Type.PURPLE;
            case 1 -> guardianType = Type.BLUEISH;
            case 2 -> guardianType = Type.YELLOW;
        }
    }

    @Override
    protected float getDropChance(EquipmentSlot slot) {
        return super.getDropChance(slot) * 0.75f;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new AvoidSunlightGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0f, 0.05f));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 16));
        this.goalSelector.add(3, new RevengeGoal(this));
        this.goalSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age % 500 == 0) {
            this.setStackInHand(Hand.MAIN_HAND, this.random.nextBoolean() ? new ItemStack(Items.BOW) : new ItemStack(ModItems.ABYSSAL_SWORD));
            this.reassessWeaponGoal();
        }
        if (this.getOffHandStack().getItem() instanceof ShieldItem && this.getMainHandStack().getItem() instanceof SwordItem)
            this.setCurrentHand(Hand.OFF_HAND);
        if (this.getTarget() != null) {
            if (this.age % 60 == 0 && this.getHealth() < this.getMaxHealth() && !this.hasStatusEffect(StatusEffects.REGENERATION)) {
                if (!this.getWorld().isClient)
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 1));
            }
        }
        this.setSneaking(this.isUsingItem());
    }

    @Override
    public boolean isTeammate(Entity other) {
        return super.isTeammate(other) || other instanceof WarriorOfChimericDarkness;
    }

    public void reassessWeaponGoal() {
        if (!this.getWorld().isClient()) {
            this.goalSelector.remove(this.meleeGoal);
            this.goalSelector.remove(this.bowGoal);
            ItemStack itemstack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
            if (itemstack.isOf(Items.BOW)) {
                int i = 20;
                if (this.getWorld().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }
                this.bowGoal.setAttackInterval(i);
                this.goalSelector.add(4, this.bowGoal);
            } else {
                this.goalSelector.add(4, this.meleeGoal);
            }
        }
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemstack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
        PersistentProjectileEntity abstractArrow = ProjectileUtil.createArrowProjectile(this, itemstack, pullProgress);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY() + 0.35f - abstractArrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractArrow.setVelocity(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.getWorld().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(abstractArrow);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
                .add(EntityAttributes.GENERIC_ARMOR, 8)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    public enum Type {
        BLUEISH("blueish"),
        PURPLE("purple"),
        YELLOW("yellow");
        public final String typeName;

        Type(String nameOfType) {
            typeName = nameOfType;
        }
    }
}