package net.sashakyotoz.common.entities.ai.goals.spells;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.sashakyotoz.common.entities.custom.VenomerEntity;
import org.jetbrains.annotations.Nullable;

public class InvisibilityGoal extends VenomerEntity.CastSpellGoal {
    public InvisibilityGoal(VenomerEntity venomerEntity) {
        super(venomerEntity);
    }

    @Override
    public boolean canStart() {
        return super.canStart()
                && !venomer.hasStatusEffect(StatusEffects.INVISIBILITY)
                && venomer.getTarget() != null
                && venomer.getTarget().squaredDistanceTo(venomer) < 9;
    }

    @Override
    protected int getSpellTicks() {
        return 80;
    }

    @Override
    protected int startTimeDelay() {
        return 100;
    }

    @Override
    protected int getInitialCooldown() {
        return 40;
    }

    @Override
    protected void castSpell() {
        venomer.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200));
        venomer.flee(16);
    }

    @Nullable
    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ENTITY_ILLUSIONER_PREPARE_MIRROR;
    }

    @Override
    public void stop() {
        venomer.flee(8);
    }

    @Override
    protected VenomerEntity.Spell getSpell() {
        return VenomerEntity.Spell.DISAPPEAR;
    }
}
