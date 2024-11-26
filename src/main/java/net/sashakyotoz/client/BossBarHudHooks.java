package net.sashakyotoz.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;

import java.util.Map;
import java.util.UUID;

public class BossBarHudHooks {
    private static final Identifier BOSSBAR_WARRIOR_OF_DARKNESS = UnseenWorld.makeID("textures/gui/bossbars/warrior_of_darkness_bossbar.png");
    private static final Identifier BOSSBAR_ECLIPSE_SENTINEL = UnseenWorld.makeID("textures/gui/bossbars/eclipse_sentinel_bossbar.png");
    public static void render(MinecraftClient client, Map<UUID, ClientBossBar> events, DrawContext context){
        if (!events.isEmpty()) {
            int i = client.getWindow().getScaledWidth();
            int j = 12;
            for (ClientBossBar clientbossinfo : events.values()) {
                int k = i / 2 - 91;
                if (shouldDisplayFrame(clientbossinfo)) {
                    context.drawTexture(getBossbarLocation(clientbossinfo), k, j - 2, 0, 0, 183, 9, 183, 9);
                }
                j += 10 + 9;
                if (j >= client.getWindow().getScaledHeight() / 3) {
                    break;
                }
            }
        }
    }
    private static boolean shouldDisplayFrame(ClientBossBar info) {
        return info.getName().contains(Text.translatable("entity.unseen_world.warrior_of_chimeric_darkness")) || info.getName().contains(Text.translatable("entity.unseen_world.eclipse_sentinel"));
    }
    private static Identifier getBossbarLocation(ClientBossBar info){
        if (info.getName().contains(Text.translatable("entity.unseen_world.warrior_of_chimeric_darkness")))
            return BOSSBAR_WARRIOR_OF_DARKNESS;
        if (info.getName().contains(Text.translatable("entity.unseen_world.eclipse_sentinel")))
            return BOSSBAR_ECLIPSE_SENTINEL;
        return new Identifier("");
    }
}
