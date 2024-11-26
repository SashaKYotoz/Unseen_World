package net.sashakyotoz.client.renderers.layers.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public interface ModelFollowingRenderer {
    <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            MatrixStack matrixStack,
            FeatureRendererContext<T, M> renderLayerParent,
            VertexConsumerProvider renderTypeBuffer,
            int light, float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks, float netHeadYaw,
            float headPitch);

    static void translateIfSwimming(final MatrixStack matrixStack, final LivingEntity livingEntity) {
        if(livingEntity.isSwimming())
            matrixStack.translate(0.0F, 0.0F, -0.1F);
    }

    static void rotateIfSneaking(final MatrixStack matrixStack, final LivingEntity livingEntity) {
        if (livingEntity.isSneaking()) {
            EntityRenderer<? super LivingEntity> render =
                    MinecraftClient.getInstance().getEntityRenderDispatcher()
                            .getRenderer(livingEntity);
            if (render instanceof LivingEntityRenderer) {
                @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
                        livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
                EntityModel<LivingEntity> model = livingRenderer.getModel();

                if (model instanceof BipedEntityModel) {
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(((BipedEntityModel<LivingEntity>) model).body.pitch *0.9f));
                }
            }
        }
    }

    static void followHeadRotations(final LivingEntity livingEntity,
                                    final ModelPart... renderers) {
        EntityRenderer<? super LivingEntity> render =
                MinecraftClient.getInstance().getEntityRenderDispatcher()
                        .getRenderer(livingEntity);

        if (render instanceof LivingEntityRenderer) {
            @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
                    livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
            EntityModel<LivingEntity> model = livingRenderer.getModel();

            if (model instanceof BipedEntityModel) {

                for (ModelPart renderer : renderers) {
                    renderer.copyTransform(((BipedEntityModel<LivingEntity>) model).head);
                }
            }
        }
    }

    @SafeVarargs
    static void followBodyRotations(final LivingEntity livingEntity, final BipedEntityModel<LivingEntity>... models) {
        EntityRenderer<? super LivingEntity> render =
                MinecraftClient.getInstance().getEntityRenderDispatcher()
                        .getRenderer(livingEntity);

        if (render instanceof LivingEntityRenderer) {
            @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
                    livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
            EntityModel<LivingEntity> entityModel = livingRenderer.getModel();

            if (entityModel instanceof BipedEntityModel) {
                for (BipedEntityModel<LivingEntity> model : models) {
                    BipedEntityModel<LivingEntity> bipedModel = (BipedEntityModel<LivingEntity>) entityModel;
                    bipedModel.copyStateTo(model);
                }
            }
        }
    }
}
