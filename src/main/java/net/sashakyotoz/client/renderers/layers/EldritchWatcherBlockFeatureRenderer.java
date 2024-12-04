package net.sashakyotoz.client.renderers.layers;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.environment.WorldClientTickEventHandler;
import net.sashakyotoz.client.models.EldritchWatcherModel;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;
import net.sashakyotoz.utils.Oscillator;

public class EldritchWatcherBlockFeatureRenderer extends FeatureRenderer<EldritchWatcherEntity, EldritchWatcherModel> {
    private final BlockRenderManager blockRenderManager;

    public EldritchWatcherBlockFeatureRenderer(FeatureRendererContext<EldritchWatcherEntity, EldritchWatcherModel> context, BlockRenderManager blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EldritchWatcherEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isCarringBlock()) {
            matrices.push();
            matrices.translate(-0.45F - ((float) Oscillator.getOscillatingWithNegativeValue(Math.round(WorldClientTickEventHandler.halfTicks.get(0) * 2)) * 0.25f),
                    0.6875F + ((float) Oscillator.getOscillatingOppositeValue(Math.round(WorldClientTickEventHandler.halfTicks.get(0) * 2)) * 0.2f), -0.8F);
            matrices.scale(-0.45F, -0.45F, 0.45F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
            this.blockRenderManager.renderBlock(ModBlocks.GRIPCRYSTAL_WART.getDefaultState().with(Properties.FACING, Direction.UP), entity.getBlockPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getRandom());
            matrices.pop();
        }
    }
}