package net.sashakyotoz.client.renderers.blocks;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.common.blocks.custom.entities.GlaciemiteTranslocatoneBlockEntity;

public class GlaciemiteTranslocatoneRenderer<T extends GlaciemiteTranslocatoneBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererFactory.Context context;

    public GlaciemiteTranslocatoneRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.scale(0.25f, 0.25f, 0.25f);
        matrices.translate(2, 2.5 + Math.sin(entity.ticks) * 0.25f, 2);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((entity.ticks % 360)*0.25f));
        this.context.getItemRenderer().renderItem(
                Items.ENDER_EYE.getDefaultStack(),
                ModelTransformationMode.GROUND,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                overlay
        );
        matrices.pop();
    }
}