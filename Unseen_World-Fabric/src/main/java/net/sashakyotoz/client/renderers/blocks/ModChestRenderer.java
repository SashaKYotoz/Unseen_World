package net.sashakyotoz.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;
import net.sashakyotoz.common.blocks.custom.chests.entities.ModChestBlockEntity;

public class ModChestRenderer<T extends ModChestBlockEntity> extends ChestRenderer<T> {

    private final ModelPart chestBottom;
    private final ModelPart chestLid;
    private final ModelPart chestLock;

    public ModChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        ModelPart modelPart = context.bakeLayer(ModelLayers.CHEST);
        this.chestBottom = modelPart.getChild("bottom");
        this.chestLid = modelPart.getChild("lid");
        this.chestLock = modelPart.getChild("lock");
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Level world = entity.getLevel();
        BlockState blockState = world != null ? entity.getBlockState() : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        if (blockState.getBlock() instanceof ModChestBlock chest) {
            matrices.pushPose();
            float f = blockState.getValue(ChestBlock.FACING).toYRot();
            matrices.translate(0.5F, 0.5F, 0.5F);
            matrices.mulPose(Axis.YP.rotationDegrees(-f));
            matrices.translate(-0.5F, -0.5F, -0.5F);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> propertySource;
            if (world != null)
                propertySource = chest.combine(blockState, world, entity.getBlockPos(), true);
            else
                propertySource = DoubleBlockCombiner.Combiner::acceptNone;
            float g = propertySource.apply(ChestBlock.opennessCombiner(entity)).get(tickDelta);
            g = 1.0F - g;
            g = 1.0F - g * g * g;
            int i = propertySource.apply(new BrightnessCombiner<>()).applyAsInt(light);
            Material spriteIdentifier = new Material(Sheets.CHEST_SHEET, chest.getType().texture);
            VertexConsumer vertexConsumer = spriteIdentifier.buffer(vertexConsumers, RenderType::entityCutout);
            this.render(matrices, vertexConsumer, this.chestLock, this.chestLid, this.chestBottom, g, i, overlay);

            matrices.popPose();
        }
    }

    private void render(PoseStack matrices, VertexConsumer vertices, ModelPart lock, ModelPart lid, ModelPart base, float openFactor, int light, int overlay) {
        lid.xRot = -(openFactor * (float) (Math.PI / 2));
        lock.xRot = lid.xRot;
        lid.render(matrices, vertices, light, overlay);
        lock.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
    }
}