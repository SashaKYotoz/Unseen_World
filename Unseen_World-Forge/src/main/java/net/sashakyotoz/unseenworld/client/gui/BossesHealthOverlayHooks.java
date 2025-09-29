package net.sashakyotoz.unseenworld.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public class BossesHealthOverlayHooks {
    private static final ResourceLocation DARK_WARRIOR_BOSSBAR_FRAMES = new ResourceLocation("unseen_world:textures/screens/boss_bars/dark_warrior_bossbar_frame.png");
    private static final ResourceLocation BLAZER_BOSSBAR_FRAMES = new ResourceLocation("unseen_world:textures/screens/boss_bars/blazer_bossbar_frame.png");
    private static final ResourceLocation KNIGHT_BOSSBAR_FRAMES = new ResourceLocation("unseen_world:textures/screens/boss_bars/wither_knight_bossbar_frame.png");

    public BossesHealthOverlayHooks() {
    }

    public static void render(Minecraft minecraft, Map<UUID, LerpingBossEvent> events, GuiGraphics graphics) {
        if (!events.isEmpty()) {
            int i = minecraft.getWindow().getGuiScaledWidth();
            int j = 12;
            for (LerpingBossEvent clientBossInfo : events.values()) {
                int k = i / 2 - 91;
                if (shouldDisplayFrame(clientBossInfo)) {
                    if(clientBossInfo.getName().getString().contains("Warrior of") || clientBossInfo.getName().contains(Component.translatable("entity.unseen_world.dark_golem")))
                        graphics.blit(DARK_WARRIOR_BOSSBAR_FRAMES, k, j - 2, 0, 0, 183, 9, 183, 9);
                    if(clientBossInfo.getName().getString().contains("Blazer") || clientBossInfo.getName().getString().contains("Блейзер"))
                        graphics.blit(BLAZER_BOSSBAR_FRAMES, k, j - 2, 0, 0, 183, 9, 183, 9);
                    if(clientBossInfo.getName().getString().contains("Knight") || clientBossInfo.getName().getString().contains("Лицар Візер"))
                        graphics.blit(KNIGHT_BOSSBAR_FRAMES, k, j - 2, 0, 0, 183, 9, 183, 9);
                }
                j += 10 + 9;
                if (j >= minecraft.getWindow().getGuiScaledHeight() / 3) {
                    break;
                }
            }
        }
    }

    private static boolean shouldDisplayFrame(LerpingBossEvent info) {
        return (info.getName().getString().contains("Warrior of the Chimeric Darkness") ||
                info.getName().getString().contains("The Blazer") || info.getName().getString().contains("The Wither Knight")) || (info.getName().getString().contains("Воїн химерної темряви") ||
                info.getName().getString().contains("Блейзер") || info.getName().getString().contains("Лицар Візер"));
    }
}
