package net.sashakyotoz.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;

public class FullScreenOverlay implements HudRenderCallback {
    private static final Identifier GRIPPING = UnseenWorld.makeID(
            "textures/gui/gripping.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player instanceof IGrippingEntity entity && entity.getGrippingData() > 0) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            context.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1, entity.getGrippingData() * 0.1f));
            context.drawTexture(GRIPPING, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}