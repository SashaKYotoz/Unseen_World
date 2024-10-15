package net.sashakyotoz.client.renderers.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneBlockEntity;
import net.sashakyotoz.common.items.ModItems;

public class KeyHandlerStoneRenderer<T extends KeyHandlerStoneBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererFactory.Context context;

    public KeyHandlerStoneRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        if (entity.data.firstKeyIn()) {
            matrices.translate(0.5, 0.75, entity.data.firstKeyOffset() + 0.8);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(135));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            this.context.getItemRenderer().renderItem(
                    ModItems.GRIPCRYSTAL_KEY.getDefaultStack(),
                    ModelTransformationMode.GROUND,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    vertexConsumers,
                    entity.getWorld(),
                    overlay
            );
        }
        matrices.pop();
        matrices.push();
        matrices.translate(0.5, 0.575, entity.data.secondKeyOffset() + 0.2);
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(45));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        if (entity.data.secondKeyIn()) {
            this.context.getItemRenderer().renderItem(
                    ModItems.GRIPCRYSTAL_KEY.getDefaultStack(),
                    ModelTransformationMode.GROUND,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    vertexConsumers,
                    entity.getWorld(),
                    overlay
            );
        }
        matrices.pop();
        if (entity.data.firstKeyIn() && entity.data.secondKeyIn()){
            matrices.push();
            matrices.scale(0.25f, 0.25f, 0.25f);
            matrices.translate(2, 5 + Math.sin(entity.ticks) * 0.25f, 2);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation((entity.ticks % 360)*0.25f));
            this.context.getRenderManager().renderBlock(Blocks.DIAMOND_BLOCK.getDefaultState(), entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), false, Random.create());
            matrices.pop();
        }
    }
}