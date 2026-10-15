package net.sashakyotoz.common.entities.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.ai.goals.WhaleHuntGoal;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;
import net.sashakyotoz.common.entities.util.GripConversion;
import net.sashakyotoz.utils.ActionsUtils;


public class GrippingGloomwhaleEntity extends WhaleEntity {
    private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(GrippingGloomwhaleEntity.class, EntityDataSerializers.BOOLEAN);
    public static final double BITE_DAMAGE = 8.0D;
    private static final int BITE_GRIPPING_SECONDS = 4;
    private final GripConversion conversion = new GripConversion(this, CONVERTING, this::finishConversion);

    public GrippingGloomwhaleEntity(EntityType<? extends WhaleEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        GripConversion.define(this.entityData, CONVERTING);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = this.conversion.tryCrystieApple(player, hand, 3600, 2400);
        if (result == InteractionResult.PASS) {
            result = this.conversion.tryGriptonite(player, hand, 1200, 1200);
        }
        return result != InteractionResult.PASS ? result : super.mobInteract(player, hand);
    }

    public boolean isConverting() {
        return this.conversion.isConverting();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        this.conversion.save(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.conversion.load(nbt);
    }

    @Override
    public void tick() {
        this.conversion.tick();
        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WhaleHuntGoal(this, 1.25D, true, BITE_GRIPPING_SECONDS));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, WhaleHuntGoal::isHuntablePlayer));
    }

    private void finishConversion(ServerLevel world, GripConversion state) {
        GloomwhaleEntity gloomwhale = this.convertTo(ModEntities.GLOOMWHALE, false);
        if (gloomwhale != null) {
            ActionsUtils.initializeConverting(this, gloomwhale, state.converter());
        }
        if (!this.isSilent()) {
            world.playSound(null, this.blockPosition(), SoundEvents.DOLPHIN_HURT, SoundSource.NEUTRAL, 2.0F, 0.25F);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.ATTACK_DAMAGE, BITE_DAMAGE)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
    }
}