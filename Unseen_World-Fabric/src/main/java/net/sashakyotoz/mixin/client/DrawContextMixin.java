package net.sashakyotoz.mixin.client;

import net.minecraft.block.MapColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.tags.ModTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public abstract int drawTextWithShadow(TextRenderer textRenderer, @Nullable String text, int x, int y, int color);

    @Shadow
    @Final
    private MatrixStack matrices;

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    private void renderOrbs(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        MinecraftClient mc = this.client;
        if (mc != null && mc.player != null && (stack.isIn(ModTags.Items.CAN_BE_CHARGED_BY_GRIPCRYSTALS) || ConfigController.getDataToStack(stack) != null)) {
            ClientPlayerEntity player = mc.player;
            IEntityDataSaver saver = (IEntityDataSaver) player;
            if (GripcrystalManaData.getOpacity(saver) < 0.25f && GripcrystalManaData.getMana(saver) > 0 && ItemStack.areEqual(player.getMainHandStack(), stack)) {
                String s = "%s".formatted(GripcrystalManaData.getMana(saver));
                this.matrices.translate(0.0F, 0.0F, 300.0F);
                this.drawTextWithShadow(textRenderer, s, x + 17 - textRenderer.getWidth(s), y + 8, MapColor.BLUE.color);
            }
        }
    }
}