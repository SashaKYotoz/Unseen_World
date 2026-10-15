
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
	public double getAttributeModifierValue(int i, AttributeModifier modifier) {
		if (i > 1)
		return modifier.getAmount() * (i + 0.5f);
		else
			return modifier.getAmount() * (i + 1f);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
