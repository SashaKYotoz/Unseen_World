package net.sashakyotoz.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.states.OrdealSpawnerState;

public class OrdealSpawnerRenderer implements BlockEntityRenderer<OrdealSpawnerBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;

    public OrdealSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(OrdealSpawnerBlockEntity spawner, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        Level level = spawner.getLevel();
        OrdealSpawnerState phase = spawner.getBlockState().getValue(OrdealSpawnerBlock.STATE);
        if (level == null || (phase != OrdealSpawnerState.WAITING_FOR_PLAYERS && phase != OrdealSpawnerState.ACTIVE)) {
            return;
        }
        Entity entity = spawner.getOrCreateDisplayEntity(level);
        if (entity == null) {
            return;
        }
        float scale = 0.53125F;
        float size = Math.max(entity.getBbWidth(), entity.getBbHeight());
        if (size > 1.0F) {
            scale /= size;
        }
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.4F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(spawner.getSpin(partialTick) * 10.0F));
        poseStack.translate(0.0F, -0.2F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
        poseStack.scale(scale, scale, scale);
        this.entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTick, poseStack, buffers, packedLight);
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(OrdealSpawnerBlockEntity spawner) {
        return new AABB(spawner.getBlockPos()).inflate(0.5D);
    }
}