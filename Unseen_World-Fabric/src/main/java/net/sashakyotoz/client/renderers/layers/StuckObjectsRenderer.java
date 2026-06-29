package net.sashakyotoz.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.api.entity_data.IModelPartsAccessor;
import net.sashakyotoz.utils.ModelPartUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

public abstract class StuckObjectsRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public StuckObjectsRenderer(LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
    }

    protected abstract int getObjectCount(T entity);

    protected abstract void renderObject(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity);

    protected void renderModelPartOverlay(ModelPart part, PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity) {
    }

    protected abstract Triple<Float, Float, Float> getSizedPart();

    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        int m = this.getObjectCount(livingEntity);
        RandomSource random = RandomSource.create(livingEntity.getId());
        if (m > 0) {
            if (this.getParentModel() instanceof IModelPartsAccessor accessor && !accessor.getAllModelParts().isEmpty()) {
                for (int n = 0; n < m; ++n) {
                    matrixStack.pushPose();
                    ModelPart modelPart = getRandomPart(accessor.getAllModelParts(), random);
                    if (modelPart != null && !modelPart.isEmpty()) {
                        calculateRotations(matrixStack, modelPart, random, modelPart.getRandomCube(random));
                        this.renderObject(matrixStack, vertexConsumerProvider, i, livingEntity);
                    }
                    matrixStack.popPose();
                }
                matrixStack.pushPose();
                ModelPart modelPart = ModelPartUtils.getPart(getSizedPart(), accessor.getAllModelParts());
                if (modelPart != null && !modelPart.isEmpty())
                    renderModelPartOverlay(modelPart, matrixStack, vertexConsumerProvider, i, livingEntity);
                matrixStack.popPose();
            }

        }
    }

    public void calculateRotations(PoseStack matrixStack, ModelPart modelPart, RandomSource random, ModelPart.Cube cuboid) {
        modelPart.translateAndRotate(matrixStack);
        float directionX = random.nextFloat();
        float directionY = random.nextFloat();
        float directionZ = random.nextFloat();
        float r = Mth.lerp(directionX, cuboid.minX, cuboid.maxX) / 16.0F;
        float s = Mth.lerp(directionY, cuboid.minY, cuboid.maxY) / 16.0F;
        float t = Mth.lerp(directionZ, cuboid.minZ, cuboid.maxZ) / 16.0F;
        matrixStack.translate(r, s, t);
        directionX = -1.0F * (directionX * 2.0F - 1.0F);
        directionY = -1.0F * (directionY * 2.0F - 1.0F);
        directionZ = -1.0F * (directionZ * 2.0F - 1.0F);
        float f = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        float g = (float) (Math.atan2(directionX, directionZ) * 57.2957763671875);
        float h = (float) (Math.atan2(directionY, f) * 57.2957763671875);
        matrixStack.mulPose(Axis.YP.rotationDegrees(g - 90.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(h));
    }

    public ModelPart getRandomPart(List<ModelPart> parts, RandomSource random) {
        return parts.get(random.nextInt(parts.size()));
    }
}