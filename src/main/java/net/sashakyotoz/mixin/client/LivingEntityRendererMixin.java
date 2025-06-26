package net.sashakyotoz.mixin.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.utils.Oscillator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
    @Shadow
    protected M model;

    @Shadow
    protected abstract float getAnimationCounter(T entity, float tickDelta);

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void renderMixinInvoke(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight, CallbackInfo ci) {
        if (livingEntity instanceof IGrippingEntity entity && entity.getGrippingData() > 0) {
            float modifer = Oscillator.calculateDecreasingValue(entity.getGrippingData() / 10f);
            VertexConsumer vertexconsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(this.getTexture(livingEntity)));
            int i = LivingEntityRenderer.getOverlay(livingEntity, getAnimationCounter(livingEntity, g));
            this.model.render(matrixStack, vertexconsumer, packedLight, i, modifer, modifer, 1.0F, 1.0F);
        }
    }
}