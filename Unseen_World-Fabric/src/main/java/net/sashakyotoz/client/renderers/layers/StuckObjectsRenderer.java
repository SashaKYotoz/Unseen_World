package net.sashakyotoz.client.renderers.layers;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.sashakyotoz.api.entity_data.IModelPartsAccessor;

import java.util.List;

public abstract class StuckObjectsRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public StuckObjectsRenderer(LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
    }

    protected abstract int getObjectCount(T entity);

    protected abstract void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity);

    protected void renderModelPartOverlay(ModelPart part, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity) {
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        int m = this.getObjectCount(livingEntity);
        Random random = Random.create(livingEntity.getId());
        if (m > 0) {
            if (this.getContextModel() instanceof IModelPartsAccessor accessor && !accessor.getAllModelParts().isEmpty()) {
                for (int n = 0; n < m; ++n) {
                    matrixStack.push();
                    ModelPart modelPart = getRandomPart(accessor.getAllModelParts(), random);
                    if (modelPart != null && !modelPart.isEmpty()) {
                        calculateRotations(matrixStack,modelPart,random,modelPart.getRandomCuboid(random));
                        this.renderObject(matrixStack, vertexConsumerProvider, i, livingEntity);
                    }
                    matrixStack.pop();
                }
                matrixStack.push();
                ModelPart modelPart = getRandomPart(accessor.getAllModelParts(), random);
                if (modelPart != null && !modelPart.isEmpty())
                    renderModelPartOverlay(modelPart, matrixStack, vertexConsumerProvider, i, livingEntity);
                matrixStack.pop();
            }

        }
    }

    public void calculateRotations(MatrixStack matrixStack, ModelPart modelPart, Random random, ModelPart.Cuboid cuboid) {
        modelPart.rotate(matrixStack);
        float directionX = random.nextFloat();
        float directionY = random.nextFloat();
        float directionZ = random.nextFloat();
        float r = MathHelper.lerp(directionX, cuboid.minX, cuboid.maxX) / 16.0F;
        float s = MathHelper.lerp(directionY, cuboid.minY, cuboid.maxY) / 16.0F;
        float t = MathHelper.lerp(directionZ, cuboid.minZ, cuboid.maxZ) / 16.0F;
        matrixStack.translate(r, s, t);
        directionX = -1.0F * (directionX * 2.0F - 1.0F);
        directionY = -1.0F * (directionY * 2.0F - 1.0F);
        directionZ = -1.0F * (directionZ * 2.0F - 1.0F);
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        float g = (float) (Math.atan2(directionX, directionZ) * 57.2957763671875);
        float h = (float) (Math.atan2(directionY, f) * 57.2957763671875);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(h));
    }

    public ModelPart getRandomPart(List<ModelPart> parts, Random random) {
        return parts.get(random.nextInt(parts.size()));
    }
}