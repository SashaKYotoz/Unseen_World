
package net.sashakyotoz.unseenworld.potion;

import com.google.common.collect.Maps;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

import java.util.Map;
import java.util.UUID;

public class ReducedOfGravityMobEffect extends MobEffect {
	private static double gravity;
	public static final UUID GRAVITY = UUID.fromString("d4395ce0-cefd-45f7-aa3c-3aee41d0390a");
	private final Map<Attribute, AttributeModifier> attributeModifiers = Maps.newHashMap();

	public ReducedOfGravityMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1077941761);
	}

	@Override
	public String getDescriptionId() {
		return "effect.unseen_world.reduced_of_gravity";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		gravity = entity.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get()) - 0.6;
	}

	@Override
	public MobEffect addAttributeModifier(Attribute p_19473_, String p_19474_, double p_19475_, AttributeModifier.Operation p_19476_) {
		AttributeModifier attributemodifier = new AttributeModifier(GRAVITY, this::getDescriptionId, gravity, AttributeModifier.Operation.ADDITION);
		this.attributeModifiers.put(ForgeMod.ENTITY_GRAVITY.get(), attributemodifier);
		return this;
	}

	public Map<Attribute, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		gravity = entity.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get());
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
