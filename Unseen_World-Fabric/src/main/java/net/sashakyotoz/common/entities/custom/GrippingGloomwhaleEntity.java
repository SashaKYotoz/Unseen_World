package net.sashakyotoz.common.entities.custom;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.ChaseBoatGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GrippingGloomwhaleEntity extends WhaleEntity {
    private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(GrippingGloomwhaleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int conversionTimer;
    @Nullable
    private UUID converter;

    public GrippingGloomwhaleEntity(EntityType<? extends WhaleEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new AquaticMoveControl(this, 75, 10, 0.35F, 0.1F, true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CONVERTING, false);
    }

    private void setConverting(@Nullable UUID uuid, int delay) {
        this.converter = uuid;
        this.conversionTimer = delay;
        this.getDataTracker().set(CONVERTING, true);
        this.removeStatusEffect(StatusEffects.GLOWING);
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, delay, Math.min(this.getWorld().getDifficulty().getId() - 1, 0)));
        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_SPLASH_PARTICLES);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ModItems.CRYSTIE_APPLE)) {
            if (this.hasStatusEffect(StatusEffects.GLOWING)) {
                if (!player.getAbilities().creativeMode)
                    itemStack.decrement(1);
                if (!this.getWorld().isClient)
                    this.setConverting(player.getUuid(), this.random.nextInt(2401) + 3600);
                return ActionResult.SUCCESS;
            } else
                return ActionResult.CONSUME;
        } else if (itemStack.isOf(ModItems.GRIPTONITE)) {
            if (!player.getAbilities().creativeMode && player instanceof ServerPlayerEntity player1)
                itemStack.damage(1, player1.getRandom(), player1);
            if (!this.getWorld().isClient)
                this.setConverting(player.getUuid(), this.random.nextInt(1201) + 1200);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    public boolean isConverting() {
        return this.getDataTracker().get(CONVERTING);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("ConversionTime", this.isConverting() ? this.conversionTimer : -1);
        if (this.converter != null) {
            nbt.putUuid("ConversionPlayer", this.converter);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ConversionTime", NbtElement.NUMBER_TYPE) && nbt.getInt("ConversionTime") > -1)
            this.setConverting(nbt.containsUuid("ConversionPlayer") ? nbt.getUuid("ConversionPlayer") : null, nbt.getInt("ConversionTime"));
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient && this.isAlive() && this.isConverting()) {
            this.conversionTimer--;
            if (this.conversionTimer <= 0)
                this.finishConversion((ServerWorld) this.getWorld());
        }
        super.tick();
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2F, true));
        this.goalSelector.add(2, new ChaseBoatGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new RevengeGoal(this, PlayerEntity.class).setGroupRevenge());
    }

    private void finishConversion(ServerWorld world) {
        GloomwhaleEntity gloomwhale = this.convertTo(ModEntities.GLOOMWHALE, false);
        ActionsUtils.initializeConverting(this, gloomwhale, uuid);
        if (!this.isSilent())
            world.playSound(this, this.getBlockPos(), SoundEvents.ENTITY_DOLPHIN_HURT, SoundCategory.NEUTRAL, 2, 0.25f);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80)
                .add(EntityAttributes.GENERIC_ARMOR, 8)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5);
    }
}