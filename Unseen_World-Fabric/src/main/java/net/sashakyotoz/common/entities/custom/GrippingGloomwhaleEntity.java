package net.sashakyotoz.common.entities.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GrippingGloomwhaleEntity extends WhaleEntity {
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(GrippingGloomwhaleEntity.class, EntityDataSerializers.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;

    public GrippingGloomwhaleEntity(EntityType<? extends WhaleEntity> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new SmoothSwimmingMoveControl(this, 75, 10, 0.35F, 0.1F, true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CONVERTING, false);
    }

    private void setConverting(@Nullable UUID uuid, int delay) {
        this.converter = uuid;
        this.conversionTimer = delay;
        this.getEntityData().set(CONVERTING, true);
        this.removeEffect(MobEffects.GLOWING);
        this.addEffect(new MobEffectInstance(MobEffects.DARKNESS, delay, Math.min(this.level().getDifficulty().getId() - 1, 0)));
        this.level().broadcastEntityEvent(this, EntityEvent.VILLAGER_SWEAT);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ModItems.CRYSTIE_APPLE)) {
            if (this.hasEffect(MobEffects.GLOWING)) {
                if (!player.getAbilities().instabuild)
                    itemStack.shrink(1);
                if (!this.level().isClientSide)
                    this.setConverting(player.getUUID(), this.random.nextInt(2401) + 3600);
                return InteractionResult.SUCCESS;
            } else
                return InteractionResult.CONSUME;
        } else if (itemStack.is(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().instabuild && player instanceof ServerPlayer player1)
                itemStack.hurt(1, player1.getRandom(), player1);
            if (!this.level().isClientSide)
                this.setConverting(player.getUUID(), this.random.nextInt(1201) + 1200);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    public boolean isConverting() {
        return this.getEntityData().get(CONVERTING);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null) {
            nbt.putUUID("ConversionPlayer", this.converter);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.hasUUID("ConversionPlayer") ? nbt.getUUID("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
            this.conversionTimer--;
            if (this.conversionTimer <= 0)
                this.finishConversion((ServerLevel) this.level());
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        this.goalSelector.addGoal(2, new FollowBoatGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, Player.class).setAlertOthers());
    }

    private void finishConversion(ServerLevel world) {
        GloomwhaleEntity gloomwhale = this.convertTo(ModEntities.GLOOMWHALE, false);
        ActionsUtils.initializeConverting(this, gloomwhale, uuid);
        if (!this.isSilent())
            world.playSound(this, this.blockPosition(), SoundEvents.DOLPHIN_HURT, SoundSource.NEUTRAL, 2, 0.25f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.5);
    }
}