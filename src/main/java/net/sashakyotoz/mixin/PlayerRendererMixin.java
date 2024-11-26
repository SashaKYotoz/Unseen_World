package net.sashakyotoz.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.client.renderers.layers.player.BladeShieldLayer;
import net.sashakyotoz.client.renderers.layers.player.GrippingLayer;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstruct(EntityRendererFactory.Context context, boolean slim, CallbackInfo ci) {
        PlayerEntityRenderer renderer = (PlayerEntityRenderer) (Object) this;
        renderer.addFeature(new BladeShieldLayer<>(renderer, context));
        renderer.addFeature(new GrippingLayer<>(renderer, context));
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void render(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (player.fallDistance > 0.1f && GrippingData.getGrippingTime((IEntityDataSaver) player) > 0){
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(player.age % 45));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(player.age % 45));
        }
    }
}