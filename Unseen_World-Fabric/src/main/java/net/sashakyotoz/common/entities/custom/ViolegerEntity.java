package net.sashakyotoz.common.entities.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.ai.goals.GrippingAbyssalBowAttackGoal;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

public class ViolegerEntity extends Monster implements RangedAttackMob {
    protected static final EntityDataAccessor<Boolean> CELEBRATING = SynchedEntityData.defineId(ViolegerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HOSTILE = SynchedEntityData.defineId(ViolegerEntity.class, EntityDataSerializers.BOOLEAN);

    private final GrippingAbyssalBowAttackGoal<ViolegerEntity> bowGoal = new GrippingAbyssalBowAttackGoal<>(this, 1.0D, 20, 16.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false);

    public ViolegerEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 5;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_HOSTILE, false);
        this.entityData.define(CELEBRATING, false);
    }

    public boolean isCelebrating() {
        return this.entityData.get(CELEBRATING);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0f, 0.25f));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 16));
        this.goalSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.5f, true));
        this.goalSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getOffhandItem().getItem() instanceof ShieldItem && this.getMainHandItem().getItem() instanceof SwordItem)
            this.startUsingItem(InteractionHand.OFF_HAND);
        if (this.getTarget() != null) {
            if (this.tickCount % 60 == 0 && this.getHealth() < this.getMaxHealth() && !this.hasEffect(MobEffects.REGENERATION)) {
                if (!this.level().isClientSide)
                    this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 1));
            }
        }
        this.setShiftKeyDown(this.isUsingItem());
    }

    public boolean isEntityLookingAtMe(LivingEntity entity, double d, boolean bl, boolean visualShape, Predicate<LivingEntity> predicate, DoubleSupplier... entityYChecks) {
        if (predicate.test(entity)) {
            Vec3 vec3d = entity.getViewVector(1.0F).normalize();
            for (DoubleSupplier doubleSupplier : entityYChecks) {
                Vec3 vec3d2 = new Vec3(this.getX() - entity.getX(), doubleSupplier.getAsDouble() - entity.getEyeY(), this.getZ() - entity.getZ());
                double e = vec3d2.length();
                vec3d2 = vec3d2.normalize();
                double f = vec3d.dot(vec3d2);
                if (f > 1.0 - d / (bl ? e : 1.0))
                    return ActionsUtils.canSee(this, visualShape ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, doubleSupplier);
            }
        }
        return false;
    }

    public void reassessWeaponGoal() {
        if (!this.level().isClientSide() && this.isHostile()) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, ModItems.GRIPPING_ABYSSAL_BOW));
            if (itemstack.is(ModItems.GRIPPING_ABYSSAL_BOW)) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD)
                    i = 40;
                this.bowGoal.setAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else
                this.goalSelector.addGoal(4, this.meleeGoal);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, ModItems.GRIPPING_ABYSSAL_BOW)));
        AbstractArrow abstractArrow = ProjectileUtil.getMobArrow(this, itemstack, pullProgress);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY() + 0.35f - abstractArrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractArrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractArrow);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player && !this.level().isClientSide() && !this.isHostile())
            this.setIsHostile(true);
        return super.hurt(source, amount);
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        return false;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (this.isHostile() && item.is(ModItems.GRIPCRYSTAL)) {
            item.shrink(1);
            this.setIsHostile(false);
            this.level().addParticle(ParticleTypes.HEART, this.getX(), this.getY() + 1.5f, this.getZ(), 0, 1, 0);
            return InteractionResult.CONSUME;
        }
        if (!this.isHostile() && item.is(ModItems.GRANULATED_GRIPCRYSTAL)) {
            this.lookAt(player, 30, 30);
//            ActionsUtils.dropItem(this, this.getInventory().getStack(6));
//            item.decrement(1);
//            this.getInventory().setStack(6, ItemStack.EMPTY);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            if (this.level().isClientSide())
                this.playSound(SoundEvents.VILLAGER_TRADE, 1.2f, 1);
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }


    public void setIsHostile(boolean flag) {
        this.entityData.set(IS_HOSTILE, flag);
        if (flag) {
            if (this.level().isClientSide())
                this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + 1, this.getZ(), 0, 1, 0);
            this.reassessWeaponGoal();
            this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.5f, 1);
        } else {
            for (EquipmentSlot value : EquipmentSlot.values()) {
                this.setItemSlot(value, ItemStack.EMPTY);
            }
        }
    }

    public boolean isHostile() {
        return this.entityData.get(IS_HOSTILE);
    }

    public State getState() {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.GRIPPING_ABYSSAL_BOW)
                || this.getItemBySlot(EquipmentSlot.OFFHAND).is(ModItems.GRIPPING_ABYSSAL_BOW)) {
            return State.BOW_AND_ARROW;
        } else
            return this.isAggressive() ? State.ATTACKING : State.NEUTRAL;
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