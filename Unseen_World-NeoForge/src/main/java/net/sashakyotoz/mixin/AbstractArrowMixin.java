package net.sashakyotoz.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.sashakyotoz.common.items.custom.GrippingAbyssalBowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Inject(method = "doPostHurtEffects", at = @At("HEAD"))
    private void unseen_world$applyBowShotEffects(LivingEntity target, CallbackInfo ci) {
        GrippingAbyssalBowItem.onArrowHurt((AbstractArrow) (Object) this, target);
    }
}
