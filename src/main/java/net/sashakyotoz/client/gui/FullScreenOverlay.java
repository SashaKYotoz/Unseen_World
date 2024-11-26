package net.sashakyotoz.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.IEntityDataSaver;

public class FullScreenOverlay implements HudRenderCallback {
    private static final Identifier GRIPPING = UnseenWorld.makeID(
            "textures/gui/gripping.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null && GrippingData.getGrippingTime((IEntityDataSaver) client.player) > 0) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            float f = client.player.age + tickDelta;
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            context.setShaderColor(1.0F, 1.0F, 1.0F, GrippingData.getGrippingTime((IEntityDataSaver) client.player)*0.1f);
            context.drawTexture(GRIPPING, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}