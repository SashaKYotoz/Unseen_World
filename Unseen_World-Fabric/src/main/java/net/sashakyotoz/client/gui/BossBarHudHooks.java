package net.sashakyotoz.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;

import java.util.Map;
import java.util.UUID;

public class BossBarHudHooks {
    private static final ResourceLocation BOSSBAR_WARRIOR_OF_DARKNESS = UnseenWorld.makeID("textures/gui/bossbars/warrior_of_darkness_bossbar.png");
    private static final ResourceLocation BOSSBAR_ECLIPSE_SENTINEL = UnseenWorld.makeID("textures/gui/bossbars/eclipse_sentinel_bossbar.png");

    public static void render(Minecraft client, Map<UUID, LerpingBossEvent> events, GuiGraphics context) {
        if (!events.isEmpty()) {
            int i = client.getWindow().getGuiScaledWidth();
            int j = 12;
            for (LerpingBossEvent clientbossinfo : events.values()) {
                int k = i / 2 - 91;
                if (shouldDisplayFrame(clientbossinfo))
                    context.blit(getBossbarLocation(clientbossinfo), k, j - 2, 0, 0, 183, 9, 183, 9);
                j += 10 + 9;
                if (j >= client.getWindow().getGuiScaledHeight() / 3)
                    break;
            }
        }
    }

    private static boolean shouldDisplayFrame(LerpingBossEvent info) {
        return info.getName().contains(Component.translatable("entity.unseen_world.warrior_of_chimeric_darkness")) || info.getName().contains(Component.translatable("entity.unseen_world.eclipse_sentinel"));
    }

    private static ResourceLocation getBossbarLocation(LerpingBossEvent info) {
        if (info.getName().contains(Component.translatable("entity.unseen_world.warrior_of_chimeric_darkness")))
            return BOSSBAR_WARRIOR_OF_DARKNESS;
        if (info.getName().contains(Component.translatable("entity.unseen_world.eclipse_sentinel")))
            return BOSSBAR_ECLIPSE_SENTINEL;
        return new ResourceLocation("");
    }
}