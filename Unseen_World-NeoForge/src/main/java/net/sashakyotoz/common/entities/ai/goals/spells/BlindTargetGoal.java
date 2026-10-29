package net.sashakyotoz.common.entities.ai.goals.spells;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.common.entities.custom.VenomerEntity;

public class BlindTargetGoal extends VenomerEntity.CastSpellGoal {
    public BlindTargetGoal(VenomerEntity venomerEntity) {
        super(venomerEntity);
    }

    private int targetId;

    @Override
    public boolean canUse() {
        if (!super.canUse())
            return false;
        else if (venomer.hasEffect(MobEffects.INVISIBILITY))
            return false;
        else if (venomer.getTarget() == null)
            return false;
        else
            return venomer.getTarget().getId() != this.targetId && venomer.level().getCurrentDifficultyAt(venomer.blockPosition()).isHarderThan((float) Difficulty.NORMAL.ordinal());
    }

    @Override
    public void start() {
        super.start();
        LivingEntity livingEntity = venomer.getTarget();
        if (livingEntity != null) {
            this.targetId = livingEntity.getId();
        }
    }

    @Override
    protected int getSpellTicks() {
        return 20;
    }

    @Override
    protected int startTimeDelay() {
        return 120;
    }

    @Override
    protected int getInitialCooldown() {
        return 40;
    }

    @Override
    protected void castSpell() {
        if (venomer.getTarget() != null)
            venomer.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200), venomer);
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    @Override
    protected VenomerEntity.Spell getSpell() {
        return VenomerEntity.Spell.BLINDNESS;
    }
}
