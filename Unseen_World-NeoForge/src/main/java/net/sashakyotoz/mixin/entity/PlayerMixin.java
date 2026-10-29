package net.sashakyotoz.mixin.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.common.items.combat.ShieldMechanics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "blockUsingShield", at = @At("HEAD"), cancellable = true)
    private void unseen_world$blockUsingShield(LivingEntity attacker, CallbackInfo ci) {
        if (ShieldMechanics.onShieldBlock((Player) (Object) this, attacker)) {
            ci.cancel();
        }
    }
}
