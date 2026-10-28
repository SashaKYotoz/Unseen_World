package net.sashakyotoz.client.gui;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.client.ui.bossbar.BossBarTextState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.sashakyotoz.UnseenWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PhasedBossBar {
    private static final String[] COLOR_NAMES = {"pink", "blue", "red", "green", "yellow", "purple", "white"};
    private static final ResourceLocation[] BACKGROUND_SPRITES = sprites("background");
    private static final ResourceLocation[] PROGRESS_SPRITES = sprites("progress");
    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;
    private static final int FRAME_WIDTH = 183;
    private static final int FRAME_HEIGHT = 9;
    private static final Map<ResourceLocation, Boolean> TEXTURE_PRESENCE = new HashMap<>();
    private static boolean reloadListenerRegistered = false;

    private final Component name;
    private final String textureBase;
    private final List<Phase> phases = new ArrayList<>();

    private record Phase(BossEvent.BossBarColor color, ResourceLocation texture, int textColor) {
    }

    private PhasedBossBar(String nameKey, String textureBase) {
        this.name = Component.translatable(nameKey);
        this.textureBase = textureBase;
    }

    public static PhasedBossBar of(String nameKey, String textureBase) {
        return new PhasedBossBar(nameKey, textureBase);
    }

    public PhasedBossBar phase(BossEvent.BossBarColor color, int textColor) {
        String suffix = this.phases.isEmpty() ? "" : "_phase" + (this.phases.size() + 1);
        ResourceLocation texture = UnseenWorld.MOD.makeID("textures/gui/bossbars/" + this.textureBase + suffix + ".png");
        this.phases.add(new Phase(color, texture, textColor));
        return this;
    }

    public void register() {
        if (this.phases.isEmpty()) {
            throw new IllegalStateException("Boss bar " + this.textureBase + " has no phases");
        }
        registerReloadListener();
        SolClientRegistries.BOSS_BAR.register(this::matches, this::render);
        SolClientRegistries.BOSS_BAR.registerTextModifier(this::matches, this::modifyText);
    }

    private boolean matches(BossEvent bossEvent) {
        return bossEvent.getName().contains(this.name);
    }

    private int phaseIndex(BossEvent bossEvent) {
        BossEvent.BossBarColor color = bossEvent.getColor();
        for (int i = this.phases.size() - 1; i >= 0; i--) {
            if (this.phases.get(i).color() == color) {
                return i;
            }
        }
        return 0;
    }

    private ResourceLocation frameTexture(int phaseIndex) {
        for (int i = phaseIndex; i > 0; i--) {
            ResourceLocation texture = this.phases.get(i).texture();
            if (isTexturePresent(texture)) {
                return texture;
            }
        }
        return this.phases.get(0).texture();
    }

    private void render(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent) {
        int colorIndex = bossEvent.getColor().ordinal();
        guiGraphics.blitSprite(BACKGROUND_SPRITES[colorIndex], BAR_WIDTH, BAR_HEIGHT, 0, 0, x, y, BAR_WIDTH, BAR_HEIGHT);
        int progressWidth = Mth.lerpDiscrete(bossEvent.getProgress(), 0, BAR_WIDTH);
        if (progressWidth > 0) {
            guiGraphics.blitSprite(PROGRESS_SPRITES[colorIndex], BAR_WIDTH, BAR_HEIGHT, 0, 0, x, y, progressWidth, BAR_HEIGHT);
        }
        guiGraphics.blit(this.frameTexture(this.phaseIndex(bossEvent)), x, y - 2, 0.0F, 0.0F, FRAME_WIDTH, FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
    }

    private static ResourceLocation[] sprites(String kind) {
        ResourceLocation[] sprites = new ResourceLocation[COLOR_NAMES.length];
        for (int i = 0; i < COLOR_NAMES.length; i++) {
            sprites[i] = ResourceLocation.withDefaultNamespace("boss_bar/" + COLOR_NAMES[i] + "_" + kind);
        }
        return sprites;
    }

    private BossBarTextState modifyText(BossEvent bossEvent, BossBarTextState state, GuiGraphics guiGraphics) {
        return state.withColor(this.phases.get(this.phaseIndex(bossEvent)).textColor()).withShadow(true);
    }

    private static boolean isTexturePresent(ResourceLocation texture) {
        Boolean present = TEXTURE_PRESENCE.get(texture);
        if (present == null) {
            present = Minecraft.getInstance().getResourceManager().getResource(texture).isPresent();
            TEXTURE_PRESENCE.put(texture, present);
            if (!present) {
                UnseenWorld.log().warn("Missing boss bar phase texture {}, falling back to an earlier phase frame", texture);
            }
        }
        return present;
    }

    private static void registerReloadListener() {
        if (reloadListenerRegistered) {
            return;
        }
        reloadListenerRegistered = true;
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return UnseenWorld.MOD.makeID("phased_boss_bars");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                TEXTURE_PRESENCE.clear();
            }
        });
    }
}