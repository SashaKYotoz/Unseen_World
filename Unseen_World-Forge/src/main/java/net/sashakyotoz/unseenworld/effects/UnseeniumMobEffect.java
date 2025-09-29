
package net.sashakyotoz.unseenworld.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class UnseeniumMobEffect extends MobEffect {
	public UnseeniumMobEffect() {
		super(MobEffectCategory.HARMFUL, 2139127936);
	}

	@Override
	public String getDescriptionId() {
		return "effect.unseen_world.unseenium";
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
		if (!entity.level().isClientSide()){
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, true, false));
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 1, true, false));
			entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 60, 1, true, false));
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
