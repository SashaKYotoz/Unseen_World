package net.sashakyotoz.mixin.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.utils.Oscillator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    @Shadow
    protected M model;

    @Shadow
    protected abstract float getWhiteOverlayProgress(T entity, float tickDelta);

    protected LivingEntityRendererMixin(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void renderMixinInvoke(T livingEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int packedLight, CallbackInfo ci) {
        if (livingEntity instanceof IGrippingEntity entity && entity.getGrippingData() > 0) {
            float modifer = Oscillator.calculateDecreasingValue(entity.getGrippingData() / 10f);
            VertexConsumer vertexconsumer = vertexConsumerProvider.getBuffer(RenderType.eyes(this.getTextureLocation(livingEntity)));
            int i = LivingEntityRenderer.getOverlayCoords(livingEntity, getWhiteOverlayProgress(livingEntity, g));
            this.model.renderToBuffer(matrixStack, vertexconsumer, packedLight, i, modifer, modifer, 1.0F, 1.0F);
        }
    }
}