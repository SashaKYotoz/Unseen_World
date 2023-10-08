
package net.sashakyotoz.unseenworld.potion;

import net.sashakyotoz.unseenworld.procedures.UnseeniumOnEffectActiveTickProcedure;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
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
		UnseeniumOnEffectActiveTickProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
