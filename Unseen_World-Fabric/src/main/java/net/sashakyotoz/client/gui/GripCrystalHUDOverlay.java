package net.sashakyotoz.client.gui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.GrippingAbyssalBowItem;
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
            drawTexture(x, y, drawContext, MANA_FRAME);
            int[][] orbPositions = {
                    {-156, -121}, {-156, -131}, {-133, -155}, {-123, -155}, {-100, -131},
                    {-100, -121}, {-123, -97}, {-133, -97}
            };

            if (stack.getItem() instanceof EclipsebaneItem item) {
                switch (item.getItemPhase(stack)) {
                    case "light_ray" -> drawTexture(x, y, drawContext, LIGHT_RAY);
                    case "blade_shield" -> drawTexture(x, y, drawContext, BLADE_SHIELD);
                    case "absorption" -> drawTexture(x, y, drawContext, ABSORPTION);
                }
            }
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem item) {
                switch (item.getItemPhase(stack)) {
                    case "hammer_smashing" -> drawTexture(x, y, drawContext, HAMMER_SMASHING);
                    case "hammer_eroflaming" -> drawTexture(x, y, drawContext, HAMMER_EROFLAMING);
                    case "heavy_winding" -> drawTexture(x, y, drawContext, HEAVY_WINDING);
                }
            }
            if (stack.getItem() instanceof GrippingAbyssalBowItem item) {
                switch (item.getItemPhase(stack)) {
                    case "crystal_crushing" ->
                            drawTexture(x,y,drawContext,CRYSTAL_CRUSHING);
                    case "crystal_rain" -> drawTexture(x,y,drawContext,CRYSTAL_RAIN);
                    case "crystal_suctioning" ->
                            drawTexture(x,y,drawContext,CRYSTAL_SUCTIONING);
                }
            }
            ConfigController.DataItem dataItem = ConfigController.getDataToStack(stack);
            if (dataItem != null) {
                if (dataItem.item() != Items.AIR)
                    drawTexture(x,y,drawContext,dataItem.spellIcon());
            }
            for (int i = 0; i < 8; i++) {
                if (((IEntityDataSaver) client.player).getPersistentData().getInt("gripcrystal_mana") >= (i * 6 == 0 ? 6 : (i + 1) * 6))
                    drawContext.drawTexture(MANA_ORB, x + orbPositions[i][0] + ConfigEntries.xGrippingManaTextureOffset, y + orbPositions[i][1] + ConfigEntries.yGrippingManaTextureOffset, 0, 0, 256, 256);
                else
                    break;
            }
        }
    }

    private void drawTexture(int x, int y, DrawContext drawContext, Identifier identifier) {
        drawContext.drawTexture(identifier, x - 128 + ConfigEntries.xGrippingManaTextureOffset, y - 142 + ConfigEntries.yGrippingManaTextureOffset, 0, 0, 256, 256);
    }
}