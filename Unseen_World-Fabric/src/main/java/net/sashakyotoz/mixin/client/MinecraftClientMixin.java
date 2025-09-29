package net.sashakyotoz.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.networking.ModMessages;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z",shift = At.Shift.AFTER))
    private void attackHandler(CallbackInfo ci) {
        if (crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.MISS && player != null) {
            ItemStack stack = player.getMainHandStack();
            if (ConfigController.canHandleGripcrystalAbility(stack))
                ClientPlayNetworking.send(ModMessages.ABILITY_CLICK_HANDLER, PacketByteBufs.create());
        }
    }
}