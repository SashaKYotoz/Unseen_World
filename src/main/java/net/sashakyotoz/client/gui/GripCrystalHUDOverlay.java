package net.sashakyotoz.client.gui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.GrippingAbyssalBowItem;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.ActionsManager;
import net.sashakyotoz.utils.IEntityDataSaver;

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
    private static final Identifier GRIPCRYSTAL_SHIELD = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/gripcrystal_shield.png");
    private static final Identifier STAFF_AURA = UnseenWorld.makeID(
            "textures/gui/gripcrystal_abilities/staff_aura.png");
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
                        || ActionsManager.canUseGripcrystalCharges(client.player.getMainHandStack()))) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            x = width / 2;
            y = height / 2;
            ItemStack stack = client.player.getMainHandStack();
            drawContext.drawTexture(MANA_FRAME, x - 128, y - 142, 0, 0, 256, 256);
            int[][] orbPositions = {
                    {-99, -160}, {-106, -154}, {-114, -149}, {-124, -147}, {-133, -147},
                    {-143, -149}, {-151, -154}, {-158, -160}
            };

            if (stack.getItem() instanceof EclipsebaneItem item) {
                switch (item.getItemPhase(stack)) {
                    case "light_ray" -> drawContext.drawTexture(LIGHT_RAY, x - 128, y - 142, 0, 0, 256, 256);
                    case "blade_shield" -> drawContext.drawTexture(BLADE_SHIELD, x - 128, y - 142, 0, 0, 256, 256);
                    case "absorption" -> drawContext.drawTexture(ABSORPTION, x - 128, y - 142, 0, 0, 256, 256);
                }
            }
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem item) {
                switch (item.getItemPhase(stack)) {
                    case "hammer_smashing" ->
                            drawContext.drawTexture(HAMMER_SMASHING, x - 128, y - 142, 0, 0, 256, 256);
                    case "hammer_eroflaming" ->
                            drawContext.drawTexture(HAMMER_EROFLAMING, x - 128, y - 142, 0, 0, 256, 256);
                    case "heavy_winding" -> drawContext.drawTexture(HEAVY_WINDING, x - 128, y - 142, 0, 0, 256, 256);
                }
            }
            if (stack.getItem() instanceof GrippingAbyssalBowItem item) {
                switch (item.getItemPhase(stack)) {
                    case "crystal_crushing" ->
                            drawContext.drawTexture(CRYSTAL_CRUSHING, x - 128, y - 142, 0, 0, 256, 256);
                    case "crystal_rain" -> drawContext.drawTexture(CRYSTAL_RAIN, x - 128, y - 142, 0, 0, 256, 256);
                    case "crystal_suctioning" ->
                            drawContext.drawTexture(CRYSTAL_SUCTIONING, x - 128, y - 142, 0, 0, 256, 256);
                }
            }
            if (ActionsManager.isModLoaded("minecells") && stack.getItem().getTranslationKey().contains("cursed_sword"))
                drawContext.drawTexture(GRIPCRYSTAL_SHIELD, x - 128, y - 142, 0, 0, 256, 256);
            if (ActionsManager.isModLoaded("sortilege") && stack.getItem().getTranslationKey().contains("staff"))
                drawContext.drawTexture(STAFF_AURA, x - 128, y - 142, 0, 0, 256, 256);
            for (int i = 0; i < 8; i++) {
                if (((IEntityDataSaver) client.player).getPersistentData().getInt("gripcrystal_mana") >= (i * 6 == 0 ? 6 : (i + 1) * 6))
                    drawContext.drawTexture(MANA_ORB, x + orbPositions[i][0], y + orbPositions[i][1], 0, 0, 256, 256);
                else
                    break;
            }
        }
    }
}