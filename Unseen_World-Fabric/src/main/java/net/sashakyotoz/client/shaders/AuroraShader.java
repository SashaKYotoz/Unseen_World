package net.sashakyotoz.client.shaders;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.compat.IrisCompat;

import java.io.IOException;

public final class AuroraShader {
    private static ShaderInstance instance;

    private AuroraShader() {
    }

    public static void register(CoreShaderRegistrationCallback.RegistrationContext context) throws IOException {
        context.register(UnseenWorld.MOD.makeID("aurora"), DefaultVertexFormat.POSITION_COLOR, shader -> instance = shader);
    }

    public static ShaderInstance get() {
        return instance != null && !IrisCompat.isShaderPipelineActive() ? instance : GameRenderer.getPositionColorShader();
    }
}