package net.sashakyotoz.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneBlockEntity;

public class KeyHandlerStoneRenderer<T extends KeyHandlerStoneBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererProvider.Context context;

    public KeyHandlerStoneRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(KeyHandlerStoneBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        ItemStack stackToRender = entity.keyToRenderer(entity.getLevel(), entity.getBlockPos());
        if (entity.data.firstKeyIn()) {
            matrices.translate(0.5, 0.65, entity.data.firstKeyOffset() + 0.8);
            matrices.mulPose(Axis.XP.rotationDegrees(90));
            matrices.mulPose(Axis.YP.rotationDegrees(270));
            this.context.getItemRenderer().renderStatic(
                    stackToRender,
                    ItemDisplayContext.GROUND,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    matrices,
                    vertexConsumers,
                    entity.getLevel(),
                    overlay
            );
        }
        matrices.popPose();
        matrices.pushPose();
        matrices.translate(0.5, 0.65, entity.data.secondKeyOffset() + 0.2);
        matrices.mulPose(Axis.XN.rotationDegrees(90));
        matrices.mulPose(Axis.YP.rotationDegrees(90));
        if (entity.data.secondKeyIn()) {
            this.context.getItemRenderer().renderStatic(
                    stackToRender,
                    ItemDisplayContext.GROUND,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    matrices,
                    vertexConsumers,
                    entity.getLevel(),
                    overlay
            );
        }
        matrices.popPose();
        if (entity.data.firstKeyIn() && entity.data.secondKeyIn() && entity.data.cooldown() > 0) {
            matrices.pushPose();
            matrices.scale(0.25f, 0.25f, 0.25f);
            matrices.translate(2, 5 + Math.sin(entity.ticks) * 0.25f, 2);
            matrices.mulPose(Axis.YP.rotation((entity.ticks % 360) * 0.25f));
            this.context.getBlockRenderDispatcher().renderBatched(Blocks.DIAMOND_BLOCK.defaultBlockState(), entity.getBlockPos(), entity.getLevel(), matrices, vertexConsumers.getBuffer(RenderType.solid()), false, RandomSource.create());
            matrices.popPose();
        }
    }
}