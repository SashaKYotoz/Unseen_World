package net.sashakyotoz.mixin.client;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.sashakyotoz.client.gui.BossBarHudHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public class BossBarHudMixin {
    @Final
    @Shadow
    private Minecraft minecraft;
    @Final
    @Shadow
    final Map<UUID, LerpingBossEvent> events = Maps.newLinkedHashMap();

    @Inject(method = "render", at = @At("HEAD"))
    private void renderBossBar(GuiGraphics context, CallbackInfo ci) {
        BossBarHudHooks.render(minecraft, events, context);
    }
}