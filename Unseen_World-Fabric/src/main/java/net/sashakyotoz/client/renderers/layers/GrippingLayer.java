package net.sashakyotoz.client.renderers.layers;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.config.ConfigEntries;

public class GrippingLayer<T extends LivingEntity, M extends EntityModel<T>> extends StuckObjectsRenderer<T, M> {
    private final EntityRendererFactory.Context context;
    private final Identifier TEXTURE = UnseenWorld.makeID("textures/entity/gripping_layer.png");

    public GrippingLayer(LivingEntityRenderer<T, M> livingEntityRenderer, EntityRendererFactory.Context context) {
        super(livingEntityRenderer);
        this.context = context;
    }

    @Override
    protected int getObjectCount(T entity) {
        if (!ConfigEntries.renderGrippingOnMobs)
            return 0;
        if (entity instanceof MobEntity entity1 && entity1.isBaby())
            return 0;
        if (entity instanceof IGrippingEntity entity1 && entity1.getGrippingData() > 0)
            return Math.round(entity1.getGrippingData() / 2f);
        return 0;
    }

    @Override
    protected boolean renderModelPartOverlay(ModelPart part, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity) {
        if (entity instanceof IGrippingEntity iGrippingEntity)
            part.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, iGrippingEntity.getGrippingData() / 10f);
        return true;
    }

    @Override
    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        float g = (float) (Math.atan2(directionX, directionZ) * 57.2957763671875);
        float h = (float) (Math.atan2(directionY, f) * 57.2957763671875);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(h));
        matrices.scale(0.35f, 0.35f, 0.35f);
        ItemStack stack = ModBlocks.GRIPCRYSTAL_WART.asItem().getDefaultStack();
        if (entity instanceof LivingEntity entity1)
            this.context.getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, false,
                    matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, this.context.getItemRenderer().getModel(stack, entity1.getWorld(), entity1, entity1.getId()));
    }
}