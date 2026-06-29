package net.sashakyotoz.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.config.ModMainConfig;
import org.apache.commons.lang3.tuple.Triple;

public class GrippingLayer<T extends LivingEntity, M extends EntityModel<T>> extends StuckObjectsRenderer<T, M> {
    private final EntityRendererProvider.Context context;
    private final ResourceLocation TEXTURE = UnseenWorld.makeID("textures/entity/layers/gripping_layer.png");

    public GrippingLayer(LivingEntityRenderer<T, M> livingEntityRenderer, EntityRendererProvider.Context context) {
        super(livingEntityRenderer);
        this.context = context;
    }

    @Override
    protected int getObjectCount(T entity) {
        if (!ModMainConfig.renderCrystalsOnMobs.get())
            return 0;
        if (entity instanceof Mob entity1 && entity1.isBaby())
            return 0;
        if (entity instanceof IGrippingEntity entity1 && entity1.getGrippingData() > 0)
            return Math.round(entity1.getGrippingData() / 2f);
        return 0;
    }

    @Override
    protected void renderModelPartOverlay(ModelPart part, PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity) {
        if (entity instanceof IGrippingEntity iGrippingEntity)
            part.render(matrices, vertexConsumers.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, iGrippingEntity.getGrippingData() / 10f);
    }

    @Override
    protected Triple<Float, Float, Float> getSizedPart() {
        return Triple.of(0.0f, 0.0f, 0.0f);
    }

    @Override
    protected void renderObject(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity) {
        matrices.scale(0.35f, 0.35f, 0.35f);
        ItemStack stack = ModBlocks.GRIPCRYSTAL_WART.asItem().getDefaultInstance();
        if (entity instanceof LivingEntity entity1)
            this.context.getItemRenderer().render(stack, ItemDisplayContext.FIXED, false,
                    matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, this.context.getItemRenderer().getModel(stack, entity1.level(), entity1, entity1.getId()));
    }
}