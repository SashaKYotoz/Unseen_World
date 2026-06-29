package net.sashakyotoz.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.sashakyotoz.common.blocks.custom.entities.TranslocatoneBlockEntity;

public class TranslocatoneRenderer<T extends TranslocatoneBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererProvider.Context context;

    public TranslocatoneRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        matrices.pushPose();
        matrices.scale(0.25f, 0.25f, 0.25f);
        matrices.translate(2, 2.5 + Math.sin(entity.ticks) * 0.25f, 2);
        matrices.mulPose(Axis.YP.rotation((entity.ticks % 360) * 0.25f));
        this.context.getItemRenderer().renderStatic(
                Items.ENDER_EYE.getDefaultInstance(),
                ItemDisplayContext.GROUND,
                light,
                OverlayTexture.NO_OVERLAY,
                matrices,
                vertexConsumers,
                entity.getLevel(),
                overlay
        );
        matrices.popPose();
    }
}