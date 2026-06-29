package net.sashakyotoz.common.entities.ai.goals.spells;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.sashakyotoz.common.entities.custom.VenomerEntity;
import org.jetbrains.annotations.Nullable;

public class InvisibilityGoal extends VenomerEntity.CastSpellGoal {
    public InvisibilityGoal(VenomerEntity venomerEntity) {
        super(venomerEntity);
    }

    @Override
    public boolean canUse() {
        return super.canUse()
                && !venomer.hasEffect(MobEffects.INVISIBILITY)
                && venomer.getTarget() != null
                && venomer.getTarget().distanceToSqr(venomer) < 9;
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
        venomer.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200));
        venomer.flee(16);
    }

    @Nullable
    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
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
