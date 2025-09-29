package net.sashakyotoz.mixin.client;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.sashakyotoz.client.gui.BossBarHudHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Final
    @Shadow
    private MinecraftClient client;
    @Final
    @Shadow
    final Map<UUID, ClientBossBar> bossBars = Maps.newLinkedHashMap();
    @Inject(method = "render",at = @At("HEAD"))
    private void renderBossBar(DrawContext context, CallbackInfo ci){
        BossBarHudHooks.render(client,bossBars,context);
    }
}