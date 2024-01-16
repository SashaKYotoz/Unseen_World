
package net.sashakyotoz.unseenworld.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class ReducedOfGravityMobEffect extends MobEffect {

	public ReducedOfGravityMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1077941761);
		addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "d4395ce0-cefd-45f7-aa3c-3aee41d0390a", -0.06F, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public String getDescriptionId() {
		return "effect.unseen_world.reduced_of_gravity";
	}
	@Override
	public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
		if (p_19457_ > 1)
		return p_19458_.getAmount() * (p_19457_ + 0.5f);
		else
			return p_19458_.getAmount() * (p_19457_ + 1f);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
