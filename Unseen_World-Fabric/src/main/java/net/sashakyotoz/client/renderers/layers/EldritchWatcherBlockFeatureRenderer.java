package net.sashakyotoz.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.models.EldritchWatcherModel;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;
import net.sashakyotoz.utils.Oscillator;

public class EldritchWatcherBlockFeatureRenderer extends RenderLayer<EldritchWatcherEntity, EldritchWatcherModel> {
    private final BlockRenderDispatcher blockRenderManager;

    public EldritchWatcherBlockFeatureRenderer(RenderLayerParent<EldritchWatcherEntity, EldritchWatcherModel> context, BlockRenderDispatcher blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EldritchWatcherEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isCarringBlock()) {
            matrices.pushPose();
            matrices.translate(-0.45F - ((float) Oscillator.getOscillatingWithNegativeValue(ClientTicks.getTicks()) * 0.25f),
                    0.6875F + ((float) Oscillator.getOscillatingOppositeValue(ClientTicks.getTicks()) * 0.2f), -0.8F);
            matrices.scale(-0.45F, -0.45F, 0.45F);
            matrices.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.blockRenderManager.renderBatched(ModBlocks.GRIPCRYSTAL_WART.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP), entity.blockPosition(), entity.level(), matrices, vertexConsumers.getBuffer(RenderType.cutout()), false, entity.getRandom());
            matrices.popPose();
        }
    }
}