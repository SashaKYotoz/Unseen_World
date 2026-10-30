package net.sashakyotoz.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;

public class FullScreenOverlay implements HudRenderCallback {
    private static final ResourceLocation GRIPPING = UnseenWorld.MOD.makeID(
            "textures/gui/gripping.png");
    private static final ResourceLocation DARKENING = UnseenWorld.MOD.makeID(
            "textures/gui/darkening.png");

    @Override
    public void onHudRender(GuiGraphics context, DeltaTracker tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.player instanceof IGrippingEntity entity) {
            int width = client.getWindow().getGuiScaledWidth();
            int height = client.getWindow().getGuiScaledHeight();
            if (entity.getGrippingData() > 0) {
                renderOverlay(context, GRIPPING, width, height, Math.min(1.0F, entity.getGrippingData() * 0.1F));
            }
            if (entity.getDarkeningData() > 0) {
                renderOverlay(context, DARKENING, width, height, Math.min(1.0F, entity.getDarkeningData() * 0.1F));
            }
        }
    }

    private static void renderOverlay(GuiGraphics context, ResourceLocation texture, int width, int height, float alpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        context.setColor(1.0F, 1.0F, 1.0F, alpha);
        context.blit(texture, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
        context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}