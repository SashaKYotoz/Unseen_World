package net.sashakyotoz.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.networking.ModMessages;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;startAttack()Z",shift = At.Shift.AFTER))
    private void attackHandler(CallbackInfo ci) {
        if (hitResult != null && hitResult.getType() == HitResult.Type.MISS && player != null) {
            ItemStack stack = player.getMainHandItem();
            if (ConfigController.canHandleGripcrystalAbility(stack))
                ClientPlayNetworking.send(ModMessages.ABILITY_CLICK_HANDLER, PacketByteBufs.create());
        }
    }
}