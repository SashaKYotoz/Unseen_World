package net.sashakyotoz.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;

public class FullScreenOverlay implements LayeredDraw.Layer {
    private static final ResourceLocation GRIPPING = UnseenWorld.MOD.makeID(
            "textures/gui/gripping.png");
    private static final ResourceLocation DARKENING = UnseenWorld.MOD.makeID(
            "textures/gui/darkening.png");

    @Override
    public void render(GuiGraphics context, DeltaTracker tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.player instanceof IGrippingEntity entity) {
            int width = client.getWindow().getGuiScaledWidth();
            int height = client.getWindow().getGuiScaledHeight();
            if (entity.getGrippingData() > 0) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                context.setColor(1.0F, 1.0F, 1.0F, Math.min(1, entity.getGrippingData() * 0.1f));
                context.blit(GRIPPING, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
            if (entity.getDarkeningData() > 0) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                context.setColor(1.0F, 1.0F, 1.0F, Math.min(1, entity.getDarkeningData() * 0.1f));
                context.blit(DARKENING, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}