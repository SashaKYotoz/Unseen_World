package net.sashakyotoz.unseenworld.mixin;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.sashakyotoz.unseenworld.client.gui.BossesHealthOverlayHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public class BossesHealthOverMixin {
    @Final
    @Shadow
    private Minecraft minecraft;
    @Final
    @Shadow
    final Map<UUID, LerpingBossEvent> events = Maps.newLinkedHashMap();
    @Inject(method = "render", at = @At("RETURN"))
    private void displayBossbar(GuiGraphics graphics, CallbackInfo callback) {
        BossesHealthOverlayHooks.render(this.minecraft, this.events, graphics);
    }
}
