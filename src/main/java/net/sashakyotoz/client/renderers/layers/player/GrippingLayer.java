package net.sashakyotoz.client.renderers.layers.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.WorldClientTickEventHandler;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.IEntityDataSaver;
import net.sashakyotoz.utils.Oscillator;

public class GrippingLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final EntityRendererFactory.Context rendererContext;

    public GrippingLayer(FeatureRendererContext<T, M> context, EntityRendererFactory.Context context1) {
        super(context);
        rendererContext = context1;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (GrippingData.getGrippingTime((IEntityDataSaver) entity) > 0) {
            matrices.push();
            matrices.scale(1.275f, 1.275f, 1.275f);
            PlayerEntityModel<LivingEntity> model = new PlayerEntityModel<>(rendererContext.getPart(EntityModelLayers.PLAYER), false);
            float f = (entity.age + tickDelta) * 0.1f;
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(UnseenWorld.makeID("textures/entity/layers/gripping.png"), this.getSwirlX(f) % 1.0F, f * 0.01F % 1.0F));
            ModelFollowingRenderer.followBodyRotations(entity, model);
            ModelFollowingRenderer.followHeadRotations(entity, model.head);
            this.getContextModel().copyStateTo((EntityModel<T>) model);
            ModelFollowingRenderer.rotateIfSneaking(matrices, entity);
            model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.75F);
            matrices.scale(0.375f, 0.375f, 0.375f);
            float offset = (float) Oscillator.getOscillatingValue(Math.round(WorldClientTickEventHandler.halfTicks.get(0) * 2));
            matrices.translate(offset, 0.6f, -offset);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.age % 360) * 10), 0, 1, 0);
            if (!entity.getWorld().getBlockState(entity.getBlockPos().down()).isAir())
                rendererContext.getBlockRenderManager().renderBlock(entity.getWorld().getBlockState(entity.getBlockPos().down()), entity.getBlockPos().up(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), false, entity.getRandom());
            matrices.pop();
        }
    }

    private float getSwirlX(float partialAge) {
        return MathHelper.cos(partialAge * 0.02F) * 3.0F;
    }
}