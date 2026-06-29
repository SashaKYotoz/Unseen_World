package net.sashakyotoz.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;
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

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract int drawString(Font textRenderer, @Nullable String text, int x, int y, int color);

    @Shadow
    @Final
    private PoseStack pose;

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
    private void renderOrbs(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        Minecraft mc = this.minecraft;
        if (mc != null && mc.player != null && (stack.is(ModTags.Items.CAN_BE_CHARGED_BY_GRIPCRYSTALS) || ConfigController.getDataToStack(stack) != null)) {
            LocalPlayer player = mc.player;
            IEntityDataSaver saver = (IEntityDataSaver) player;
            if (GripcrystalManaData.getOpacity(saver) < 0.25f && GripcrystalManaData.getMana(saver) > 0 && ItemStack.matches(player.getMainHandItem(), stack)) {
                String s = "%s".formatted(GripcrystalManaData.getMana(saver));
                this.pose.translate(0.0F, 0.0F, 300.0F);
                this.drawString(textRenderer, s, x + 17 - textRenderer.width(s), y + 8, MapColor.COLOR_BLUE.col);
            }
        }
    }
}