package net.sashakyotoz.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.items.custom.*;
import net.sashakyotoz.common.tags.ModTags;

public class GripCrystalHUDOverlay implements HudRenderCallback {
    private static final Identifier MANA_FRAME = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/mana_frame.png");
    private static final Identifier MANA_ORB = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/mana_orb.png");

    private static final Identifier ABSORPTION = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/absorption.png");
    private static final Identifier LIGHT_RAY = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/light_ray.png");
    private static final Identifier BLADE_SHIELD = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/blade_shield.png");
    private static final Identifier HAMMER_SMASHING = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/hammer_smashing.png");
    private static final Identifier HEAVY_WINDING = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/heavy_winding.png");
    private static final Identifier HAMMER_EROFLAMING = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/hammer_eroflaming.png");
    private static final Identifier CRYSTAL_CRUSHING = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/crystal_crushing.png");
    private static final Identifier CRYSTAL_RAIN = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/crystal_rain.png");
    private static final Identifier CRYSTAL_SUCTIONING = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/crystal_suctioning.png");
    private static final Identifier GAUNTLET_STEAL = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/gauntlet_steal.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        int x, y;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null &&
                (client.player.getMainHandStack().isIn(ModTags.Items.CAN_BE_CHARGED_BY_GRIPCRYSTALS)
                        || ConfigController.getDataToStack(client.player.getMainHandStack()) != null)) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            x = width / 2;
            y = height / 2;
            ItemStack stack = client.player.getMainHandStack();
            float opacity = GripcrystalManaData.getOpacity((IEntityDataSaver) client.player);
            drawTexture(x, y, drawContext, MANA_FRAME, opacity);
            int[][] orbPositions = {
                    {-156, -121}, {-156, -131}, {-133, -155}, {-123, -155},
                    {-100, -131}, {-100, -121}, {-123, -97}, {-133, -97}
            };

            if (stack.getItem() instanceof EclipsebaneItem) {
                switch (IGrippingWeapons.getPhase(stack)) {
                    case "light_ray" -> drawTexture(x, y, drawContext, LIGHT_RAY, opacity);
                    case "blade_shield" -> drawTexture(x, y, drawContext, BLADE_SHIELD, opacity);
                    case "absorption" -> drawTexture(x, y, drawContext, ABSORPTION, opacity);
                }
            }
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem) {
                switch (IGrippingWeapons.getPhase(stack)) {
                    case "hammer_smashing" -> drawTexture(x, y, drawContext, HAMMER_SMASHING, opacity);
                    case "hammer_eroflaming" -> drawTexture(x, y, drawContext, HAMMER_EROFLAMING, opacity);
                    case "heavy_winding" -> drawTexture(x, y, drawContext, HEAVY_WINDING, opacity);
                }
            }
            if (stack.getItem() instanceof GrippingAbyssalBowItem) {
                switch (IGrippingWeapons.getPhase(stack)) {
                    case "crystal_crushing" -> drawTexture(x, y, drawContext, CRYSTAL_CRUSHING, opacity);
                    case "crystal_rain" -> drawTexture(x, y, drawContext, CRYSTAL_RAIN, opacity);
                    case "crystal_suctioning" -> drawTexture(x, y, drawContext, CRYSTAL_SUCTIONING, opacity);
                }
            }
            if (stack.getItem() instanceof GrippingGauntletItem)
                drawTexture(x, y, drawContext, GAUNTLET_STEAL, opacity);
            ConfigController.DataItem dataItem = ConfigController.getDataToStack(stack);
            if (dataItem != null) {
                if (dataItem.item() != Items.AIR)
                    drawTexture(x, y, drawContext, dataItem.spellIcon(), opacity);
            }
            for (int i = 0; i < 8; i++) {
                if (((IEntityDataSaver) client.player).getPersistentData().getInt("gripcrystal_mana") >= (i * 6 == 0 ? 6 : (i + 1) * 6))
                    drawOrbTexture(x, y, orbPositions[i][0], orbPositions[i][1], drawContext, opacity, true);
                else
                    break;
            }
        }
    }

    public static void drawOrbTexture(int x, int y, int xO, int yO, DrawContext drawContext, float opacity, boolean useOffset) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        int xF = x + xO + ConfigEntries.xGrippingManaTextureOffset;
        int yF = y + yO + ConfigEntries.yGrippingManaTextureOffset;
        drawContext.drawTexture(
                GripCrystalHUDOverlay.MANA_ORB,
                useOffset ? x + xO : xF,
                useOffset ? y + yO : yF,
                0,
                0,
                256,
                256
        );
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    private void drawTexture(int x, int y, DrawContext drawContext, Identifier identifier, float opacity) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        drawContext.drawTexture(
                identifier,
                x - 128 + ConfigEntries.xGrippingManaTextureOffset,
                y - 142 + ConfigEntries.yGrippingManaTextureOffset,
                0,
                0,
                256,
                256
        );
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}