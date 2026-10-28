package net.sashakyotoz.client.renderers.layers;

import net.minecraft.util.FastColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GlowingPartsLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation texture;
    private final Optional<AlphaFunction<T>> alphaFunction;

    public GlowingPartsLayer(RenderLayerParent<T, M> featureRendererContext, ResourceLocation texture, @Nullable AlphaFunction<T> alphaFunction) {
        super(featureRendererContext);
        this.texture = texture;
        this.alphaFunction = Optional.ofNullable(alphaFunction);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            T entity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(texture));
        float alpha = this.alphaFunction.map(tAlphaFunction -> tAlphaFunction.apply(entity, tickDelta, animationProgress)).orElse(1.0F);
        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
    }

    @FunctionalInterface
    public interface AlphaFunction<T extends Entity> {
        float apply(T entity, float tickDelta, float progress);
    }
}
