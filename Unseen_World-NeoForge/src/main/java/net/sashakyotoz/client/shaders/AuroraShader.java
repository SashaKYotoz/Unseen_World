package net.sashakyotoz.client.shaders;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.compat.iris.IrisCompat;

import java.io.IOException;

public final class AuroraShader {
    private static ShaderInstance instance;

    private AuroraShader() {
    }

    public static void register(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), UnseenWorld.MOD.makeID("aurora"), DefaultVertexFormat.POSITION_COLOR), shader -> instance = shader);
    }

    public static ShaderInstance get() {
        return instance != null && !IrisCompat.isShaderPipelineActive() ? instance : GameRenderer.getPositionColorShader();
    }
}