package net.sashakyotoz.client.renderers.types;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

public final class OwnedOutlineRenderType extends RenderType {
    private static final LayeringStateShard FRONT_FACE_CULLING = new LayeringStateShard(
            "unseen_world_front_face_culling",
            () -> {
                RenderSystem.enableCull();
                GL11.glCullFace(GL11.GL_FRONT);
            },
            () -> GL11.glCullFace(GL11.GL_BACK)
    );

    private static final Function<ResourceLocation, RenderType> OWNED_OUTLINE = Util.memoize(texture -> create(
            "unseen_world_owned_outline",
            DefaultVertexFormat.POSITION_COLOR_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            CompositeState.builder()
                    .setShaderState(RENDERTYPE_OUTLINE_SHADER)
                    .setTextureState(new TextureStateShard(texture, false, false))
                    .setCullState(CULL)
                    .setLayeringState(FRONT_FACE_CULLING)
                    .createCompositeState(false)
    ));

    private OwnedOutlineRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType ownedOutline(ResourceLocation texture) {
        return OWNED_OUTLINE.apply(texture);
    }
}
