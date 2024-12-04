package net.sashakyotoz.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.networking.ModMessages;
import net.sashakyotoz.utils.ActionsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Inject(method = "doAttack", at = @At("TAIL"))
    private void attackHandler(CallbackInfoReturnable<Boolean> cir) {
        if (crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.MISS && player != null && !ActionsManager.isModLoaded("bettercombat")) {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof EclipsebaneItem item && item.getItemPhase(stack).equals("light_ray"))
                ClientPlayNetworking.send(ModMessages.LEFT_CLICK_HANDLER, PacketByteBufs.create());
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem item && item.getItemPhase(stack).equals("heavy_winding"))
                ClientPlayNetworking.send(ModMessages.LEFT_CLICK_HANDLER, PacketByteBufs.create());
            if (ActionsManager.isModLoaded("sortilege") && stack.getItem().getTranslationKey().contains("staff"))
                ClientPlayNetworking.send(ModMessages.LEFT_CLICK_HANDLER, PacketByteBufs.create());
        }
    }
}