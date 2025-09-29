package net.sashakyotoz.client.renderers.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;
import net.sashakyotoz.common.blocks.custom.chests.entities.ModChestBlockEntity;

public class ModChestRenderer<T extends ModChestBlockEntity> extends ChestBlockEntityRenderer<T> {

    private final ModelPart chestBottom;
    private final ModelPart chestLid;
    private final ModelPart chestLock;

    public ModChestRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
        ModelPart modelPart = context.getLayerModelPart(EntityModelLayers.CHEST);
        this.chestBottom = modelPart.getChild("bottom");
        this.chestLid = modelPart.getChild("lid");
        this.chestLock = modelPart.getChild("lock");
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockState blockState = world != null ? entity.getCachedState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        if (blockState.getBlock() instanceof ModChestBlock chest) {
            matrices.push();
            float f = blockState.get(ChestBlock.FACING).asRotation();
            matrices.translate(0.5F, 0.5F, 0.5F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
            matrices.translate(-0.5F, -0.5F, -0.5F);
            DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource;
            if (world != null)
                propertySource = chest.getBlockEntitySource(blockState, world, entity.getPos(), true);
            else
                propertySource = DoubleBlockProperties.PropertyRetriever::getFallback;
            float g = propertySource.apply(ChestBlock.getAnimationProgressRetriever(entity)).get(tickDelta);
            g = 1.0F - g;
            g = 1.0F - g * g * g;
            int i = propertySource.apply(new LightmapCoordinatesRetriever<>()).applyAsInt(light);
            SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, chest.getType().texture);
            VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
            this.render(matrices, vertexConsumer, this.chestLock, this.chestLid, this.chestBottom, g, i, overlay);

            matrices.pop();
        }
    }

    private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lock, ModelPart lid, ModelPart base, float openFactor, int light, int overlay) {
        lid.pitch = -(openFactor * (float) (Math.PI / 2));
        lock.pitch = lid.pitch;
        lid.render(matrices, vertices, light, overlay);
        lock.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
    }
}