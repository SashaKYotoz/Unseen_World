package net.sashakyotoz.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.sashakyotoz.common.entities.custom.VexalBeetleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerJumpScaleMixin {
    @Inject(method = "getJumpRidingScale", at = @At("HEAD"), cancellable = true)
    private void unseen_world$showBeetleStamina(CallbackInfoReturnable<Float> cir) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (player.getVehicle() instanceof VexalBeetleEntity beetle && beetle.isSaddled()) {
            cir.setReturnValue(beetle.getFlightStaminaRatio());
        }
    }
}