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
import net.minecraft.util.math.random.Random;
import net.sashakyotoz.api.entity_data.IModelPartsAccessor;

import java.util.List;

public abstract class StuckObjectsRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public StuckObjectsRenderer(LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
    }

    protected abstract int getObjectCount(T entity);

    protected abstract void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta);

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        int m = this.getObjectCount(livingEntity);
        Random random = Random.create(livingEntity.getId());
        if (m > 0) {
            for (int n = 0; n < m; ++n) {
                matrixStack.push();
                if (this.getContextModel() instanceof IModelPartsAccessor accessor && !accessor.getAllModelParts().isEmpty()) {
                    ModelPart modelPart = getRandomPart(accessor.getAllModelParts(), random);
                    if (modelPart != null && !modelPart.isEmpty()) {
                        ModelPart.Cuboid cuboid = modelPart.getRandomCuboid(random);
                        modelPart.rotate(matrixStack);
                        float o = random.nextFloat();
                        float p = random.nextFloat();
                        float q = random.nextFloat();
                        float r = MathHelper.lerp(o, cuboid.minX, cuboid.maxX) / 16.0F;
                        float s = MathHelper.lerp(p, cuboid.minY, cuboid.maxY) / 16.0F;
                        float t = MathHelper.lerp(q, cuboid.minZ, cuboid.maxZ) / 16.0F;
                        matrixStack.translate(r, s, t);
                        o = -1.0F * (o * 2.0F - 1.0F);
                        p = -1.0F * (p * 2.0F - 1.0F);
                        q = -1.0F * (q * 2.0F - 1.0F);
                        this.renderObject(matrixStack, vertexConsumerProvider, i, livingEntity, o, p, q, h);
                    }
                }
                matrixStack.pop();
            }

        }
    }

    public ModelPart getRandomPart(List<ModelPart> parts, Random random) {
        return parts.get(random.nextInt(parts.size()));
    }
}