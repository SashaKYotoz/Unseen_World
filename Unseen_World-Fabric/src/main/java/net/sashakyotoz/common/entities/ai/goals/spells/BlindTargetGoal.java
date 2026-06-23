package net.sashakyotoz.common.entities.ai.goals.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.Difficulty;
import net.sashakyotoz.common.entities.custom.VenomerEntity;

public class BlindTargetGoal extends VenomerEntity.CastSpellGoal {
    public BlindTargetGoal(VenomerEntity venomerEntity) {
        super(venomerEntity);
    }

    private int targetId;

    @Override
    public boolean canStart() {
        if (!super.canStart())
            return false;
        else if (venomer.hasStatusEffect(StatusEffects.INVISIBILITY))
            return false;
        else if (venomer.getTarget() == null)
            return false;
        else
            return venomer.getTarget().getId() != this.targetId && venomer.getWorld().getLocalDifficulty(venomer.getBlockPos()).isHarderThan((float) Difficulty.NORMAL.ordinal());
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
            venomer.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200), venomer);
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS;
    }

    @Override
    protected VenomerEntity.Spell getSpell() {
        return VenomerEntity.Spell.BLINDNESS;
    }
}
