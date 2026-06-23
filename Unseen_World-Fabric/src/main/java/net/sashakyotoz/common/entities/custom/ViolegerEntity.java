package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.sashakyotoz.common.entities.ai.goals.GrippingAbyssalBowAttackGoal;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

public class ViolegerEntity extends HostileEntity implements RangedAttackMob {
    protected static final TrackedData<Boolean> CELEBRATING = DataTracker.registerData(ViolegerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_HOSTILE = DataTracker.registerData(ViolegerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final GrippingAbyssalBowAttackGoal<ViolegerEntity> bowGoal = new GrippingAbyssalBowAttackGoal<>(this, 1.0D, 20, 16.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false);

    public ViolegerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IS_HOSTILE, false);
        this.dataTracker.startTracking(CELEBRATING, false);
    }

    public boolean isCelebrating() {
        return this.dataTracker.get(CELEBRATING);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0f, 0.25f));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 16));
        this.goalSelector.add(3, new RevengeGoal(this));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.5f, true));
        this.goalSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    public void tick() {
        super.tick();
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

    public boolean isEntityLookingAtMe(LivingEntity entity, double d, boolean bl, boolean visualShape, Predicate<LivingEntity> predicate, DoubleSupplier... entityYChecks) {
        if (predicate.test(entity)) {
            Vec3d vec3d = entity.getRotationVec(1.0F).normalize();
            for (DoubleSupplier doubleSupplier : entityYChecks) {
                Vec3d vec3d2 = new Vec3d(this.getX() - entity.getX(), doubleSupplier.getAsDouble() - entity.getEyeY(), this.getZ() - entity.getZ());
                double e = vec3d2.length();
                vec3d2 = vec3d2.normalize();
                double f = vec3d.dotProduct(vec3d2);
                if (f > 1.0 - d / (bl ? e : 1.0))
                    return ActionsUtils.canSee(this, visualShape ? RaycastContext.ShapeType.VISUAL : RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, doubleSupplier);
            }
        }
        return false;
    }

    public void reassessWeaponGoal() {
        if (!this.getWorld().isClient() && this.isHostile()) {
            this.goalSelector.remove(this.meleeGoal);
            this.goalSelector.remove(this.bowGoal);
            ItemStack itemstack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, ModItems.GRIPPING_ABYSSAL_BOW));
            if (itemstack.isOf(ModItems.GRIPPING_ABYSSAL_BOW)) {
                int i = 20;
                if (this.getWorld().getDifficulty() != Difficulty.HARD)
                    i = 40;
                this.bowGoal.setAttackInterval(i);
                this.goalSelector.add(4, this.bowGoal);
            } else
                this.goalSelector.add(4, this.meleeGoal);
        }
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemstack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, ModItems.GRIPPING_ABYSSAL_BOW)));
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
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity && !this.getWorld().isClient() && !this.isHostile())
            this.setIsHostile(true);
        return super.damage(source, amount);
    }

    @Override
    public boolean canGather(ItemStack stack) {
        return false;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack item = player.getStackInHand(hand);
        if (this.isHostile() && item.isOf(ModItems.GRIPCRYSTAL)) {
            item.decrement(1);
            this.setIsHostile(false);
            this.getWorld().addParticle(ParticleTypes.HEART, this.getX(), this.getY() + 1.5f, this.getZ(), 0, 1, 0);
            return ActionResult.CONSUME;
        }
        if (!this.isHostile() && item.isOf(ModItems.GRANULATED_GRIPCRYSTAL)) {
            this.lookAtEntity(player, 30, 30);
//            ActionsUtils.dropItem(this, this.getInventory().getStack(6));
//            item.decrement(1);
//            this.getInventory().setStack(6, ItemStack.EMPTY);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            if (this.getWorld().isClient())
                this.playSound(SoundEvents.ENTITY_VILLAGER_TRADE, 1.2f, 1);
            return ActionResult.CONSUME;
        }
        return super.interactMob(player, hand);
    }


    public void setIsHostile(boolean flag) {
        this.dataTracker.set(IS_HOSTILE, flag);
        if (flag) {
            if (this.getWorld().isClient())
                this.getWorld().addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + 1, this.getZ(), 0, 1, 0);
            this.reassessWeaponGoal();
            this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.5f, 1);
        } else {
            for (EquipmentSlot value : EquipmentSlot.values()) {
                this.equipStack(value, ItemStack.EMPTY);
            }
        }
    }

    public boolean isHostile() {
        return this.dataTracker.get(IS_HOSTILE);
    }

    public State getState() {
        if (this.getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.GRIPPING_ABYSSAL_BOW)
                || this.getEquippedStack(EquipmentSlot.OFFHAND).isOf(ModItems.GRIPPING_ABYSSAL_BOW)) {
            return State.BOW_AND_ARROW;
        } else
            return this.isAttacking() ? State.ATTACKING : State.NEUTRAL;
    }

    public enum State {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CELEBRATING,
        NEUTRAL;
    }
}