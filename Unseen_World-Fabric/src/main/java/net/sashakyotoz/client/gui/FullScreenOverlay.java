package net.sashakyotoz.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;

public class FullScreenOverlay implements HudRenderCallback {
    private static final ResourceLocation GRIPPING = UnseenWorld.makeID(
            "textures/gui/gripping.png");
    private static final ResourceLocation DARKENING = UnseenWorld.makeID(
            "textures/gui/darkening.png");

    @Override
    public void onHudRender(GuiGraphics context, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.player instanceof IGrippingEntity entity) {
            int width = client.getWindow().getGuiScaledWidth();
            int height = client.getWindow().getGuiScaledHeight();
            if (entity.getGrippingData() > 0) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                context.setColor(1.0F, 1.0F, 1.0F, Math.min(1, entity.getGrippingData() * 0.1f));
                context.blit(GRIPPING, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
            if (entity.getDarkeningData() > 0) {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                context.setColor(1.0F, 1.0F, 1.0F, Math.min(1, entity.getDarkeningData() * 0.1f));
                context.blit(DARKENING, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}