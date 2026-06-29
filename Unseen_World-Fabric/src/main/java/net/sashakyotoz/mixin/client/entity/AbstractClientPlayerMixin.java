package net.sashakyotoz.mixin.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.common.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
    @Inject(method = "getFieldOfViewModifier", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void fixBowFovMul(CallbackInfoReturnable<Float> cir, float f) {
        AbstractClientPlayer player = (AbstractClientPlayer) ((Object) this);
        ItemStack itemStack = player.getUseItem();
        if (player.isUsingItem() && itemStack.is(ModItems.GRIPPING_ABYSSAL_BOW)) {
            int i = player.getTicksUsingItem();
            float g = (float) i / 20.0f;
            g = g > 1.0f ? 1.0f : g * g;
            f *= 1.0f - g * 0.15f;
            cir.setReturnValue(Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get().floatValue(), 1.0f, f));
        }
    }
}